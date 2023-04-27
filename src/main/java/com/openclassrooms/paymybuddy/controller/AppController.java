package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.*;
import com.openclassrooms.paymybuddy.model.UserAccount;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class AppController {
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private TransactionService transactionService;

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
         if (result.hasErrors()) {
            model.addAttribute("user_account", userAccountCreationDto);
            return "register";
        }

        try {
            userAccountService.saveUserAccount(userAccountCreationDto);
            return "redirect:/register?success";
        } catch (Exception e) {
            result.rejectValue("email", null, "An account already exist with the email " + userAccountCreationDto.getEmail());
            return "register";
        }
    }

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

    @GetMapping(value = "/transaction")
    public String showTransactionPage(Model model,
                                      Principal principal,
                                      @RequestParam(name = "page", required = false, defaultValue = "1") Integer currentPage,
                                      @RequestParam(name = "size", required = false, defaultValue = "5") Integer pageSize) {

        Page<TransactionDto> transactionDtoPage = transactionService.findPaginated(PageRequest.of(currentPage - 1, pageSize), principal.getName());

        model.addAttribute("transactionDtoPage", transactionDtoPage);

        int totalPages = transactionDtoPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }


        return "transaction";
    }

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
            model.addAttribute("add_contact", contactDto);
            model.addAttribute("contact_list", userAccountService.findContactList(principal.getName()));
            return "contact";
        }
        try {
            userAccountService.addContact(contactDto, principal.getName());
        } catch (Exception e) {
            model.addAttribute("message_error", e.getMessage());
            model.addAttribute("contact_list", userAccountService.findContactList(principal.getName()));
            return showContactPage(model, principal);
        }

        return "redirect:/contact?success";
    }
}
