package kr.spring.care.admin.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageVO {

	private int totPage;
	private int blockPage;
	private int startPage;
	private int endPage;
	private int currentPage;
	
	public PageVO(int count, int pageSize, int currentPage) {
		totPage = (count/pageSize + (count % pageSize == 0 ? 0 : 1));
		blockPage = 3;
		startPage = ((currentPage-1)/blockPage) * blockPage +1;
		endPage = startPage + blockPage -1;
		if(endPage > totPage) endPage = totPage;
		
		setBlockPage(blockPage);
		setStartPage(startPage);
		setEndPage(endPage);
		setCurrentPage(currentPage);
		setTotPage(totPage);
	}
}
