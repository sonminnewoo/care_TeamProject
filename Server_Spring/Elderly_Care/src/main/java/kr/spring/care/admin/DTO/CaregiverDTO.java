package kr.spring.care.admin.DTO;

import kr.spring.care.user.entity.Caregiver;
import kr.spring.care.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class CaregiverDTO {

	private Long caregiverId;
    private String certification;
    private String specialization;
    private String experience;
    private int experienceYears;
    private String availableHours;
    private User user;
    
    public CaregiverDTO(Caregiver entity) {
		this.caregiverId = entity.getCaregiverId();
		this.certification = entity.getCertification();
		this.specialization = entity.getSpecialization();
		this.experience = entity.getExperience();
		this.experienceYears = entity.getExperienceYears();
		this.availableHours = entity.getAvailableHours();
		this.user = entity.getUser();
	}
}
