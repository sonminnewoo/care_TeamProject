package kr.spring.care.caregiver_page.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.spring.care.admin.DTO.CaregiverDTO;
import kr.spring.care.caregiver_page.service.CaregiverPageService;
import kr.spring.care.matching.constant.MatchingStatus;
import kr.spring.care.senior_page.dto.MatchingDTO;
import kr.spring.care.user_page.dto.UserDTO;
import lombok.RequiredArgsConstructor;

@RequestMapping("/caregiverPage/*")
@Controller
@RequiredArgsConstructor
public class CaregiverPageController {
	
	private final CaregiverPageService caregiverPageService;
	
	// 기본사항(User) + 특정사항(Caregiver) 정보
	@GetMapping("myinfo/{id}")
	public String myinfo(@PathVariable("id") long userId, Model model) {
		UserDTO userInfo = caregiverPageService.myInfo(userId);
		CaregiverDTO caregiverInfo = caregiverPageService.caregiverInfo(userId);
		model.addAttribute("myInfo", userInfo);
		model.addAttribute("caregiverInfo", caregiverInfo);
		return "caregiverPage/myinfo";
	}
	
	@PutMapping("edit")
	@ResponseBody
	public String edit(@RequestBody UserDTO userDTO) {
		caregiverPageService.editUser(userDTO);
		System.out.println();
		return userDTO.getEmail();
	}
	
	
	
	@GetMapping("matchingInfo/{id}")
	public String matchingInfo(@PathVariable long id, Model model) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	    
	    List<MatchingDTO> allMatchings = caregiverPageService.matchingInfo(id);
	    
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
		return "caregiverPage/matchingInfo";
	}
	
	
}
