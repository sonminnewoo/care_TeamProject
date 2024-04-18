package kr.spring.care.caregiver_page.dto;

import kr.spring.care.user.entity.Caregiver;
import kr.spring.care.user.entity.Guardian;
import kr.spring.care.user.entity.Senior;
import kr.spring.care.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class caregiverDTO {

	private Long caregiverId;
	
    private String certification;
    private String specialization;
    private String experience;
    private int experienceYears;
    private String availableHours;
    private User user;
    
    public caregiverDTO(Caregiver entity) {
		this.certification = entity.getCertification();
		this.specialization = entity.getSpecialization();
		this.experience = entity.getExperience();
		this.experienceYears = entity.getExperienceYears();
		this.availableHours = entity.getAvailableHours();
		this.user = entity.getUser();
	}
}
