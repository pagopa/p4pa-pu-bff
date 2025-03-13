package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.client.TreasuryViewSearchClient;
import it.gov.pagopa.pu.bff.dto.TreasuryViewFiltersDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelTreasuryView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TreasuryServiceTest {

  @Mock
  private TreasuryViewSearchClient treasuryViewSearchClient;

  private TreasuryService service;

  @BeforeEach
  void setUp() {
    service = new TreasuryServiceImpl(treasuryViewSearchClient);
  }

  @Test
  void whenGetTreasuriesThenInvokeClient() {
    TreasuryViewFiltersDTO filtersDTO = new TreasuryViewFiltersDTO();
    String accessToken = "ACCESSTOKEN";
    Pageable pageable = Mockito.mock(Pageable.class);
    PagedModelTreasuryView expectedResult = new PagedModelTreasuryView();

    when(treasuryViewSearchClient.getTreasuries(Mockito.same(filtersDTO), Mockito.same(pageable), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    PagedModelTreasuryView result = service.getTreasuries(filtersDTO, pageable, accessToken);

    assertSame(expectedResult, result);
  }
}
