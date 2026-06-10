package jungwon.splearn.domain;

import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

class EmailTest {

    @Test
    void equality(){
        var email1 = new Email("jungwon@naver.app");
        var email2 = new Email("jungwon@naver.app");


        assertThat(email1).isEqualTo(email2);
    }

}