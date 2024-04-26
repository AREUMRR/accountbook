package org.example.account_book.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.account_book.Constant.AccountRole;
import org.example.account_book.Constant.AccountType;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountBookDTO {

    //가계부 번호
    private Long accountId;

    //금액
    @NotBlank
    private Long money;

    //내용
    @NotBlank
    private String content;

    //분류(수입, 지출, 총액)
    @NotBlank
    private AccountRole accountRole;

    //지출 타입(카드, 현금, 이체)
    @NotBlank
    private AccountType accountType;

    //거래일
    @NotBlank
    private String date;

    //회원 정보
    private Long memberId;

    //등록일
    private LocalDateTime createdDate;

    //수정일
    private LocalDateTime modifiedDate;
}
