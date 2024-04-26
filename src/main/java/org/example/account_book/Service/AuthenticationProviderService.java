package org.example.account_book.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.LoginException;
import javax.security.sasl.AuthenticationException;


@Service
@RequiredArgsConstructor
public class AuthenticationProviderService implements AuthenticationProvider {
    private final LoginService loginService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) {
        //입력한 email
        String email = authentication.getName();
        //email로 회원 조회
        UserDetails user = loginService.loadUserByUsername(email);

        //입력한 패스워드
        String rawPassword = authentication.getCredentials().toString();
        //보안 처리 된 패스워드
        String hashPassword = user.getPassword();

        checkPassword(rawPassword, hashPassword);

        System.out.println(checkPassword(rawPassword, hashPassword));

        return new UsernamePasswordAuthenticationToken(email, rawPassword, user.getAuthorities());
    }

    private boolean checkPassword(String rawPassword, String hashPassword) {

        //입력한 패스워드와 DB에 보안 처리 되어 저장된 패스워드 비교
        if (passwordEncoder.matches(rawPassword, hashPassword)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}