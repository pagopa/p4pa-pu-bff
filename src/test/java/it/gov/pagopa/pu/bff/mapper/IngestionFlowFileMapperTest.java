package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.dto.generated.PagedIngestionFlowFile;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile;
import it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile.StatusEnum;
import it.gov.pagopa.pu.processexecutions.dto.generated.PageMetadata;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelIngestionFlowFile;
import it.gov.pagopa.pu.processexecutions.dto.generated.PagedModelIngestionFlowFileEmbedded;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IngestionFlowFileMapperTest {
  @Mock
  private AuthzService authzServiceMock;

  private IngestionFlowFileMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new IngestionFlowFileMapper(authzServiceMock);
  }

  @Test
  void givenPopulatedPagedModelWhenMapToPagedIngestionFlowFileThenCorrectMapping() {
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
    PagedModelIngestionFlowFile pagedModelIngestionFlowFile = new PagedModelIngestionFlowFile();
    PagedModelIngestionFlowFileEmbedded embedded = new PagedModelIngestionFlowFileEmbedded();
    IngestionFlowFile ingestionFlowFileMatchingOperator = new IngestionFlowFile();
    ingestionFlowFileMatchingOperator.setCreationDate(OffsetDateTime.now());
    ingestionFlowFileMatchingOperator.setIngestionFlowFileId(1L);
    ingestionFlowFileMatchingOperator.setOperatorExternalId(operatorExternalId);
    ingestionFlowFileMatchingOperator.setFileName("fileName");
    ingestionFlowFileMatchingOperator.setStatus(StatusEnum.COMPLETED);
    ingestionFlowFileMatchingOperator.setNumTotalRows(10L);
    ingestionFlowFileMatchingOperator.setNumCorrectlyImportedRows(8L);
    IngestionFlowFile flowFileWithNoCorrectlyImportedRows = new IngestionFlowFile();
    flowFileWithNoCorrectlyImportedRows.setCreationDate(OffsetDateTime.now());
    flowFileWithNoCorrectlyImportedRows.setIngestionFlowFileId(2L);
    flowFileWithNoCorrectlyImportedRows.setOperatorExternalId(otherOperatorExternalId);
    flowFileWithNoCorrectlyImportedRows.setFileName("fileName");
    flowFileWithNoCorrectlyImportedRows.setStatus(StatusEnum.UPLOADED);
    flowFileWithNoCorrectlyImportedRows.setNumTotalRows(10L);
    flowFileWithNoCorrectlyImportedRows.setNumCorrectlyImportedRows(null);
    IngestionFlowFile flowFileWithNoTotalAndCorrectlyImportedRows = new IngestionFlowFile();
    flowFileWithNoTotalAndCorrectlyImportedRows.setCreationDate(OffsetDateTime.now());
    flowFileWithNoTotalAndCorrectlyImportedRows.setIngestionFlowFileId(3L);
    flowFileWithNoTotalAndCorrectlyImportedRows.setOperatorExternalId(otherOperatorExternalId);
    flowFileWithNoTotalAndCorrectlyImportedRows.setFileName("fileName");
    flowFileWithNoTotalAndCorrectlyImportedRows.setStatus(StatusEnum.PROCESSING);
    flowFileWithNoTotalAndCorrectlyImportedRows.setNumTotalRows(null);
    flowFileWithNoTotalAndCorrectlyImportedRows.setNumCorrectlyImportedRows(null);
    IngestionFlowFile flowFileWithNoTotalRows = new IngestionFlowFile();
    flowFileWithNoTotalRows.setCreationDate(OffsetDateTime.now());
    flowFileWithNoTotalRows.setIngestionFlowFileId(3L);
    flowFileWithNoTotalRows.setFileName("fileName");
    flowFileWithNoTotalRows.setOperatorExternalId(otherOperatorExternalId);
    flowFileWithNoTotalRows.setStatus(StatusEnum.ERROR);
    flowFileWithNoTotalRows.setNumTotalRows(null);
    flowFileWithNoTotalRows.setNumCorrectlyImportedRows(8L);

    embedded.setIngestionFlowFiles(List.of(ingestionFlowFileMatchingOperator,flowFileWithNoCorrectlyImportedRows,flowFileWithNoTotalAndCorrectlyImportedRows,flowFileWithNoTotalRows));
    pagedModelIngestionFlowFile.setEmbedded(embedded);
    PageMetadata page = new PageMetadata();
    page.setSize(10L);
    page.setTotalElements(4L);
    page.setTotalPages(1L);
    page.setNumber(4L);
    pagedModelIngestionFlowFile.setPage(page);

    Mockito.when(authzServiceMock.getUserInfoFromMappedExternaUserId(operatorExternalId,accessToken)).thenReturn(userInfo);
    Mockito.when(authzServiceMock.getUserInfoFromMappedExternaUserId(otherOperatorExternalId,accessToken)).thenReturn(otherUserInfo);

    PagedIngestionFlowFile result = mapper.mapToPagedIngestionFlowFile(
      pagedModelIngestionFlowFile,userInfo,accessToken);

    TestUtils.checkNotNullFields(result);

    assertNotNull(result);
    assertEquals(4L, result.getNumber());
    assertEquals(4L, result.getTotalElements());
    assertEquals(1L, result.getTotalPages());
    assertEquals(10L, result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(4, result.getContent().size());
    checkIngestionFlowFile(ingestionFlowFileMatchingOperator,
      result.getContent().getFirst(),
      userInfo.getFamilyName() + " " +userInfo.getName(),
      2L,
      it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile.StatusEnum.COMPLETED);
    checkIngestionFlowFile(flowFileWithNoCorrectlyImportedRows,
      result.getContent().get(1),
      otherUserInfo.getFamilyName() + " " +otherUserInfo.getName(),
      10L,
      it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile.StatusEnum.UPLOADED, "correctlyImportedRows");
    checkIngestionFlowFile(flowFileWithNoTotalAndCorrectlyImportedRows,
      result.getContent().get(2),
      otherUserInfo.getFamilyName() + " " +otherUserInfo.getName(),
      0L,
      it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile.StatusEnum.PROCESSING, "correctlyImportedRows","totalRows");
    checkIngestionFlowFile(flowFileWithNoTotalRows,
      result.getContent().get(3),
      otherUserInfo.getFamilyName() + " " +otherUserInfo.getName(),
      0L,
      it.gov.pagopa.pu.bff.dto.generated.IngestionFlowFile.StatusEnum.ERROR, "totalRows");
    Mockito.verify(authzServiceMock, Mockito.times(3)).getUserInfoFromMappedExternaUserId(otherOperatorExternalId,accessToken);
    Mockito.verifyNoMoreInteractions(authzServiceMock);
  }

  private void checkIngestionFlowFile(
    IngestionFlowFile expectedIngestionFlowFile, it.gov.pagopa.pu.bff.dto.generated.IngestionFlowFile mappedIngestionFlowFile, String expectedOperator, Long expectedDiscardedRows, it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile.StatusEnum expectedStatus, String... nullFields){
    TestUtils.checkNotNullFields(mappedIngestionFlowFile, nullFields);
    assertEquals(expectedIngestionFlowFile.getIngestionFlowFileId(), mappedIngestionFlowFile.getIngestionFlowFileId());
    assertEquals(expectedIngestionFlowFile.getFileName(), mappedIngestionFlowFile.getFileName());
    assertEquals(expectedIngestionFlowFile.getCreationDate(), mappedIngestionFlowFile.getCreationDate());
    assertEquals(expectedOperator, mappedIngestionFlowFile.getOperator());
    assertEquals(expectedIngestionFlowFile.getNumTotalRows(), mappedIngestionFlowFile.getTotalRows());
    assertEquals(expectedIngestionFlowFile.getNumCorrectlyImportedRows(), mappedIngestionFlowFile.getCorrectlyImportedRows());
    assertEquals(expectedDiscardedRows, mappedIngestionFlowFile.getDiscardedRows());
    assertEquals(expectedStatus, mappedIngestionFlowFile.getStatus());
  }

  @Test
  void givenNoContentWhenMapToPagedDebtPositionWithCountThenPartialMapping() {
    String accessToken = "ACCESS_TOKEN";
    UserInfo userInfo = new UserInfo();
    PagedModelIngestionFlowFile pagedModelIngestionFlowFile = new PagedModelIngestionFlowFile();
    PageMetadata page = new PageMetadata();
    page.setSize(10L);
    page.setTotalElements(1L);
    page.setTotalPages(1L);
    page.setNumber(1L);
    pagedModelIngestionFlowFile.setPage(page);

    PagedIngestionFlowFile result = mapper.mapToPagedIngestionFlowFile(
      pagedModelIngestionFlowFile, userInfo, accessToken);

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
    PagedModelIngestionFlowFile pagedModelIngestionFlowFile = new PagedModelIngestionFlowFile();
    PagedModelIngestionFlowFileEmbedded embedded = new PagedModelIngestionFlowFileEmbedded();
    IngestionFlowFile ingestionFlowFileMatchingOperator = new IngestionFlowFile();
    ingestionFlowFileMatchingOperator.setCreationDate(OffsetDateTime.now());
    ingestionFlowFileMatchingOperator.setIngestionFlowFileId(1L);
    ingestionFlowFileMatchingOperator.setOperatorExternalId(operatorExternalId);
    ingestionFlowFileMatchingOperator.setFileName("fileName");
    ingestionFlowFileMatchingOperator.setStatus(StatusEnum.COMPLETED);
    ingestionFlowFileMatchingOperator.setNumTotalRows(10L);
    ingestionFlowFileMatchingOperator.setNumCorrectlyImportedRows(8L);
    embedded.setIngestionFlowFiles(List.of(ingestionFlowFileMatchingOperator));
    pagedModelIngestionFlowFile.setEmbedded(embedded);

    PagedIngestionFlowFile result = mapper.mapToPagedIngestionFlowFile(
      pagedModelIngestionFlowFile, userInfo, accessToken);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(1, result.getContent().size());
    TestUtils.checkNotNullFields(result.getContent().getFirst());
    checkIngestionFlowFile(ingestionFlowFileMatchingOperator,
      result.getContent().getFirst(),
      userInfo.getFamilyName() + " " +userInfo.getName(),
      2L,
      it.gov.pagopa.pu.processexecutions.dto.generated.IngestionFlowFile.StatusEnum.COMPLETED);
  }
}
