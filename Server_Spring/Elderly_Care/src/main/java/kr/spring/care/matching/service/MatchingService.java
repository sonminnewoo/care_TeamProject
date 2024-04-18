package kr.spring.care.matching.service;


import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import kr.spring.care.matching.dto.MatchingDetail;
import kr.spring.care.matching.dto.MatchingRequestDto;
import kr.spring.care.matching.entity.Matching;
import kr.spring.care.matching.repository.MatchingRepository;
import kr.spring.care.user.constant.Role;
import kr.spring.care.user.entity.Caregiver;
import kr.spring.care.user.entity.Guardian;
import kr.spring.care.user.entity.Senior;
import kr.spring.care.user.entity.User;
import kr.spring.care.user.repository.CaregiverRepository;
import kr.spring.care.user.repository.GuardianRepository;
import kr.spring.care.user.repository.SeniorRepository;
import kr.spring.care.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class MatchingService {

    @Autowired
    private MatchingRepository matchingRepository;
    
    @Autowired
    private CaregiverRepository caregiverRepository;
    
    @Autowired
    private SeniorRepository seniorRepository;

    @Autowired
    private GuardianRepository guardianRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // 매칭 생성
    public Matching createMatching(@Valid MatchingRequestDto matchingRequestDto) {
        Matching matching = new Matching();
        matching.setSeniorId(matchingRequestDto.getSeniorId());
        matching.setCaregiverId(matchingRequestDto.getCaregiverId());
        matching.setStartDate(matchingRequestDto.getStartDate());
        matching.setEndDate(matchingRequestDto.getEndDate());
        matching.setMatchingCountry(matchingRequestDto.getMatchingCountry());
        matching.setStartTime(matchingRequestDto.getStartTime());
        matching.setEndTime(matchingRequestDto.getEndTime());
        matching.setStatus(matchingRequestDto.getStatus());
        return matchingRepository.save(matching);
    }

    // 매칭 ID로 매칭 정보 조회
    public Matching getMatchingById(Long matchingId) {
        return matchingRepository.findById(matchingId)
                .orElseThrow(() -> new EntityNotFoundException("Matching not found: " + matchingId));
    }

    // 매칭 정보 업데이트
    public Matching updateMatching(Matching matching) {
        // 데이터베이스에 이미 존재하는 매칭인지 확인
        Matching existingMatching = matchingRepository.findById(matching.getId())
                .orElseThrow(() -> new EntityNotFoundException("Matching not found: " + matching.getId()));
        // 필요한 속성 업데이트
        existingMatching.setStartDate(matching.getStartDate());
        existingMatching.setEndDate(matching.getEndDate());
        existingMatching.setStatus(matching.getStatus());
        return matchingRepository.save(existingMatching);
    }
    
    public Page<Matching> findMatchingsPageable(Pageable pageable) {
        return matchingRepository.findAll(pageable);
    }

    public MatchingDetail getMatchingDetailById(Long matchingId) {
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new NoSuchElementException("해당 매칭을 찾을 수 없습니다: " + matchingId));

        Long caregiverId = matching.getCaregiverId();
        Caregiver caregiver = null;
        User caregiverUser = null;
        if (caregiverId != null) {
            caregiver = caregiverRepository.findById(caregiverId)
                    .orElseThrow(() -> new NoSuchElementException("해당 요양보호사를 찾을 수 없습니다: " + caregiverId));
            caregiverUser = caregiver.getUser();
        }

        Long seniorId = matching.getSeniorId();
        Senior senior = null;
        User seniorUser = null;
        if (seniorId != null) {
            senior = seniorRepository.findById(seniorId)
                    .orElseThrow(() -> new NoSuchElementException("해당 노인을 찾을 수 없습니다: " + seniorId));
            seniorUser = userRepository.findById(senior.getSeniorId())
                    .orElseThrow(() -> new NoSuchElementException("해당 노인의 사용자 정보를 찾을 수 없습니다: " + seniorId));
        }

        return new MatchingDetail(caregiverUser, caregiver, seniorUser, senior, matching);
    }

    public Senior createSenior(MatchingRequestDto matchingRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        User currentUser = userRepository.findByEmail(username);
        if (currentUser == null) {
            throw new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + username);
        }

        // 사용자가 이미 Senior로 등록되어 있는지 확인
        Optional<Senior> existingSenior = seniorRepository.findByUser(currentUser);
        if (existingSenior.isPresent()) {
            return existingSenior.get();
        }

        currentUser.setRole(Role.SENIOR);
        userRepository.save(currentUser);

        Senior senior = new Senior();
        // senior.setSeniorName(currentUser.getName());
        senior.setHealth(matchingRequestDto.getHealth());
        senior.setRequirements(matchingRequestDto.getRequirements());
        senior.setHasGuardian(matchingRequestDto.getHasGuardian());
        senior.setUser(currentUser);
        senior = seniorRepository.save(senior);

        if (matchingRequestDto.getHasGuardian()) {
            Guardian guardian = new Guardian();
            guardian.setGuardianName(matchingRequestDto.getGuardianName());
            guardian.setUser(currentUser);
            guardian.setRelationship(matchingRequestDto.getRelationship());
            guardian.setSenior(senior);
            guardianRepository.save(guardian);
        }

        return senior;
    }


    public Guardian createGuardian(MatchingRequestDto matchingRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        User currentUser = userRepository.findByEmail(username);
        if (currentUser == null) {
            throw new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + username);
        }

        // 사용자가 이미 Guardian으로 등록되어 있는지 확인
        Optional<Guardian> existingGuardian = guardianRepository.findByUser(currentUser);
        if (existingGuardian.isPresent()) {
            return existingGuardian.get();
        }

        currentUser.setRole(Role.GUARDIAN);
        userRepository.save(currentUser);
        
        Guardian guardian = new Guardian();
        guardian.setGuardianName(currentUser.getName());
        guardian.setUser(currentUser);
        guardian.setRelationship(matchingRequestDto.getRelationship());

        Senior senior = new Senior();
        // senior.setSeniorName(matchingRequestDto.getElderlyName());
        senior.setHealth(matchingRequestDto.getHealth());
        senior.setRequirements(matchingRequestDto.getRequirements());
        senior.setHasGuardian(true);
        senior.setUser(currentUser);
        senior = seniorRepository.save(senior);

        guardian.setSenior(senior);
        return guardianRepository.save(guardian);
    }

    public Senior createSeniorForMobile(MatchingRequestDto matchingRequestDto) {
        Long userId = matchingRequestDto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + userId));

        // 사용자가 이미 Senior로 등록되어 있는지 확인
        Optional<Senior> existingSenior = seniorRepository.findByUser(user);
        if (existingSenior.isPresent()) {
            return existingSenior.get();
        }

        user.setRole(Role.SENIOR);
        userRepository.save(user);

        Senior senior = new Senior();
        senior.setHealth(matchingRequestDto.getHealth());
        senior.setRequirements(matchingRequestDto.getRequirements());
        senior.setHasGuardian(matchingRequestDto.getHasGuardian());
        senior.setUser(user);
        senior = seniorRepository.save(senior);

        if (matchingRequestDto.getHasGuardian()) {
            Guardian guardian = new Guardian();
            guardian.setGuardianName(matchingRequestDto.getGuardianName());
            guardian.setUser(user);
            guardian.setRelationship(matchingRequestDto.getRelationship());
            guardian.setSenior(senior);
            guardianRepository.save(guardian);
        }

        return senior;
    }

    public Guardian createGuardianForMobile(MatchingRequestDto matchingRequestDto) {
        Long userId = matchingRequestDto.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + userId));
        
        // 사용자가 이미 Guardian으로 등록되어 있는지 확인
        Optional<Guardian> existingGuardian = guardianRepository.findByUser(user);
        if (existingGuardian.isPresent()) {
            return existingGuardian.get();
        }

        user.setRole(Role.GUARDIAN);
        userRepository.save(user);

        Guardian guardian = new Guardian();
        guardian.setGuardianName(user.getName());
        guardian.setUser(user);
        guardian.setRelationship(matchingRequestDto.getRelationship());

        Senior senior = new Senior();
        senior.setHealth(matchingRequestDto.getHealth());
        senior.setRequirements(matchingRequestDto.getRequirements());
        senior.setHasGuardian(true);
        senior.setUser(user);
        senior = seniorRepository.save(senior);

        guardian.setSenior(senior);
        return guardianRepository.save(guardian);
    }
    
}
