package auth.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.service.LoginService;
import auth.service.User;
import mvc.command.CommandHandler;

public class LoginHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/loginForm.jsp"; //이건 그냥 상수임
	private LoginService loginService = new LoginService(); //로그인 서비스를 참조하는 객체

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) 
	throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {// url에서 ? 뒤에 입력하는 get 방식
			return processForm(req, res);//사실 get방식을 사용하지 않기 때문에 없어도 되지 않나 싶다
		} else if (req.getMethod().equalsIgnoreCase("POST")) {// 폼에서 로그인을 시도하는 경우 post 방식으로 오게됨
			return processSubmit(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);//이상한경우 에러
			return null;
		}
	}

	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW;//겟방식은 url에 넣으면 input에 넣어짐 그래서 그냥 폼 다시 불러오는듯
	}

	private String processSubmit(HttpServletRequest req, HttpServletResponse res) 
	throws Exception {//post 방식으로 온 경우 submmit을 누른경우
		String id = trim(req.getParameter("id"));//트림은 공백을 지우는 메서드다
		String password = trim(req.getParameter("password"));//비밀번호받고

		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);//errors의 속성값을 "errors" 로 지정
		// 요청이 오거나 갈때 jsp에서 "erroer"로 오는 정보가 Map 객체인 errors에 속성으로 지정되는거임
		if (id == null || id.isEmpty())//아이디가 없나?
			errors.put("id", Boolean.TRUE);
		if (password == null || password.isEmpty())//패스워드가 없다?
			errors.put("password", Boolean.TRUE);
		if (!errors.isEmpty()) {//에러가 있다면?
			return FORM_VIEW;//다시 폼 뷰 실행
		}

		try {//위에서 문제가 없는 경우 이 트라이문이 실행됨
			User user = loginService.login(id, password);//로그인 서비스에 문제없는 아이디와 비밀번호를 넣는다.
			req.getSession().setAttribute("authUser", user);//db 세션에 입력된 유저의 아이디 정보가 유저고 그 유저가 "authUser"라는 속성에 저장
			//"authUser" 유저는 index 파일에서  ${authUser.name}게 표현됨 
			res.sendRedirect(req.getContextPath() + "/index.jsp");//getContextPath()는 프로젝트 페스를 따는 메서드고 sendRedirect는 어떤 주소로 보낸다는 뜻임
			//요약하면 현재프로젝트 페스 + /index.jsp => http://localhost:0000/javajspboard/ + index.jsp
			return null;
		} catch (RuntimeException e) {//원래 로그인 에러 클래스였는데 런타임으로 바꿈
			errors.put("idOrPwNotMatch", Boolean.TRUE);//아이디나 비밀번호가 안맞을경우
			return FORM_VIEW;//다시 로그인창 보여줌
		}
	}

	private String trim(String str) {//공백이 있으면 true임 str.trim()가 실행됨  없으면 안지움
		return str == null ? null : str.trim();
	}
}
