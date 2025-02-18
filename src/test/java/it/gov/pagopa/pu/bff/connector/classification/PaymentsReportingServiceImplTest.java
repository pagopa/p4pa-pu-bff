package it.gov.pagopa.pu.bff.connector.classification;

import it.gov.pagopa.pu.bff.connector.classification.PaymentsReportingService;
import it.gov.pagopa.pu.bff.connector.classification.PaymentsReportingServiceImpl;
import it.gov.pagopa.pu.bff.connector.classification.client.PaymentsReportingClient;
import it.gov.pagopa.pu.bff.dto.LocalDateIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingView;
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
class PaymentsReportingServiceImplTest {

  @Mock
  private PaymentsReportingClient client;

  private PaymentsReportingService service;

  @BeforeEach
  void setUp() {
    service = new PaymentsReportingServiceImpl(client);
  }

  @Test
  void whenGetPaymentsReportingThenInvokeClient() {
    Long organizationId = 1L;
    String iuf = "IUF123";
    String regulationUniqueIdentifier = "RUI123";
    LocalDateIntervalFilter regulationDateFilter = new LocalDateIntervalFilter(LocalDate.now().minusDays(10), LocalDate.now());
    Pageable pageable = Mockito.mock(Pageable.class);
    String accessToken = "ACCESSTOKEN";
    PagedModelPaymentsReportingView expectedResult = new PagedModelPaymentsReportingView();

    when(client.getPaymentsReporting(organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter, pageable, accessToken))
      .thenReturn(expectedResult);

    PagedModelPaymentsReportingView result = service.getPaymentsReporting(organizationId, iuf, regulationUniqueIdentifier, regulationDateFilter, pageable, accessToken);

    assertSame(expectedResult, result);
  }
}
