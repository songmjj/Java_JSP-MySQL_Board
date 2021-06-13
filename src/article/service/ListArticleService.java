package article.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import article.dao.ArticleDao;
import article.model.Article;
import jdbc.connection.ConnectionProvider;

public class ListArticleService {
	
	private ArticleDao articleDao = new ArticleDao(); // ArticleDao 객체 생성
	private int size = 10; // size값은 10
	
	public ArticlePage getArticlePage(int pageNum) { // pageNum에 해당하는 게시글 목록을 구한다.
		try {
			Connection conn = ConnectionProvider.getConnection(); // 
			int total = articleDao.selectCount(conn); // total = select count(*) from article(전체 게시글 숫자)
			List<Article> content = articleDao.select(conn, (pageNum-1)*size,size); //시작행이 0번 기준이므로 pageNum-1을 해준다.
			return new ArticlePage(total, pageNum, size, content); // ArticlePage 인스턴스 를 반환시켜준다.
		}catch (SQLException e) { // SQL 예외처리가 나올경우
			throw new RuntimeException(e); // 런타임익셉션
		}
	}
}
