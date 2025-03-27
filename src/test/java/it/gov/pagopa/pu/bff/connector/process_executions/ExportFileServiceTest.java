package it.gov.pagopa.pu.bff.connector.process_executions;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.connector.process_executions.client.ExportFileClient;
import it.gov.pagopa.pu.bff.connector.process_executions.client.ExportFileSearchClient;
import it.gov.pagopa.pu.bff.dto.ExportFileFiltersDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFileRequestDTO;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelExportFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ExportFileServiceTest {

  @Mock
  private ExportFileClient client;
  @Mock
  private ExportFileSearchClient searchClient;

  private ExportFileService service;

  @BeforeEach
  void setUp() {
    service = new ExportFileServiceImpl(client, searchClient);
  }

  @Test
  void whenGetExportFileByIdThenInvokeClient() {
    ExportFileFiltersDTO filtersDTO = new ExportFileFiltersDTO();
    String operatorExternalUserId = "MAPPEDEXTERNALUSERID";
    String accessToken = "ACCESSTOKEN";
    Pageable pageable = Mockito.mock(Pageable.class);
    PagedModelExportFile expectedResult = new PagedModelExportFile();

    when(searchClient.getExportFiles(Mockito.same(filtersDTO), Mockito.same(operatorExternalUserId), Mockito.same(pageable), Mockito.same(accessToken)))
      .thenReturn(expectedResult);

    PagedModelExportFile result = service.getExportFiles(filtersDTO, operatorExternalUserId, pageable, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void whenCreateExportFileThenInvokeClient() {
    ExportFileRequestDTO requestDTO = ExportFileRequestDTO.builder()
      .organizationId(1L)
      .exportFileType(ExportFileRequestDTO.ExportFileTypeEnum.CLASSIFICATIONS)
      .fileVersion("version1")
      .filterFields(ExportFileFilter.builder()
        .iuv("iuv")
        .build())
      .build();
    String accessToken = "ACCESSTOKEN";

    service.createExportFile(requestDTO, accessToken);

    Mockito.verify(client).createExportFile(requestDTO, accessToken);
  }

}
