package jungwon.splearn.adapter.integration;

import jungwon.splearn.domain.shared.Email;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DummyEmailSenderTest {

    @Test
    @StdIo
    void dummyEmailSender(StdOut out){
        DummyEmailSender dummyEmailSender = new DummyEmailSender();
        dummyEmailSender.send(new Email("jungwon@splearn.app"),"subject","body");

        assertThat(out.capturedLines()[0]).isEqualTo("DummyEmailSender send Email:Email[address=jungwon@splearn.app]");
    }
}