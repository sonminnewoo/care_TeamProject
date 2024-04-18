package kr.spring.care.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.spring.care.user.entity.Caregiver;


public interface CareBoardRepository extends JpaRepository<Caregiver, Long>{
	
	@Query(value = "select c from Caregiver c where c.user.name like %:word%")
	public Page<Caregiver> findByNameContaining(@Param("word") String word, Pageable pageable);
	
	@Query(value = "select c from Caregiver c where c.user.country like %:word%")
	public Page<Caregiver> findByCountryContaining(@Param("word")String word, Pageable pageable);
	
	
   @Query(value ="select count(c) from Caregiver c where c.user.name like %:word%") 
   public long cntNameSearch(@Param("word") String word); 
   @Query(value = "select count(c) from Caregiver c where c.user.country like %:word%") 
   public long cntCountrySearch(@Param("word") String word);
	
}
