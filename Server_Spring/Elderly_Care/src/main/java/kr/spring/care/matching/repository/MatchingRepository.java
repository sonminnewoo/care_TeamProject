package kr.spring.care.matching.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.spring.care.matching.entity.Matching;

public interface MatchingRepository extends JpaRepository<Matching, Long> {
	
	List<Matching> findBySeniorId(long id);
	List<Matching> findByCaregiverId(long id);
}
