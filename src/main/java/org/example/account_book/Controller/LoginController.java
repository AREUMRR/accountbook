package org.example.account_book.Controller;

import lombok.RequiredArgsConstructor;
import org.example.account_book.DTO.MemberDTO;
import org.example.account_book.Service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/login")
    public String loginForm() {

        return "login/form";
    }

}
