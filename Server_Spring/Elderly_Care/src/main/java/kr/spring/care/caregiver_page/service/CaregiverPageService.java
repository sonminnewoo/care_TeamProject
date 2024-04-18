package kr.spring.care.caregiver_page.service;


import java.util.List;

import kr.spring.care.admin.DTO.CaregiverDTO;
import kr.spring.care.senior_page.dto.MatchingDTO;
import kr.spring.care.user.entity.User;
import kr.spring.care.user_page.dto.UserDTO;

public interface CaregiverPageService {
	
	// 기본사항(User)
	public UserDTO myInfo(long userId);
	
	// 특정사항(Senior) 정보
	public CaregiverDTO caregiverInfo(long userId);
	
	// 회원정보 수정(기본+특정)
	public void editUser(UserDTO userDTO);
	
	// 비밀번호 수정
	public void editPw(User user);
	
	// 매칭 정보
	public List<MatchingDTO> matchingInfo(long userId);
	
}
