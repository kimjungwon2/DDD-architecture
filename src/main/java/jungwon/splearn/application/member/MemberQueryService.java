package jungwon.splearn.application.member;

import jungwon.splearn.application.member.provided.MemberFinder;
import jungwon.splearn.application.member.required.MemberRepository;
import jungwon.splearn.domain.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Transactional
@Validated
@Service
public class MemberQueryService implements MemberFinder {

    private final MemberRepository memberRepository;

    public MemberQueryService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member find(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(()-> new IllegalArgumentException("회원을 찾을 수 없습니다. id: "+memberId));
    }
}
