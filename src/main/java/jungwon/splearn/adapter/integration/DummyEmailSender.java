package jungwon.splearn.adapter.integration;

import jungwon.splearn.application.required.EmailSender;
import jungwon.splearn.domain.Email;

public class DummyEmailSender implements EmailSender {
    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("DummyEmailSender send Email:" + email);
    }
}
