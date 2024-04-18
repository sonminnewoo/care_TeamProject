package kr.spring.care.senior_page.dto;

import kr.spring.care.user.entity.Guardian;
import kr.spring.care.user.entity.Senior;
import kr.spring.care.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SeniorDTO {

	private Long seniorId;
	
    private String seniorName;
    private String health;
    private String requirements;
    private Boolean hasGuardian;
    
    private User user;
    private Guardian guardian;
    
    public SeniorDTO(Senior entity) {
		this.seniorId = entity.getSeniorId();
		this.seniorName = entity.getSeniorName();
		this.health = entity.getHealth();
		this.requirements = entity.getRequirements();
		this.hasGuardian = entity.getHasGuardian();
		this.user = entity.getUser();
		this.guardian = entity.getGuardian();
	}
}
