package jungwon.splearn.application;

import jungwon.splearn.application.provided.MemberRegister;
import jungwon.splearn.application.required.EmailSender;
import jungwon.splearn.application.required.MemberRepository;
import jungwon.splearn.domain.Member;
import jungwon.splearn.domain.MemberRegisterRequest;
import jungwon.splearn.domain.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements MemberRegister {

    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, EmailSender emailSender, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Member register(MemberRegisterRequest registerRequest) {
        Member member = Member.register(registerRequest, passwordEncoder);

        memberRepository.save(member);

        emailSender.send(member.getEmail(),"등록을 완료해주세요.","아래 링크를 클릭해서 등록을 완료해주세요.");

        return member;
    }

}
