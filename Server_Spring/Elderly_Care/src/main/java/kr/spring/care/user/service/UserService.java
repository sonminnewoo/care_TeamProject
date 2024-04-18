package kr.spring.care.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import kr.spring.care.user.service.UserService;
import kr.spring.care.user.dto.LoginResponseDto;
import kr.spring.care.user.entity.User;
import kr.spring.care.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
	
	@Autowired
	private final UserRepository userRepository;

	public User saveUser(User user) {
		validateDuplicate(user);
		return userRepository.save(user);
	}

	private void validateDuplicate(User user) {
		User existingUser = userRepository.findByEmail(user.getEmail());
		if (existingUser != null) {
			throw new IllegalStateException("이미 등록된 사용자입니다.");
		}
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("해당 사용자가 없습니다: " + email);
		}

		List<SimpleGrantedAuthority> authorities = Collections
				.singletonList(new SimpleGrantedAuthority("ROLE_"+user.getRole().toString()));

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}

	public boolean login(User user, PasswordEncoder passwordEncoder) {
		User findUser = userRepository.findByEmail(user.getEmail());
		if (findUser == null || !passwordEncoder.matches(user.getPassword(), findUser.getPassword())) {
			return false;
		}
		return true;
	}
    
    public LoginResponseDto mobileLogin(User user, PasswordEncoder passwordEncoder) {
        User foundUser = userRepository.findByEmail(user.getEmail());
        if (foundUser != null && passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            return new LoginResponseDto(foundUser.getEmail(), foundUser.getRole(), foundUser.getUserId());
        }
        return null;
    }
}
