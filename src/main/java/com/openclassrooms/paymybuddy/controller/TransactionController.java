package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.TransactionDto;
import com.openclassrooms.paymybuddy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

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
}
