package jungwon.splearn.application.member.provided;

import jakarta.persistence.EntityManager;
import jungwon.splearn.SplearnTestConfiguration;
import jungwon.splearn.domain.member.Member;
import jungwon.splearn.domain.MemberFixture;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Import(SplearnTestConfiguration.class)
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
record MemberFinderTest(MemberFinder memberFinder, MemberRegister memberRegister, EntityManager entityManager) {
    @Test
    void find(){
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        Member found = memberFinder.find(member.getId());

        assertThat(member.getId()).isEqualTo(found.getId());
    }

    @Test
    void findFail(){
        assertThatThrownBy(()->memberFinder.find(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}