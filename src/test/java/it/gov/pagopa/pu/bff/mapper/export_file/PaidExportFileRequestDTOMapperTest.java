package it.gov.pagopa.pu.bff.mapper.export_file;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.gov.pagopa.pu.bff.dto.generated.PaidExportFileRequest;
import it.gov.pagopa.pu.bff.util.DateUtils;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaidExportFileFilter;
import it.gov.pagopa.pu.processexecutions.dto.generated.PaidExportFileRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class PaidExportFileRequestDTOMapperTest {

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  private PaidExportFileRequestDTOMapper mapper;

  @BeforeEach
  void setup() {
    mapper = new PaidExportFileRequestDTOMapper();
  }

  @Test
  void givenCorrectRequestDTOWhenMap2ProcessExecutionsDTOThenCorrectMapping() {
    PaidExportFileRequest request = podamFactory.manufacturePojo(PaidExportFileRequest.class);
    PaidExportFileFilter expectedFilterFields = PaidExportFileFilter.builder()
      .paymentDateTime(DateUtils.toRangeClosedOffsetDateTimeIntervalFilter(request.getFilterFields().getPaymentDate()))
      .installmentUpdateDateTime(DateUtils.toRangeClosedOffsetDateTimeIntervalFilter(request.getFilterFields().getInstallmentUpdateDate()))
      .debtPositionTypeOrgId(request.getFilterFields().getDebtPositionTypeOrgId())
      .build();
    PaidExportFileRequestDTO expected = PaidExportFileRequestDTO.builder()
      .organizationId(request.getOrganizationId())
      .exportFileType(request.getExportFileType())
      .fileVersion(request.getFileVersion())
      .filterFields(expectedFilterFields)
      .build();

    PaidExportFileRequestDTO result = mapper.map2ProcessExecutionsDto(request);

    TestUtils.checkNotNullFields(result);
    assertEquals(expected, result);
  }
}
