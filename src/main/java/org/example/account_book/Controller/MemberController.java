package org.example.account_book.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.account_book.Constant.RoleType;
import org.example.account_book.DTO.MemberDTO;
import org.example.account_book.Service.MemberService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    //삽입
    @GetMapping("/member/save")
    public String saveForm(Model model) {

        MemberDTO memberDTO = new MemberDTO();
        model.addAttribute("data", memberDTO);

        return "member/save";
    }

    @PostMapping("/member/save")
    public String saveProcess(@Valid MemberDTO memberDTO, BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        //오류가 있으면 회원 가입페이지로 이동
        if (bindingResult.hasErrors()) {
            return "redirect:/member/save";
        }

        if (!memberDTO.getPassword().equals(memberDTO.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "<PASSWORD>",
                    "2개의 비밀번호가 일치하지 않습니다.");

            return "redirect:/member/save";
        }

        //회원 ID 또는 이메일 주소가 존재할 경우 예외 발생처리
        try {
            memberDTO.setRoleType(RoleType.USER);
            memberService.save(memberDTO);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "redirect:/member/save";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "redirect:/member/save";
        }

        //회원 리스트 조회
        List<MemberDTO> memberDTO1 = memberService.getmemberList();
        //닉네임이 있으면
        if (memberDTO1.get(0).getNickName().equals(memberDTO.getNickName())) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "닉네임이 중복입니다.");

            return "redirect:/member/save";
        }

        redirectAttributes.addFlashAttribute("successMassage",
                "가입 되었습니다.");

        return "redirect:/login";
    }

    //수정
    @GetMapping("/member/update")
    public String updateForm(Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);

        model.addAttribute("data", memberDTO);

        return "member/update";
    }

    @PostMapping("/member/update")
    public String updateProcess(MemberDTO memberDTO, RedirectAttributes redirectAttributes) {
        memberService.update(memberDTO);

        redirectAttributes.addFlashAttribute("successMassage",
                "수정 되었습니다.");

        return "redirect:/";
    }

    //삭제
    @GetMapping("/member/delete")
    public String deleteProcess(Long id, RedirectAttributes redirectAttributes) {
        memberService.delete(id);

        redirectAttributes.addFlashAttribute("successMassage",
                "삭제 되었습니다.");

        return "redirect:/";
    }

    //전체조회
    @GetMapping("/member/list")
    public String memberList(Model model) {
        List<MemberDTO> memberDTO = memberService.getmemberList();

        model.addAttribute("list", memberDTO);

        return "member/list";
    }

    //개별조회
    @GetMapping("/member/detail")
    public String memberDetail(Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);

        model.addAttribute("data", memberDTO);

        return "member/detail";
    }

    //마이페이지
    //로그인 한 경우에만 실행
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/member/mypage")
    public String myPageForm(Authentication authentication,
                             Model model) {

        //보안 인증 된 유저의 이메일로 회원 정보 찾기
        String memberEmail = authentication.getName();
        MemberDTO memberDTO = memberService.findByEmail(memberEmail);

        if (memberDTO == null) {
            return "redirect:/login";
        }

        model.addAttribute("data", memberDTO);

        return "mypage/detail";
    }
}
