package article.model;

import java.util.Date;

public class Article {
	private Integer article_no;
	private Writer writer;
	private String title;
	private Date regDate;
	private Date moddate;
	private int read_cnt;
	
	public Article(Integer article_no, Writer writer, String title, Date regDate, Date moddate, int read_cnt) {
		this.article_no = article_no;
		this.writer = writer;
		this.title = title;
		this.regDate = regDate;
		this.moddate = moddate;
		this.read_cnt = read_cnt;
	}
	public int getArticle_no() {
		return article_no;
	}
	public Writer getWriter() {
		return writer;
	}
	public String getTitle() {
		return title;
	}
	public Date getRegDate() {
		return regDate;
	}
	public Date getModdate() {
		return moddate;
	}
	public int getRead_cnt() {
		return read_cnt;
	}
	
	
}


