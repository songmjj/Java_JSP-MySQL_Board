package article.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.model.Writer;
import article.service.WriteArticleService;
import article.service.WriteRequest;
import auth.service.User;
import mvc.command.CommandHandler;

public class WriteArticleHandler implements CommandHandler {
	private static final String Form_view = "/WEB-INF/view/newArticleForm.jsp";
	private WriteArticleService writeService = new WriteArticleService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
			if(req.getMethod().equalsIgnoreCase("GET")) {
				return processForm(req,res);
			}else if(req.getMethod().equalsIgnoreCase("POST")) {
				return processSubmit(req, res);
			}else {
				res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				return null;
			}

	}

	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		// TODO Auto-generated method stub
		return Form_view;
	}
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) {
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);

		//로그인 정보를 가져온다.
		User user = (User)req.getSession(false).getAttribute("authUser");
		//user와 req를 이용해 writeRequest 객체를 생성한다.
		WriteRequest writeReq = createWriteRequest(user, req);
		//유호검사
		writeReq.validate(errors);
		
		if (!errors.isEmpty()) {
			return Form_view;
		}
		//WriteArticleService를 이용해서 게시글 등록한다.
	int newArticleNo = writeService.write(writeReq);
	req.setAttribute("newArticleNo", newArticleNo);
	return "/WEB-INF/view/newArticleSuccess.jsp";
	
}
	private WriteRequest createWriteRequest(User user, HttpServletRequest req) {
		return new WriteRequest(
				new Writer(user.getId(), user.getName()),
				req.getParameter("title"),
				req.getParameter("content"));				
	}
}


