package member.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.service.User;
import member.service.ChangePasswordService;
import member.service.InvalidPasswordException;
import member.service.MemberNotFoundException;
import mvc.command.CommandHandler;

public class ChangePasswordHandler implements CommandHandler {
	private static final String FORM_VIEW = "/WEB-INF/view/changePwdForm.jsp";
	// 상수로 FORM_VIEW 라는 객체에 /WEB-INF/view/changePwdForm.jsp값으로 선언한다.
	private ChangePasswordService changePwdSvc = new ChangePasswordService();
	// ChangePasswordService의 객체 changePwdSvc를 만든다.
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) 
	throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			// GET을 요청시 폼을 위한 뷰 의 경로에 대한  processForm메서드를 호출한다.
			// equalsIgnoreCase는 대소문자 관계 없이(ignore case) equals 검사를 해줍니다.
			return processForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
			//post 요청시 폼 전송 processSubmit 메서드을 호출한다.
			
		} else { // 위의 경우가 아니라면  SC_METHOD_NOT_ALLOWED 에러처리를 한다.
			res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	// processForm 메서드 호출시/WEB-INF/view/changePwdForm.jsp로 리턴한다. 
	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW; 
	}


	private String processSubmit(HttpServletRequest req, HttpServletResponse res)
	throws Exception {
		
		// 세션에서 authUser 속성에 담긴 사용자 정보를 구한다.	
		User user = (User)req.getSession().getAttribute("authUser");
		
		// 에러 정보를 담을 맵 객체를 생성하고 errors 속성에 사용자저장한다.
		Map<String, Boolean> errors = new HashMap<>(); 
		req.setAttribute("errors", errors);

		String curPwd = req.getParameter("curPwd"); // 매개변수로 받은 현재암호를 curPwd 변수에 저장한다
		String newPwd = req.getParameter("newPwd"); // 매개변수로 받은 바꿀암호를 newPwd 변수에 저장한다
		
		// 파라미터로 받은 curPwd, newPwd가 빈칸이거나 값이 없는 경우 
		// errors객체에 에러코드를 추가한다.
		if (curPwd == null || curPwd.isEmpty()) {
			errors.put("curPwd", Boolean.TRUE);
		}
		if (newPwd == null || newPwd.isEmpty()) {
			errors.put("newPwd", Boolean.TRUE);
		}
		// 에러가 존재한다면 새로고침형식으로 다시 폼을 위한 뷰로 리턴하게된다.
		if (!errors.isEmpty()) {
			return FORM_VIEW;
		}
		
		try { // ChangePasswordService의 객체 changePwdSvc로 암호변경 메서드 changePassword를 실행한다.
			changePwdSvc.changePassword(user.getId(), curPwd, newPwd);
			// 암호 변경에 성공하면 /WEB-INF/view/changePwdSuccess.jsp로 리턴한다.
			return "/WEB-INF/view/changePwdSuccess.jsp";
			// 현재 암호가 정확하지 않으면 InvalidPasswordException 예외가 발생하고
		} catch (InvalidPasswordException e) {
			// 에러객체에 에러코드를 추가하고
			errors.put("badCurPwd", Boolean.TRUE);
			// 새로고침형식으로 다시 폼을 위한 뷰로 리턴하게된다.
			return FORM_VIEW;
			// 암호를 변경할 회원 아이디가 존재하지 않는 경우 MemberNotFoundException 예외를 발생시킨다.
		} catch (MemberNotFoundException e) {
			// 잘못된 요청을 의미하는 SC_BAD_REQUEST에러 코드를 요청에 대한 응답으로 전송한다.
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
	}

}
