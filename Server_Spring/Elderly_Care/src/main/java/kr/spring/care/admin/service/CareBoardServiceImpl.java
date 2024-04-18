package kr.spring.care.admin.service;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import kr.spring.care.admin.DTO.CaregiverDTO;
import kr.spring.care.admin.repository.CareBoardRepository;
import kr.spring.care.user.entity.Caregiver;
import kr.spring.care.user.entity.User;
import kr.spring.care.user_page.dto.UserDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CareBoardServiceImpl implements CareBoardService{
	
	private final CareBoardRepository careBoardRepository;

	@Override
	public Page<CaregiverDTO> listPaging(Pageable pageable, String field, String word) {
		int page = pageable.getPageNumber() -1;
		int pageLimit = 10;
		
		Page<Caregiver> caregiverPages = careBoardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Direction.DESC, "caregiverId")));
		if(field.equals("name")) {
			caregiverPages = careBoardRepository.findByNameContaining(word, PageRequest.of(page, pageLimit, Sort.by(Direction.DESC, "caregiverId")));
		}else if(field.equals("country")) {
			caregiverPages = careBoardRepository.findByCountryContaining(word, PageRequest.of(page, pageLimit, Sort.by(Direction.DESC, "caregiverId")));
		}
		Page<CaregiverDTO> caregiverDTOs = caregiverPages.map(
				caregiverPage -> new CaregiverDTO(caregiverPage));
		return caregiverDTOs;
	}

	@Override
	public void deleteList(long userId) {
		careBoardRepository.deleteById(userId);
	}

	@Override
	public long countAllCare() {
		return careBoardRepository.count();
	}

	@Override
	public long countCare(String field, String word) {
		long count = careBoardRepository.count();
		if(field.equals("name")) {
			count = careBoardRepository.cntNameSearch(word);
		}else if(field.equals("country")) {
			count = careBoardRepository.cntCountrySearch(word);
		}
		
		return count;
	}

	@Override
	public CaregiverDTO careView(long caregiverId) {
		Optional<Caregiver> careOptional = careBoardRepository.findById(caregiverId);
	    return careOptional.map(CaregiverDTO::new).orElse(null); 
	}
	
	
}
