package kr.spring.care.matching.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import kr.spring.care.matching.constant.MatchingStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class MatchingRequestDto {

	private Long userId;
    private Long seniorId;
    private Long caregiverId;
    
    @NotNull(message = "지역 입력은 필수입니다.")
    private String matchingCountry;

    @NotNull(message = "시작 날짜는 필수입니다.")
    @FutureOrPresent(message = "시작 날짜는 현재 혹은 미래 날짜여야 합니다.")
    private LocalDate startDate;

    @NotNull(message = "종료 날짜는 필수입니다.")
    @Future(message = "종료 날짜는 미래의 날짜여야 합니다.")
    private LocalDate endDate;

    @NotNull(message = "시작 시간은 필수입니다.")    
    private LocalTime startTime;
    
    @NotNull(message = "종료 시간은 필수입니다.")
    private LocalTime endTime;

    private MatchingStatus status;
    
    private String userRole;
    
    // Senior 관련 필드
    private String health;
    private String seniorName;
    private String requirements;
    private Boolean hasGuardian;
    
    // Guardian 관련 필드
    private String guardianName;
    private String relationship;
    private String elderlyName;
    private String elderlyGender;
}
