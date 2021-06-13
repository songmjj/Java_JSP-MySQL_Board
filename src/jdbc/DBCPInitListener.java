package jdbc;

import java.io.IOException;
import java.io.StringReader;
import java.sql.DriverManager;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/* DBCPInitListener클래스(리스너 클래스)
 * : 웹어플리케이션(=WAS, 컨텍스트)이 시작 or 종료되는 시점에 특정기능을 실행하는 클래스
 * -클래스 구현 방법
 * 1. javax.servlet.ServletContextListener인터페이스를 구현한 클래스를 작성한다.(DBCPInitListener클래스)
 * 2. web.xml파일에 1번에서 작성한 클래스를 등록한다.
 * 예) <listener>
	<listener-class>jdbc.DBCPInitListener</listener-class>
	</listener>  
  
	<context-param>
		<param-name>poolConfig</param-name>
		<param-value>
			jdbcdriver=com.mysql.cj.jdbc.Driver
			jdbcUrl=jdbc:mysql://localhost:3306/board?serverTimezone=UTC
			dbUser=jspexam
			dbPass=jsppw
			validationQuery=select1
			minIdle=3
			maxTotal=30
			poolName=board
		</param-value>
	</context-param>
 */
/* DBCPInitListener클래스 구동 순서
 * : 웹어플리케이션(=WAS, 웹컨테이너) 구동
 *  → web.xml파일에 등록한 DBCPInitListener클래스의 contextInitialized()메서드 실행(= 커넥션풀 초기화 됨)
 *   → 위 메서드 실행시 loadJDBCDriver(prop), initConnectionPool(prop)메서드 실행  
 *     (poolConfig(초기화파라미터)를 이용해서 생성한 객체로부터 커넥션풀을 생성함)*/
public class DBCPInitListener implements ServletContextListener{

	/* ServletContextListener인터페이스의  contextInitialized()메서드 오버라이딩
	 * : 웹어플리케이션(=WAS)을 초기화할 때 호출한다. */
	@Override 
	public void contextInitialized(ServletContextEvent sce) {
		/* web.xml파일에서 <context-param>을 가져와서 poolConfig에 대입 */
		/* getServletContext()메서드
		 * : 웹어플리케이션컨텍스트(=<web-app><context-param>)를 구해주는 메서드(ServletContextEvent클래스의 메서드) */
		/* getInitParameter(String name)메서드
		 * : <context-param><param-name>태그로 지정한  이름(name)의 컨텍스트초기화파라미터(<param-value>)값들을 리턴함
		 *  즉, 컨텍스트초기화파라미터(<param-value>)값들를 이용해서 커넥션풀을 초기화하는데 필요한 값들을 로딩함*/
		String poolConfig = 
				sce.getServletContext().getInitParameter("poolConfig");
		/* Properties클래스의 load()메서드
		 * : "키=값"형식으로 구성된 문자열들로부터 (이름,값)오로 구성된 프로퍼티들로 리턴함
		 *  즉, 위에서 리턴한 "키=값"형식의 문자열들을  프로퍼티(이름,값)들로 리턴해서 prop변수에 대입(=저장) */
		Properties prop = new Properties();
		try {
			prop.load(new StringReader(poolConfig));
		}catch (IOException e) { // 입출력예외처리
			throw new RuntimeException("config load fail", e);
		}
		//예외가 없으면 아래에 정의한 두 메서드 실행한다.
		/* loadJDBCDriver(prop)메서드 : 1.JDBC Driver을 로딩하는 메서드 */
		loadJDBCDriver(prop);
		/* initConnectionPool(prop)메서드 : ConnectionPool을 초기화해줌 */
		initConnectionPool(prop);
	}
	
	/* loadJDBCDriver(prop)메서드 : Properties객체를 매개변수(=parameter)로 받아와 JDBC Driver을 로딩하는 메서드 */
	private void loadJDBCDriver(Properties prop) {
		String driverClass = prop.getProperty("jdbcdriver"); // prop프로퍼티객체로부터 jdbcdriver(키)의 값을 가져옴
		try {
			Class.forName(driverClass); //1.JDBC Driver을 로딩
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("fail to load JDBC Driver", ex);
		}
	}

