package mvc.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.command.CommandHandler;
import mvc.command.NullHandler;

/* ControllerUsingURI클래스(컨트롤러)
 * : 웹어플리케이션(=WAS, 컨텍스트)이 시작되는 시점에 web.xml파일에 등록한 ControllerUsingURI클래스 안의 특정기능을 실행하는 클래스
 * 즉, 
 */
/* ControllerUsingURI클래스 구동 순서
 * : 웹어플리케이션(=WAS, 웹컨테이너) 구동
 *  → web.xml파일에 등록한 ControllerUsingURI클래스의 init()메서드 실행(= )
 *  → 요청 기능에 따른 handler를  */
public class ControllerUsingURI extends HttpServlet{
	
	// <커맨드, 핸들러인스턴스> 매핑 정보 저장
	private Map<String, CommandHandler> commandHandlerMap = new HashMap<String, CommandHandler>();

	/* init()메서드
	 * : commandHandlerURI.properties파일로부터 (핸들러클래스주소에 매칭되는 URI,핸들러클래스주소)을 가져와 prop변수에 대입(저장)함
	 * → commandHandlerMap변수에 (핸들러클래스주소에 매칭되는 URI, 해당 핸들러클래스로 생성된 핸들러객체) 매핑 정보를 저장한다.
	 * 즉, commandHandlerURI.properties파일로부터 (핸들러클래스주소에 매칭되는 URI,핸들러클래스주소)을 가져와 commandHandlerMap변수에 (핸들러클래스주소에 매칭되는 URI, 핸들러객체) 매핑 정보를 저장함*/
	@Override
	public void init() throws ServletException {
		// configFile 초기화 파라미터 값을 읽어온다.
		// 다시말해, web.xml파일에 있는 configFile초기화파라미터 값('/WEB-INF/commandHandlerURI.properties')을 불러옴&대입
		String configFile = getInitParameter("configFile");
		// Properties 객체를 참조하는 객체 참조 변수 prop에 Properties를 생성해 준다.		
		Properties prop = new Properties();
		// 설정 파일 경로를 가져와 지정해 준다.(configFile초기화파라미터 값('/WEB-INF/commandHandlerURI.properties')을 통해서)
		String configFilePath = getServletContext().getRealPath(configFile);
		 /*설정 파일로부터 매핑 정보를 읽어와서 Properties 객체에 저장한다. 
		   Properties는 목록(이름, 값)을 갖는 클래스로서, 이름을  커맨드이름으로 사용하고 값을 핸들러클래스이름으로 사용한다. 
		   즉, (URI이름 또는 커맨드이름,핸들러클래스이름)을 가져와 prop변수에 대입함*/
		try(FileReader fr = new FileReader(configFilePath)) {
			prop.load(fr);			
		} catch(IOException e){
			throw new ServletException(e);
		}
		// Properties에 저장된 각 프로퍼티의 키(URI이름 또는 커맨드이름)에 대해 처리 작업을 반복한다.
		Iterator keyIter = prop.keySet().iterator();
		while(keyIter.hasNext()) {
			// 프로퍼티 키(=이름)을 URI이름 또는 커맨드이름으로 사용한다.
			String command = (String)keyIter.next();
			// URI이름 또는 커맨드이름에 해당하는 핸들러클래스이름(handlerClassName)을 Properties에서 구한다.
			String handlerClassName = prop.getProperty(command);
			try {
				// 핸들러 클래스 이름(handlerClassName)을 이용해서 Class객체를 구해서 handlerClass변수에 대입
				Class<?> handlerClass = Class.forName(handlerClassName);
				// 앞서 구했던 handlerClass로부터 핸들러 객체를 생성한다.
				// 즉, 앞서 구했던 핸들러클래스이름(handlerClassName)에 해당하는 핸들러클래스의 객체를 생성한다.
				CommandHandler handlerInstance = (CommandHandler) handlerClass.newInstance();
				// commandHandlerMap에  (핸들러클래스주소에 매칭되는 URI, 해당 핸들러클래스로 생성된 핸들러객체) 매핑 정보를 저장한다.
				commandHandlerMap.put(command, handlerInstance);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e){
				throw new ServletException(e);
			}
		}
	}

