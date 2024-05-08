package org.example.account_book.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.account_book.Constant.AccountRole;
import org.example.account_book.Constant.AccountType;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "account_book")
@SequenceGenerator(name = "account_book_SEQ", sequenceName = "account_book_SEQ", allocationSize = 1)
public class AccountBookEntity extends BaseEntity{

    //가계부 번호
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_book_SEQ")
    @Column
    private Long accountId;

    //금액
    @Column
    private Long money;

    //내용
    @Column
    private String content;

    //거래일
    @Column
    private String date;

    //내역 분류(수입, 지출)
    @Enumerated(EnumType.STRING)
    private AccountRole accountRole;

    //지출 타입(카드, 현금, 이체)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    //회원 정보
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;

}