	private void initConnectionPool(Properties prop) {
		try {
		String jdbcUrl = prop.getProperty("jdbcUrl"); // prop변수를 통해 web.xml에 있는 jdbcUrl내용 가져옴
		String username = prop.getProperty("dbUser"); // prop변수를 통해 web.xml에 있는 dbUser내용 가져옴
		String pw = prop.getProperty("dbPass"); // prop변수를 통해 web.xml에 있는 dbPass내용 가져옴
		
		/* ConnectionFactory클래스의 connFactory변수
		 * : 살제 커낵션을 생성함 
		 * 	 즉, 커넥션 풀이 새로운 커넥션을 생성할 때 사용할 커넥션 팩토리(ConnectionFactory)를 생성한다.*/
		ConnectionFactory connFactory = 
				new DriverManagerConnectionFactory(jdbcUrl, username, pw);
		
		/* PoolableConnectionFactory클래스의 poolableConnFactory변수
		 * : PoolableConnection을 생성하는 팩토리(Factory)를 생성한다.
			 DBCP는 커넥션 풀에 커넥션을 보관할 때 PoolableConnection을 사용한다.
			 이 클래스는 내부적으로 실제 커넥션을 담고 있으며, 커넥션 풀을 관리하는 데 필요한 기능을 추가로 제공한다.
			 예를들면, close() 메서드를 실행하면 실제 커넥션을 종료하지 않고 풀에 커넥션을 반환한다. */
		PoolableConnectionFactory poolableConnFactory = 
				new PoolableConnectionFactory(connFactory, null);
		
		/* DB 커넥션이 유효한지 여부를 검사할 때 사용할 쿼리("validationQuery")를 지정합니다.
		     이것은 특정 시간마다 트랜잭션 DB 세션 재접속을 검증하는 validationQuery를 실행하게 됩니다. */
		String validationQuery = prop.getProperty("validationQuery"); 
		if (validationQuery != null && !validationQuery.isEmpty()) {
			poolableConnFactory.setValidationQuery(validationQuery);
		}
		
		/* GenericObjectPoolConfig클래스의 poolConfig변수
		 * : 커넥션 풀의 설정 정보를 생성한다. */
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setTimeBetweenEvictionRunsMillis(1000L * 60L * 5L); // 유휴 커넥션 검사 주기 (1000L*60L*5L) 설정
		poolConfig.setTestWhileIdle(true); // 풀에 보관중인 커넥션 유효 검사 설정(true)
		int minIdle = getIntProperty(prop, "minIdle", 5); 
		poolConfig.setMinIdle(minIdle); // 커넥션 최소 개수 (getIntProperty()메서드를 통한 설정 프로퍼티값) 설정
		int maxTotal = getIntProperty(prop, "maxTotal", 50);
		poolConfig.setMaxTotal(maxTotal); // 커넥션 최대 개수 (getIntProperty()메서드를 통한 설정 프로퍼티값) 설정

		/* GenericObjectPool<PoolableConnection>클래스의 connectionPool변수
		 * : 커넥션 풀을 생성한다. 생성자는 PoolableConnection을 생성할 때 사용할 팩토리(PoolableConnectionFactory)와
			 커넥션 풀 설정(GenericObjectPoolConfig)을 파라미터로 전달 받는다. */
		GenericObjectPool<PoolableConnection> connectionPool = 
				new GenericObjectPool<>(poolableConnFactory, poolConfig);
		poolableConnFactory.setPool(connectionPool);
		
		// 커넥션 풀을 제공하는 JDBC 드라이버를 등록한다.
		Class.forName("org.apache.commons.dbcp2.PoolingDriver");
		/* PoolingDriver클래스의 driver변수
		 * : 커넥션 풀 드라이버에 GenericObjectPool에서 생성한 커넥션 풀을 등록한다.
			 이때, "poolName"의 값인 javajspboard를 커넥션 풀 이름으로 주었는데,
			 이 경우 프로그램에서 사용하는 JDBC URL은 "jdbc:apache:commons:dbcp:javajspboard"가 된다. */
		PoolingDriver driver = (PoolingDriver)
			DriverManager.getDriver("jdbc:apache:commons:dbcp:");
		String poolName = prop.getProperty("poolName");
		driver.registerPool(poolName, connectionPool);
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
	
	}
	
	/* Property클래스의  getIntProperty()메서드
	 * : prop(server.xml에서 가져옴)변수에 저장된 키(propName)와 매칭되는 값을 반환함 */
	private int getIntProperty(Properties prop, String propName, int defaultValue) {
		String value = prop.getProperty(propName);
		if (value == null) return defaultValue;
		return Integer.parseInt(value);
	}
	
	/* ServletContextListener인터페이스의  contextDestroyed()메서드 오버라이딩
	 * : 웹어플리케이션(=WAS)을 종료할 때 호출한다.(초기화 시킴) */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
	
}