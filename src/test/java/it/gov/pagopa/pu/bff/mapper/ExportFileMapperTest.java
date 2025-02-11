package it.gov.pagopa.pu.bff.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.client.AuthzClient;
import it.gov.pagopa.pu.bff.dto.generated.PagedExportFile;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile;
import it.gov.pagopa.pu.processexecutions.dto.generated.ExportFile.StatusEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.PageMetadata;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelExportFile;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelExportFileEmbedded;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

@ExtendWith(MockitoExtension.class)
class ExportFileMapperTest {

  @Mock
  private AuthzClient authzClientMock;

  private ExportFileMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ExportFileMapper(authzClientMock);
  }

  @Test
  void givenPopulatedPagedModelWhenMapToPagedExportFileThenCorrectMapping() {
    String accessToken = "ACCESS_TOKEN";
    String operatorExternalId = "operatorExternalId";
    String otherOperatorExternalId = "otherOperatorExternalId";
    UserInfo userInfo = new UserInfo();
    userInfo.setMappedExternalUserId(operatorExternalId);
    userInfo.setFamilyName("familyName");
    userInfo.setName("name");
    UserInfo otherUserInfo = new UserInfo();
    otherUserInfo.setMappedExternalUserId(operatorExternalId);
    otherUserInfo.setFamilyName("otherFamilyName");
    otherUserInfo.setName("otherName");
    PagedModelExportFile pagedModelExportFile = new PagedModelExportFile();
    PagedModelExportFileEmbedded embedded = new PagedModelExportFileEmbedded();
    ExportFile exportFileMatchingOperator = new ExportFile();
    exportFileMatchingOperator.setCreationDate(OffsetDateTime.now());
    exportFileMatchingOperator.setExportFileId(1L);
    exportFileMatchingOperator.setOperatorExternalId(operatorExternalId);
    exportFileMatchingOperator.setFileName("fileName");
    exportFileMatchingOperator.setStatus(StatusEnum.COMPLETED);
    exportFileMatchingOperator.setNumTotalRows(10L);
    ExportFile flowFileWithNoTotalRows = new ExportFile();
    flowFileWithNoTotalRows.setCreationDate(OffsetDateTime.now());
    flowFileWithNoTotalRows.setExportFileId(3L);
    flowFileWithNoTotalRows.setFileName("fileName");
    flowFileWithNoTotalRows.setOperatorExternalId(otherOperatorExternalId);
    flowFileWithNoTotalRows.setStatus(StatusEnum.ERROR);
    flowFileWithNoTotalRows.setNumTotalRows(null);

    embedded.setExportFiles(
      List.of(exportFileMatchingOperator, flowFileWithNoTotalRows));
    pagedModelExportFile.setEmbedded(embedded);
    PageMetadata page = new PageMetadata();
    page.setSize(10L);
    page.setTotalElements(4L);
    page.setTotalPages(1L);
    page.setNumber(4L);
    pagedModelExportFile.setPage(page);

    Mockito.when(authzClientMock.getUserInfoFromMappedExternaUserId(
      otherOperatorExternalId, accessToken)).thenReturn(otherUserInfo);

    PagedExportFile result = mapper.mapToPagedExportFile(
      pagedModelExportFile, userInfo, accessToken);

    TestUtils.checkNotNullFields(result);

    assertNotNull(result);
    assertEquals(4L, result.getNumber());
    assertEquals(4L, result.getTotalElements());
    assertEquals(1L, result.getTotalPages());
    assertEquals(10L, result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(2, result.getContent().size());
    checkExportFile(exportFileMatchingOperator,
      result.getContent().getFirst(),
      userInfo.getFamilyName() + " " + userInfo.getName(),
      it.gov.pagopa.pu.bff.dto.generated.ExportFile.StatusEnum.COMPLETED
    );
    checkExportFile(flowFileWithNoTotalRows,
      result.getContent().get(1),
      otherUserInfo.getFamilyName() + " " + otherUserInfo.getName(),
      it.gov.pagopa.pu.bff.dto.generated.ExportFile.StatusEnum.ERROR,
      "totalRows");
    Mockito.verify(authzClientMock)
      .getUserInfoFromMappedExternaUserId(otherOperatorExternalId, accessToken);
    Mockito.verifyNoMoreInteractions(authzClientMock);
  }

  private void checkExportFile(
    ExportFile expectedExportFile,
    it.gov.pagopa.pu.bff.dto.generated.ExportFile mappedIngestionFlowFile,
    String expectedOperator,
    it.gov.pagopa.pu.bff.dto.generated.ExportFile.StatusEnum expectedStatus,
    String... nullFields) {
    TestUtils.checkNotNullFields(mappedIngestionFlowFile, nullFields);
    assertEquals(expectedExportFile.getExportFileId(),
      mappedIngestionFlowFile.getExportFileId());
    assertEquals(expectedExportFile.getFileName(),
      mappedIngestionFlowFile.getFileName());
    assertEquals(expectedExportFile.getCreationDate(),
      mappedIngestionFlowFile.getCreationDate());
    assertEquals(expectedOperator, mappedIngestionFlowFile.getOperator());
    assertEquals(expectedExportFile.getNumTotalRows(),
      mappedIngestionFlowFile.getTotalRows());
    assertEquals(expectedStatus, mappedIngestionFlowFile.getStatus());
  }

  @Test
  void givenNoContentWhenMapToPagedDebtPositionWithCountThenPartialMapping() {
    String accessToken = "ACCESS_TOKEN";
    UserInfo userInfo = new UserInfo();
    PagedModelExportFile pagedModelExportFile = new PagedModelExportFile();
    PageMetadata page = new PageMetadata();
    page.setSize(10L);
    page.setTotalElements(1L);
    page.setTotalPages(1L);
    page.setNumber(1L);
    pagedModelExportFile.setPage(page);

    PagedExportFile result = mapper.mapToPagedExportFile(
      pagedModelExportFile, userInfo, accessToken);

    assertNotNull(result);
    assertEquals(1L, result.getNumber());
    assertEquals(1L, result.getTotalElements());
    assertEquals(1L, result.getTotalPages());
    assertEquals(10L, result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }

  @Test
  void givenNoPageWhenMapToPagedDebtPositionWithCountThenPartialMapping() {
    String accessToken = "ACCESS_TOKEN";
    String operatorExternalId = "operatorExternalId";
    UserInfo userInfo = new UserInfo();
    userInfo.setMappedExternalUserId(operatorExternalId);
    userInfo.setFamilyName("familyName");
    userInfo.setName("name");
    PagedModelExportFile pagedModelExportFile = new PagedModelExportFile();
    PagedModelExportFileEmbedded embedded = new PagedModelExportFileEmbedded();
    ExportFile exportFileMatchingOperator = new ExportFile();
    exportFileMatchingOperator.setCreationDate(OffsetDateTime.now());
    exportFileMatchingOperator.setExportFileId(1L);
    exportFileMatchingOperator.setOperatorExternalId(operatorExternalId);
    exportFileMatchingOperator.setFileName("fileName");
    exportFileMatchingOperator.setStatus(StatusEnum.COMPLETED);
    exportFileMatchingOperator.setNumTotalRows(10L);
    embedded.setExportFiles(List.of(exportFileMatchingOperator));
    pagedModelExportFile.setEmbedded(embedded);

    PagedExportFile result = mapper.mapToPagedExportFile(
      pagedModelExportFile, userInfo, accessToken);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(1, result.getContent().size());
    TestUtils.checkNotNullFields(result.getContent().getFirst());
    checkExportFile(exportFileMatchingOperator,
      result.getContent().getFirst(),
      userInfo.getFamilyName() + " " + userInfo.getName(),
      it.gov.pagopa.pu.bff.dto.generated.ExportFile.StatusEnum.COMPLETED
    );
  }
}
