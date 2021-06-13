package article.service;

import java.util.Map;

//게시글을 수정하기 위해 수정할 데이터가 필요해서 이 클래스를 만든다.
public class ModifyRequest {
	private String userId; //사용자 아이디
	private int articleNumber;//게시글 번호
	private String title;//제목
	private String content; //내용
	
	
	public ModifyRequest(String userId, int articleNumber, String title, String content) {
		this.userId = userId;
		this.articleNumber = articleNumber;
		this.title = title;
		this.content = content;
	}


	public String getUserId() {
		return userId;
	}


	public int getArticleNumber() {
		return articleNumber;
	}


	public String getTitle() {
		return title;
	}


	public String getContent() {
		return content;
	}
	//제목이 없으면(articleDao 클래스에 값이 없으면) 수정할 수 없으므로 에러가 나게 한다.
	public void validate(Map<String, Boolean> errors) {
		if (title ==null||title.trim().isEmpty()) {
			errors.put("title", Boolean.TRUE);
		}
		
	}
	
}
