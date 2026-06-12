package jungwon.splearn.application.member.required;

import jakarta.persistence.EntityManager;
import jungwon.splearn.domain.member.Member;
import jungwon.splearn.domain.member.MemberStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static jungwon.splearn.domain.MemberFixture.createMemberRegisterRequest;
import static jungwon.splearn.domain.MemberFixture.createPasswordEncoder;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void createMember(){
        Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

        assertThat(member.getId()).isNull();

        memberRepository.save(member);

        assertThat(member.getId()).isNotNull();

        entityManager.flush();
        entityManager.clear();

        var found = memberRepository.findById(member.getId()).orElseThrow();

        assertThat(found.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(found.getDetail().getRegisteredAt()).isNotNull();
    }

    @Test
    void duplicateEmailFail(){
        Member member1 = Member.register(createMemberRegisterRequest(), createPasswordEncoder());

        memberRepository.save(member1);

        Member member2 = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
        assertThatThrownBy(()->memberRepository.save(member2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}