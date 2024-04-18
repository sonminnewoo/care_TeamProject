package kr.spring.care.user.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.spring.care.user.entity.Senior;
import kr.spring.care.user.entity.User;
import kr.spring.care.user.repository.SeniorRepository;
import kr.spring.care.user.repository.UserRepository;

@Service
public class SeniorService {
    private final SeniorRepository seniorRepository;
    private final UserRepository userRepository;

    public SeniorService(SeniorRepository seniorRepository, UserRepository userRepository) {
        this.seniorRepository = seniorRepository;
        this.userRepository = userRepository;
    }

    public Senior getOrCreateSenior(Long userId, String health, String requirements, Boolean hasGuardian) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new NoSuchElementException("사용자를 찾을 수 없습니다: " + userId);
        }
        User user = userOpt.get();

        return seniorRepository.findByUser(user)
                .orElseGet(() -> createSenior(user, health, requirements, hasGuardian));
    }

    private Senior createSenior(User user, String health, String requirements, Boolean hasGuardian) {
        Senior senior = new Senior();
        senior.setUser(user);
        senior.setHealth(health);
        senior.setRequirements(requirements);
        senior.setHasGuardian(hasGuardian);
        return seniorRepository.save(senior);
    }


}
