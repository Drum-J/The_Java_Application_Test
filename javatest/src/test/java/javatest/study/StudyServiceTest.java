package javatest.study;

import javatest.domain.Member;
import javatest.domain.Study;
import javatest.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Mock MemberService memberService;
    @Mock StudyRepository studyRepository;

    @Test
    void createStudyService() throws Exception {
        //given
        MemberService memberService = mock(MemberService.class);
        StudyRepository studyRepository = mock(StudyRepository.class);
        //when
        StudyService studyService = new StudyService(memberService, studyRepository);
        //then
        assertNotNull(studyService);
    }

    @Test
    void createStudyServiceWithMock() throws Exception {
        //given
        // @Mock 만 사용했을 경우에는 assertionError 가 발생한다.
        // @ExtendWith(MockitoExtension.class) 추가 이후에는 에러가 발생하지 않는다.
        //when
        StudyService studyService = new StudyService(memberService, studyRepository);
        //then
        assertNotNull(studyService);
    }

    @Test
    void createStudyServiceWithMockParameter(@Mock MemberService ms, @Mock StudyRepository sr) throws Exception {
        //given
        // @Mock 을 전역으로 사용하지 않고 해당 테스트에서만 사용하고 싶다면 이렇게 사용할 수도 있다.
        //when
        StudyService studyService = new StudyService(ms, sr);
        //then
        assertNotNull(studyService);
    }

    @Test
    void stubbing() throws Exception {
        //given
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        //when
        Member member = new Member();
        member.setId(1L);
        member.setEmail("tmdgh@test.com");
        //stubbing 처리
        when(memberService.findById(1L)).thenReturn(Optional.of(member));

        Member findById = memberService.findById(1L).orElseThrow(() -> new IllegalStateException("찾을 수 없음."));

        //then
        assertEquals("tmdgh@test.com", findById.getEmail());
    }

    @Test
    void stubbing2() throws Exception {
        //given
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        //when
        Member member = new Member();
        member.setId(1L);
        member.setEmail("tmdgh@test.com");
        //stubbing 처리
        when(memberService.findById(any()))
                .thenReturn(Optional.of(member)) //첫번째 호출 Stubbing
                .thenThrow(new RuntimeException()) //두번째 호출 Stubbing
                .thenReturn(Optional.empty()); //세번째 호출 Stubbing

        //then
        assertAll(
                () -> {
                    Member find = memberService.findById(1L).orElseThrow(() -> new IllegalStateException("찾을 수 없음"));
                    assertEquals("tmdgh@test.com", find.getEmail());
                },
                () -> assertThrows(RuntimeException.class, () -> {
                    memberService.findById(2L);
                }),
                () -> {
                    Optional<Member> findEmpty = memberService.findById(3L);
                    assertEquals(Optional.empty(), findEmpty);
                }
        );
    }

    @Test
    void stubbingExample() throws Exception {
        //given
        //stubbing1
        Member member = new Member(1L ,"tmdgh@test.com");
        when(memberService.findById(1L)).thenReturn(Optional.of(member));

        //stubbing2
        Study study = new Study(10, "test");
        when(studyRepository.save(study)).thenReturn(study);

        //when
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study newStudy = studyService.createNewStudy(1L, study);

        //then
        assertEquals(member.getId(), newStudy.getOwnerId());
    }
}