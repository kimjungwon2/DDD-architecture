package jungwon.splearn.application.member;

import jungwon.splearn.application.member.provided.MemberFinder;
import jungwon.splearn.application.member.provided.MemberRegister;
import jungwon.splearn.application.member.required.EmailSender;
import jungwon.splearn.application.member.required.MemberRepository;
import jungwon.splearn.domain.member.DuplicateEmailException;
import jungwon.splearn.domain.member.Member;
import jungwon.splearn.domain.member.MemberRegisterRequest;
import jungwon.splearn.domain.member.PasswordEncoder;
import jungwon.splearn.domain.shared.Email;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Transactional
@Validated
@Service
public class MemberService implements MemberRegister {

    private final MemberFinder memberFinder;
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberFinder memberFinder, MemberRepository memberRepository, EmailSender emailSender, PasswordEncoder passwordEncoder) {
        this.memberFinder = memberFinder;
        this.memberRepository = memberRepository;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Member register(MemberRegisterRequest registerRequest) {
        checkDuplicateEmail(registerRequest);

        Member member = Member.register(registerRequest, passwordEncoder);

        memberRepository.save(member);

        sendWelcomeEmail(member);

        return member;
    }

    @Override
    public Member activate(Long memberId) {
        Member member = memberFinder.find(memberId);

        member.activate();

        return memberRepository.save(member);
    }

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(),"등록을 완료해주세요.","아래 링크를 클릭해서 등록을 완료해주세요.");
    }

    private void checkDuplicateEmail(MemberRegisterRequest registerRequest) {
        if(memberRepository.findByEmail(new Email(registerRequest.email())).isPresent() ){
            throw new DuplicateEmailException("이미 사용중인 이메일입니다: "+ registerRequest.email());
        }
    }

}
