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
  void givenLabelWhenMapStatusThenExpectedStatus(ClassificationsEnum label, String receiptPaymentRequestId, ClassificationStatus expectedStatus) {
    assertEquals(expectedStatus, ClassificationStatusMapper.mapStatus(label, receiptPaymentRequestId));
  }

  static Stream<Arguments> statusCases() {
    return Stream.of(
      Arguments.of(ClassificationsEnum.IUD_RT_IUF, null, ClassificationStatus.INFO),
      Arguments.of(ClassificationsEnum.RT_IUF, null, ClassificationStatus.INFO),
      Arguments.of(ClassificationsEnum.RT_TES, null, ClassificationStatus.INFO),
      Arguments.of(ClassificationsEnum.IUD_RT_IUF_TES, null, ClassificationStatus.INFO),
      Arguments.of(ClassificationsEnum.RT_IUF_TES, null, ClassificationStatus.INFO),
      Arguments.of(ClassificationsEnum.RT_NO_IUF, null, ClassificationStatus.WARNING),
      Arguments.of(ClassificationsEnum.RT_NO_IUD, null, ClassificationStatus.WARNING),
      Arguments.of(ClassificationsEnum.IUF_NO_TES, null, ClassificationStatus.ERROR),
      Arguments.of(ClassificationsEnum.IUF_NO_TES, "receiptId", ClassificationStatus.WARNING),
      Arguments.of(ClassificationsEnum.DOPPI, null, ClassificationStatus.ERROR),
      Arguments.of(ClassificationsEnum.IUV_NO_RT, null, ClassificationStatus.ERROR),
      Arguments.of(ClassificationsEnum.TES_NO_IUF_OR_IUV, null, ClassificationStatus.ERROR),
      Arguments.of(ClassificationsEnum.IUF_TES_DIV_IMP, null, ClassificationStatus.ERROR),
      Arguments.of(ClassificationsEnum.IUD_NO_RT, null, ClassificationStatus.ERROR),
      Arguments.of(ClassificationsEnum.TES_NO_MATCH, null, ClassificationStatus.ERROR),
      Arguments.of(ClassificationsEnum.UNKNOWN, null, ClassificationStatus.ERROR),
      Arguments.of(null, null, ClassificationStatus.ERROR)
    );
  }
}

