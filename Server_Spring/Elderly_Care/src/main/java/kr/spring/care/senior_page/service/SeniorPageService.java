package kr.spring.care.senior_page.service;


import java.util.List;

import kr.spring.care.admin.DTO.CaregiverDTO;
import kr.spring.care.senior_page.dto.GuardianDTO;
import kr.spring.care.senior_page.dto.MatchingDTO;
import kr.spring.care.senior_page.dto.SeniorDTO;
import kr.spring.care.user.entity.Guardian;
import kr.spring.care.user.entity.User;
import kr.spring.care.user_page.dto.UserDTO;

public interface SeniorPageService {
	
	// 나의 정보
	// 기본사항(User)
	public UserDTO myInfo(long userId);
	
	// 특정사항(Senior) 정보
	public SeniorDTO seniorInfo(long userId);
	
	// 보호자(Guardian) 정보
	public GuardianDTO guardianInfo(long userId); 
	
	// 회원정보 수정(기본+특정)
	public void editUser(UserDTO userDTO);
	
	// 비밀번호 수정
	public void editPw(User user);
	
	// 매칭 정보
	public List<MatchingDTO> matchingInfo(long userId);
	

	
	
	
}
