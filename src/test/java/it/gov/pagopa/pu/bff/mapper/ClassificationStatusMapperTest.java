package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassificationStatusMapperTest {

  @ParameterizedTest
  @MethodSource("statusCases")
  void givenLabelWhenMapStatusThenExpectedStatus(ClassificationsEnum label, String expectedStatus) {
    assertEquals(expectedStatus, ClassificationStatusMapper.mapStatus(label));
  }

  static Stream<Arguments> statusCases() {
    return Stream.of(
      Arguments.of(ClassificationsEnum.IUD_RT_IUF, "INFO"),
      Arguments.of(ClassificationsEnum.RT_IUF, "INFO"),
      Arguments.of(ClassificationsEnum.RT_TES, "INFO"),
      Arguments.of(ClassificationsEnum.IUD_RT_IUF_TES, "INFO"),
      Arguments.of(ClassificationsEnum.RT_IUF_TES, "INFO"),
      Arguments.of(ClassificationsEnum.RT_NO_IUF, "WARNING"),
      Arguments.of(ClassificationsEnum.RT_NO_IUD, "WARNING"),
      Arguments.of(ClassificationsEnum.IUF_NO_TES, "WARNING"),
      Arguments.of(ClassificationsEnum.DOPPI, "ERROR"),
      Arguments.of(ClassificationsEnum.IUV_NO_RT, "ERROR"),
      Arguments.of(ClassificationsEnum.TES_NO_IUF_OR_IUV, "ERROR"),
      Arguments.of(ClassificationsEnum.IUF_TES_DIV_IMP, "ERROR"),
      Arguments.of(ClassificationsEnum.IUD_NO_RT, "ERROR"),
      Arguments.of(ClassificationsEnum.TES_NO_MATCH, "ERROR"),
      Arguments.of(ClassificationsEnum.UNKNOWN, "ERROR"),
      Arguments.of(null, "ERROR")
    );
  }
}

