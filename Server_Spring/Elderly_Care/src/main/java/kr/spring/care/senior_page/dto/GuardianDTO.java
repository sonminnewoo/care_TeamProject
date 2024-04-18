package kr.spring.care.senior_page.dto;

import kr.spring.care.user.entity.Guardian;
import kr.spring.care.user.entity.Senior;
import kr.spring.care.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuardianDTO {

	private Long guardianId;
    private String guardianName;
    private String guardianPhoneNumber;
    private String relationship;
    private Senior senior;
    private User user;
    
    public GuardianDTO(Guardian entity) {
		this.guardianId = entity.getGuardianId();
		this.guardianName = entity.getGuardianName();
		this.guardianPhoneNumber = entity.getGuardianPhoneNumber();
		this.relationship = entity.getRelationship();
		this.senior = entity.getSenior();
		this.user = entity.getUser();
	}
}
