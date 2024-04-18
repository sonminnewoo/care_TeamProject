package kr.spring.care.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.spring.care.user.dto.LoginResponseDto;
import kr.spring.care.user.dto.UserFormDto;
import kr.spring.care.user.entity.User;
import kr.spring.care.user.service.UserService;

@RestController
@RequestMapping("/m/user")
@CrossOrigin(origins = "http://10.0.2.16")
public class RestUserController {
	@Autowired
	UserService userService;
	@Autowired
	PasswordEncoder passwordEncoder;
	
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginId(@RequestBody User user) {
    	LoginResponseDto loginResponse = userService.mobileLogin(user, passwordEncoder);
        if (loginResponse != null) {
            return ResponseEntity.ok(loginResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
	
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@Valid @RequestBody UserFormDto userFormDto,
	                                           BindingResult result,
	                                           @RequestParam(required = false) String userType) {
	    if (result.hasErrors()) {
	        return ResponseEntity.badRequest().body("입력 정보를 확인해주세요");
	    }
	    try {
	        User user = User.createUser(userFormDto, passwordEncoder);
	        userService.saveUser(user);
	    } catch (IllegalStateException e) {
	        return ResponseEntity.badRequest().body("입력 정보를 확인해주세요");
	    }
	    return ResponseEntity.ok("회원가입 성공");
	}
}