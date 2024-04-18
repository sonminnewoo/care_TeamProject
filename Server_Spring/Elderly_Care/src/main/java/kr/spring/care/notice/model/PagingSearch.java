package kr.spring.care.notice.model;

public class PagingSearch {
	private int page; //현재 페이지 번호
	private int recordSize; //페이지 당 출력할 데이터 개수
	private int pageSize; //화면 하단에 출력할 페이지 사이즈
	private String keyword; //검색 키워드
	private String searchType; //검색 유형
	
	public PagingSearch() {
		this.page = 1;
		this.recordSize = 10;
		this.pageSize =10;
	}

	public int getOffset() {
		return (page -1) * recordSize;
	}
}
