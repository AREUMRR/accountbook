package org.example.account_book.Repository;

import org.example.account_book.Entity.AccountBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface AccountBookRepository extends JpaRepository<AccountBookEntity, Long> {

    @Query("select w from AccountBookEntity w where w.memberEntity.memberId = :memberId")
    List<AccountBookEntity> findAllByMemberId(@RequestParam("memberId") Long memberId);

    @Query("select w from AccountBookEntity w where w.accountId = :accountId and w.memberEntity.memberId = :memberId")
    AccountBookEntity findByAccountIdAndMemberId(@RequestParam("accountId") Long accountId, @RequestParam("memberId") Long memberId);

    //거래일에서 조회
    @Query("select w from AccountBookEntity w where w.date like :keyword% order by w.date desc limit 1")
    List<AccountBookEntity> findByDate(@RequestParam("keyword") String keyword);

    //내용에서 조회
    @Query("select w from AccountBookEntity w where w.content like %:keyword% order by w.date desc limit 1")
    List<AccountBookEntity> findByContent(@RequestParam("keyword") String keyword);

    //수입내역 에서 조회
    @Query("select w from AccountBookEntity w where w.accountRole= :INCOMES order by w.date desc limit 1")
    List<AccountBookEntity> findByAccountRole_Incomes();

    //지출내역 에서 조회
    @Query("select w from AccountBookEntity w where w.accountRole= :EXPENSES order by w.date desc limit 1")
    List<AccountBookEntity> findByAccountRole_Expenses();

    //은행내역 에서 조회
    @Query("select w from AccountBookEntity w where w.accountType= :BANK order by w.date desc limit 1")
    List<AccountBookEntity> findByAccountType_Bank();

    //카드내역 에서 조회
    @Query("select w from AccountBookEntity w where w.accountType= :CARD order by w.date desc limit 1")
    List<AccountBookEntity> findByAccountType_Card();

    //현금내역 에서 조회
    @Query("select w from AccountBookEntity w where w.accountType= :CASH order by w.date desc limit 1")
    List<AccountBookEntity> findByAccountType_Cash();

}
