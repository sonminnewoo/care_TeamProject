package kr.spring.care.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.spring.care.user.entity.Caregiver;

public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {
    
}
