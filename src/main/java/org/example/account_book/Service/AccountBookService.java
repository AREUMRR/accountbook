package org.example.account_book.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.account_book.Constant.AccountRole;
import org.example.account_book.Constant.AccountType;
import org.example.account_book.DTO.AccountBookDTO;
import org.example.account_book.Entity.AccountBookEntity;
import org.example.account_book.Entity.MemberEntity;
import org.example.account_book.Repository.AccountBookRepository;
import org.example.account_book.Repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountBookService {
    private final ModelMapper modelMapper;

    private final AccountBookRepository accountBookRepository;

    private final MemberRepository memberRepository;

    //삽입
    public void save(AccountBookDTO accountBookDTO, Long memberId) {
        //회원 조회
        MemberEntity member = memberRepository.findById(memberId).orElseThrow();

        //회원 정보를 저장
        AccountBookEntity accountBookEntity = modelMapper.map(accountBookDTO, AccountBookEntity.class);
        accountBookEntity.setMemberEntity(member);
        accountBookRepository.save(accountBookEntity);
    }

    //수정
    public void update(AccountBookDTO accountBookDTO, Long memberId) {
        //회원 조회
        MemberEntity member = memberRepository.findById(memberId).orElseThrow();

        //날짜 문자열 유효성 검사
        //날짜 문자열을 LocalDate 객체로 변환
//        LocalDate date;
//        try {
//            date = LocalDate.parse(accountBookDTO.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        } catch (DateTimeParseException e) {
//            throw new IllegalArgumentException("Invalid date format. Expected 'yyyy-MM-dd'.", e);
//        }

        AccountBookEntity accountBookEntity = modelMapper.map(accountBookDTO, AccountBookEntity.class);
        accountBookEntity.setMemberEntity(member);
        //accountBookEntity.setDate(String.valueOf(date));
        accountBookRepository.save(accountBookEntity);

    }

    //삭제
    public void delete(Long accountBookId) {
        accountBookRepository.deleteById(accountBookId);
    }

    //전체조회
    public List<AccountBookDTO> getaccountBookList(Long memberId,
                                                   @RequestParam("type") String type,
                                                   @RequestParam("keyword") String keyword) {
        //가계부 조회
        List<AccountBookEntity> accountBook;
        //내용에서 키워드 가지고 검색
        if (type.equals("c") && keyword != null) {
            accountBook = accountBookRepository.findByContentContaining(keyword);
            //수입 내역 조회
        } else if (type.equals("i") && keyword.equals("INCOMES")) {
            accountBook = accountBookRepository.findByAccountRole(AccountRole.valueOf(keyword));
            //지출 내역 조회
        } else if (type.equals("e") && keyword.equals("EXPENSES")) {
            accountBook = accountBookRepository.findByAccountRole(AccountRole.valueOf(keyword));
            //카드 내역 조회
        } else if (type.equals("cd") && keyword.equals("CARD")) {
            accountBook = accountBookRepository.findByAccountType(AccountType.valueOf(keyword));
            //현금 내역 조회
        } else if (type.equals("ch") && keyword.equals("CASH")) {
            accountBook = accountBookRepository.findByAccountType(AccountType.valueOf(keyword));
            //은행 거래 내역 조회
        } else if (type.equals("b") && keyword.equals("BANK")) {
            accountBook = accountBookRepository.findByAccountType(AccountType.valueOf(keyword));
        } else {
            accountBook = accountBookRepository.findAll();
        }

        List<AccountBookDTO> accountBookDTO;

        //회원의 가계부 일 때 변환
        if (accountBook.get(0).getMemberEntity().getMemberId().equals(memberId)) {
            accountBookDTO = Arrays.asList(modelMapper.map(accountBook, AccountBookDTO[].class));
        } else {
            //조회한 내역이 없을 경우 예외발생 처리
            throw new RuntimeException("The member's account book list is empty.");
        }

        return accountBookDTO;
    }

    //개별조회
    public AccountBookDTO findById(Long accountBookId, Long memberId) {
        AccountBookEntity accountBook = accountBookRepository.findById(accountBookId).orElseThrow();

        AccountBookDTO accountBookDTO = null;
        //회원의 가계부 일 때 변환
        if (accountBook.getMemberEntity().getMemberId().equals(memberId)) {
            accountBookDTO = modelMapper.map(accountBook, AccountBookDTO.class);
        }

        return accountBookDTO;
    }

    //월별 조회
    public List<AccountBookDTO> getMonth(@RequestParam(value = "date") String date,
                                         Long memberId) {
        //가계부 조회
        List<AccountBookEntity> accountBook;

        //거래일(년,월) 기준으로 조회
        if (date != null) {
            accountBook = accountBookRepository.findByDateContaining(date);
        } else {
            accountBook = accountBookRepository.findAll();
        }

        List<AccountBookDTO> accountBookDTO;

        //회원의 가계부 일 때 변환
        if (accountBook.get(0).getMemberEntity().getMemberId().equals(memberId)) {
            accountBookDTO = Arrays.asList(modelMapper.map(accountBook, AccountBookDTO[].class));
        } else {
            //조회한 내역이 없을 경우 예외발생 처리
            throw new RuntimeException("The member's account book list is empty.");
        }

        return accountBookDTO;
    }

    public Long income(List<AccountBookDTO> accountBookDTO) {
        //수입 총액구하기
        Long income = 0L;

        for (AccountBookDTO bookDTO : accountBookDTO) {
            // Check if the account role is INCOMES
            if (bookDTO.getAccountRole().name().equals("INCOMES")) {
                // Add money to income
                income += bookDTO.getMoney();
            }
        }

        return income;
    }

    public Long expense(List<AccountBookDTO> accountBookDTO) {
        //지출 총액 구하기
        Long expense = 0L;

        for (AccountBookDTO bookDTO : accountBookDTO) {
            // Check if the account role is EXPENSES
            if (bookDTO.getAccountRole().name().equals("EXPENSES")) {
                // Add money to expense
                expense += bookDTO.getMoney();
            }
        }

        return expense;
    }

}
