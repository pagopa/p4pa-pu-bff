package it.gov.pagopa.pu.bff.mapper.export_file;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.gov.pagopa.pu.bff.dto.generated.ReceiptsArchivingExportFileRequest;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.processexecutions.dto.generated.ReceiptsArchivingExportFileFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.ReceiptsArchivingExportFileRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class ReceiptsArchivingExportFileRequestDTOMapperTest {

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  private ReceiptsArchivingExportFileRequestDTOMapper mapper;

  @BeforeEach
  void setup() {
    mapper = new ReceiptsArchivingExportFileRequestDTOMapper();
  }

  @Test
  void givenCorrectRequestDTOWhenMap2ProcessExecutionsDTOThenCorrectMapping() {
    ReceiptsArchivingExportFileRequest request = podamFactory.manufacturePojo(ReceiptsArchivingExportFileRequest.class);
    ReceiptsArchivingExportFileFilter expectedFilterFields = ReceiptsArchivingExportFileFilter.builder()
      .paymentDateTime(DateUtils.toRangeClosedOffsetDateTimeIntervalFilter(request.getFilterFields().getPaymentDate()))
      .build();
    ReceiptsArchivingExportFileRequestDTO expected = ReceiptsArchivingExportFileRequestDTO.builder()
      .organizationId(request.getOrganizationId())
      .exportFileType(request.getExportFileType())
      .fileVersion(request.getFileVersion())
      .filterFields(expectedFilterFields)
      .build();

    ReceiptsArchivingExportFileRequestDTO result = mapper.map2ProcessExecutionsDto(request);

    TestUtils.checkNotNullFields(result);
    assertEquals(expected, result);
  }
}
