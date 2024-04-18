package kr.spring.care.user_page.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kr.spring.care.board.model.Board;
import kr.spring.care.board.service.BoardService;
import kr.spring.care.user_page.dto.UserDTO;
import kr.spring.care.user_page.service.UserPageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mobile/userPage/*") // baseUrl에 맞춰서
public class UserPageRestController {
	
	private final UserPageService userPageService;

	
	@PostMapping("saveUser") // 안드 interface REST API Url에 맞춰서
	public ResponseEntity<String> insert(@RequestBody UserDTO user){ // @RequestBody 가 꼭 필요한지는 아직 잘 모르겠음.. 
		userPageService.insertUser(user);
		return new ResponseEntity("Insert Successfully",HttpStatus.OK);
	}
	
	// 객체 받환시 String -> UserDTO, "Insert Successfully" -> 객체 변수명

	
	


}
