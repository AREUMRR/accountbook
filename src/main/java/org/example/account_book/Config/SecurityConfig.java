package org.example.account_book.Config;

import org.example.account_book.Service.PrincipalDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfig {

    //authenticate 인증 매니저 생성(인증과정 처리)
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //비밀번호 보안 작업
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //권한범위(인증후 응답)
        http.authorizeHttpRequests((auth)-> {
            //모든 이용자가 접근가능
            auth.requestMatchers("/").permitAll();
            auth.requestMatchers("/login").permitAll();
            auth.requestMatchers("/logout").permitAll();
            auth.requestMatchers("/member/**").hasAnyRole("USER");
            auth.requestMatchers("/account/**").hasAnyRole("USER");
            auth.requestMatchers("/css/**", "/js/**", "/image/**").permitAll();
        });

        //로그인설정(기본 /login)
        //로그인 페이지는 모든 사용자가 접근 가능하고, 로그인 성공시 index 페이지로 이동
        http.formLogin(login-> login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                //로그인 성공 시 index.html 로 이동
                .defaultSuccessUrl("/", true)
                //로그인 실패
                .failureUrl("/login?error=true")
                .permitAll()
        );

        //로그아웃설정(기본 /logout)
        http.logout(logout->logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                //로그아웃 시 사용자 세션 삭제
                .invalidateHttpSession(true)
        );

        //csrf 사용X
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
