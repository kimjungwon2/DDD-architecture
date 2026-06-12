package jungwon.splearn;

import jungwon.splearn.application.required.EmailSender;
import jungwon.splearn.domain.MemberFixture;
import jungwon.splearn.domain.PasswordEncoder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class SplearnTestConfiguration {
    @Bean
    public EmailSender emailSender(){
        return (email, subject, body) -> System.out.println("Sending Email: "+email);
    }

    @Bean
    @Primary
    public static PasswordEncoder passwordEncoder(){
        return MemberFixture.createPasswordEncoder();
    }
}
