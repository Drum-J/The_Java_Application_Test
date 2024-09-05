package study.javatest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {

    @Test
    @DisplayName("스터디 만들기")
    void create_new_study() throws Exception {
        //given
        Study study = new Study(10);
        //when

        //then
        assertAll(
                () -> assertNotNull(study),
                // 파라미터 순서 : 기댓값(expected), 실제값(actual) 하지만 순서가 바껴도 테스트는 제대로 동작함.
                // 또한 메세지 자리에 Supplier 가 들어오기 때문에 람다로 사용가능 하다.
                // 왜 Supplier, 람다를 사용할까? Message 를 String 으로 그대로 넣으면 성공,실패 관계없이 Message 연산을 항상 실행하지만
                // 람다를 사용하게 되면 실패할 경우에만 Message 연산을 실행한다고 한다.
                () ->assertEquals(StudyStatus.DRAFT,study.getStatus(),
                        () -> "스터디를 처음 만들면 " + StudyStatus.DRAFT + " 상태다."),
                () -> assertTrue(study.getLimit() > 0 ,"스터디 최대 참석 가능 인원은 0보다 커야한다.")
        );
        System.out.println("create");
    }

    @Test
    void create_new_study_exception() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new Study(-10));
        assertEquals("limit은 0보다 커야 한다.", exception.getMessage());
    }

    @Test
    void timeout() {
        assertTimeout(Duration.ofMillis(100), () -> {
            new Study(10);
            Thread.sleep(300);
        });
        // 실패 시 즉시 끝내고 싶다 하면 assertTimeoutPreemptively 를 사용하면 100 밀리세컨드 이후에 바로 종료된다.
    }

    @BeforeAll // static void 로 작성해야 한다. private 안되고, default public 가능, return 타입이 있어선 안된다.
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