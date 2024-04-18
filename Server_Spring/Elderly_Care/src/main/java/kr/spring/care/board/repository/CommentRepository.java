package kr.spring.care.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kr.spring.care.board.model.Comment;


//@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
		
		//댓글추가
//		@Modifying
//		@Query(value = "insert into comment(content, regdate, bnum, user_id) values(?1, now(), ?2, ?3) ", 
//				nativeQuery = true)
//		public void insert(String content, long bnum, long user_id);
		
		//JPQL(Java Persistence Query Language : 엔티티 객체를 중심)
		//@Query(value= "select * from tbl_comment4  where bnum=?1", nativeQuery=true) 
//		@Query( "select sc from comment  sc where sc.board.num=?1")
		public List<Comment> findByBoard_num(Long bnum);

		
}
