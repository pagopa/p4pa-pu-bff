package it.gov.pagopa.pu.bff.mapper;

import static it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum.DOPPI;
import static it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum.IUD_NO_RT;
import static it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum.IUD_RT_IUF;
import static it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum.IUD_RT_IUF_TES;
import static it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum.IUF_NO_TES;
import static it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum.IUF_TES_DIV_IMP;
import static it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum.IUV_NO_RT;
import static it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum.RT_IUF;
import static it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum.RT_IUF_TES;
import static it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum.RT_NO_IUD;
import static it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum.RT_NO_IUF;
import static it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum.RT_TES;
import static it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum.TES_NO_IUF_OR_IUV;
import static it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum.TES_NO_MATCH;
import static it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum.UNKNOWN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import it.gov.pagopa.pu.bff.dto.ClassificationDetailDTO;
import it.gov.pagopa.pu.bff.enums.ClassificationStatus;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import uk.co.jemos.podam.api.PodamFactory;

class ClassificationDetailDTOMapperTest {

  private final ClassificationDetailDTOMapper mapper = Mappers.getMapper(ClassificationDetailDTOMapper.class);
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @ParameterizedTest()
  @MethodSource("mapValueSource")
  void givenPopulatedClassificationDetailViewDTOWhenMapThenCorrectMapping(ClassificationsEnum label, boolean payed, boolean reported, boolean collected, ClassificationStatus status) {
    ClassificationDetailViewDTO classificationDetailViewDTO = podamFactory.manufacturePojo(ClassificationDetailViewDTO.class);
    classificationDetailViewDTO.setLabel(label);

    Organization organization = new Organization();
    organization.setFlagPaymentNotification(true);
    organization.setFlagTreasury(true);

    ClassificationDetailDTO result = mapper.map(classificationDetailViewDTO, organization);

    assertNotNull(result);
    TestUtils.reflectionEqualsByName(classificationDetailViewDTO,result);
    assertEquals(payed,result.isPayed());
    assertEquals(reported,result.isReported());
    assertEquals(collected,result.isCollected());
    assertEquals(status, result.getStatus());
    assertEquals(organization.getFlagPaymentNotification(), result.getFlagPaymentNotification());
    assertEquals(organization.getFlagTreasury(), result.getFlagTreasury());
  }

  @Test
  void givenOrganizationNullWhenMapThenFlagsAreNull() {
    ClassificationDetailViewDTO classificationDetailViewDTO = new ClassificationDetailViewDTO();
    classificationDetailViewDTO.setLabel(ClassificationsEnum.RT_TES);

    ClassificationDetailDTO result = mapper.map(classificationDetailViewDTO, null);

    assertNotNull(result);
    assertEquals(ClassificationStatus.INFO, result.getStatus());
    assertNull(result.getFlagPaymentNotification());
    assertNull(result.getFlagTreasury());
  }

  static Stream<Arguments> mapValueSource() {
    return Stream.of(
      Arguments.of(DOPPI, true, true, true, ClassificationStatus.ERROR),
      Arguments.of(RT_NO_IUF, true, false, false, ClassificationStatus.WARNING),
      Arguments.of(RT_NO_IUD, true, false, false, ClassificationStatus.WARNING),
      Arguments.of(IUV_NO_RT, true, false, false, ClassificationStatus.ERROR),
      Arguments.of(TES_NO_IUF_OR_IUV, false, false, true, ClassificationStatus.ERROR),
      Arguments.of(IUF_NO_TES, true, true, false, ClassificationStatus.WARNING),
      Arguments.of(IUD_RT_IUF, true, true, true, ClassificationStatus.INFO),
      Arguments.of(RT_IUF, true, true, false, ClassificationStatus.INFO),
      Arguments.of(RT_TES, true, true, true, ClassificationStatus.INFO),
      Arguments.of(IUD_RT_IUF_TES, true, false, false, ClassificationStatus.INFO),
      Arguments.of(RT_IUF_TES, true, true, true, ClassificationStatus.INFO),
      Arguments.of(IUF_TES_DIV_IMP, true, true, true, ClassificationStatus.ERROR),
      Arguments.of(IUD_NO_RT, true, true, false, ClassificationStatus.ERROR),
      Arguments.of(TES_NO_MATCH, false, false, true, ClassificationStatus.ERROR),
      Arguments.of(UNKNOWN, false, false, false, ClassificationStatus.ERROR)
    );
  }
}
