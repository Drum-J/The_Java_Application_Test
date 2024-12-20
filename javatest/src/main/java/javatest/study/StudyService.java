package javatest.study;

import javatest.domain.Member;
import javatest.domain.Study;
import javatest.member.MemberService;

public class StudyService {

    private final MemberService memberService;
    private final StudyRepository studyRepository;

    public StudyService(MemberService memberService, StudyRepository studyRepository) {
        assert memberService != null;
        assert studyRepository != null;

        this.memberService = memberService;
        this.studyRepository = studyRepository;
    }

    public Study createNewStudy(Long memberId, Study study) {
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member doesn't exist for id: '" + memberId + "'"));
        study.setOwnerId(member.getId());

        Study newStudy = studyRepository.save(study);
        memberService.notify(newStudy);
        //memberService.notify(member);
        return newStudy;
    }

    public Study openStudy(Study study) {
        study.open();
        Study openedStudy = studyRepository.save(study);
        memberService.notify(openedStudy);

        return openedStudy;
    }
}
