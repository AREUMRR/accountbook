package org.example.account_book.Repository;

import org.example.account_book.Constant.AccountRole;
import org.example.account_book.Constant.AccountType;
import org.example.account_book.Entity.AccountBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface AccountBookRepository extends JpaRepository<AccountBookEntity, Long> {

    @Query("select w from AccountBookEntity w where w.memberEntity.memberId = :memberId")
    List<AccountBookEntity> findByMemberId(@RequestParam(value = "memberId") Long memberId);

    @Query("select w from AccountBookEntity w where w.accountId = :accountId and w.memberEntity.memberId = :memberId")
    AccountBookEntity findByAccountIdAndMemberId(@RequestParam(value = "accountId") Long accountId, @RequestParam("memberId") Long memberId);

    //거래일에서 조회
    List<AccountBookEntity> findByDateContaining(String date);

    //내용에서 조회
    List<AccountBookEntity> findByContentContaining(String keyword);

    //지출내역 에서 조회
    List<AccountBookEntity> findByAccountRole(AccountRole accountRole);

    //은행내역 에서 조회
    List<AccountBookEntity> findByAccountType(AccountType accountType);

}
