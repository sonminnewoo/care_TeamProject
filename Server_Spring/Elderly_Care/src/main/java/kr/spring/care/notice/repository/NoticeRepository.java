package kr.spring.care.notice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.spring.care.notice.model.Notice;


public interface NoticeRepository  
      extends JpaRepository<Notice, Long>{
	
	public Page<Notice> findByTitleContaining(String word, Pageable pageable);
	public Page<Notice> findByContentContaining(String word, Pageable pageable);
	
	@Query(value="select count(*) from notice where title like CONCAT('%' , :word, '%' )", nativeQuery = true)
	public long cntTitleSearch(@Param("word") String word);
	
	@Query(value="select count(*) from notice where content like CONCAT('%' , :word, '%' )", nativeQuery = true)
	public long cntContentSearch(@Param("word") String   word);
	
}
