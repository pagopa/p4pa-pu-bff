package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.TreasuredClassificationExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuredClassificationExtendedDTO;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import it.gov.pagopa.pu.classification.dto.generated.TreasuredClassificationView;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreasuredClassificationExtendedDTOMapperTest {

  private final TreasuredClassificationExtendedDTOMapper mapper =
    Mappers.getMapper(TreasuredClassificationExtendedDTOMapper.class);

  @Test
  void givenSingleView_whenMap_thenFieldsAndStatusAreMapped() {
    TreasuredClassificationView view = new TreasuredClassificationView();
    view.setClassificationId(123L);
    view.setOrganizationId(456L);
    view.setLabel(ClassificationsEnum.RT_NO_IUF);
    view.setLastClassificationDate(LocalDate.now());

    TreasuredClassificationExtendedDTO dto = mapper.map(view);

    assertNotNull(dto);
    assertEquals(view.getClassificationId(), dto.getClassificationId());
    assertEquals(view.getOrganizationId(), dto.getOrganizationId());
    assertEquals(view.getLabel(), dto.getLabel());
    assertEquals("WARNING", dto.getStatus());
  }

  @Test
  void givenListOfViews_whenMap_thenAllMapped() {
    TreasuredClassificationView view1 = new TreasuredClassificationView();
    view1.setClassificationId(1L);
    view1.setLabel(ClassificationsEnum.IUD_RT_IUF);

    TreasuredClassificationView view2 = new TreasuredClassificationView();
    view2.setClassificationId(2L);
    view2.setLabel(ClassificationsEnum.DOPPI);

    List<TreasuredClassificationExtendedDTO> dtos = mapper.map(List.of(view1, view2));

    assertEquals(2, dtos.size());
    assertEquals("INFO", dtos.get(0).getStatus());
    assertEquals("ERROR", dtos.get(1).getStatus());
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
