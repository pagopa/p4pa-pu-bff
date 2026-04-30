package it.gov.pagopa.pu.bff.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import it.gov.pagopa.pu.bff.dto.TreasuredClassificationExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedTreasuredClassificationExtendedDTO;
import it.gov.pagopa.pu.bff.enums.ClassificationStatus;
import it.gov.pagopa.pu.classification.dto.generated.ClassificationsEnum;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import it.gov.pagopa.pu.classification.dto.generated.TreasuredClassificationView;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

class TreasuredClassificationExtendedDTOMapperTest {

  private final TreasuredClassificationExtendedDTOMapper mapper =
    Mappers.getMapper(TreasuredClassificationExtendedDTOMapper.class);

  @ParameterizedTest
  @MethodSource("mapCases")
  void givenViewWithLabelWhenMapThenStatusAndFlagsMappedCorrectly(ClassificationsEnum label, ClassificationStatus expectedStatus) {
    TreasuredClassificationView view = new TreasuredClassificationView();
    view.setClassificationId(1L);
    view.setOrganizationId(99L);
    view.setLabel(label);

    Organization organization = new Organization();
    organization.setFlagPaymentNotification(true);
    organization.setFlagTreasury(true);

    TreasuredClassificationExtendedDTO dto = mapper.map(view, organization);

    assertNotNull(dto);
    assertEquals(view.getClassificationId(), dto.getClassificationId());
    assertEquals(view.getOrganizationId(), dto.getOrganizationId());
    assertEquals(label, dto.getLabel());
    assertEquals(expectedStatus, dto.getStatus());
    assertEquals(organization.getFlagPaymentNotification(), dto.getFlagPaymentNotification());
    assertEquals(organization.getFlagTreasury(), dto.getFlagTreasury());
  }

  static Stream<Arguments> mapCases() {
    return Stream.of(
      Arguments.of(ClassificationsEnum.IUD_RT_IUF, ClassificationStatus.INFO),
      Arguments.of(ClassificationsEnum.RT_IUF, ClassificationStatus.INFO),
      Arguments.of(ClassificationsEnum.RT_TES, ClassificationStatus.INFO),
      Arguments.of(ClassificationsEnum.IUD_RT_IUF_TES, ClassificationStatus.INFO),
      Arguments.of(ClassificationsEnum.RT_IUF_TES, ClassificationStatus.INFO),
      Arguments.of(ClassificationsEnum.RT_NO_IUF, ClassificationStatus.WARNING),
      Arguments.of(ClassificationsEnum.RT_NO_IUD, ClassificationStatus.WARNING),
      Arguments.of(ClassificationsEnum.IUF_NO_TES, ClassificationStatus.ERROR),
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

  @Test
  void givenPagedSourceWhenMapThenPagedAndFlagsMapped() {
    TreasuredClassificationView view = new TreasuredClassificationView();
    view.setClassificationId(999L);
    view.setLabel(ClassificationsEnum.RT_IUF);

    PagedTreasuredClassification paged = new PagedTreasuredClassification();
    paged.setContent(List.of(view));
    paged.setSize(1L);
    paged.setTotalElements(10L);
    paged.setTotalPages(1L);
    paged.setNumber(0L);

    Organization organization = new Organization();
    organization.setFlagPaymentNotification(false);
    organization.setFlagTreasury(true);

    PagedTreasuredClassificationExtendedDTO extended = mapper.map(paged, organization);

    assertNotNull(extended);
    assertEquals(1, extended.getContent().size());
    assertEquals(ClassificationStatus.INFO, extended.getContent().get(0).getStatus());
    assertEquals(organization.getFlagPaymentNotification(), extended.getContent().get(0).getFlagPaymentNotification());
    assertEquals(organization.getFlagTreasury(), extended.getContent().get(0).getFlagTreasury());
    assertEquals(1L, extended.getSize());
    assertEquals(10L, extended.getTotalElements());
    assertEquals(1L, extended.getTotalPages());
    assertEquals(0L, extended.getNumber());
  }

  @Test
  void givenNullSingleViewWhenMapThenReturnNull() {
    assertNull(mapper.map((TreasuredClassificationView) null, new Organization()));
  }

  @Test
  void givenNullListWhenMapThenReturnNull() {
    assertNull(mapper.map((List<TreasuredClassificationView>) null, new Organization()));
  }

  @Test
  void givenNullPagedWhenMapThenReturnNull() {
    assertNull(mapper.map((PagedTreasuredClassification) null, new Organization()));
  }

  @Test
  void givenSingleViewWhenOrganizationNullThenFlagsAreNull() {
    TreasuredClassificationView view = new TreasuredClassificationView();
    view.setClassificationId(123L);
    view.setOrganizationId(456L);
    view.setLabel(ClassificationsEnum.RT_TES);

    TreasuredClassificationExtendedDTO dto = mapper.map(view, null);

    assertNotNull(dto);
    assertEquals(view.getClassificationId(), dto.getClassificationId());
    assertEquals(view.getOrganizationId(), dto.getOrganizationId());
    assertEquals(ClassificationStatus.INFO, dto.getStatus());
    assertNull(dto.getFlagPaymentNotification());
    assertNull(dto.getFlagTreasury());
  }

  @Test
  void givenPagedSourceWhenOrganizationNullThenFlagsAreNull() {
    TreasuredClassificationView view = new TreasuredClassificationView();
    view.setClassificationId(789L);
    view.setLabel(ClassificationsEnum.DOPPI);

    PagedTreasuredClassification paged = new PagedTreasuredClassification();
    paged.setContent(List.of(view));
    paged.setSize(1L);
    paged.setTotalElements(5L);
    paged.setTotalPages(1L);
    paged.setNumber(0L);

    PagedTreasuredClassificationExtendedDTO extended = mapper.map(paged, null);

    assertNotNull(extended);
    assertEquals(1, extended.getContent().size());
    TreasuredClassificationExtendedDTO dto = extended.getContent().get(0);

    assertEquals(ClassificationStatus.ERROR, dto.getStatus());
    assertNull(dto.getFlagPaymentNotification());
    assertNull(dto.getFlagTreasury());
  }
}

