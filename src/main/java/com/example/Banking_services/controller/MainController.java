package com.example.Banking_services.controller;

import com.example.Banking_services.model.Account;
import com.example.Banking_services.model.User;
import com.example.Banking_services.service.BankingService;
import com.example.Banking_services.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.math.BigDecimal;
import java.security.Principal;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private BankingService bankingService;

    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        model.addAttribute("user", user);
        model.addAttribute("account", user.getAccount());
        model.addAttribute("transactions", bankingService.getTransactionHistory(user.getAccount()));

        return "dashboard";
    }

    @GetMapping("/transfer")
    public String transferPage(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("account", user.getAccount());
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transferMoney(
            @RequestParam String toAccountNumber,
            @RequestParam BigDecimal amount,
            Principal principal,
            Model model) {
        User user = userService.findByUsername(principal.getName());
        try {
            bankingService.transfer(user.getAccount(), toAccountNumber, amount);
            return "redirect:/dashboard?success";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("account", user.getAccount());
            return "transfer";
        }
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam BigDecimal amount, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        bankingService.deposit(user.getAccount(), amount);
        return "redirect:/dashboard?success";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam BigDecimal amount, Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        try {
            bankingService.withdraw(user.getAccount(), amount);
            return "redirect:/dashboard?success";
        } catch (RuntimeException e) {
            return "redirect:/dashboard?error=" + e.getMessage();
        }
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") User user, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());

        if (!currentUser.getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        userService.updateUser(user);

        return "redirect:/profile?success";
    }

    @PostMapping("/profile/delete")
    public String deleteAccount(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        userService.deleteUser(user.getId());
        return "redirect:/logout";
    }
}