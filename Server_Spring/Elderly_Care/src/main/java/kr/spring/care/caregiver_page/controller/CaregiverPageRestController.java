package kr.spring.care.caregiver_page.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
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

import kr.spring.care.admin.DTO.CaregiverDTO;
import kr.spring.care.caregiver_page.service.CaregiverPageService;
import kr.spring.care.matching.constant.MatchingStatus;
import kr.spring.care.senior_page.dto.MatchingDTO;
import kr.spring.care.senior_page.dto.MatchingResponse;
import kr.spring.care.user_page.dto.UserDTO;
import lombok.RequiredArgsConstructor;

@RequestMapping("/m/caregiverPage/*")
@RestController
@RequiredArgsConstructor
public class CaregiverPageRestController {
	
	private final CaregiverPageService caregiverPageService;
	
	// 기본사항(User) + 특정사항(Caregiver) 정보
	@GetMapping("caregiverInfo/{id}")
	public ResponseEntity<UserDTO> myinfo(@PathVariable("id") long userId, Model model) {
		UserDTO userInfo = caregiverPageService.myInfo(userId);
		CaregiverDTO caregiverInfo = caregiverPageService.caregiverInfo(userId);
		
		if(caregiverInfo == null) {
			caregiverInfo = new CaregiverDTO();
			caregiverInfo.setAvailableHours("");
			caregiverInfo.setCertification("");
			caregiverInfo.setExperience("");
			caregiverInfo.setExperienceYears(0);
			caregiverInfo.setSpecialization("");
		} else {
			userInfo.setRoleStr(userInfo.getRole().toString());
			userInfo.setAvailableHours(caregiverInfo.getAvailableHours());
			userInfo.setCaregiverId(caregiverInfo.getCaregiverId());
			userInfo.setCertification(caregiverInfo.getCertification());
			userInfo.setExperience(caregiverInfo.getExperience());
			userInfo.setExperienceYears(caregiverInfo.getExperienceYears());
			userInfo.setSpecialization(caregiverInfo.getSpecialization());
		}
		
		return new ResponseEntity<UserDTO>(userInfo, HttpStatus.OK); 
	}
	
	@PutMapping("edit")
	@ResponseBody
	public String edit(@RequestBody UserDTO userDTO) {
		caregiverPageService.editUser(userDTO);
		System.out.println();
		return userDTO.getEmail();
	}
	
	
	
	@GetMapping("matchingInfo/{id}")
	public ResponseEntity<MatchingResponse> matchingInfo(@PathVariable long id) {
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
	    
	    List<MatchingDTO> filteredPastMatchings = formattedMatchings.stream()
	            .filter(m -> m.getStatus() == MatchingStatus.COMPLETED || m.getStatus() == MatchingStatus.CANCELLED)
	            .collect(Collectors.toList());

	    List<MatchingDTO> filteredProgressMatchings = formattedMatchings.stream()
	            .filter(m -> m.getStatus() == MatchingStatus.POSTED || m.getStatus() == MatchingStatus.REQUESTED || m.getStatus() == MatchingStatus.IN_PROGRESS)
	            .collect(Collectors.toList());
	    
	    MatchingResponse response = new MatchingResponse(filteredPastMatchings, filteredProgressMatchings);
	    
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
}
