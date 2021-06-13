package article.service;

import java.util.List;

import article.model.Article;

public class ArticlePage {
	
	private int total; // 전체 게시글 갯수
	private int currentPage; // 사용자가 요청한 페이지
	private List<Article> content; //화면에 출력할 게시글
	private int totalPages; // 전체페이지 수
	private int startPage; //페이징 시작번호
	private int endPage; //페이징 마지막번호
	public ArticlePage(int total, int currentPage,int size, List<Article> content) {
		this.total = total;
		this.currentPage = currentPage;
		this.content = content;
		if(total==0) { // 전체 게시글이 0 이라면
			totalPages = 0; // 전체페이지 0
			startPage = 0; // 페이징 시작번호 0 
			endPage = 0; // 페이징 마지막번호 0
		}else { // 전체 게시글이 0이 아니라면
			totalPages = total/size; // 전체페이지는 전체게시글/size size=ListArticleService에서 10으로 값을 지정해줌
			if(total % size >0) { // total/size 나머지가 0보다 클경우
				totalPages++; // totalPages=totalPages+1;
			}
			int modVal = currentPage % 5; // modVal 사용자요청페이지/5 나머지값
			startPage = currentPage / 5 * 5 + 1; // 페이징시작번호 = 사용자 요청한페이지/5*5+1
			if(modVal ==0)startPage -=5; // modVal값이 0 이라면 startPage=startPage-5;
			endPage = startPage + 4; // 페이징마지막번호는 시작페이징번호+4
			if(endPage>totalPages) endPage = totalPages; // 페이징마지막번호가 전체페이지보다 클경우 마지막페이징번호는 전체페이지수
			// 화면 하단에 보여줄 페이지 이동 링크의 시작 페이지 번호를 구한다
			// 화면 하단에 [ 1,2,3,4,5]나[5,6,7,8,9,10]처럼 5개 페이지씩 이동 링크를 출력한다고 가정하면
			// 이경우 현재 페이지가 3이면 시작페이지가 1이되고 현재 페이지가 10이면 시작페이지가 6이된다.
		}
	}
	public int getTotal() {
		return total; // total값 반환
	}
	public int getCurrentPage() {
		return currentPage; // currentPage값 반환
	}
	public List<Article> getContent() {
		return content; // content값 반환
	}
	public int getTotalPages() {
		return totalPages; // totalPages값 반환
	}
	public int getStartPage() {
		return startPage; // startPage값 반환
	}
	public int getEndPage() {
		return endPage; // endPage값 반환
	}
	public boolean hasNoArticles() {
		return total == 0; // total이 0 일경우 true 아니면 false
	}
	public boolean hasArticles() {
		return total > 0; // total 0보다 크면 true 아니면 false
	}
	
}
