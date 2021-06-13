package article.service;


//게시글을 삭제하기 위해 수정할 데이터가 필요해서 이 클래스를 만든다.
//수정과 다른점은 삭제할 때 자신이 접속한 아이디(해당 아이디만 삭제 가능)와 번호만 있어도 된다.
public class DeleteRequest {
	private String userId; //사용자 아이디
	private int articleNumber;//게시글 번호

	
	
	public DeleteRequest(String userId, int articleNumber) {

		this.userId = userId;
		this.articleNumber = articleNumber;
	}


	public String getUserId() {
		return userId;
	}


	public int getArticleNumber() {
		return articleNumber;
	}


	
	
	
}
