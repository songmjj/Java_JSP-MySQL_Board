package article.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import article.model.ArticleContent;
import jdbc.JdbcUtil;

public class ArticleContentDao {
	
	public ArticleContent insert(Connection conn, ArticleContent content) throws SQLException {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("insert into article_content"+
		"(article_no, content) values(?,?)");
			ps.setLong(1, content.getArticle_no());
			ps.setString(2, content.getContent());
			
			//작성되는 글이 없으면 null 반환
			int insertedCount = ps.executeUpdate();
			if(insertedCount > 0) {
				return content;
			}else {
				return null;
			}
					
					
		}finally {
			JdbcUtil.close(ps);
		}
	}

	public ArticleContent selectById(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(
					"select * from article_content where article_no = ?");
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();
			ArticleContent content = null;
			if (rs.next()) {
				content = new ArticleContent(
						rs.getInt("article_no"), rs.getString("content"));
			}
			return content;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	
	// 파라미터에서 전달받은 게시글 번호와 제목을 이용해서 데이터를 수정한다.
		public int update(Connection conn, int no, String content) throws SQLException {
			try (	//데이터베이스에 연결해 업데이트 쿼리문을 쓴다. 
					PreparedStatement pstmt = 
					conn.prepareStatement(
															
										//articleDao와 다른 점은  article_content인데 테이블 이름이 다르다.
												//첫번쨰 ?는 수정할 게시글의 제목이 들어가고  
							"update article_content set content = ? "+
												// 두번째 ?에는 게시글 번호가 들어간다.					
							"where article_no = ?")) {
				pstmt.setString(1, content);
				pstmt.setInt(2, no);
						//수행결과로 int값이 들어간다. 이 메서드가 인트형이기 때문이다.
				return pstmt.executeUpdate();
			}
		}
		// 파라미터에서 전달받은 게시글 번호를 통해 게시글을 삭제한다.
		public int delete(Connection conn, int no) throws SQLException{
				try(//데이터베이스에 연결해 삭제 쿼리문을 쓴다.
					PreparedStatement pstmt =
					
							//articleDao와 다른 점은 article_content인데 테이블이 다르다.
											//?에 게시글 번호가 들어간다.
					conn.prepareStatement("delete from article_content where article_no=?")){
						pstmt.setInt(1, no);
						//수행결과로 int값이 들어간다. 이 메서드가 인트형이기 때문이다.
						return pstmt.executeUpdate();
				}
			
		}
}
