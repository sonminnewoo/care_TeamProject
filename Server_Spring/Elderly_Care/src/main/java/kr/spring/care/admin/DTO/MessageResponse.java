package kr.spring.care.admin.DTO;

public class MessageResponse {

	private String message;

    // 생성자
    public MessageResponse(String message) {
        this.message = message;
    }

    // getter
    public String getMessage() {
        return message;
    }

    // setter (필요한 경우에만)
    public void setMessage(String message) {
        this.message = message;
    }
}
