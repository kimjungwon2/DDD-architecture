package jungwon.splearn.application.member.provided;


import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import jungwon.splearn.SplearnTestConfiguration;
import jungwon.splearn.domain.*;
import jungwon.splearn.domain.member.*;
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
    void deactivate(){
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.activate(member.getId());
        entityManager.flush();

        memberRegister.deactivate(member.getId());

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }
    
    @Test
    void updateInfo(){
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("Peter", "toby100", "자기소개"));
        assertThat(member.getDetail().getProfile().address()).isEqualTo("toby100");
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
