package kr.spring.care.matching.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.spring.care.matching.constant.MatchingStatus;
import kr.spring.care.matching.dto.CaregiverDetail;
import kr.spring.care.matching.dto.MatchingDetail;
import kr.spring.care.matching.dto.MatchingRequestDto;
import kr.spring.care.matching.entity.Matching;
import kr.spring.care.matching.service.MatchingService;
import kr.spring.care.user.entity.Guardian;
import kr.spring.care.user.entity.Senior;
import kr.spring.care.user.service.CaregiverService;

@Controller
@RequestMapping("/matching")
public class MatchingController {

    @Autowired
    private MatchingService matchingService;
    @Autowired
    private CaregiverService caregiverService;

    // 요양보호사 구인 페이지
    @GetMapping("/findcaregiver")
    public String matchingFindCaregiver(
    		@RequestParam(required = false) String field, 
            @RequestParam(required = false) String word, 
            Pageable pageable, Model model) {
		Page<CaregiverDetail> caregiversPage;
		
		if (field != null && word != null && !field.isEmpty() && !word.isEmpty()) {
		caregiversPage = caregiverService.searchCaregivers(field, word, pageable);
		} else {
		caregiversPage = caregiverService.findCaregiversPageable(pageable);
		}
		
		model.addAttribute("caregiversPage", caregiversPage);
	    model.addAttribute("field", field);
	    model.addAttribute("word", word);

        return "matching/matchingFindCaregiver";
    }

    // 요양보호사 구인 페이지 상세
    @GetMapping("/findcaregiver/detail")
    public String matchingFindCaregiverDetail(@RequestParam Long caregiverId, Model model) {
        CaregiverDetail caregiverDetail = caregiverService.findCaregiverById(caregiverId);
        model.addAttribute("caregiver", caregiverDetail);
        return "matching/matchingFindCaregiverDetail";
    }

    // 요양보호사 구직 페이지
    @GetMapping("/findjob")
    public String matchingFindJob(Model model, Pageable pageable) {
        Page<Matching> matchingsPage = matchingService.findMatchingsPageable(pageable);
        model.addAttribute("matchingsPage", matchingsPage);
        return "matching/matchingFindJob";
    }

    // 요양보호사 구직 페이지 상세
    @GetMapping("/findjob/detail")
    public String matchingFindJobDetail(@RequestParam Long matchingId, Model model) {
        MatchingDetail matchingDetail = matchingService.getMatchingDetailById(matchingId);
        model.addAttribute("matchingDetail", matchingDetail);
        return "matching/matchingFindJobDetail";
    }

    // 매칭 생성 페이지
    @GetMapping("/request")
    public String requestMatching(@RequestParam(required = false) Long caregiverId, Model model) {
        if (caregiverId != null) {
            // 요양보호사 ID가 주어진 경우 요양보호사 정보를 가져옵니다.
            CaregiverDetail caregiverDetail = caregiverService.findCaregiverById(caregiverId);
            model.addAttribute("caregiver", caregiverDetail);
        }
        return "matching/requestMatching";
    }

	 // 매칭 요청 처리
	 @PostMapping("/request")
	 public String processRequestMatching(MatchingRequestDto matchingRequestDto, BindingResult bindingResult) {
	     if (bindingResult.hasErrors()) {
	         return "matching/requestMatching";
	     }

        // 사용자 역할에 따른 처리
        String userRole = matchingRequestDto.getUserRole();
        if (userRole.equals("elderly")) {
            Senior senior = matchingService.createSenior(matchingRequestDto);
            matchingRequestDto.setSeniorId(senior.getSeniorId());
        } else if (userRole.equals("guardian")) {
            Guardian guardian = matchingService.createGuardian(matchingRequestDto);
            matchingRequestDto.setSeniorId(guardian.getSenior().getSeniorId());
        }

        // 매칭 상태 설정: CaregiverId가 양의 정수인 경우 REQUESTED, 그렇지 않은 경우 POSTED
        boolean isCaregiverIdValid = matchingRequestDto.getCaregiverId() != null && matchingRequestDto.getCaregiverId() > 0;
        MatchingStatus status = isCaregiverIdValid ? MatchingStatus.REQUESTED : MatchingStatus.POSTED;
        matchingRequestDto.setStatus(status);

        matchingService.createMatching(matchingRequestDto);

        // 처리 후 리디렉션 또는 적절한 페이지로 이동
        return "redirect:/";
	 }
}
