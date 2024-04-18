package kr.spring.care.admin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import kr.spring.care.admin.repository.TotalUserRepository;
import kr.spring.care.user.constant.Role;
import kr.spring.care.user.entity.User;
import kr.spring.care.user_page.dto.UserDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TotalUserServiceImpl implements TotalUserService{
	
	private final TotalUserRepository totalUserRepository; 
	
	// 리스트(페이지+검색)
	@Override
	public Page<UserDTO> userPaging(Pageable pageable, String field, String word) {
		int page = pageable.getPageNumber() -1;
		int pageLimit = 10;
		
		Page<User> allUserPages = totalUserRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Direction.DESC, "userId")));
		if(field.equals("name")) {
			allUserPages = totalUserRepository.findByNameContaining(word, PageRequest.of(page, pageLimit, Sort.by(Direction.DESC, "userId")));
		}else if(field.equals("email")) {
			allUserPages = totalUserRepository.findByEmailContaining(word, PageRequest.of(page, pageLimit, Sort.by(Direction.DESC, "userId")));
		}
		Page<UserDTO> UserDTOs = allUserPages.map(
				allUserPage -> new UserDTO(allUserPage));
		return UserDTOs;
	}
	
	// 권한 변경
	@Transactional
	@Override
	public void authChange(long userId, User user) {
		User b = totalUserRepository.findById(userId).get();
		if(user.getRole().equals(Role.CAREGIVER)) {
			b.setRole(Role.CAREGIVER);
		}else if(user.getRole().equals(Role.USER)) {
			b.setRole(Role.USER);
		}
		else if(user.getRole().equals(Role.SENIOR)) {
			b.setRole(Role.SENIOR);
		}
		else if(user.getRole().equals(Role.GUARDIAN)) {
			b.setRole(Role.GUARDIAN);
		}
		
	}

	// 전체유저 수
	@Override
	public long countAllUser() {
		return totalUserRepository.count();
	}
	
	// 유저 수(검색)
	@Override
	public long countUser(String field, String word) {
		long count = totalUserRepository.count();
		if(field.equals("name")) {
			count = totalUserRepository.cntNameSearch(word);
		}else if(field.equals("email")) {
			count = totalUserRepository.cntEmailSearch(word);
		}
		
		return count;
	}
	
	// 유저 상세정보(모달)
	@Override
	public UserDTO userView(long userId) {
		Optional<User> userOptional = totalUserRepository.findById(userId);
	    return userOptional.map(UserDTO::new).orElse(null);
		
		/*
		 * Optional<User> userOptional = totalUserRepository.findById(userId); if
		 * (userOptional.isPresent()) { User user = userOptional.get(); return new
		 * UserDTO(user); } else { return null; // 또는 원하는 대응 방법에 따라 다른 처리를 수행할 수 있습니다. }
		 */
	}

	@Override
	public void updateRole(long userId, String role) {
		Optional<User> optionalUser = totalUserRepository.findById(userId);
		User user = optionalUser.get();
		user.setRole(Role.valueOf(role));
		totalUserRepository.save(user);

	}

	@Override
	public void deleteUser(long userId) {
		totalUserRepository.deleteById(userId);
	}



	


	

}
