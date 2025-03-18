package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.classification.controller.generated.TreasurySearchControllerApi;
import it.gov.pagopa.pu.classification.controller.generated.TreasuryViewSearchControllerApi;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelTreasuryView;
import it.gov.pagopa.pu.classification.dto.generated.Treasury;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TreasuryClientTest {

  @Mock
  private ClassificationApisHolder classificationApisHolderMock;
  @Mock
  private TreasuryViewSearchControllerApi treasuryViewSearchControllerApiMock;
  @Mock
  private TreasurySearchControllerApi treasurySearchControllerApiMock;
  private TreasuryClient treasuryClient;

  @BeforeEach
  void setUp() {
    treasuryClient = new TreasuryClient(classificationApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      classificationApisHolderMock,
      treasuryViewSearchControllerApiMock
    );
  }

  @Test
  void whenGetTreasuriesThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    PagedModelTreasuryView expectedResult = new PagedModelTreasuryView();

    long organizationId = 1L;
    String iuv = "iuv123";
    String iuf = "iuf123";
    long billAmountCents = 1000L;
    LocalDate billDate = LocalDate.now().minusDays(10);
    String provisionalCode = "PROV123";
    String billCode = "BILL123";
    String pspLastName = "PSPLastName";
    LocalDate regionValueDate = LocalDate.now().minusDays(5);
    String documentCode = "DOC123";
    Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());

    TreasuryViewFiltersDTO filtersDTO = new TreasuryViewFiltersDTO(
      organizationId, iuv, iuf, billAmountCents, billDate, provisionalCode, billCode, pspLastName, regionValueDate, documentCode);

    when(classificationApisHolderMock.getTreasuryViewSearchControllerApi(accessToken))
      .thenReturn(treasuryViewSearchControllerApiMock);

    when(treasuryViewSearchControllerApiMock.crudTreasuriesViewFindTreasuriesByFilters(
      filtersDTO.getOrganizationId(),
      filtersDTO.getIuv(),
      filtersDTO.getIuf(),
      filtersDTO.getBillAmountCents(),
      filtersDTO.getBillDate(),
      filtersDTO.getProvisionalCode(),
      filtersDTO.getBillCode(),
      filtersDTO.getPspLastName(),
      filtersDTO.getRegionValueDate(),
      filtersDTO.getDocumentCode(),
      PageUtils.getPageNumber(pageable),
      PageUtils.getPageSize(pageable),
      PageUtils.getSortList(pageable)))
      .thenReturn(expectedResult);

    PagedModelTreasuryView result = treasuryClient.getTreasuries(filtersDTO, pageable, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetTreasuryDetailThenInvokeWithAccessToken() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    String treasuryId = "TREASURY123";
    Treasury expectedTreasury = Treasury.builder()
      .treasuryId(treasuryId)
      .organizationId(organizationId)
      .billYear("2025")
      .billCode("BILL123")
      .ingestionFlowFileId(100L)
      .billAmountCents(1000L)
      .billDate(LocalDate.now().minusDays(10))
      .pspLastName("PSPLastName")
      .build();

    when(classificationApisHolderMock.getTreasurySearchControllerApi(accessToken))
      .thenReturn(treasurySearchControllerApiMock);

    when(treasurySearchControllerApiMock.crudTreasuryFindByOrganizationIdAndTreasuryId(
      organizationId, treasuryId))
      .thenReturn(expectedTreasury);

    Treasury result = treasuryClient.getTreasuryDetail(organizationId, treasuryId, accessToken);

    assertSame(expectedTreasury, result);
  }

  @Test
  void whenGetTreasuryDetailNotFoundThenReturnNull() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    String treasuryId = "TREASURY123";

    when(classificationApisHolderMock.getTreasurySearchControllerApi(accessToken))
      .thenReturn(treasurySearchControllerApiMock);

    when(treasurySearchControllerApiMock.crudTreasuryFindByOrganizationIdAndTreasuryId(organizationId, treasuryId))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    Treasury result = treasuryClient.getTreasuryDetail(organizationId, treasuryId, accessToken);

    assertNull(result);
    Mockito.verify(treasurySearchControllerApiMock).crudTreasuryFindByOrganizationIdAndTreasuryId(
      organizationId, treasuryId);
  }

}
