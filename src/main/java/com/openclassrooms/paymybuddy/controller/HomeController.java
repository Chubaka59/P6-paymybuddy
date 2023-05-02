package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.ContactDto;
import com.openclassrooms.paymybuddy.dto.TransferMoneyDto;
import com.openclassrooms.paymybuddy.service.UserAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private UserAccountService userAccountService;

    @GetMapping(value = "/home")
    public String showHomePage(Model model,
                               Principal principal,
                               TransferMoneyDto transferMoneyDto){
        BigDecimal balance = userAccountService.getBalance(principal.getName());
        List<ContactDto> contactDtoList = userAccountService.findContactList(principal.getName());
        model.addAttribute("transfer_money", transferMoneyDto);
        model.addAttribute("contact_list", contactDtoList);
        model.addAttribute("balance", balance);
        return "home";
    }

    @PostMapping(value = "/home")
    public String transferMoney(@Valid @ModelAttribute("transfer_money")TransferMoneyDto transferMoneyDto,
                                BindingResult result,
                                Model model,
                                Principal principal){
        if (result.hasErrors()) {
            model.addAttribute("transfer_money", transferMoneyDto);
            return showHomePage(model, principal, transferMoneyDto);
        }
        try{
            userAccountService.transferMoney(transferMoneyDto, principal.getName());
        }catch (Exception e){
            model.addAttribute("message_error", e.getMessage());
            return showHomePage(model, principal, transferMoneyDto);
        }
        return "redirect:/home?success";
    }
}
