package kr.spring.care.user.dto;

import kr.spring.care.user.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDto {
    private String email;
    private Role role;
    private Long userId;
}