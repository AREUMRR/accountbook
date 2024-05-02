package org.example.account_book.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.account_book.Constant.RoleType;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "member")
@SequenceGenerator(name = "member_SEQ", sequenceName = "member_SEQ", allocationSize = 1)
public class MemberEntity extends BaseEntity {

    //회원 번호
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_SEQ")
    @Column
    private Long memberId;

    //회원 이름
    @Column(length = 10)
    private String name;

    //회원 닉네임
    @Column(unique = true, length = 10)
    private String nickName;

    //회원 이메일
    @Column(unique = true, length = 20, nullable = false)
    private String email;

    //회원 비밀번호
    @Column(length = 50, nullable = false)
    private String password;

    //회원 비밀번호 확인
    @Column(length = 20, nullable = false)
    private String passwordConfirm;

    //회원 전화번호
    @Column(length = 20)
    private String phone;

    //회원 분류
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
}
