package jungwon.splearn.domain.member;

import jakarta.persistence.*;
import jungwon.splearn.domain.AbstractEntity;
import jungwon.splearn.domain.shared.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

@Entity
@Table(name = "MEMBER",
        uniqueConstraints = @UniqueConstraint(name="UK_MEMBER_EMAIL_ADDRESS", columnNames = "email_address"))
@Getter
@ToString(callSuper = true, exclude = "detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NaturalIdCache
public class Member extends AbstractEntity {

    @Embedded
    @NaturalId
    @Column(unique = true)
    private Email email;

    @Column(length = 100, nullable = false)
    private String nickname;

    @Column(length =200, nullable = false)
    private String passwordHash;

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "detail_id")
    private MemberDetail detail;

    public static Member register(MemberRegisterRequest createRequest, PasswordEncoder passwordHash){
        Member member = new Member();

        member.email = new Email(createRequest.email());
        member.nickname = requireNonNull(createRequest.nickname());
        member.passwordHash = requireNonNull(passwordHash.encode(createRequest.password()));

        member.status = MemberStatus.PENDING;
        member.detail = MemberDetail.create();

        return member;
    }

    public void activate() {
        state(status == MemberStatus.PENDING,"PENDING 상태가 아닙니다.");

        this.status = MemberStatus.ACTIVE;
        this.detail.setActivatedAt();
    }

    public void deactivate() {
        state(status == MemberStatus.ACTIVE,"ACTIVE 상태가 아닙니다.");

        this.status = MemberStatus.DEACTIVATED;
        this.detail.setDeactivatedAt();
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void changeNickName(String nickname) {
        this.nickname = requireNonNull(nickname);
    }

    public void updateInfo(MemberInfoUpdateRequest updateRequest){
        this.nickname = Objects.requireNonNull(updateRequest.nickname());
        this.detail.updateInfo(updateRequest);

    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(requireNonNull(password));
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }
}
