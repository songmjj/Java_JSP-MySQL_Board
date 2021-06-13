package article.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticlePage;
import article.service.ListArticleService;
import mvc.command.CommandHandler;

public class ListArticleHandler implements CommandHandler {
	private ListArticleService listService = new ListArticleService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String pageNoVal = req.getParameter("pageNo"); // pageNo 파라미터 값을 받아온다.
		int pageNo = 1; // pageNo 초기값은 1
		if(pageNoVal != null) { // pageNoVal null 이 아닐경우
			pageNo = Integer.parseInt(pageNoVal); // integer형으로 형변환 시킨다 String->integer
		}
		ArticlePage articlePage = listService.getArticlePage(pageNo);// ListArticleService에서 지정한 페이지 번호에 해당하는 게시글 데이터를 구한다.
		req.setAttribute("articlePage", articlePage);// 객체를 JSP에서 사용할수있도록 request의 articlePage 속성에 저장한다.
		return "/WEB-INF/view/listArticle.jsp"; // 경로에 있는 jsp를 반환시킨다.
	}

	
}
