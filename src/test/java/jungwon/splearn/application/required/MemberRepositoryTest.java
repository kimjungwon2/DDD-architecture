package jungwon.splearn.application.required;

import jakarta.persistence.EntityManager;
import jungwon.splearn.domain.Member;
import jungwon.splearn.domain.MemberRegisterRequest;
import jungwon.splearn.domain.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static jungwon.splearn.domain.MemberFixture.createMemberRegisterRequest;
import static jungwon.splearn.domain.MemberFixture.createPasswordEncoder;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void setUp(){
        Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

        assertThat(member.getId()).isNull();

        memberRepository.save(member);

        assertThat(member.getId()).isNotNull();

        entityManager.flush();
    }


    @Test
    void createMember(){
    }
}