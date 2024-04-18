package kr.spring.care.notice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import kr.spring.care.notice.model.Notice;
import kr.spring.care.notice.service.NoticeRestService;
/*import kr.spring.care.config.auth.PrincipalUser;*/
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequestMapping("/m/notices")
@RequiredArgsConstructor
@Log4j2
@RestController
public class NoticeRestController {
	private final NoticeRestService noticeRestService;

//	@PreAuthorize("isAuthenticated()")
	//글 작성 폼으로
	/*
	 * @GetMapping("insert") public String insert(Model model) {
	 * model.addAttribute("admin", noticeService.searchAdmin());
	 * System.out.println("어드민"+ noticeService.searchAdmin()); return
	 * "notice/insert"; }
	 */
	// GET 메서드를 사용하여 모든 공지사항을 가져오는 엔드포인트
    @GetMapping
    public List<Notice> getAllNotices() {
        return noticeRestService.getAllNotices();
    }
    
    // 최신순으로 5개의 공지사항 데이터를 가져오는 로직을 구현
    @GetMapping(("/main-notices"))
    public List<Notice> getMainNotices() {
    	 List<Notice> notices = noticeRestService.getNoticesOrderByDateDesc(5);
    	 return notices;
    }


	// 글추가 후 리스트
	@PostMapping
	public void createNotice(@RequestBody Notice notice) {
		noticeRestService.insert(notice);
	}


	// 상세보기
	@GetMapping("/{num}")
	public Notice getNotice(@PathVariable long num) {
		return noticeRestService.view(num);
	}

	//게시글 삭제
	@DeleteMapping("/{num}")
	public void deleteNotice(@PathVariable long num) {
		noticeRestService.delete(num);
		log.info("delete");
	}
	
	//수정
	@PutMapping("/{num}")
	public String updateNotice(@PathVariable long num, @RequestBody Notice notice) {
		notice.setNum(num);
		noticeRestService.noticeupdate(notice);
		return "Notice updated successfully";

	}

}
