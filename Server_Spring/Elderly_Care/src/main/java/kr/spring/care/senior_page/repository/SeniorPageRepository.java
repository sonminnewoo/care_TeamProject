package kr.spring.care.senior_page.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kr.spring.care.user.entity.Senior;
import kr.spring.care.user.entity.User;

public interface SeniorPageRepository extends JpaRepository<Senior, Long>{

//	@Query(value = "select * from senior where user_id = :userId", nativeQuery = true)
//	Senior findByUserId(long userId);
	
	Optional<Senior> findByUserUserId(Long userId);
}
