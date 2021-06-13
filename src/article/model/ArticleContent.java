package article.model;

public class ArticleContent {
	private Integer article_no;
	private String content;
	public ArticleContent(Integer article_no, String content) {
		super();
		this.article_no = article_no;
		this.content = content;
	}
	public Integer getArticle_no() {
		return article_no;
	}
	public String getContent() {
		return content;
	}
	
}
