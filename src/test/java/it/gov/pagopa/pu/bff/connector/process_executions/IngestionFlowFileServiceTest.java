package it.gov.pagopa.pu.bff.connector.process_executions;

import it.gov.pagopa.pu.bff.connector.process_executions.client.IngestionFlowFileSearchClient;
import it.gov.pagopa.pu.bff.dto.IngestionFlowFileFiltersDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelIngestionFlowFile;
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
class IngestionFlowFileServiceTest {

  @Mock
  private IngestionFlowFileSearchClient client;

  private IngestionFlowFileService service;

  @BeforeEach
  void setUp() {
    service = new IngestionFlowFileServiceImpl(client);
  }

  @Test
  void whenGetIngestionFlowFileByIdThenInvokeClient() {
    IngestionFlowFileFiltersDTO filtersDTO = new IngestionFlowFileFiltersDTO();
    String operatorExternalUserId = "MAPPEDEXTERNALUSERID";
    String accessToken = "ACCESSTOKEN";
    Pageable pageable = Mockito.mock(Pageable.class);
    PagedModelIngestionFlowFile expectedResult = new PagedModelIngestionFlowFile();

    when(client.getIngestionFlowFiles(Mockito.same(filtersDTO), Mockito.same(operatorExternalUserId), Mockito.same(pageable), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    PagedModelIngestionFlowFile result = service.getIngestionFlowFiles(filtersDTO, operatorExternalUserId, pageable, accessToken);

    assertSame(expectedResult, result);
  }

}
