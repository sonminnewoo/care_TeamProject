package kr.spring.care.senior_page.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import kr.spring.care.matching.constant.MatchingStatus;
import kr.spring.care.matching.entity.Matching;
import kr.spring.care.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchingDTO {

	private Long id;
	private Long seniorId;
	private Long caregiverId;
	private String matchingCountry;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private MatchingStatus status;
    
    private String startDateStr;
    private String endDateStr;
    private String startTimeStr;
    private String endTimeStr;
    
    private String matchingUserName;
    
    public MatchingDTO(Matching entity) {
    	this.id = entity.getId();
    	this.seniorId = entity.getSeniorId();
    	this.caregiverId = entity.getCaregiverId();
    	this.matchingCountry = entity.getMatchingCountry();
    	this.startDate = entity.getStartDate();
    	this.endDate = entity.getEndDate();
    	this.startTime = entity.getStartTime();
    	this.endTime = entity.getEndTime();
    	this.status = entity.getStatus();
    }
    
    public MatchingDTO(Matching entity, String matchingUserName) {
    	this(entity);
    	this.matchingUserName = matchingUserName;
    }
    
    
    
}
