package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import hello.jdbc.repository.MemberRepositoryV2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceV2Test {

    public static final String MA = "memberA";
    public static final String MB = "memberB";
    public static final String EX = "ex";

    private MemberRepositoryV2 memberRepository;
    private MemberServiceV2 memberService;

    @BeforeEach
    void init(){
        DriverManagerDataSource data = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepository = new MemberRepositoryV2(data);
        memberService = new MemberServiceV2(data,memberRepository);
    }

    @AfterEach
    void after() throws SQLException {
        memberRepository.delete(MA);
        memberRepository.delete(MB);
        memberRepository.delete(EX);
    }


    @Test
    @DisplayName("normal")
    void transferTest() throws SQLException {
        Member memberA = new Member(MA, 10000);
        Member memberB = new Member(MB, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        Member findA = memberRepository.findById(MA);
        Member findB = memberRepository.findById(MB);

        assertThat(findA.getMoney()).isEqualTo(8000);
        assertThat(findB.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("UnNormal")
    void transferTestFail() throws SQLException {
        Member memberA = new Member(MA, 10000);
        Member memberEX = new Member(EX, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberEX);

        assertThatThrownBy(()->memberService.accountTransfer(memberA.getMemberId(), memberEX.getMemberId(), 2000)).isInstanceOf(IllegalStateException.class);


        Member findA = memberRepository.findById(MA);
        Member findEX = memberRepository.findById(EX);

        assertThat(findA.getMoney()).isEqualTo(10000);
        assertThat(findEX.getMoney()).isEqualTo(10000);
    }




}