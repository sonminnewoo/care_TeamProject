package kr.spring.care.caregiver_page.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.care.admin.DTO.CaregiverDTO;
import kr.spring.care.caregiver_page.repository.CaregiverPageRepository;
import kr.spring.care.matching.entity.Matching;
import kr.spring.care.matching.repository.MatchingRepository;
import kr.spring.care.senior_page.dto.MatchingDTO;
import kr.spring.care.senior_page.dto.SeniorDTO;
import kr.spring.care.senior_page.repository.SeniorPageRepository;
import kr.spring.care.user.entity.Caregiver;
import kr.spring.care.user.entity.Senior;
import kr.spring.care.user.entity.User;
import kr.spring.care.user_page.dto.UserDTO;
import kr.spring.care.user_page.repository.UserPageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CaregiverPageServiceImpl implements CaregiverPageService{

	private final UserPageRepository userPageRepository;
	private final CaregiverPageRepository caregiverPageRepository;
	private final SeniorPageRepository seniorPageRepository;
	private final MatchingRepository matchingRepository;
	private final PasswordEncoder passwordEncoder; 
	
	@Override
	public UserDTO myInfo(long userId) {
		Optional<User> userOptional = userPageRepository.findById(userId);
		return userOptional.map(UserDTO::new).orElse(null); 
	}
	
	@Override
	public CaregiverDTO caregiverInfo(long userId) {
	    return caregiverPageRepository.findByUser_userId(userId)
	           .map(CaregiverDTO::new)
	           .orElse(null); 
	}


	@Override
	@Transactional
	public void editUser(UserDTO userDTO) {
		User bfUser = userPageRepository.findById(userDTO.getUserId())
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
		bfUser.setAddress(userDTO.getAddress());
		bfUser.setGender(userDTO.getGender());
		bfUser.setName(userDTO.getName());
		bfUser.setPhoneNumber(userDTO.getPhoneNumber());
		Caregiver bfCare = caregiverPageRepository.findByUser_userId(userDTO.getUserId()).get();
		bfCare.setAvailableHours(userDTO.getAvailableHours());
		bfCare.setCertification(userDTO.getCertification());
		bfCare.setExperience(userDTO.getExperience());
		bfCare.setExperienceYears(userDTO.getExperienceYears());
		bfCare.setSpecialization(userDTO.getSpecialization());
		bfUser.setCaregiver(bfCare);
		userPageRepository.save(bfUser);
	}

	@Override
	@Transactional
	public void editPw(User user) {
		User bfUser = userPageRepository.findById(user.getUserId()).get();
		String afPw = passwordEncoder.encode(user.getPassword());
		bfUser.setPassword(afPw);
		userPageRepository.save(bfUser);
	}

	@Override
	public List<MatchingDTO> matchingInfo(long userId) {
		Caregiver caregiver = caregiverPageRepository.findByUser_userId(userId).orElseThrow(() -> new
		NoSuchElementException("Caregiver not found"));
		
		List<Matching> matchings = matchingRepository.findByCaregiverId(caregiver.getCaregiverId());
		return matchings.stream().map(matching -> {
			Optional<Senior> seniorOpt = seniorPageRepository.findById(matching.getSeniorId());
			String seniorName = seniorOpt.isPresent() ? seniorOpt.get().getUser().getName() : "N/A";
			return new MatchingDTO(matching, seniorName); // MatchingDTO 생성자에 caregiverName을 전달
		}).collect(Collectors.toList());
		
//		List<Matching> matchingList = matchingRepository.findByCaregiverId(caregiver.getCaregiverId()); List<MatchingDTO>
//		matLiDTO = matchingList.stream() .map(matching -> new MatchingDTO(matching))
//		.collect(Collectors.toList());
//		 
//		 return matLiDTO; 
		
	}

	


	

	

}
