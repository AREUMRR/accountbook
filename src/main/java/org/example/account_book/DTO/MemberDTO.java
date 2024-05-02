package org.example.account_book.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.account_book.Constant.RoleType;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {

    //회원 번호
    private Long memberId;

    //회원 이름
    @NotBlank
    private String name;

    //회원 닉네임
    private String nickName;

    //회원 이메일
    @NotBlank(message = "이메일은 필수입니다.")
    @Email
    private String email;

    //회원 비밀번호
    @NotBlank (message = "비밀번호는 필수입니다.")
    @Length(min = 4, max = 20, message = "4~20자 사이로 입력해주세요")
    private String password;

    @NotBlank (message = "비밀번호 확인은 필수입니다.")
    private String passwordConfirm;

    //회원 전화번호
    private String phone;

    //회원 분류
    private RoleType roleType;

    //등록일
    private LocalDateTime createdDate;

    //수정일
    private LocalDateTime modifiedDate;
}
