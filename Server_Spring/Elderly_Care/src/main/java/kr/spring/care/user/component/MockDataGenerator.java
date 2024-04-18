package kr.spring.care.user.component;

import java.util.Random;

import org.springframework.stereotype.Component;

import kr.spring.care.user.constant.Role;
import kr.spring.care.user.entity.Caregiver;
import kr.spring.care.user.entity.Guardian;
import kr.spring.care.user.entity.Senior;
import kr.spring.care.user.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@Component
public class MockDataGenerator {

    private Random random = new Random();

    public List<User> generateUsers() {
        List<User> users = new ArrayList<>();
        // User 목데이터 생성
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setName("User" + i);
            user.setPassword("password" + i);
            user.setEmail("user" + i + "@example.com");
            user.setPhoneNumber("010-0000-" + String.format("%04d", i));
            user.setAddress("Some Address " + i);
            user.setCountry("Country" + i);
            user.setGender(i % 2 == 0 ? "Male" : "Female");
            user.setImage("UserImg" + i);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            if (i < 10) {
                user.setRole(i % 2 == 0 ? Role.SENIOR : Role.GUARDIAN); // 번갈아가며 Senior와 Guardian 할당
            } else {
                user.setRole(Role.CAREGIVER); // 11~20은 Caregiver
            }
            users.add(user);
        }
        return users;
    }

    public List<Caregiver> generateCaregivers(List<User> users) {
        List<Caregiver> caregivers = new ArrayList<>();
        // Caregiver 목데이터 생성
        for (int i = 10; i < 20; i++) {
            Caregiver caregiver = new Caregiver();
            caregiver.setCertification("Certification" + i);
            caregiver.setSpecialization("Specialization" + i);
            caregiver.setExperience("Experience" + i);
            caregiver.setExperienceYears(random.nextInt(10));
            caregiver.setAvailableHours("9AM-5PM");
            caregiver.setUser(users.get(i)); // User와 연결
            caregivers.add(caregiver);
        }
        return caregivers;
    }

    public List<Senior> generateSeniors(List<User> users) {
        List<Senior> seniors = new ArrayList<>();
        // Senior 목데이터 생성
        for (int i = 0; i < 10; i++) {
            Senior senior = new Senior();
            senior.setHealth("Health" + i);
            senior.setRequirements("Requirements" + i);
            senior.setHasGuardian(random.nextBoolean());
            senior.setUser(users.get(i)); // User와 연결
            seniors.add(senior);
        }
        return seniors;
    }

    public List<Guardian> generateGuardians(List<User> users, List<Senior> seniors) {
        List<Guardian> guardians = new ArrayList<>();
        // Guardian 목데이터 생성
        for (int i = 0; i < seniors.size(); i++) {
            if (seniors.get(i).getHasGuardian()) {
                Guardian guardian = new Guardian();
                guardian.setGuardianName("Guard" + i);
                guardian.setRelationship("Relationship" + i);
                guardian.setUser(users.get(i)); // User와 연결
                guardian.setSenior(seniors.get(i)); // 연결된 Senior와 연결
                guardians.add(guardian);
            }
        }
        return guardians;
    }
}