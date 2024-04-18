package kr.spring.care.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import kr.spring.care.user.entity.Guardian;
import kr.spring.care.user.entity.User;

public interface GuardianRepository extends JpaRepository<Guardian, Long> {
    Optional<Guardian> findByUser(User user);
    
}
