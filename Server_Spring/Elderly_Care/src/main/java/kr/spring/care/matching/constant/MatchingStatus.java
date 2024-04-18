package kr.spring.care.matching.constant;

public enum MatchingStatus {
	POSTED, //구직중
    REQUESTED, // 특정 요양보호사에게 요청됨
    IN_PROGRESS, // 진행 중
    COMPLETED, // 완료됨
    CANCELLED // 취소됨
}
