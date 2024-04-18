package kr.spring.care.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.spring.care.matching.dto.CaregiverDetail;
import kr.spring.care.user.constant.Role;
import kr.spring.care.user.entity.Caregiver;
import kr.spring.care.user.entity.User;
import kr.spring.care.user.repository.CaregiverRepository;
import kr.spring.care.user.repository.UserRepository;

@Service
public class CaregiverService {
    private final CaregiverRepository caregiverRepository;
    private final UserRepository userRepository;

    public CaregiverService(CaregiverRepository caregiverRepository, UserRepository userRepository) {
        this.caregiverRepository = caregiverRepository;
        this.userRepository = userRepository;
    }

	public CaregiverDetail findCaregiverById(Long caregiverId) {
        // 요양보호사의 상세 정보를 조회합니다.
        Caregiver caregiver = caregiverRepository.findById(caregiverId)
            .orElseThrow(() -> new NoSuchElementException("해당 요양보호사를 찾을 수 없습니다." + caregiverId));

        // Caregiver 엔티티에 직접 연결된 User 객체를 가져옵니다.
        User user = caregiver.getUser();
        if (user == null) {
            throw new NoSuchElementException("해당 ID에 연결된 사용자가 없습니다." + caregiverId);
        }

        // CaregiverDetail 객체를 생성하고 반환합니다.
        return new CaregiverDetail(user, caregiver);
    }

	public Page<CaregiverDetail> findCaregiversPageable(Pageable pageable) {
	    // 요양보호사 리포지토리를 사용하여 요양보호사 페이지를 가져옵니다.
	    Page<Caregiver> caregiversPage = caregiverRepository.findAll(pageable);

	    // 각 Caregiver 객체를 CaregiverDetail 객체로 변환합니다.
	    List<CaregiverDetail> caregiverDetails = caregiversPage.stream()
	        .map(caregiver -> {
	            User user = caregiver.getUser();
	            return user != null ? new CaregiverDetail(user, caregiver) : null;
	        })
	        .filter(Objects::nonNull)
	        .collect(Collectors.toList());

	    // Page<CaregiverDetail> 객체를 생성합니다.
	    return new PageImpl<>(caregiverDetails, pageable, caregiversPage.getTotalElements());
	}
	
    // 검색 기능을 위한 메소드 추가
    public Page<CaregiverDetail> searchCaregivers(String field, String keyword, Pageable pageable) {
        Page<User> usersPage;
        Stream<User> userStream;

        if (keyword == null || keyword.trim().isEmpty()) {
            // 키워드가 없는 경우에는 단순히 역할에 따라 필터링
            usersPage = userRepository.findByRole(Role.CAREGIVER, pageable);
            userStream = usersPage.stream();
        } else if ("name".equals(field)) {
            // 이름으로 검색
            usersPage = userRepository.findByNameContaining(keyword, pageable);
            userStream = usersPage.stream().filter(user -> user.getRole() == Role.CAREGIVER);
            // 여기서 필요한 경우 역할로 추가 필터링
        } else if ("email".equals(field)) {
            // 이메일로 검색
            usersPage = userRepository.findByEmailContaining(keyword, pageable);
            userStream = usersPage.stream().filter(user -> user.getRole() == Role.CAREGIVER);
            // 여기서 필요한 경우 역할로 추가 필터링
        } else if ("country".equals(field)) {
            // 지역으로 검색
            usersPage = userRepository.findByCountryContaining(keyword, pageable);
            userStream = usersPage.stream().filter(user -> user.getRole() == Role.CAREGIVER);
        } else {
            usersPage = userRepository.findByRole(Role.CAREGIVER, pageable);
            userStream = usersPage.stream();
        }

        // User 스트림을 CaregiverDetail 리스트로 변환
        List<CaregiverDetail> caregiverDetails = userStream
            .map(user -> {
                Optional<Caregiver> caregiverOpt = caregiverRepository.findById(user.getUserId());
                return caregiverOpt.map(caregiver -> new CaregiverDetail(user, caregiver)).orElse(null);
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        return new PageImpl<>(caregiverDetails, pageable, usersPage.getTotalElements());
    }

}