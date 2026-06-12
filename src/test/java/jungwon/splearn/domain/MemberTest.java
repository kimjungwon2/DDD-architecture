package jungwon.splearn.domain;

import jungwon.splearn.domain.member.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static jungwon.splearn.domain.MemberFixture.createMemberRegisterRequest;
import static jungwon.splearn.domain.MemberFixture.createPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp(){
        this.passwordEncoder = createPasswordEncoder();

        member = Member.register(createMemberRegisterRequest(), passwordEncoder);
    }

    @Test
    void createMember(){
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(member.getDetail().getRegisteredAt()).isNotNull();
    }
    
    @Test
    void activate(){
        member.activate();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void activateFail(){
        member.activate();

        assertThatThrownBy(()->{
            member.activate();
        }).isInstanceOf(IllegalStateException.class);
    }
    
    @Test
    void deactivate(){
        member.activate();
        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    @Test
    void deActivateFail(){
        assertThatThrownBy(()->{
            member.deactivate();
        }).isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(()->{
            member.deactivate();
        }).isInstanceOf(IllegalStateException.class);
    }
    
    @Test
    void verifyPassword(){

        assertThat(member.verifyPassword("secretArdfjl", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("hello", passwordEncoder)).isFalse();
    }
    
    @Test
    void changeNickName(){
        assertThat(member.getNickname()).isEqualTo("Kimdmds");
        member.changeNickName("Charlie");

        assertThat(member.getNickname()).isEqualTo("Charlie");
    }
    
    @Test
    void changePassword(){
        member.changePassword("verysecret", passwordEncoder);

        assertThat(member.verifyPassword("verysecret",passwordEncoder)).isTrue();
    }

    @Test
    void isActive(){
        assertThat(member.isActive()).isFalse();

        member.activate();

        assertThat(member.isActive()).isTrue();

        member.deactivate();

        assertThat(member.isActive()).isFalse();
    }


    @Test
    void invalidEmail(){
        assertThatThrownBy(()->{
            Member.register(createMemberRegisterRequest("Invalid email"), passwordEncoder);
        }).isInstanceOf(IllegalArgumentException.class);

        Member.register(new MemberRegisterRequest("jungwon@splearn.app", "Kim", "secret"), passwordEncoder);
    }

    @Test
    void updateInfo(){
        member.activate();
        var request = new MemberInfoUpdateRequest("Leo", "toby100","자기소개")
        member.updateInfo(new MemberInfoUpdateRequest("Leo","toby100","자기소개"));


        assertThat(member.getNickname()).isEqualTo(request.nickname());
        assertThat(member.getDetail().getProfile().address()).isEqualTo(request.profileAddress());
        assertThat(member.getDetail().getIntroduction()).isEqualTo(request.introduction());
    }
}