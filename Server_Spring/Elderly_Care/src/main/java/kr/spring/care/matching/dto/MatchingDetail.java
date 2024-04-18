package kr.spring.care.matching.dto;

import kr.spring.care.matching.entity.Matching;
import kr.spring.care.user.entity.Caregiver;
import kr.spring.care.user.entity.Senior;
import kr.spring.care.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchingDetail {
    private User caregiverUser;
    private Caregiver caregiver;
    private User seniorUser;
    private Senior senior;
    private Matching matching;

    public MatchingDetail(User caregiverUser, Caregiver caregiver, User seniorUser, Senior senior, Matching matching) {
        this.caregiverUser = caregiverUser;
        this.caregiver = caregiver;
        this.seniorUser = seniorUser;
        this.senior = senior;
        this.matching = matching;
    }
}
