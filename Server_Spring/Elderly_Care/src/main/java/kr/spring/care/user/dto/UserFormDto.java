package kr.spring.care.user.dto;

import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserFormDto {
    @NotBlank(message = "이름은 필수 항목 입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 항목 입니다.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @NotEmpty(message = "주소는 필수 항목 입니다.")
    private String address;

    @NotEmpty(message = "비밀번호는 필수 항목 입니다.")
    @Length(min = 4, max = 12, message = "최소 4자, 최대 12자를 입력하세요.")
    private String password;

    @NotEmpty(message = "비밀번호 확인은 필수 항목 입니다.")
    @Length(min = 4, max = 12, message = "최소 4자, 최대 12자를 입력하세요.")
    private String confirmPassword;

    @NotEmpty(message = "국가는 필수 항목 입니다.")
    private String country;

    @NotEmpty(message = "성별은 필수 항목 입니다.")
    private String gender;

    @NotEmpty(message = "전화번호는 필수 항목 입니다.")
    private String phoneNumber;

    private String image;

     // Senior 관련 정보
	 private String seniorName;
	 private String health;
	 private String requirements;
	 private Boolean hasGuardian;

    // 보호자가 있을 경우에만 필요한 필드
    private String guardianName;
    private String guardianPhoneNumber;
    private String relationship; // 보호자와의 관계

     // Caregiver 정보
	 private String certification;
	 private String specialization;
	 private String experience;
	 private Integer experienceYears;
	 private String availableHours;
}
