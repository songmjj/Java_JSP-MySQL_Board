package article.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import javax.management.RuntimeErrorException;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import article.model.ArticleContent;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class WriteArticleService {
	
	private ArticleDao articleDao = new ArticleDao();
	private ArticleContentDao articleContentDao = new ArticleContentDao();
	
	public Integer write(WriteRequest req) {
		Connection conn = null;
		try{
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);

			//insert쿼리가 실행되야 id를 알수 있으므로 toArticle로 number값으로 null을 전달한다. 
			Article article = toArticle(req);
			
			//ArticleAdo로 insert()를 실행하고 결과를 savedArticle에 할당.
			//저장되지 않으면 null값이므로 에러가 발생한다. 
			//성공하면 article테이블에 추가한 pk값인 article_no를 가진다. 
			Article savedArticle = articleDao.insert(conn, article);
			if(savedArticle == null) {
				throw new RuntimeException("실패하였습니다.");
			}
			//saved article의 번호를 가진 articleContent객체를 생성한다. 
			ArticleContent content = new ArticleContent(
					savedArticle.getArticle_no(),req.getContent());
			
			ArticleContent savedContent = articleContentDao.insert(conn, content);
			if(savedContent == null) {
				throw new RuntimeException("내용 입력을 실패하였습니다.");
			}
			//트랜젝션 커밋
			conn.commit();
			//새로 추가한 게시글 번호를 리턴한다.
			return savedArticle.getArticle_no();
			//예외발생 트랜젝션 롤백구현
		}catch(SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		}catch(RuntimeErrorException e) {
			JdbcUtil.rollback(conn);
			throw e;
		}finally {
			JdbcUtil.close(conn);
		}
	}

	private Article toArticle(WriteRequest req) {
		Date now = new Date();
		return new Article(null, req.getWriter(), req.getTitle(), now, now, 0);
		// TODO Auto-generated method stub
	}

}
