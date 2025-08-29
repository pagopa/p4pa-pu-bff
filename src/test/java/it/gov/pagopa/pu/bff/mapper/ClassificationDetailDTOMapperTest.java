package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.ClassificationDetailDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.stream.Stream;

import static it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClassificationDetailDTOMapperTest {

  private final ClassificationDetailDTOMapper mapper = Mappers.getMapper(ClassificationDetailDTOMapper.class);
  private final PodamFactory podamFactory= TestUtils.getPodamFactory();

  @ParameterizedTest()
  @MethodSource("mapValueSource")
  void givenPopulatedClassificationDetailViewDTOWhenMapThenCorrectMapping(ClassificationsEnum label, boolean payed, boolean reported, boolean collected, String status) {
    ClassificationDetailViewDTO classificationDetailViewDTO = podamFactory.manufacturePojo(ClassificationDetailViewDTO.class);
    classificationDetailViewDTO.setLabel(label);

    ClassificationDetailDTO result = mapper.map(classificationDetailViewDTO);

    assertNotNull(result);
    TestUtils.reflectionEqualsByName(classificationDetailViewDTO,result);
    assertEquals(payed,result.isPayed());
    assertEquals(reported,result.isReported());
    assertEquals(collected,result.isCollected());
    assertEquals(status, result.getStatus());
  }

  static Stream<Arguments> mapValueSource() {
    return Stream.of(
      Arguments.of(DOPPI, true, true, true, "ERROR"),
      Arguments.of(RT_NO_IUF, true, false, false, "WARNING"),
      Arguments.of(RT_NO_IUD, true, false, false, "WARNING"),
      Arguments.of(IUV_NO_RT, true, false, false, "ERROR"),
      Arguments.of(TES_NO_IUF_OR_IUV, false, false, true, "ERROR"),
      Arguments.of(IUF_NO_TES, false, true, false, "WARNING"),
      Arguments.of(IUD_RT_IUF, true, true, true, "INFO"),
      Arguments.of(RT_IUF, true, true, false, "INFO"),
      Arguments.of(RT_TES, true, true, true, "INFO"),
      Arguments.of(IUD_RT_IUF_TES, true, false, false, "INFO"),
      Arguments.of(RT_IUF_TES, true, true, true, "INFO"),
      Arguments.of(IUF_TES_DIV_IMP, true, true, true, "ERROR"),
      Arguments.of(IUD_NO_RT, true, true, false, "ERROR"),
      Arguments.of(TES_NO_MATCH, false, false, true, "ERROR"),
      Arguments.of(UNKNOWN, false, false, false, "ERROR")
    );
  }
}
