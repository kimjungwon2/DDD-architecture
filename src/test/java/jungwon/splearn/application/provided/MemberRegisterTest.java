package jungwon.splearn.application.provided;

import jungwon.splearn.application.MemberService;
import jungwon.splearn.application.required.EmailSender;
import jungwon.splearn.application.required.MemberRepository;
import jungwon.splearn.domain.Email;
import jungwon.splearn.domain.Member;
import jungwon.splearn.domain.MemberFixture;
import jungwon.splearn.domain.MemberStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class MemberRegisterTest {

    @Test
    void registerTestStub() {
        MemberRegister register = new MemberService(
          new MemberRepositoryStub(), new EmailSenderStub(), MemberFixture.createPasswordEncoder()
        );

        Member member = register.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void registerTestMockito() {
        EmailSender emailSenderMock = Mockito.mock(EmailSender.class);

        MemberRegister register = new MemberService(
                new MemberRepositoryStub(), emailSenderMock, MemberFixture.createPasswordEncoder()
        );

        Member member = register.register(MemberFixture.createMemberRegisterRequest());

        Mockito.verify(emailSenderMock).send(eq(member.getEmail()),any(),any());
    }

    static class MemberRepositoryStub implements MemberRepository {
        @Override
        public Member save(Member member){
            ReflectionTestUtils.setField(member, "id", 1L);
            return member;
        }
    }
    static class EmailSenderStub implements EmailSender {

        @Override
        public void send(Email email, String subject, String body) {

        }
    }


}