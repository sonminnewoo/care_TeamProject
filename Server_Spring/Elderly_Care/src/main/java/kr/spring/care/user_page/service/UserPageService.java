package kr.spring.care.user_page.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import kr.spring.care.user.entity.User;
import kr.spring.care.user_page.dto.UserDTO;

public interface UserPageService {

	// 전역 아이디용
	public User getUser(String email);
	
	public UserDTO myInfo(long userId);

	public void editUser(UserDTO user);
	
	public void editPw(User user);
	
	public void insertUser(UserDTO user);
	
	
	///// 테스트용
	public void regUSer(UserDTO user);
	
}
