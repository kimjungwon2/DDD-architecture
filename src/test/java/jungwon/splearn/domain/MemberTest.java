package jungwon.splearn.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    void createMember(){
        var member = new Member("toby@splearn.app","Toby","secret");

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }
}