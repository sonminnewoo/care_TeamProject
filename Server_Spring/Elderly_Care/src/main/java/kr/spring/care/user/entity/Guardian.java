package kr.spring.care.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Guardian {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guardianId;

    private String guardianName;
    private String guardianPhoneNumber;
    private String relationship;
    
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "senior_id")
    private Senior senior;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    
}