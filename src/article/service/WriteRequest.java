package article.service;

import java.util.Map;

import article.model.Writer;

public class WriteRequest {

	private Writer writer;
	private String title;
	private String content;
	
	public WriteRequest(Writer writer, String title, String content) {
		this.writer = writer;
		this.title = title;
		this.content = content;
	}

	public Writer getWriter() {
		return writer;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}
	
	//데이터 유요여부 검사로 title이 비면 errors맵 객체에 관련 코드 추가한다.
	public void validate(Map<String, Boolean> errors) {
		if(title==null || title.trim().isEmpty()) {
		errors.put("title",Boolean.TRUE);	
		}
	}
	
	
}
