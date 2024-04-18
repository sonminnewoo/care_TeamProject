package kr.spring.care.user_page.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kr.spring.care.user.entity.User;

public interface UserPageRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);
}
