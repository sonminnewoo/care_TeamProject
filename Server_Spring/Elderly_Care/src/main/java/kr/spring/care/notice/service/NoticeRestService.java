package kr.spring.care.notice.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import groovy.util.logging.Log;
import kr.spring.care.notice.model.Notice;
import kr.spring.care.notice.model.Notice;
import kr.spring.care.notice.repository.NoticeRepository;
import kr.spring.care.user.entity.User;
import kr.spring.care.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;



@Transactional(readOnly = true)  // Transaction 을 수동으로 처리
@Service
@RequiredArgsConstructor
@Log4j2
public class NoticeRestService {
	private final NoticeRepository noticeRepository;
	private final UserRepository userRepository; 
	
	public User searchAdmin() {
		return userRepository.searchAdmin();
	}
	
	
	//글 추가
	@Transactional 
	public void insert(Notice notice) {
		noticeRepository.save(notice); 
	}
	
	//전체보기; 기존의 getAllNotices() 메서드를 변경하여 최신순으로 정렬된 공지사항 리스트를 반환하는 메서드로 수정합니다.
	public List<Notice> getAllNotices(){
		return noticeRepository.findAll(Sort.by(Sort.Direction.DESC, "regdate"));
	}
	
	//검색 기능(id로)
	public Notice findNoticeByid(Long id){
		Notice notice = noticeRepository.findById(id).orElse(new Notice());
		return notice;
	}
	
	//개수(검색)
//	public long count(String field, String word) {
//		long count = noticeRepository.count();
//		if(field.equals("title")) {
//			count = noticeRepository.cntTitleSearch(word);
//		}else if(field.equals("content")) {
//			count=noticeRepository.cntContentSearch(word);
//		}
//		
//		return count;
//		
//	}
	//개수
	public long count() {
		return noticeRepository.count();
	}
	
	//상세보기
	@Transactional
	public Notice view(long num) {
		//조회수 증가
		Notice notice = noticeRepository.findById(num).get();
		notice.setHitcount(notice.getHitcount()+1);
		return notice ;
	}
	
	//게시글 삭제
		@Transactional
		public void delete(long num) {
			noticeRepository.deleteById(num);
		}

		
		public void noticeupdate(Notice notice) {
		    // Optional을 사용하여 findById의 결과를 처리
		    Optional<Notice> optionalNotice = noticeRepository.findById(notice.getNum());
		    
		    if (optionalNotice.isPresent()) {
		        Notice modify = optionalNotice.get();
		        modify.setTitle(notice.getTitle());
		        modify.setContent(notice.getContent());
		        modify.setRegdate(new Date());
		        
		        // 변경된 엔티티 저장
		        noticeRepository.save(modify);
		    } else {
		        // 엔티티를 찾을 수 없는 경우의 처리 (예외 던지기 등)
		        log.warn("Notice not found with id: " + notice.getNum());
		        // 여기서 적절한 예외를 던지거나 다른 처리를 할 수 있습니다.
		        // 예: throw new EntityNotFoundException("Notice not found with id: " + notice.getNum());
		    }
		}

		// 최신순으로 정렬된 공지사항을 가져오는 메소드
		public List<Notice> getNoticesOrderByDateDesc(int limit) {
			// 최신순으로 정렬하고, limit 개수만큼 데이터를 가져오기 위해 Pageable을 설정합니다.
	        Pageable pageable = PageRequest.of(0, limit, Sort.by("regdate").descending());
	        
	        // 페이지네이션 없이 최신순으로 정렬된 limit 개수의 공지사항을 가져옵니다.
			return noticeRepository.findAll(pageable).getContent();
		}


}

