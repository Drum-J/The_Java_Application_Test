package javatest.study;

import javatest.domain.Member;
import javatest.domain.Study;
import javatest.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudyVerifyingTest {

    @Mock MemberService memberService;
    @Mock StudyRepository studyRepository;

    @Test
    void verifyTest() throws Exception {
        //given
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        //stubbing1
        Member member = new Member(1L ,"tmdgh@test.com");
        when(memberService.findById(1L)).thenReturn(Optional.of(member));

        //stubbing2
        Study study = new Study(10, "test");
        when(studyRepository.save(study)).thenReturn(study);

        //when
        studyService.createNewStudy(1L, study);
        assertEquals(member.getId(), study.getOwnerId());

        //then
        //memberService 에 notify 메서드가 1번 호출 됐어야 한다! (study / member 각 1번)
        verify(memberService, times(1)).notify(study);
        verify(memberService, times(1)).notify(member);

        //memberService 에 validate 는 전혀 호출되지 않아야 한다.
        verify(memberService, never()).validate(anyLong());

        //순서 체크, notify(study) 먼저 notify(member) 2번째
        InOrder inOrder = inOrder(memberService);
        inOrder.verify(memberService).notify(study);
        inOrder.verify(memberService).notify(member);
    }
}
