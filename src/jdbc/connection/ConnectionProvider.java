package jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/* ConnectionProvider클래스
 * :DBCPInitListener에서 만들어진 커넥션풀을 가져다쓰는클래스 */
public class ConnectionProvider{
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:apache:commons:dbcp:team5"); // web.xml = poolname
	}

}
