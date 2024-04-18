package kr.spring.care.board.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import kr.spring.care.user.entity.User;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
@Entity(name="comment")
public class Comment {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cnum;
	private String content;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="regdate")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private Date regdate;
	
	//User  ==> userid
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	//Board  ==>bnum
	@ManyToOne
	@JoinColumn(name="bnum")
	private Board board;
}
