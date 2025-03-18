package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.TreasuryClient;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelTreasuryView;
import it.gov.pagopa.pu.classification.dto.generated.Treasury;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TreasuryServiceTest {

  @Mock
  private TreasuryClient treasuryClient;

  private TreasuryService service;

  @BeforeEach
  void setUp() {
    service = new TreasuryServiceImpl(treasuryClient);
  }

  @Test
  void whenGetTreasuriesThenInvokeClient() {
    TreasuryViewFiltersDTO filtersDTO = new TreasuryViewFiltersDTO();
    String accessToken = "ACCESSTOKEN";
    Pageable pageable = Mockito.mock(Pageable.class);
    PagedModelTreasuryView expectedResult = new PagedModelTreasuryView();

    when(treasuryClient.getTreasuries(Mockito.same(filtersDTO), Mockito.same(pageable), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    PagedModelTreasuryView result = service.getTreasuries(filtersDTO, pageable, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenGetTreasuryDetailThenInvokeClient() {
    Long organizationId = 1L;
    String treasuryId = "TREASURY123";
    String accessToken = "ACCESSTOKEN";
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

    when(treasuryClient.getTreasuryDetail(Mockito.same(organizationId), Mockito.same(treasuryId), Mockito.same(accessToken)))
      .thenReturn(expectedTreasury);

    Treasury result = service.getTreasuryDetail(organizationId, treasuryId, accessToken);

    assertSame(expectedTreasury, result);
  }

}
