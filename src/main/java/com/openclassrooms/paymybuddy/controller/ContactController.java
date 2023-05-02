package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.ContactDto;
import com.openclassrooms.paymybuddy.service.UserAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class ContactController {
    @Autowired
    private UserAccountService userAccountService;

    @GetMapping(value = "/contact")
    public String showContactPage(Model model, Principal principal) {
        model.addAttribute("contact_list", userAccountService.findContactList(principal.getName()));
        model.addAttribute("add_contact", new ContactDto());
        return "contact";
    }

    @PostMapping("/contact")
    public String addContact(@Valid @ModelAttribute("add_contact") ContactDto contactDto,
                             BindingResult result,
                             Model model,
                             Principal principal){
        if (result.hasErrors()) {
            return showContactPage(model, principal);
        }
        try {
            userAccountService.addContact(contactDto, principal.getName());
        } catch (Exception e) {
            model.addAttribute("message_error", e.getMessage());
            return showContactPage(model, principal);
        }

        return "redirect:/contact?success";
    }
}
