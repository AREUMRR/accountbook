package org.example.account_book.Controller;

import lombok.RequiredArgsConstructor;
import org.example.account_book.DTO.AccountBookDTO;
import org.example.account_book.DTO.MemberDTO;
import org.example.account_book.Service.AccountBookService;
import org.example.account_book.Service.MemberService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountBookController {
    private final AccountBookService accountBookService;
    private final MemberService memberService;

    //삽입
    //로그인 한 경우에만 실행
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/account/save")
    public String saveForm(Authentication authentication, Model model) {

        MemberDTO memberDTO = memberService.findByEmail(authentication.getName());

        model.addAttribute("member", memberDTO);

        return "accountbook/save";
    }

    //로그인 한 경우에만 실행
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/account/save")
    public String saveProcess(AccountBookDTO accountBookDTO, Authentication authentication,
                              RedirectAttributes redirectAttributes) {

        //로그인한 회원의 정보를 읽어온다
        MemberDTO memberDTO = memberService.findByEmail(authentication.getName());

        accountBookService.save(accountBookDTO, memberDTO.getMemberId());

        redirectAttributes.addFlashAttribute("successMessage",
                "게시글이 등록되었습니다.");

        System.out.println(memberDTO);
        System.out.println(accountBookDTO);

        return "redirect:/account/list";
    }

    //수정
    @GetMapping("/account/update")
    public String updateForm(@RequestParam("id") Long id, Model model,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        //로그인한 회원의 정보를 읽어온다
        MemberDTO memberDTO = memberService.findByEmail(authentication.getName());

        //가계부 조회
        AccountBookDTO accountBookDTO = accountBookService.findById(id, memberDTO.getMemberId());

        //가계부에 있는 회원 ID와 로그인한 회원의 ID가 일치하지 않으면
        if (!accountBookDTO.getMemberId().equals(memberDTO.getMemberId())) {
            redirectAttributes.addFlashAttribute("error",
                    "회원 정보가 일치하지 않습니다.");
            return "redirect:/account/list";
        }

        model.addAttribute("data", accountBookDTO);

        System.out.println(memberDTO);
        System.out.println(accountBookDTO);

        return "accountbook/update";
    }

    @PostMapping("/account/update")
    public String updateProcess(AccountBookDTO accountBookDTO,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        //로그인한 회원의 정보를 읽어온다
        MemberDTO memberDTO = memberService.findByEmail(authentication.getName());

        if (memberDTO != null) {
            accountBookService.update(accountBookDTO, memberDTO.getMemberId());
        }

        redirectAttributes.addFlashAttribute("successMessage",
                "게시글이 수정되었습니다.");

        System.out.println(accountBookDTO);
        return "redirect:/account/list";
    }

    //삭제
    @GetMapping("/account/delete")
    public String deleteProcess(Long id, Authentication authentication,
                                RedirectAttributes redirectAttributes) {

        //로그인한 회원의 정보를 읽어온다
        MemberDTO memberDTO = memberService.findByEmail(authentication.getName());
        //가계부 조회
        AccountBookDTO accountBookDTO = accountBookService.findById(id, memberDTO.getMemberId());

        //가계부에 있는 회원 ID와 로그인한 회원의 ID가 일치하면 삭제
        if (accountBookDTO.getMemberId().equals(memberDTO.getMemberId())) {
            accountBookService.delete(id);
        }

        redirectAttributes.addFlashAttribute("successMessage",
                "게시글이 삭제되었습니다.");

        System.out.println(accountBookDTO);
        return "redirect:/account/list";
    }

    //전체조회
    @GetMapping("/account/list")
    public String accountBookList(Model model, Authentication authentication,
                                  @RequestParam(value = "type", defaultValue = "") String type,
                                  @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        //로그인한 회원의 정보를 읽어온다
        MemberDTO memberDTO = memberService.findByEmail(authentication.getName());

        List<AccountBookDTO> accountBookDTO = accountBookService.getaccountBookList(memberDTO.getMemberId(), type, keyword);

        Long income = accountBookService.income(accountBookDTO);
        Long expense = accountBookService.expense(accountBookDTO);

        model.addAttribute("income", income);

        model.addAttribute("expense", expense);

        model.addAttribute("list", accountBookDTO);

        System.out.println(accountBookDTO);
        return "accountbook/list";
    }

    //월별조회
    @GetMapping("/account/month")
    public String monthList(Model model, String date, Authentication authentication) {

        //로그인한 회원의 정보를 읽어온다
        MemberDTO memberDTO = memberService.findByEmail(authentication.getName());

        List<AccountBookDTO> accountBookDTO = accountBookService.getMonth(date, memberDTO.getMemberId());

        Long income = accountBookService.income(accountBookDTO);
        Long expense = accountBookService.expense(accountBookDTO);

        model.addAttribute("income", income);

        model.addAttribute("expense", expense);

        model.addAttribute("list", accountBookDTO);

        model.addAttribute("date", date);

        System.out.println(accountBookDTO);
        return "accountbook/month";
    }

    //개별조회
    @GetMapping("/account/detail")
    public String accountBookDetail(Long id, Authentication authentication, Model model) {

        //로그인한 회원의 정보를 읽어온다
        MemberDTO memberDTO = memberService.findByEmail(authentication.getName());

        AccountBookDTO accountBookDTO = accountBookService.findById(id, memberDTO.getMemberId());

        model.addAttribute("data", accountBookDTO);

        System.out.println(accountBookDTO);
        return "accountbook/detail";
    }

}