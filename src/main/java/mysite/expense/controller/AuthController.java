package mysite.expense.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mysite.expense.dto.UserDTO;
import mysite.expense.entity.User;
import mysite.expense.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

//인증컨트롤러
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService uService;

    @GetMapping({"/login", "/"})
    public String showLoginPage(Principal principal) {
        if(principal == null) {
            return "login";
        }
        return "redirect:/expenses";
    }

    //등록하기 페이지(가입)
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new UserDTO());
        return "register";
    }

    //유저등록 처리
    @PostMapping("/register")
    public String processRegister(@Valid  @ModelAttribute("user") UserDTO user,
                                  BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "register";//되돌아감
        }

        System.out.println("유저DTO객체 : " + user);
        uService.save(user);
        model.addAttribute("successMsg", true);
        return "response";
    }
}
