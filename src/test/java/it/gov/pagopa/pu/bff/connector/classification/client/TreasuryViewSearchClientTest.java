package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.classification.controller.generated.TreasuryViewSearchControllerApi;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelTreasuryView;
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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TreasuryViewSearchClientTest {

  @Mock
  private ClassificationApisHolder classificationApisHolderMock;
  @Mock
  private TreasuryViewSearchControllerApi treasuryViewSearchControllerApiMock;
  private TreasuryViewSearchClient treasuryViewSearchClient;

  @BeforeEach
  void setUp() {
    treasuryViewSearchClient = new TreasuryViewSearchClient(classificationApisHolderMock);
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

    PagedModelTreasuryView result = treasuryViewSearchClient.getTreasuries(filtersDTO, pageable, accessToken);

    assertSame(expectedResult, result);
  }

}
