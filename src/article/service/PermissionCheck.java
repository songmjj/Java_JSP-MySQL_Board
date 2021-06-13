package article.service;

import article.model.Article;

public class PermissionCheck  {
	//게시글을 수정할 수 있는지 검사해주는 메서드이다.
			public static boolean canModify( String userId,  Article article) {
				//아이디가 일치해서 읽어들어 올 수 있음을 리턴한다.
				return article.getWriter().getWrtier_id().equals(userId);
			}
			
			
}
