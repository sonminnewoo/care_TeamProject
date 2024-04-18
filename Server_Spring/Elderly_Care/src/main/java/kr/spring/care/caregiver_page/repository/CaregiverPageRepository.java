package kr.spring.care.caregiver_page.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.spring.care.user.entity.Caregiver;

public interface CaregiverPageRepository extends JpaRepository<Caregiver, Long>{

//	Optional<Caregiver> findByUserUserId(Long userId);
	
	Optional<Caregiver> findByUser_userId(Long userId);
	Optional<Caregiver> findByCaregiverId(Long caregiverId);
}
