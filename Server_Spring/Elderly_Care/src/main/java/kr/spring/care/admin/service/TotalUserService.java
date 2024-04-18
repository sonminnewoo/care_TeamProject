package kr.spring.care.admin.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.spring.care.user.entity.User;
import kr.spring.care.user_page.dto.UserDTO;

public interface TotalUserService {
	
	// 유저리스트(페이징+검색)
	public Page<UserDTO> userPaging(Pageable pageable, String field, String word);
	
	// 권한 변경
	public void authChange(long userId, User user);
	
	// 전체유저 수
	public long countAllUser();
	
	// 유저 수(검색)
	public long countUser(String field, String word);
	
	// 유저 상세정보(모달)
	public UserDTO userView(long userId);
	
	// 권한 변경(모달)
	public void updateRole(long userId, String role);
	
	// 유저 삭제
	public void deleteUser(long userId);
	

}
