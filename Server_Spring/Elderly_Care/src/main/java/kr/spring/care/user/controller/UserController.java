package kr.spring.care.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import kr.spring.care.user.dto.UserFormDto;
import kr.spring.care.user.entity.User;
import kr.spring.care.user.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "user/userLogin";
    }
    
    @GetMapping("/joinCheck")
	public String joinCheck() {
		return "user/userJoinCheck";
	}
    @GetMapping("/joinChoose")
	public String joinChoose() {
		return "user/userChoose";
	}
    
    @GetMapping("/register")
    public String userForm(Model model, @RequestParam(required = false) String userType) {
        model.addAttribute("userFormDto", new UserFormDto());
        model.addAttribute("userType", userType);
        return "user/userForm";
    }
    
	@GetMapping("/login/requireLogin")
	public String requireLogin(Model model) {
		model.addAttribute("loginErrorMsg", "로그인이 필요한 기능입니다");
		return "user/userLogin";
	}
	
	@GetMapping("/login/error")
	public String loginError(Model model) {
		model.addAttribute("loginErrorMsg", "아이디 또는 패스워드가 잘못되었습니다.");
		return "user/userLogin";
	}
    
    @PostMapping("/login")
    public String loginId(@ModelAttribute User user) {
        if (userService.login(user, passwordEncoder)) {
            return "redirect:/";
        }
        return "redirect:/user/login?error";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userFormDto") UserFormDto userFormDto,
                               BindingResult result,
                               Model model,
                               @RequestParam(required = false) String userType) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "입력 정보를 확인해주세요");
            model.addAttribute("userType", userType);
            return "user/userForm";
        }
        try {
            User user = User.createUser(userFormDto, passwordEncoder);
            userService.saveUser(user);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", "입력 정보를 확인해주세요");
            model.addAttribute("userType", userType);
            return "user/userForm";
        }
        return "redirect:/user/login";
    }

}
