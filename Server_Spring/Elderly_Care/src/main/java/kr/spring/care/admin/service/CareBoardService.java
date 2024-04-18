package kr.spring.care.admin.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.spring.care.admin.DTO.CaregiverDTO;
import kr.spring.care.user_page.dto.UserDTO;


public interface CareBoardService {
	
	// 요양보호사 구인 리스트
	public Page<CaregiverDTO> listPaging(Pageable pageable, String field, String word);
	
	// 요양보호사 구인 리스트 삭제
	public void deleteList(long userId);
	
	// 요양보호사 전체 리스트 갯수
	public long countAllCare();
	
	// 요양보호사 검색시 리스트 갯수
	public long countCare(String field, String word);
	
	// 요양보호사 구인 상세정보(모달창)
	public CaregiverDTO careView(long caregiverId);
}
