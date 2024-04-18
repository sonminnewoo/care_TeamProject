package kr.spring.care.admin.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import kr.spring.care.admin.DTO.CaregiverDTO;
import kr.spring.care.admin.DTO.MessageResponse;
import kr.spring.care.admin.service.CareBoardService;
import kr.spring.care.admin.service.TotalUserService;
import kr.spring.care.user.entity.User;
import kr.spring.care.user_page.dto.UserDTO;
import lombok.RequiredArgsConstructor;

@RequestMapping("/admin/*")
@Controller
@RequiredArgsConstructor
public class AdminController {
	
	private final TotalUserService totalUserService; 
	private final CareBoardService careBoardService; 
	
	
	
	// 회원관리 페이지(페이징)
	@GetMapping("totUser")
	public String userListPaging(Model model,
			@PageableDefault(page = 1) Pageable pageable,
			@RequestParam(required = false, defaultValue = "")String field,
			@RequestParam(required = false, defaultValue = "")String word) {
		Page<UserDTO> alluser = totalUserService.userPaging(pageable, field, word);
		long allCount = totalUserService.countAllUser();
		long count = totalUserService.countUser(field, word);
		
		int blockLimit = 5;
		int startPage = (((int)Math.ceil((double)pageable.getPageNumber() / blockLimit)) -1) * blockLimit +1;   
		int endPage = Math.min((startPage + blockLimit -1), alluser.getTotalPages());	
		
		model.addAttribute("allUser", alluser);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("AlluserCnt", allCount);
		model.addAttribute("userCnt", count);
		model.addAttribute("field", field);
	    model.addAttribute("word", word);
		
		return "admin/totalUser";
	}
	
	// 회원권한 업데이트
	@PostMapping("authChange/{userId}")
	public String update(@PathVariable("userId") long userId, User user) {
		totalUserService.authChange(userId, user);
		return "redirect:/admin/totUser";
	}
	
	// 모든회원 상세정보(모달창)
	@GetMapping("getUserInfo")
	@ResponseBody
	public UserDTO userView(@RequestParam long userId) {

		UserDTO userDTO = totalUserService.userView(userId);
		if(userDTO != null) {
			return userDTO;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		
	}
		
	// 권한 변경(모달창)
	@PostMapping("updateRole")
	@ResponseBody
	public ResponseEntity<?> updateRole(@RequestParam("userId") long userId, @RequestParam("role") String role){
		try {
			totalUserService.updateRole(userId, role);
			return ResponseEntity.ok().body(new MessageResponse("Role updated successfully!"));
		}catch(Exception e){
			return ResponseEntity.badRequest().body(new MessageResponse("Error updating role"));
		}
		
	}
		
	// 요양보호사 게시판 관리
	@GetMapping("careList")
	public String careListPaging(Model model,
			@PageableDefault(page = 1) Pageable pageable,
			@RequestParam(required = false, defaultValue = "")String field,
			@RequestParam(required = false, defaultValue = "")String word) {
		Page<CaregiverDTO> allCare = careBoardService.listPaging(pageable, field, word);
		long allCount = careBoardService.countAllCare();
		long count = careBoardService.countCare(field, word);
	
		int blockLimit = 5;
		int startPage = (((int)Math.ceil((double)pageable.getPageNumber() / blockLimit)) -1) * blockLimit +1;   
		int endPage = Math.min((startPage + blockLimit -1), allCare.getTotalPages());	
		model.addAttribute("allCare", allCare);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("AllCareCnt", allCount);
		model.addAttribute("careCnt", count);
		model.addAttribute("field", field);
		model.addAttribute("word", word);
		
		return "admin/careBoard";
	}
	
	// 요양보호사 리스트 삭제
	@DeleteMapping("delCare/{userId}")
	@ResponseBody
	public long delCare(@PathVariable("userId") long userId) {
		careBoardService.deleteList(userId);
		return userId;
	}
	
	// 회원 삭제
	@DeleteMapping("delUser/{userId}")
	@ResponseBody
	public long delUser(@PathVariable("userId") long userId) {
		System.out.println("아디"+ userId);
		totalUserService.deleteUser(userId);
		return userId;
	}
	
	// 요양보호사 구인정보(모달창)
	@GetMapping("getCareInfo")
	@ResponseBody
	public CaregiverDTO careView(@RequestParam long userId) {
		System.out.println("케어아뒤"+ userId);
		CaregiverDTO caregiverDTO = careBoardService.careView(userId);
		if(caregiverDTO != null) {
			System.out.println("요양보호"+ caregiverDTO);
			return caregiverDTO;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		
	}
	
	
	
	

	
	
	
	
	

	
}
