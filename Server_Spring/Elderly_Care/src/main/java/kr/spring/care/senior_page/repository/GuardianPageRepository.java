package kr.spring.care.senior_page.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kr.spring.care.user.entity.Guardian;
import kr.spring.care.user.entity.Senior;
import kr.spring.care.user.entity.User;

public interface GuardianPageRepository extends JpaRepository<Guardian, Long>{

	
	
	Optional<Guardian> findByUser_UserId(long userId);
	
}
