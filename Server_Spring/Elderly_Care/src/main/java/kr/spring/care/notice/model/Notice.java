package kr.spring.care.notice.model;

import java.util.Date;
import java.util.List;

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

@Entity(name="notice")
@Getter
@Setter
public class Notice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  Long num;
	private String title;
	private String writer;
	private String content;
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="regdate")
	private Date regdate;
	private Long hitcount;
	private Long replycnt;
	
	
	
	@OneToMany(mappedBy = "notice", 
			cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JsonIgnoreProperties("notice")
	private List<NoticeComment> comments;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	/*
	 * @PrePersist public void prePersist() { this.hitcount = this.hitcount == null
	 * ? 0 : this.hitcount; this.replycnt = this.replycnt == null ? 0 :
	 * this.replycnt; }
	 */
	public Notice() {
        this.title = "제목 없음";
        this.writer = "익명";
        this.content = "내용 없음";
        this.regdate = new Date();
        this.hitcount = 0L;
        this.replycnt = 0L;
    }
	

}






