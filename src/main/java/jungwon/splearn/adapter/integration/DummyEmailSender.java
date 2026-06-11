package jungwon.splearn.adapter.integration;

import jungwon.splearn.application.required.EmailSender;
import jungwon.splearn.domain.Email;
import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Component;

@Component
@Fallback
public class DummyEmailSender implements EmailSender {
    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("DummyEmailSender send Email:" + email);
    }
}
