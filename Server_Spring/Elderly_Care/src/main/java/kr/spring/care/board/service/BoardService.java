package kr.spring.care.board.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.care.board.model.Board;
import kr.spring.care.board.model.BoardDTO;
import kr.spring.care.board.repository.BoardRepository;
import kr.spring.care.main.controller.GlobalControllerAdvice;
import kr.spring.care.user.constant.Role;
import kr.spring.care.user.entity.Caregiver;
import kr.spring.care.user.entity.User;
import kr.spring.care.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class BoardService {
	private final UserRepository userRepository; 
	private final BoardRepository boardRepository;
	private final GlobalControllerAdvice globalControllerAdvice; 
	
//	public User searchUser() {
//		userRepository.
//	}
	
	//글 작성
	@Transactional
	public void write(Board board) {
		User writer = userRepository.searchUserID(board.getWriter());
//		System.out.println("작성자아디"+globalControllerAdvice.userId());
		board.setUser(writer);
		boardRepository.save(board);
	}
	
	//전체보기
	public List<Board> boardlist(){
		return boardRepository.findAll();
	}
	
	//페이징
	 public Page<Board> findAll(Pageable pageable) { 
		 return boardRepository.findAll(pageable); 
	 }
	 
	//전체보기(페이징+검색)
		public Page<Board> userPaging(Pageable pageable, String field, String word) {
		      int page = pageable.getPageNumber() -1;
		      int pageLimit = 5;
		      
		      Page<Board> allBoardPages = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Direction.DESC, "num")));
		      log.info(allBoardPages);
		      if(field.equals("title")) {
		         allBoardPages = boardRepository.findByTitleContaining(word, PageRequest.of(page, pageLimit, Sort.by(Direction.DESC, "num")));
		      }else if(field.equals("content")) {
		         allBoardPages = boardRepository.findByContentContaining(word, PageRequest.of(page, pageLimit, Sort.by(Direction.DESC, "num")));
		      }
		      
		      System.out.println("유저dto");
		      return allBoardPages;
		   }
	 
	
	//게시글 상세보기
	@Transactional
	public Board boardview(long num) {
		//조회수 증가
		Board board = boardRepository.findById(num).get();
		board.setHitcount(board.getHitcount()+1);
		return board;
	}
	
	//게시글 삭제
	@Transactional
	public void delete(long num) {
		boardRepository.deleteById(num);
	}
	
//	//게시글 수정
//	public Board update(Board boardUpdate) {
//		boardRepository.save(boardUpdate);
//		return findById(boardUpdate.getNum());
//	}
//	
//	// 수정 findById
//	public Board findById(Long num) {
//		Optional<Board> optionalBoard = boardRepository.findById(num);
//	    if (optionalBoard.isPresent()) {
//	        Board update = optionalBoard.get();
//	        update.setTitle(update.getTitle());
//	        update.setContent(update.getContent());
//	        update.setRegdate(new Date());
//	        
//	        Board board = boardRepository.save(update);
//	        return board;
//	        		
//	    } else {
//	        // 엔티티를 찾을 수 없는 경우의 처리 (예외 던지기 등)]
//	        // 여기서 적절한 예외를 던지거나 다른 처리를 할 수 있습니다.
//	        log.warn("해당 게시글을 찾을 수 없습니다.");
//	        return null;
//	    }
//	}
	

//게시물 수정	
	public void boardupdate(Board board) {
//	     Optional을 사용하여 findById의 결과를 처리
	    Optional<Board> optionalBoard = boardRepository.findById(board.getNum());
	    
	    if (optionalBoard.isPresent()) {
	        Board modify = optionalBoard.get();
	        modify.setTitle(board.getTitle());
	        modify.setContent(board.getContent());
	        modify.setRegdate(new Date());
	        
	        // 변경된 엔티티 저장
	        boardRepository.save(modify);
	    } else {
	        // 엔티티를 찾을 수 없는 경우의 처리 (예외 던지기 등)
	        log.warn("Board not found with id: " + board.getNum());
	        // 여기서 적절한 예외를 던지거나 다른 처리를 할 수 있습니다.
	        // 예: throw new EntityNotFoundException("Board not found with id: " + board.getNum());
	    }
	}
	
	//총 게시물 수
	public long count() {
		return boardRepository.count();
	}

	
}
