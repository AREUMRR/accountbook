package org.example.account_book;

import org.example.account_book.Constant.RoleType;
import org.example.account_book.DTO.MemberDTO;
import org.example.account_book.Entity.MemberEntity;
import org.example.account_book.Repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class MemberTest {

    @Autowired
    MemberRepository memberRepository;
    
    @Autowired
    ModelMapper modelMapper;

    @Test
    public void memberSaveTest() {
        for(int i=2; i<=10; i++) {
            MemberEntity memberEntity = MemberEntity.builder()
                    .email("sksk"+i+"@gmail.com")
                    .password("1234")
                    .name("김나나")
                    .phone("010-0000-0000")
                    .nickName("rr")
                    .roleType(RoleType.USER)
                    .build();
            memberRepository.save(memberEntity);
        }
    }
    
    @Test
    public void memberUpdateTest() {

        MemberDTO memberDTO = MemberDTO.builder()
                .memberId(2L)
                .nickName("김김김")
                .build();
        MemberEntity member = modelMapper.map(memberDTO, MemberEntity.class);
        
        memberRepository.save(member);
    }
}
