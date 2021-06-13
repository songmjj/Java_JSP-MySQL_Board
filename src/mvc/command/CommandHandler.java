package mvc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
				//CommandHandler 인터페이스(모델)
public interface CommandHandler {
	/* 명령어 핸들어 클래스는 process 메서드를 이용해서 알맞은 로직 코드를 구현하고,
	     결과를 보여줄 JSP의 URI를 리턴함 */
	/* 모든 명령어 핸들러 클래스가 공통으로 구현해야 하는 process()메서드 선언*/
	public String process(HttpServletRequest req, HttpServletResponse res) 
		throws Exception;
}
