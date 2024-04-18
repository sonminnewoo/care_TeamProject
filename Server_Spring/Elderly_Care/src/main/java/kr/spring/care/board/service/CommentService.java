package kr.spring.care.board.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;
import kr.spring.care.board.model.Board;
import kr.spring.care.board.model.Comment;
import kr.spring.care.board.repository.BoardRepository;
import kr.spring.care.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;


	@Service
	@RequiredArgsConstructor
	public class CommentService {
		private final CommentRepository commentRepository;
		private final BoardRepository boardRepository;
		
		
		//댓글전체보기
	   public List<Comment> list(long bnum){
		   return commentRepository.findByBoard_num(bnum);
		
	   }
		
	  //댓글추가
	   @Transactional
		public void insert(Comment comment2) {
		 //댓글개수  +1
			  Board b =	boardRepository.findById(comment2.getBoard().getNum()).get();
	 		  b.setReplycnt(b.getReplycnt()+1);
	 		  System.out.println(comment2.getContent() + "// " + comment2.getBoard().getNum() +"// " +comment2.getUser().getUserId());
//		   commentRepository.insert(comment2.getContent(),
//				                               comment2.getBoard().getNum(),
//				                               comment2.getUser().getUserId());  // 만든 함수
	 		  commentRepository.save(comment2);
		}
	    
	    // 댓글 수정
	    public void update(Comment comment) {
	    	// 변수 comment에 있는 cnum을 통해 수정 전 db의 데이터를 가져 옴
	    	Comment bfCm = commentRepository.findById(comment.getCnum()).get(); 
	    	
	    	// 수정 전 데이터를 가진 객체에 수정 할 데이터를 setter 를 통해서 저장 
	    	bfCm.setContent(comment.getContent());
	    	
	    	// 수정 된 데이터를 가진 객체를 db 에 insert
	    	commentRepository.save(bfCm);
	    }
	   
		//댓글삭제
		@Transactional
		public void delete(long cnum) {
			//댓글개수 -1
			Optional<Comment>  c=  commentRepository.findById(cnum);
			//board에 있는 replycnt 값을 1 감소로 수정
			 Board b =  c.get().getBoard();
			 b.setReplycnt(b.getReplycnt()-1);
	  		commentRepository.deleteById(cnum);
		}
		//개수
		public long count(long num) {
			return  boardRepository.findById(num).get().getReplycnt();
		}
}
