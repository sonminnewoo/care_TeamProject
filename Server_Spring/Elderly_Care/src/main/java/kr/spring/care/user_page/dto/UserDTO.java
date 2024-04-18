package kr.spring.care.user_page.dto;

import java.time.LocalDateTime;

import kr.spring.care.user.constant.Role;
import kr.spring.care.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

	private Long userId;
	private String name;
	private String email;
	private String phoneNumber;
	private String address;
	private String gender;
	private Role role;
	private String roleStr;
	private String country;
	private String image;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	// senior
	private Long seniorId;
	private String seniorName;
    private String health;
    private String requirements;
    private Boolean hasGuardian;
    
    // caregiver
    private Long caregiverId;
    private String caregiverName;
    private String experience;
    private String certification;
    private String availableHours;
    private String specialization;
    private int experienceYears;
    
    // guardian
    private Long guardianId;
    private String guardianName;
    private String guardianPhoneNumber;
    private String relationship;
    
    
	public UserDTO(User entity) {
		this.userId = entity.getUserId();
		this.name = entity.getName();
		this.email = entity.getEmail();
		this.address = entity.getAddress();
		this.gender = entity.getGender();
		this.phoneNumber = entity.getPhoneNumber();
		this.role = entity.getRole();
		this.country = entity.getCountry();
		this.image = entity.getImage();
		this.createdAt = entity.getCreatedAt();
		this.updatedAt = entity.getUpdatedAt();
		
//		this.seniorName = entity.getSenior().getSeniorName();
//		this.health = entity.getSenior().getHealth();
//		this.requirements = entity.getSenior().getRequirements();
//		this.hasGuardian = entity.getSenior().getHasGuardian();
				
	}
	
}
