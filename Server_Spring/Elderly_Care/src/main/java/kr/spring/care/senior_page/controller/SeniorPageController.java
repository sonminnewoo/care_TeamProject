package kr.spring.care.senior_page.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.ipc.http.HttpSender.Response;
import kr.spring.care.matching.constant.MatchingStatus;
import kr.spring.care.senior_page.dto.GuardianDTO;
import kr.spring.care.senior_page.dto.MatchingDTO;
import kr.spring.care.senior_page.dto.SeniorDTO;
import kr.spring.care.senior_page.service.SeniorPageService;
import kr.spring.care.user.entity.User;
import kr.spring.care.user_page.dto.UserDTO;
import kr.spring.care.user_page.service.UserPageService;
import lombok.RequiredArgsConstructor;

@RequestMapping("/seniorPage/*")
@Controller
@RequiredArgsConstructor
public class SeniorPageController {
	
	private final UserPageService userPageService;
	private final SeniorPageService seniorPageService; 
	
	// 기본사항(User) + 특정사항(Senior) 정보
	@GetMapping("myinfo/{id}")
	public String myinfo(@PathVariable("id") long userId, Model model) {
		UserDTO userInfo = seniorPageService.myInfo(userId);
		SeniorDTO seniorInfo = seniorPageService.seniorInfo(userId);
		GuardianDTO guardianInfo = seniorPageService.guardianInfo(userId);
		if (guardianInfo == null) {
			guardianInfo = new GuardianDTO(); // GuardInfo는 당신의 객체 타입
		    // 필요하다면 기본값 설정
			guardianInfo.setGuardianName("");
			guardianInfo.setGuardianPhoneNumber("");
			guardianInfo.setRelationship("");
		}
		model.addAttribute("myInfo", userInfo);
		model.addAttribute("seniorInfo", seniorInfo);
		model.addAttribute("guardInfo" ,guardianInfo);
		return "seniorPage/myinfo";
	}
	
	// 회원정보 수정
	@PutMapping("edit")
	@ResponseBody
	public String edit(@RequestBody UserDTO userDTO) {
		seniorPageService.editUser(userDTO);
		return userDTO.getEmail();
	}
	
	// 비밀번호 변경
	@PutMapping("editPw")
	@ResponseBody
	public String editPw(@RequestBody User user) {
		userPageService.editPw(user);
		return "success";
	}
	
	@GetMapping("matchingInfo/{id}")
	public String matchingInfo(@PathVariable long id, Model model) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	    
	    List<MatchingDTO> allMatchings = seniorPageService.matchingInfo(id);
	    
	    // 변환 작업을 여기에서 수행
	    List<MatchingDTO> formattedMatchings = allMatchings.stream().map(matching -> {
	        // LocalDate 형식의 날짜 필드를 문자열로 변환, null 체크
	        if (matching.getStartDate() != null) {
	            matching.setStartDateStr(matching.getStartDate().format(dateFormatter));
	        }
	        if (matching.getEndDate() != null) {
	            matching.setEndDateStr(matching.getEndDate().format(dateFormatter));
	        }
	        
	        // LocalTime 형식의 시간 필드를 문자열로 변환, null 체크
	        if (matching.getStartTime() != null) {
	            matching.setStartTimeStr(matching.getStartTime().format(timeFormatter));
	        }
	        if (matching.getEndTime() != null) {
	            matching.setEndTimeStr(matching.getEndTime().format(timeFormatter));
	        }
	        return matching; // 한 번만 반환
	    }).collect(Collectors.toList());
	    
	    List<MatchingDTO> filteredMatchings = formattedMatchings.stream()
	            .filter(m -> m.getStatus() == MatchingStatus.COMPLETED || m.getStatus() == MatchingStatus.CANCELLED)
	            .collect(Collectors.toList());

	    List<MatchingDTO> filteredMatchings2 = formattedMatchings.stream()
	            .filter(m -> m.getStatus() == MatchingStatus.POSTED || m.getStatus() == MatchingStatus.REQUESTED || m.getStatus() == MatchingStatus.IN_PROGRESS)
	            .collect(Collectors.toList());
	    model.addAttribute("matchings", filteredMatchings);
	    model.addAttribute("matchings2", filteredMatchings2);

	    return "seniorPage/matchingInfo";
	}
	
	

	
	
	/*
	 * @GetMapping("matchingInfo/{id}") public String matchingInfo(@PathVariable
	 * long id ,Model model) { List<MatchingDTO> matchingInfo =
	 * seniorPageService.matchingInfo(id);
	 * 
	 * model.addAttribute("matchings", matchingInfo); model.addAttribute("isEmpty",
	 * matchingInfo.isEmpty()); matchingInfo.forEach(matching ->
	 * System.out.println(matching)); // List<CaregiverDTO> careInfo =
	 * seniorPageService.careInfo(); return "seniorPage/matchingInfo"; }
	 */
	
	
	
	
}
