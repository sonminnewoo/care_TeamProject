package kr.spring.care.matching.entity;

import jakarta.persistence.*;
import kr.spring.care.matching.constant.MatchingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matching_id")
    private Long id;

    @Column(name = "senior_id")
    private Long seniorId;

    @Column(name = "caregiver_id")
    private Long caregiverId;

    private String matchingCountry;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    
    @Enumerated(EnumType.STRING)
    private MatchingStatus status;
}
