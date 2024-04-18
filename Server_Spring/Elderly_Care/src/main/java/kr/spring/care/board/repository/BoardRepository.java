package kr.spring.care.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.spring.care.board.model.Board;
import kr.spring.care.notice.model.Notice;
import kr.spring.care.user.entity.User;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{
	
	public Page<Board> findByTitleContaining(String word, Pageable pageable);
   public Page<Board> findByContentContaining(String word, Pageable pageable);
   
  
}
