package study.javatest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudyTest {

    @Test
    void create() throws Exception {
        //given
        Study study = new Study();
        //when

        //then
        assertNotNull(study);
    }

}