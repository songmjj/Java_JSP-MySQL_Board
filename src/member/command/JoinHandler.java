package member.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.service.DuplicateIdException;
import member.service.JoinRequest;
import member.service.JoinService;
import mvc.command.CommandHandler;
/* JoinHandler클래스(모델)
 * : CommandHandler인터페이스를 구현하여 process()메서드를 오버라이딩해서 '회원가입 기능'요청이 오면, '회원가입 기능'요청 처리하는 클래스 
 * 즉, '회원가입 기능'요청이 오면, 뷰(joinForm.jsp,joinSuccess.jsp)와 모델(JoinRequest.java, JoinService.java) 등을 이용하여 요청 처리함 */
/* JoinHandler클래스 구동 순서
 * : JoinHandler클래스의 process()메서드 실행 구동
 *  → GET방식으로 요청이 오면 processForm()메서드 실행 → joinForm.jsp를 리턴
 *  → POST방식으로 요청이 오면 processSubmit()메서드 실행  → 에러 및 아이디중복예외(DuplicateIdException)처리 or 회원가입 처리하고 joinSuccess.jsp를 리턴함  */
public class JoinHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/joinForm.jsp";
	private JoinService joinService = new JoinService();
	
	/* process()메서드 
	 * : 회원가입 기능을 하는 메서드
	 * → GET방식으로 요청이 오면 processForm()메서드 실행 → joinForm.jsp를 리턴
	 *  → POST방식으로 요청이 오면 processSubmit()메서드 실행  → 에러 및 아이디중복예외(DuplicateIdException)처리 or 회원가입 처리하고 joinSuccess.jsp를 리턴함 */
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}
	
	/* processForm()메서드 
	 * joinForm.jsp를 리턴함 */
	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW;
	}

	/* processForm()메서드 
	 * 에러 및 아이디중복예외(DuplicateIdException)처리 or 회원가입 처리하고 joinSuccess.jsp를 리턴함  */
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) {
		// joinReq인스턴스에 회원정보 저장
		JoinRequest joinReq = new JoinRequest();
		joinReq.setId(req.getParameter("id"));
		joinReq.setName(req.getParameter("name"));
		joinReq.setPassword(req.getParameter("password"));
		joinReq.setConfirmPassword(req.getParameter("confirmPassword"));
		
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);
		// validate()메서드 : 회원정보 중에 비어있는것 유무를 체크함
		joinReq.validate(errors);
		// 만약 비어있으면 다시 joinForm.jsp를 리턴함
		if (!errors.isEmpty()) {
			return FORM_VIEW;
		}
		/* 
		 * 
		 */
		try {
			joinService.join(joinReq);
			return "/WEB-INF/view/joinSuccess.jsp";
		} catch (DuplicateIdException e) {
			errors.put("duplicateId", Boolean.TRUE);
			return FORM_VIEW;
		}
	}

}
