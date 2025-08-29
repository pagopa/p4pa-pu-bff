package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.TreasuredClassificationExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuredClassificationExtendedDTO;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import it.gov.pagopa.pu.classification.dto.generated.TreasuredClassificationView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TreasuredClassificationExtendedDTOMapperTest {

  private final TreasuredClassificationExtendedDTOMapper mapper =
    Mappers.getMapper(TreasuredClassificationExtendedDTOMapper.class);

  @ParameterizedTest
  @MethodSource("mapCases")
  void givenViewWithLabel_whenMap_thenStatusMappedCorrectly(ClassificationsEnum label, String expectedStatus) {
    TreasuredClassificationView view = new TreasuredClassificationView();
    view.setClassificationId(1L);
    view.setOrganizationId(99L);
    view.setLabel(label);

    TreasuredClassificationExtendedDTO dto = mapper.map(view);

    assertNotNull(dto);
    assertEquals(view.getClassificationId(), dto.getClassificationId());
    assertEquals(view.getOrganizationId(), dto.getOrganizationId());
    assertEquals(label, dto.getLabel());
    assertEquals(expectedStatus, dto.getStatus());
  }

  static Stream<Arguments> mapCases() {
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

  @Test
  void givenPagedSource_whenMap_thenPagedMapped() {
    TreasuredClassificationView view = new TreasuredClassificationView();
    view.setClassificationId(999L);
    view.setLabel(ClassificationsEnum.RT_IUF);

    PagedTreasuredClassification paged = new PagedTreasuredClassification();
    paged.setContent(List.of(view));
    paged.setSize(1L);
    paged.setTotalElements(10L);
    paged.setTotalPages(1L);
    paged.setNumber(0L);

    PagedTreasuredClassificationExtendedDTO extended = mapper.map(paged);

    assertNotNull(extended);
    assertEquals(1, extended.getContent().size());
    assertEquals("INFO", extended.getContent().get(0).getStatus());
    assertEquals(1L, extended.getSize());
    assertEquals(10L, extended.getTotalElements());
    assertEquals(1L, extended.getTotalPages());
    assertEquals(0L, extended.getNumber());
  }

  @Test
  void givenNullSource_whenMap_thenReturnNull() {
    assertNull(mapper.map((TreasuredClassificationView) null));
    assertNull(mapper.map((List<TreasuredClassificationView>) null));
    assertNull(mapper.map((PagedTreasuredClassification) null));
  }
}

