package org.example.account_book.Service;

import lombok.RequiredArgsConstructor;
import org.example.account_book.Constant.RoleType;
import org.example.account_book.Entity.MemberEntity;
import org.example.account_book.Repository.LoginRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PrincipalDetail implements UserDetails {

    private User user;

    public PrincipalDetail(User user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정이 만료되지 않았는지를 확인(true = 만료 안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겨있지 않은지를 확인(true = 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    // 비밀번호가 만료되지 않았는지를 확인(true = 만료 안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    // 계정 활성화(사용 가능)가 되어있는지를 확인(true = 활성화)
    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

    // 계정이 갖고 있는 권한 목록을 확인
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collectors = new ArrayList<>();

        collectors.add(() -> {
            return Arrays.toString(RoleType.values());	// ROLE_USER
        });

        return collectors;
    }

}
