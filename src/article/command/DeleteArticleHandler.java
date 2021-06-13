package article.command;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticleData;
import article.service.ArticleNotFoundException;
import article.service.DeleteArticleService;
import article.service.DeleteRequest;
import article.service.PermissionCheck;
import article.service.ReadArticleService;
import auth.service.User;
import mvc.command.CommandHandler;


public class DeleteArticleHandler implements CommandHandler {

	private ReadArticleService readService = new ReadArticleService();
	private DeleteArticleService deleteService = new DeleteArticleService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
			try {
			//게시판 번호를 받아온다.
			String noVal = req.getParameter("no");
			// int형으로형변환시켜줌
			int no = Integer.parseInt(noVal);
			// 해당 게시물을 구하고 조회수는 증가시키지 않는다.
			ArticleData articleData = readService.getArticle(no, false);
			// 게시글 삭제를 요청한 사용자 정보를 구한다
			User authUser = (User) req.getSession().getAttribute("authUser");
			//사용자 아이디랑 게시물 사용자랑 비교한다.
			if (!PermissionCheck.canModify(authUser.getId(), articleData.getArticle())) {
										// 서버 응답 실행 거부 403 응답 선송
				res.sendError(HttpServletResponse.SC_NOT_FOUND);
				return null;
			}
			DeleteRequest delReq = new DeleteRequest(authUser.getId(), no);
			deleteService.delete(delReq);
			
			return "/WEB-INF/view/deleteSuccess.jsp";
			// 게시글이 존재하지 않으면  ArticleNotFoundException 예외처리를 발생하고
		} catch (ArticleNotFoundException e) {
									  	   // 404 응답 코드 전송(요청한 자원이 존재하지 않음)
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}

}
