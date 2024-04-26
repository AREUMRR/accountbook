package org.example.account_book.Repository;

import org.example.account_book.Entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<MemberEntity, Long> {
    //회원 이메일 조회
    Optional<MemberEntity> findByEmail(String email);

    //회원 비밀번호, 이름 조회
    Optional<MemberEntity> findByPasswordAndName(String password, String name);
}
