package org.example.account_book.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.account_book.Constant.RoleType;
import org.example.account_book.DTO.MemberDTO;
import org.example.account_book.Entity.MemberEntity;
import org.example.account_book.Repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final ModelMapper modelMapper;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    //삽입
    public void save(MemberDTO memberDTO) throws IllegalAccessException{
        //기존 사용하는 email을 조회
        Optional<MemberEntity> memberEntity = Optional.ofNullable(memberRepository
                .findByEmail(memberDTO.getEmail()));

        String rawPassword = memberDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        //email이 일치하는 회원이 없으면
        if (memberEntity.isEmpty()) {
            //비밀번호를 암호화해서
            MemberEntity member = modelMapper.map(memberDTO, MemberEntity.class);
            //member.setPassword(encodedPassword);
            //저장
            memberRepository.save(member);
        } else {
            throw new IllegalAccessException("이미 가입된 회원입니다.");
        }

    }

    //수정
    public void update(MemberDTO memberDTO) {
        MemberEntity member = modelMapper.map(memberDTO, MemberEntity.class);

        //읽어온 값에서 비밀번호가 존재하면
        if (!memberDTO.getPassword().isEmpty()) {
            //비밀번호 암호화 작업
            member.setPassword(passwordEncoder.
                    encode(memberDTO.getPassword()));
        }
        memberRepository.save(member);

    }

    //삭제
    public void delete(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    //전체조회
    public List<MemberDTO> getmemberList() {
        List<MemberEntity> memberEntities = memberRepository.findAll();
        List<MemberDTO> memberDTOS = Arrays.asList(modelMapper.map(memberEntities, MemberDTO[].class));

        return memberDTOS;
    }

    //개별조회
    public MemberDTO findById(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow();
        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);

        return memberDTO;
    }

    //마이페이지 조회
    public MemberDTO findByEmail(String email) {
        MemberEntity member = memberRepository.findByEmail(email);
        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);

        return memberDTO;
    }
}
