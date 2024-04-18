package kr.spring.care.main.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import kr.spring.care.senior_page.repository.SeniorPageRepository;
import kr.spring.care.senior_page.service.SeniorPageService;
import kr.spring.care.user.entity.User;
import kr.spring.care.user_page.service.UserPageService;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

	private final UserPageService userPageService;
	
	@ModelAttribute("loginId")
	public Long userId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
		// 여기서 authentication.getPrincipal()이 "anonymousUser" 문자열일 수 있으므로 이를 체크합니다.
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            
        	// authentication.getName()<- 이메일을 통해 사용자 식별 정보를 얻습니다.
            User user = userPageService.getUser(authentication.getName());
            
            // user가 null이 아닐 경우에만 userId를 반환합니다.
            if (user != null) {
                System.out.println("로그인유저: " + user);
                return user.getUserId();
            }
        }
        // 로그인하지 않았거나, user 객체를 찾을 수 없는 경우 -1L 또는 다른 적절한 기본값을 반환합니다.
        System.out.println("로그인유저: -1L");
        return -1L;
    }
	
	
}
