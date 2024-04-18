package kr.spring.care.admin.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.spring.care.user.entity.User;


public interface TotalUserRepository extends JpaRepository<User, Long>{
	

	public Page<User> findByNameContaining(String word, Pageable pageable);
	public Page<User> findByEmailContaining(String word, Pageable pageable);
	
	@Query(value = "select count(*) from user where name like CONCAT('%',:word,'%')", nativeQuery = true)
	public long cntNameSearch(@Param("word") String word);
	
	@Query(value = "select count(*) from user where email like CONCAT('%',:word,'%')", nativeQuery = true)
	public long cntEmailSearch(@Param("word") String word);
	
	
}
