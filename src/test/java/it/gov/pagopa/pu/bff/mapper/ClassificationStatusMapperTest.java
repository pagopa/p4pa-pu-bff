package it.gov.pagopa.pu.bff.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.gov.pagopa.pu.bff.enums.ClassificationStatus;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ClassificationStatusMapperTest {

  @ParameterizedTest
  @MethodSource("statusCases")
  void givenLabelWhenMapStatusThenExpectedStatus(ClassificationsEnum label, ClassificationStatus expectedStatus) {
    assertEquals(expectedStatus, ClassificationStatusMapper.mapStatus(label));
  }

  static Stream<Arguments> statusCases() {
    return Stream.of(
      Arguments.of(ClassificationsEnum.IUD_RT_IUF, ClassificationStatus.INFO),
      Arguments.of(ClassificationsEnum.RT_IUF, ClassificationStatus.INFO),
      Arguments.of(ClassificationsEnum.RT_TES, ClassificationStatus.INFO),
      Arguments.of(ClassificationsEnum.IUD_RT_IUF_TES, ClassificationStatus.INFO),
      Arguments.of(ClassificationsEnum.RT_IUF_TES, ClassificationStatus.INFO),
      Arguments.of(ClassificationsEnum.RT_NO_IUF, ClassificationStatus.WARNING),
      Arguments.of(ClassificationsEnum.RT_NO_IUD, ClassificationStatus.WARNING),
      Arguments.of(ClassificationsEnum.IUF_NO_TES, ClassificationStatus.WARNING),
      Arguments.of(ClassificationsEnum.DOPPI, ClassificationStatus.ERROR),
      Arguments.of(ClassificationsEnum.IUV_NO_RT, ClassificationStatus.ERROR),
      Arguments.of(ClassificationsEnum.TES_NO_IUF_OR_IUV, ClassificationStatus.ERROR),
      Arguments.of(ClassificationsEnum.IUF_TES_DIV_IMP, ClassificationStatus.ERROR),
      Arguments.of(ClassificationsEnum.IUD_NO_RT, ClassificationStatus.ERROR),
      Arguments.of(ClassificationsEnum.TES_NO_MATCH, ClassificationStatus.ERROR),
      Arguments.of(ClassificationsEnum.UNKNOWN, ClassificationStatus.ERROR),
      Arguments.of(null, ClassificationStatus.ERROR)
    );
  }
}

