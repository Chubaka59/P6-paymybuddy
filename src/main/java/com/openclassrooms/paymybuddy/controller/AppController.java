package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.UserAccountCreationDto;
import com.openclassrooms.paymybuddy.model.UserAccount;
import com.openclassrooms.paymybuddy.service.UserAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class AppController {
    @Autowired
    private UserAccountService userAccountService;

    @GetMapping(value =  {"/login"})
    public String showLoginPage(){
        return "login";
    }

    @GetMapping(value = "/register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user_account", new UserAccountCreationDto());
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user_account")UserAccountCreationDto userAccountCreationDto,
                               BindingResult result,
                               Model model) {
        Optional<UserAccount> existingUser = userAccountService.findUserByEmail(userAccountCreationDto.getEmail());

        if (existingUser.isPresent()) {
            result.rejectValue("email", null, "An account already exist with the Email" + userAccountCreationDto.getEmail());
        }

        if (result.hasErrors()) {
            model.addAttribute("user_account", userAccountCreationDto);
            return "register";
        }

        userAccountService.saveUserAccount(userAccountCreationDto);
        return "redirect:/register?success";
    }

    @GetMapping(value = "/home")
    public String showHomePage() {
        return "home";
    }
}
