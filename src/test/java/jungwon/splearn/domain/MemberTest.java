package jungwon.splearn.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    void createMember(){
        var member = new Member("toby@splearn.app","Kim","secret");

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void consturctorNullCheck(){
        assertThatThrownBy(()->new Member(null,"Kim","secret"))
                .isInstanceOf(NullPointerException.class);
    }
    
    @Test
    void activate(){
        var member = new Member("kim","kim","secret");
        member.activate();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void activateFail(){
        var member = new Member("kim","kim","secret");
        member.activate();

        assertThatThrownBy(()->{
            member.activate();
        }).isInstanceOf(IllegalStateException.class);
    }
    
    @Test
    void deactivate(){
        var member = new Member("toby","Toby","secret");
        member.activate();

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    void deActivateFail(){
        var member = new Member("toby","Toby","secret");


        assertThatThrownBy(()->{
            member.deactivate();
        }).isInstanceOf(IllegalStateException.class);
    }
}