package kr.spring.care.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.spring.care.user.entity.Senior;
import kr.spring.care.user.entity.User;

public interface SeniorRepository extends JpaRepository<Senior, Long> {
    Optional<Senior> findByUser(User user);
}
