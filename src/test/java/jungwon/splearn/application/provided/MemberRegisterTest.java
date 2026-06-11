package jungwon.splearn.application.provided;


import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import jungwon.splearn.SplearnTestConfiguration;
import jungwon.splearn.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Import(SplearnTestConfiguration.class)
@Transactional
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

    @Test
    void register(){
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmailFail(){
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        assertThatThrownBy(()->memberRegister.register(MemberFixture.createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmailException.class);
    }
    
    @Test
    void activate(){
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.activate(member.getId());

        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }
    
    @Test
    void memberRegisterRequestFail(){
        checkValidation(new MemberRegisterRequest("toby@Splearn.app", "Toby", "longsecret"));
        checkValidation(new MemberRegisterRequest("toby@Splearn.app", "Charlie________________________________________", "longsecret"));
        checkValidation(new MemberRegisterRequest("tobySplearn.app", "Charlie________________________________________", "longsecret"));
    }

    private void checkValidation(MemberRegisterRequest invalid){
        assertThatThrownBy(()->memberRegister.register(invalid))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
