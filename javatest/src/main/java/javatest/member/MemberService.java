package javatest.member;

import javatest.domain.Member;
import javatest.domain.Study;

import java.util.Optional;

public interface MemberService {

    Optional<Member> findById(Long memberId);

    void validate(Long memberId);

    void notify(Study study);

    void notify(Member member);
}
