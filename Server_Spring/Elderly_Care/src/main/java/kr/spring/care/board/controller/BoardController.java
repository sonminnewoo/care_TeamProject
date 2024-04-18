package kr.spring.care.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.spring.care.board.model.Board;
import kr.spring.care.board.service.BoardService;
import kr.spring.care.board.controller.BoardController;
import kr.spring.care.board.model.Board;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping("/board/*")
public class BoardController {
	private final BoardService boardService;
	
	// 게시판 페이지(페이징)
	   @GetMapping("board/boardlist")
	   public String userListPaging(Model model,
	         @PageableDefault(page = 1) Pageable pageable,
	         @RequestParam(required = false, defaultValue = "")String field,
	         @RequestParam(required = false, defaultValue = "")String word) {
	      Page<Board> alluser = boardService.userPaging(pageable, field, word);
//	      long allCount = boardService.countAllUser();
//	      long count = boardService.countUser(field, word);
	      
	      int blockLimit = 5;
	      int startPage = (((int)Math.ceil((double)pageable.getPageNumber() / blockLimit)) -1) * blockLimit +1;   
	      int endPage = Math.min((startPage + blockLimit -1), alluser.getTotalPages());   
	      
	      model.addAttribute("boards", alluser);
	      model.addAttribute("startPage", startPage);
	      model.addAttribute("endPage", endPage);
	      model.addAttribute("field", field);
		  model.addAttribute("word", word);
//	      model.addAttribute("AlluserCnt", allCount);
//	      model.addAttribute("userCnt", count);
	      
	      
	      return "board/boardlist";
	   }
	
	
	//글작성 폼
	@GetMapping("write")
	public String write() {
		return "board/write";
	}
	
	//글 추가
	@GetMapping("/board/writedo")
	public String write(Board board) {
		boardService.write(board);
		return "redirect:boardlist";
	}
	
	//상세보기
	@GetMapping("boardview/{num}")
	public String boardview(Model model, @PathVariable long num) {
		model.addAttribute("board", boardService.boardview(num));
		return "board/boardview";
	}
	
	//게시글 삭제
	@GetMapping("delete/{num}")
	public String delete(@PathVariable long num) {
		boardService.delete(num);
		log.info("delete");
		return "redirect:/board/boardlist";
	}
	
	//게시글 수정 폼
	@GetMapping("boardupdate/{num}")
	public String update(@PathVariable long num, Model model) {
		model.addAttribute("board", boardService.boardview(num));
		return "board/boardupdate";
	}
	
	//수정
//	@PostMapping("boardupdate")
//	public String update (Board board, Model model) {
//		Board boardUpdate = boardService.update(board);
////		System.out.println("업뎃넘"+ board.getNum());
////		System.out.println("업뎃제목"+ board.getTitle());
////		System.out.println("업뎃내용"+ board.getContent());
////		log.info("업뎃"+ board);
//		return "redirect:/board/" + boardUpdate.getNum();
//	}
	
	//수정
	@PutMapping("boardupdate")
	@ResponseBody
	public Long update(@RequestBody Board board) {
		boardService.boardupdate(board);
		return board.getNum();
	}
}
