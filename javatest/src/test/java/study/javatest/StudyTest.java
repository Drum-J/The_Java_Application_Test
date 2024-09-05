package study.javatest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {

    @FastTest
    @DisplayName("스터디 만들기")
    void create_new_study() throws Exception {
        //given
        Study study = new Study(10);
        //when

        //then
        assertAll(
                () -> assertNotNull(study),
                () ->assertEquals(StudyStatus.DRAFT,study.getStatus(),
                        () -> "스터디를 처음 만들면 " + StudyStatus.DRAFT + " 상태다."),
                () -> assertTrue(study.getLimit() > 0 ,"스터디 최대 참석 가능 인원은 0보다 커야한다.")
        );
        System.out.println("create");
    }

    @FastTest
    @DisplayName("스터디 정원 TEST")
    void create_new_study_exception() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new Study(-10));
        assertEquals("limit은 0보다 커야 한다.", exception.getMessage());
    }

    @SlowTest
    @DisplayName("스터디 생성 시간 TEST")
    void timeout() {
        assertTimeout(Duration.ofMillis(100), () -> {
            new Study(10);
            Thread.sleep(50);
        });
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("before all");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("after all");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("before each");
    }

    @AfterEach
    void afterEach() {
        System.out.println("after each");
    }
}