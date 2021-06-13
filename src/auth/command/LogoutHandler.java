package auth.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mvc.command.CommandHandler;

public class LogoutHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) 
	throws Exception {
		HttpSession session = req.getSession(false);//세션을 만드는것을 끄는것 세션을 만들기 않고 null 반환하기
		if (session != null) {//
			session.invalidate();//invalidate() 세션을 완전히 지우는 메서드 이걸 실행하면 로그인 정보가 기록된 세션이 지워지니 로그아웃되는것
		}
		res.sendRedirect(req.getContextPath() + "/index.jsp"); //로그아웃되었으면 다시 인덱스로 보낸다
		return null;// 위의 주소로 보냈으니 메서드 종료
	}

}
