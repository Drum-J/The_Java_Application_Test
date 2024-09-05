package study.javatest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudyTest {

    int value = 1;

    @FastTest
    @DisplayName("스터디 만들기")
    void create_new_study() throws Exception {
        //given
        System.out.println(this);
        System.out.println(value++);
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
        System.out.println(this);
        System.out.println(value++);
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

    @DisplayName("반복 TEST")
    @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepetitions}")
    void repeatTest(RepetitionInfo info) {
        System.out.println("test" + info.getCurrentRepetition() + "/" + info.getTotalRepetitions());
    }

    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(ints = {10, 20, 30, 40})
    @DisplayName("파라미터 TEST")
    void parameterTest(@ConvertWith(StudyConverter.class) Study study) {
        System.out.println(study.getLimit());
    }

    @DisplayName("스터디 만들기 TEST")
    @ParameterizedTest(name = "{index} {displayName} limit={0}")
    @CsvSource({"10, '자바 스터디'", "20, 스프링 스터디"})
    void parameterTest2(Integer limit, String name) {
        Study study = new Study(limit, name);
        System.out.println(study);
    }

    @DisplayName("스터디 만들기 TEST")
    @ParameterizedTest(name = "{index} {displayName} limit={0}")
    @CsvSource({"10, '자바 스터디'", "20, 스프링 스터디"})
    void parameterTest3(@AggregateWith(StudyAggregator.class) Study study) {
        System.out.println(study);
    }

    static class StudyConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object o, Class<?> aClass) throws ArgumentConversionException {
            assertEquals(Study.class, aClass, "Can only convert to Study");

            return new Study(Integer.parseInt(o.toString()));
        }
    }

    static class StudyAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
            return new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
        }
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