package jungwon.splearn.domain;

import jungwon.splearn.domain.member.MemberRegisterRequest;
import jungwon.splearn.domain.member.PasswordEncoder;

public class MemberFixture {
    public static MemberRegisterRequest createMemberRegisterRequest(String email) {
        return new MemberRegisterRequest(email, "Kimdmds", "secretArdfjl");
    }

    public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
    }

    public static MemberRegisterRequest createMemberRegisterRequest(){
        return createMemberRegisterRequest("jungwon@splearn.app");
    }
}
