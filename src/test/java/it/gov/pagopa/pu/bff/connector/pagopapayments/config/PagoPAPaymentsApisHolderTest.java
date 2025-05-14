package it.gov.pagopa.pu.bff.connector.pagopapayments.config;

import it.gov.pagopa.pu.bff.connector.BaseApiHolderTest;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.util.DefaultUriBuilderFactory;

@ExtendWith(MockitoExtension.class)
class PagoPAPaymentsApisHolderTest extends BaseApiHolderTest {
  @Mock
  private RestTemplateBuilder restTemplateBuilderMock;

  private PagoPAPaymentsApisHolder pagoPAPaymentsApisHolder;

  @BeforeEach
  void setUp() {
    Mockito.when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
    Mockito.when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());
    PagoPAPaymentsApiClientConfig clientConfig = PagoPAPaymentsApiClientConfig.builder()
      .baseUrl("http://example.com")
      .build();
    pagoPAPaymentsApisHolder = new PagoPAPaymentsApisHolder(clientConfig, restTemplateBuilderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      restTemplateBuilderMock,
      restTemplateMock
    );
  }

  @Test
  void whenGetPrintPaymentNoticeControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> pagoPAPaymentsApisHolder.getPrintPaymentNoticeControllerApi(accessToken)
        .generateNotice("iuv",new DebtPositionDTO()),
      new ParameterizedTypeReference<>() {},
      pagoPAPaymentsApisHolder::unload
    );
  }
}

