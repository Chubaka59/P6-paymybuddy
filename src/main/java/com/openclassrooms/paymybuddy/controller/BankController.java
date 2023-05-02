package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.BankDto;
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

@Controller
public class BankController {
    @Autowired
    private UserAccountService userAccountService;

    @GetMapping(value = "/bank")
    public String showBankPage(Model model,
                               Principal principal,
                               BankDto bankDto) {
        BigDecimal balance = userAccountService.getBalance(principal.getName());
        model.addAttribute("transfer_amount", bankDto);
        model.addAttribute("balance", balance);
        return "bank";
    }

    @PostMapping("/bank")
    public String bank(@Valid @ModelAttribute("transfer_amount") BankDto bankDto,
                       BindingResult result,
                       Model model,
                       Principal principal){
        BigDecimal balance = userAccountService.getBalance(principal.getName());
        if (result.hasErrors()) {
            model.addAttribute("transfer_amount", bankDto);
            model.addAttribute("balance", balance);
            return "bank";
        }
        try {
            userAccountService.bankTransfer(bankDto, principal.getName());
            return "redirect:/bank?success";
        } catch (Exception e) {
            model.addAttribute("transfer_amount", bankDto);
            model.addAttribute("balance", balance);
            result.rejectValue("amount", null, e.getMessage());
            return "bank";
        }
    }
}
