package kr.spring.care.board.model;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import kr.spring.care.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "board")
@Getter
@Setter
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long num;
	private String title;
	private String writer;
	private String content;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "regdate")
	private Date regdate;
	private Long hitcount;
	private Long replycnt;

	// 댓글
	@OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JsonIgnoreProperties("board")
//	private List<Comment> comments;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	// 조회수

	@PrePersist public void prePersist() { this.hitcount = this.hitcount == null
	 ? 0 : this.hitcount; this.replycnt = this.replycnt == null ? 0 :
	  this.replycnt; }

}
