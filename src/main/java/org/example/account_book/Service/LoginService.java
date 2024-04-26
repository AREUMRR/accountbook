package org.example.account_book.Service;

import lombok.RequiredArgsConstructor;
import org.example.account_book.Entity.MemberEntity;
import org.example.account_book.Repository.LoginRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //회원 이메일이 존재하는지 조회
        Optional<MemberEntity> memberEntity = loginRepository.findByEmail(email);

        //조회한 데이터가 있으면
        if (memberEntity.isPresent()) {
            return User.withUsername(memberEntity.get().getEmail())
                    //passwordEncoder 비밀번호 보안작업
                    .password(passwordEncoder.encode(memberEntity.get().getPassword()))
                    .roles(memberEntity.get().getRoleType().name())
                    .build();

        } else { //조회된 데이터가 없으면
            throw new UsernameNotFoundException("존재하지 않는 회원입니다.");
        }
    }


}
