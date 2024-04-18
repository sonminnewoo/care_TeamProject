package kr.spring.care.senior_page.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchingResponse {

	private List<MatchingDTO> pastMatchings;
    private List<MatchingDTO> progressMatchings;
    
    public MatchingResponse(List<MatchingDTO> pastMatchings, List<MatchingDTO> progressMatchings) {
        this.pastMatchings = pastMatchings;
        this.progressMatchings = progressMatchings;
    }
}