	/* HttpServlet클래스에 있는 메서드오버라이딩한 doGet()메서드 
	 * : Get방식으로 process()메서드 실행함 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	/* HttpServlet클래스에 있는 메서드오버라이딩한 doPost()메서드 
	 * : Post방식으로 process()메서드 실행함 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}
	
	/* process()메서드
	 * : 전체 URL경로를 가져오고 잘라서, init()메서드에서 저장한 commandHandlerMap변수를 이용해서, 전체 URL경로에 해당하는 핸들러객체를 구한다.
	 * → 앞서 구한 핸들러객체의 process()메서드를 호출(사용)해서 요청을 처리하고,결과로 보여줄 뷰 페이지 경로를 리턴값(request)으로 전달 받고, viewPage변수에 대입(저장)함
	 * → viewPage가 null이 아니고 예외처리가 일어나지 않은 경우, 핸들러인스턴스가 리턴한 뷰 페이지 경로(=viewPage)로 이동한다.
	 * 즉, 전체 URL경로를 가져오고 잘라서 전체 URL경로에 해당하는 핸들러객체를 구하고, 핸들러객체의 process()메서드를 호출(사용)해서 요청을 처리하고, 핸들러인스턴스가 리턴한 뷰 페이지 경로(=viewPage)로 이동한다.*/
	private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
				
				// request.getRequestURI()를 통해 전체 URL경로를 가져와서 command에 대입함
				String command = request.getRequestURI();
				 /*예를들어 전체 URL경로는 http://localhost:9994/board/join.do 라면
				     요청 URI(request.getRequestURI()) 값 = /chap18/hello.do
				     컨텍스트 경로(request.getContextPath()) 값 = /board/ */
				/* 만약에, command변수의 request.getContextPath()의 indexOf값이 0과 같다면,
				 /* 다시말해, 만약에, command변수 내에 컨텍스트 경로값(/board/)이 맨처음(index==0)에 존재한다면, */
				if(command.indexOf(request.getContextPath())==0) {
					/* command변수에 substring()메서드를 통해 컨텍스트 경로값(/board/)을 잘라서 대입(저장)함 */
					command = command.substring(request.getContextPath().length());
					 /* 위에서 컨텍스트 경로의 크기(=request.getContextPath().length())는 7('/board/'의 갯수)을 나타냄
					 그러므로, command 변수에 있는 컨텍스트 경로를 substring(7)로 해서, 컨텍스트 경로를 잘라줌(제거함)  
					 즉, 요청 URI에서 컨텍스트 경로(request.getContextPath())부분을 잘라줌(제거함)으로써,
					 웹 어플리케이션 내에서의  요청 URI만을 사용하게 하기 위함임. */
				}
				
				// init()메서드에서 저장한 commandHandlerMap변수에서 요청을 처리할 핸들러객체를 구한다. command(파라미터 값)을 키로 사용한다.
				CommandHandler handler = commandHandlerMap.get(command);
				// 명령어에 해당하는 핸들러 객체가 존재하지 않을 경우 NullHandler를 사용한다.
				if(handler == null) {
					// NullHandler 클래스는 404 에러를 응답으로 전송하는 핸들러 클래스
					handler = new NullHandler();
				}
				String viewPage = null;
				try {
					/*앞서 구한 핸들러인스턴스(=핸들러객체)인 handler의 process()메서드는 클라이언트의 요청을 알맞게 처리한 후,
					 결과로 보여줄 뷰 페이지 경로를 request(또는 session)의 속성에 저장하고, viewPage변수에 대입(저장)함 */
					 /*즉, 앞서 구한 핸들러객체의 process()메서드를 호출(사용)해서 요청을 처리하고,결과로 보여줄 뷰 페이지 경로를 리턴값(request)으로 전달 받고, viewPage변수에 대입(저장)함 */
					viewPage = handler.process(request, response);
				} catch(Exception e) { //예외처리 함
					throw new ServletException(e);
				}
				
				// viewPage가 null이 아니고 예외처리가 일어나지 않은 경우, 핸들러인스턴스가 리턴한 뷰 페이지 경로(=viewPage)로 이동한다.
				if(viewPage != null) {
					RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
					dispatcher.forward(request, response);
				}
	}
	
}
