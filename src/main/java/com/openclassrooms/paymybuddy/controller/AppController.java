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
import java.util.ArrayList;
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

        String email = userAccountCreationDto.getEmail();
        Optional<UserAccount> existingUser = userAccountService.findUserByEmail(email);

        if (existingUser.isPresent()) {
            result.rejectValue("email", null, "An account already exist with the Email" + email);
            return "register";
        }

        userAccountService.saveUserAccount(userAccountCreationDto);
        return "redirect:/register?success";
    }

    @GetMapping(value = "/reload")
    public String showReloadPage(Model model,
                                 Principal principal,
                                 ReloadDto reloadDto) {
        BigDecimal balance = userAccountService.getBalance(principal.getName());
        model.addAttribute("reload_amount", reloadDto);
        model.addAttribute("balance", balance);
        return "reload";
    }

    @PostMapping("/reload")
    public String reload(@Valid @ModelAttribute("reload_amount")ReloadDto reloadDto,
                         BindingResult result,
                         Model model,
                         Principal principal){
        if (result.hasErrors()) {
            model.addAttribute("reload_amount", reloadDto);
            return "reload";
        }
        userAccountService.reloadBalance(reloadDto, principal.getName());
        return "redirect:/reload?success";
    }

    @GetMapping(value = "/home")
    public String showHomePage(Model model,
                               Principal principal,
                               TransferMoneyDto transferMoneyDto){
        BigDecimal balance = userAccountService.getBalance(principal.getName());
        List<ContactDto> contactDtoList = userAccountService.findContactList(principal.getName());
        List<String> contactEmailList = new ArrayList<>();
        for (ContactDto contact : contactDtoList  ) {
            contactEmailList.add(contact.getEmail());
        }
        model.addAttribute("transfer_money", transferMoneyDto);
        model.addAttribute("contact_list", contactEmailList);
        model.addAttribute("balance", balance);
        return "home";
    }

    @PostMapping(value = "/home")
    public String transferMoney(@Valid @ModelAttribute("transfer_money")TransferMoneyDto transferMoneyDto,
                                Model model,
                                BindingResult result,
                                Principal principal){
        if (result.hasErrors()) {
            model.addAttribute("transfer_money", transferMoneyDto);
            return "home";
        }
        if (userAccountService.hasNotEnoughBalance(transferMoneyDto.getAmount(), principal.getName())) {
            result.rejectValue("amount", null, "There is not enough on your account to transfer " + transferMoneyDto.getAmount() + ". Please reload your account.");
            model.addAttribute("transfer_money", transferMoneyDto);
            return "home";
        }
        userAccountService.transferMoney(transferMoneyDto, principal.getName());
        return "redirect:/home?success";
    }

    @GetMapping(value = "/transaction")
    public String showTransactionPage(Model model,
                                      Principal principal,
                                      @RequestParam("page") Optional<Integer> page,
                                      @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

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
        List<ContactDto> contactDtoList = userAccountService.findContactList(principal.getName());
        model.addAttribute("contact_list", contactDtoList);
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
            return "contact";
        }

        String email = contactDto.getEmail();
        Optional<UserAccount> contact = userAccountService.findUserByEmail(email);

        if (contact.isEmpty()) {
            result.rejectValue("email", null, "There is no account created for the mail : " + email);
            return "contact";
        }

        userAccountService.addContact(contact.get(), principal.getName());
        return "redirect:/contact?success";
    }
}
