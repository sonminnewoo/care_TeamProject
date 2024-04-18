package kr.spring.care.board.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import kr.spring.care.board.model.Board;
import kr.spring.care.board.model.BoardDTO;
import kr.spring.care.board.service.BoardService;
import kr.spring.care.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/m/board/*")
public class BoardRestController {
private final BoardService boardService;
	
	// 게시판 페이지(페이징)
	   @GetMapping("boardlist")
	   public List<Board> userListPaging() {
	      List<Board> boardList = boardService.boardlist();
	      return boardList;
	   }
	

	//글 추가
	@PostMapping("write")
	public ResponseEntity write(@RequestBody BoardDTO boardDTO) {
		Board board = new Board();
		System.out.println("데이터"+ boardDTO.getTitle()+ boardDTO.getContent());
		board.setTitle(boardDTO.getTitle());
		board.setContent(boardDTO.getContent());
		User user = new User();
		user.setUserId(boardDTO.getUserId());
		board.setUser(user);
		boardService.write(board);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

	//게시글 삭제
	@DeleteMapping("delete/{num}")
	public ResponseEntity <Long> delete(@PathVariable long num) {
		boardService.delete(num);
		
		return new ResponseEntity<Long>(num, HttpStatus.OK);
	}

	
//	//수정
//	@PutMapping("boardupdate")
//	@ResponseBody
//	public Long update(@RequestBody Board board) {
//		boardService.boardupdate(board);
//		return board.getNum();
//	}
	
}
