package javatest.study;

import javatest.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

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
}