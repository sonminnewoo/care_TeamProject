package kr.spring.care.notice.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.spring.care.notice.model.Notice;
import kr.spring.care.notice.service.NoticeService;
/*import kr.spring.care.config.auth.PrincipalUser;*/
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequestMapping("/notice/*")
@RequiredArgsConstructor
@Log4j2
@Controller
public class NoticeController {
	private final NoticeService noticeService;

//	@PreAuthorize("isAuthenticated()")
	//글 작성 폼으로
	@GetMapping("insert")
	public String insert(Model model) {
		model.addAttribute("admin", noticeService.searchAdmin());
		System.out.println("어드민"+ noticeService.searchAdmin());
		return "notice/insert";
	}

	// 글추가 후 리스트
	@GetMapping("/notice/insertlist") // Notice =>title/writer(????)/content
	public String insert(Notice notice) {
		noticeService.insert(notice);
		return "redirect:list";
	}

	
	// 공지사항 페이지(페이징)
	   @GetMapping("notice/list")
	   public String userListPaging(Model model,
	         @PageableDefault(page = 1) Pageable pageable,
	         @RequestParam(required = false, defaultValue = "")String field,
	         @RequestParam(required = false, defaultValue = "")String word) {
	      Page<Notice> alluser = noticeService.userPaging(pageable, field, word);
//	      long allCount = noticeService.countAllUser();
//	      long count = noticeService.countUser(field, word);
	      
	      int blockLimit = 5;
	      int startPage = (((int)Math.ceil((double)pageable.getPageNumber() / blockLimit)) -1) * blockLimit +1;   
	      int endPage = Math.min((startPage + blockLimit -1), alluser.getTotalPages());   
	      
	      model.addAttribute("notices", alluser);
	      model.addAttribute("startPage", startPage);
	      model.addAttribute("endPage", endPage);
//	      model.addAttribute("AlluserCnt", allCount);
//	      model.addAttribute("userCnt", count);
	      
	      
	      return "notice/list";
	   }

	// 상세보기
	@GetMapping("view/{num}")
	public String view(@PathVariable long num, Model model) {
		model.addAttribute("notice", noticeService.view(num));
		return "notice/view";
	}

	//게시글 삭제
		@DeleteMapping("delete/{num}")
		@ResponseBody
		public long delete(@PathVariable long num) {
			noticeService.delete(num);
			log.info("delete");
			return num;
		}
		
		//게시글 수정 폼
		@GetMapping("noticeupdate/{num}")
		public String update(@PathVariable long num, Model model) {
			model.addAttribute("notice", noticeService.view(num));
			return "notice/update";
		}
		
		//수정
		@GetMapping("update")
		public String update(Notice notice) {
			System.out.println("업뎃넘"+ notice.getNum());
			System.out.println("업뎃제목"+ notice.getTitle());
			System.out.println("업뎃내용"+ notice.getContent());
			log.info("업뎃"+ notice);
			noticeService.noticeupdate(notice);
			return "notice/list";
		}

}
