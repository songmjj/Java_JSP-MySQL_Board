package article.service;

import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;
//Modify를 이용해 게시글 수정 기능을 제공하는 클래스이다.
public class ModifyArticleService {
	private ArticleDao articleDao = new ArticleDao();
	private ArticleContentDao contentDao = new ArticleContentDao();
	
	
	public void modify(ModifyRequest modReq){
		Connection conn = null;
		try {
			//수정기능을 사용하기 위해 DB를 이용한다.
			conn = ConnectionProvider.getConnection();
			//Connection 기본값이 true인데 자동으로 커밋된다. false로 해주는 이유는 오류도 커밋해 버리기 때문에 문제가 있어도 오류처리 안된다.
			conn.setAutoCommit(false);
			//selectById에 넣는 이유는 해당 게시글 번호를 구하기 위해 article 객체를 구한다. 
			Article article = articleDao.selectById(conn, modReq.getArticleNumber());
			
			//해당  게시글 번호가 없으면 게시글을 찾을 수없는 에러를 발생시킨다.
			if (article == null) {
				throw new ArticleNotFoundException();
			}
			//수정하려는 사용자가 해당 게시글을 수정할 수 없으면 허가를 거부하는 에러를 발생시킨다.
			if (!PermissionCheck.canModify(modReq.getUserId(), article)) {
				throw new PermissionDeniedException();
			}
			//두 DAO의 update() 메서드를 이용해서 제목과 내용을 수정한다. articleDao의 update() 메서드는 게시글 번호와 제목이 필요하고
			// contentDao의 update() 메서드는 게시글 번호와 내용이 필요하다.
			articleDao.update(conn, modReq.getArticleNumber(),modReq.getTitle());
			contentDao.update(conn, modReq.getArticleNumber(),modReq.getContent());
			conn.commit();
			
		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
//			throw new RuntimeException();
		} catch (PermissionDeniedException e) {
			JdbcUtil.rollback(conn);
			throw e;
		}finally {
			JdbcUtil.close(conn);
		}
	}

//	//게시글을 수정할 수 있는지 검사해주는 메서드이다.
//	private boolean canModify(String modfyingUserId, Article article) {
//		//아이디가 일치해서 읽어들어 올 수 있음을 리턴한다.
//		return article.getWriter().getId().equals(modfyingUserId);
//	}
}
