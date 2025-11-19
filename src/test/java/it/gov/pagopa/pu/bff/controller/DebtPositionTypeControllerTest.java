package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.*;
import it.gov.pagopa.pu.bff.mapper.DebtPositionTypeRequestBodyMapper;
import it.gov.pagopa.pu.bff.security.SecurityUtilsTest;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionTypeRetrieverService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.organization.dto.generated.Taxonomy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeControllerTest {

  @Mock
  private DebtPositionTypeRetrieverService debtPositionTypeRetrieverServiceMock;
  @Mock
  private DebtPositionTypeRequestBodyMapper debtPositionTypeRequestBodyMapperMock;

  @InjectMocks
  private DebtPositionTypeController debtPositionTypeController;

  private DebtPositionType debtPositionTypeDTO;

  private final String accessToken = "ACCESSTOKEN";
  private final UserInfo loggedUser = TestUtils.getPodamFactory().manufacturePojo(UserInfo.class);

  @BeforeEach
  void setUp() {
    SecurityUtilsTest.configureSecurityContext(accessToken, loggedUser);

    debtPositionTypeDTO = new DebtPositionType();
    debtPositionTypeDTO.setDebtPositionTypeId(123L);
    debtPositionTypeDTO.setBrokerId(456L);
    debtPositionTypeDTO.setCode("CODE001");
    debtPositionTypeDTO.setDescription("Test Debt Position Type");
    debtPositionTypeDTO.setOrgType("OrgType001");
    debtPositionTypeDTO.setMacroArea("MacroArea001");
    debtPositionTypeDTO.setServiceType("ServiceType001");
    debtPositionTypeDTO.setCollectingReason("Collecting Reason 001");
    debtPositionTypeDTO.setTaxonomyCode("TaxonomyCode001");
    debtPositionTypeDTO.setFlagAnonymousFiscalCode(true);
    debtPositionTypeDTO.setFlagMandatoryDueDate(false);
    debtPositionTypeDTO.setFlagNotifyIo(true);
    debtPositionTypeDTO.setIoTemplateMessage("Test IO Template Message");
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
      debtPositionTypeRetrieverServiceMock,
      debtPositionTypeRequestBodyMapperMock
    );
  }

  @AfterEach
  void clearContext(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void testGetDebtPositionType() {
    // Given
    when(debtPositionTypeRetrieverServiceMock.getDebtPositionTypeById(accessToken, 123L)).thenReturn(debtPositionTypeDTO);

    // When
    ResponseEntity<DebtPositionType> response = debtPositionTypeController.getDebtPositionType(
      "123");

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(123L, response.getBody().getDebtPositionTypeId());
    assertEquals(456L, response.getBody().getBrokerId());
    assertEquals("CODE001", response.getBody().getCode());
    assertEquals("Test Debt Position Type",
      response.getBody().getDescription());
    assertEquals("OrgType001", response.getBody().getOrgType());
    assertEquals("MacroArea001", response.getBody().getMacroArea());
    assertEquals("ServiceType001", response.getBody().getServiceType());
    assertEquals("Collecting Reason 001",
      response.getBody().getCollectingReason());
    assertEquals("TaxonomyCode001", response.getBody().getTaxonomyCode());
    assertEquals(true, response.getBody().getFlagAnonymousFiscalCode());
    assertEquals(false, response.getBody().getFlagMandatoryDueDate());
    assertEquals(true, response.getBody().getFlagNotifyIo());
    assertEquals("Test IO Template Message",
      response.getBody().getIoTemplateMessage());
  }

  @Test
  void givenCorrectRequestWhenGetDebtPositionTypeWithCountThenOk() {
    long organizationId = 1L;
    String code = "code";
    String description = "description";
    PagedDebtPositionTypeWithCount expectedResult = new PagedDebtPositionTypeWithCount();
    expectedResult.setContent(List.of(DebtPositionTypeWithCount.builder()
      .debtPositionTypeId(1L)
      .code("code")
      .description("description")
      .updateDate(OffsetDateTime.now())
      .activeOrganizations(10)
      .build()));
    expectedResult.setSize(10L);
    expectedResult.setTotalElements(1L);
    expectedResult.setTotalPages(0L);
    expectedResult.setNumber(0L);

    Mockito.when(
        debtPositionTypeRetrieverServiceMock.getDebtPositionTypeWithCount(
          Mockito.eq(organizationId), Mockito.eq(code), Mockito.eq(description),
          Mockito.argThat(
            p -> p.getPageNumber() == 0 && p.getPageSize() == 10 && p.getSort()
              .isUnsorted()),
          same(loggedUser), same(accessToken)))
      .thenReturn(expectedResult);

    ResponseEntity<PagedDebtPositionTypeWithCount> response = debtPositionTypeController.getDebtPositionTypeWithCount(
      organizationId, code, description,
      PageRequest.of(0, 10));

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertSame(expectedResult, response.getBody());
  }

  @Test
  void whenGetDebtPositionTypeDetailThenOk() {
    Taxonomy taxonomy = Taxonomy.builder()
      .organizationType("organizationType")
      .organizationTypeDescription("orgTypeDesc")
      .macroAreaCode("macroAreaCode")
      .macroAreaName("macroAreaName")
      .macroAreaDescription("macroAreaDesc")
      .serviceTypeCode("serviceTypeCode")
      .serviceType("serviceType")
      .serviceTypeDescription("serviceTypeDesc")
      .collectionReason("collectionReason")
      .startDateValidity(OffsetDateTime.now())
      .endDateOfValidity(OffsetDateTime.now())
      .taxonomyCode(debtPositionTypeDTO.getTaxonomyCode())
      .build();

    DebtPositionTypeDetailDTO expectedResult = DebtPositionTypeDetailDTO.builder()
      .debtPositionTypeId(debtPositionTypeDTO.getDebtPositionTypeId())
      .code(debtPositionTypeDTO.getCode())
      .description(debtPositionTypeDTO.getDescription())
      .orgType(taxonomy.getOrganizationTypeDescription())
      .macroArea(taxonomy.getMacroAreaName())
      .serviceType(taxonomy.getServiceType())
      .collectingReason(taxonomy.getCollectionReason())
      .taxonomyCode(debtPositionTypeDTO.getTaxonomyCode())
      .flagAnonymousFiscalCode(debtPositionTypeDTO.getFlagAnonymousFiscalCode())
      .flagMandatoryDueDate(debtPositionTypeDTO.getFlagMandatoryDueDate())
      .flagNotifyIo(debtPositionTypeDTO.getFlagNotifyIo())
      .ioTemplateMessage(debtPositionTypeDTO.getIoTemplateMessage())
      .build();

    Mockito.when(
        debtPositionTypeRetrieverServiceMock.getDebtPositionTypeDetail(anyLong(),
          Mockito.eq(debtPositionTypeDTO.getDebtPositionTypeId()), same(loggedUser), same(accessToken)))
      .thenReturn(expectedResult);

    ResponseEntity<DebtPositionTypeDetailDTO> response = debtPositionTypeController.getDebtPositionTypeDetail(
      1L,
      debtPositionTypeDTO.getDebtPositionTypeId());

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertEquals(expectedResult, response.getBody());
  }

  @Test
  void givenNullResultWhenGetDebtPositionTypeDetailThenNotFound() {
    Mockito.when(
        debtPositionTypeRetrieverServiceMock.getDebtPositionTypeDetail(anyLong(),
          Mockito.eq(debtPositionTypeDTO.getDebtPositionTypeId()), same(loggedUser), same(accessToken)))
      .thenReturn(null);

    ResponseEntity<DebtPositionTypeDetailDTO> response = debtPositionTypeController.getDebtPositionTypeDetail(
      1L,
      debtPositionTypeDTO.getDebtPositionTypeId());

    Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void whenCreateDebtPositionTypeThenOk() {
    DebtPositionTypeRequestBody requestBody = new DebtPositionTypeRequestBody();
    it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeRequestBody mappedBody = new it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeRequestBody();
    DebtPositionType expectedResult = new DebtPositionType();

    Mockito.when(debtPositionTypeRequestBodyMapperMock.map(Mockito.same(requestBody), Mockito.same(loggedUser.getBrokerId())))
        .thenReturn(mappedBody);
    Mockito.when(
        debtPositionTypeRetrieverServiceMock.createDebtPositionType(
          Mockito.same(mappedBody), same(loggedUser), same(accessToken)))
      .thenReturn(expectedResult);

    ResponseEntity<DebtPositionType> response = debtPositionTypeController.createDebtPositionType(
      requestBody);

    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertEquals(expectedResult, response.getBody());
  }

  @Test
  void whenPatchDebtPositionTypeThenOk() {
    Long debtPositionTypeId = 1L;
    DebtPositionTypePatchRequestBody requestBody = new DebtPositionTypePatchRequestBody();
    DebtPositionType expectedResult = new DebtPositionType();

    Mockito.when(
        debtPositionTypeRetrieverServiceMock.patchDebtPositionType(
          Mockito.eq(debtPositionTypeId),Mockito.eq(requestBody), same(loggedUser), same(accessToken)))
      .thenReturn(expectedResult);

    ResponseEntity<DebtPositionType> response = debtPositionTypeController.patchDebtPositionType(
      debtPositionTypeId,
      requestBody);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertEquals(expectedResult, response.getBody());
  }

  @Test
  void givenNullResultWhenPatchDebtPositionTypeThenNotFound() {
    Long debtPositionTypeId = 1L;
    DebtPositionTypePatchRequestBody requestBody = new DebtPositionTypePatchRequestBody();
    Mockito.when(
        debtPositionTypeRetrieverServiceMock.patchDebtPositionType(eq(debtPositionTypeId),
          Mockito.eq(requestBody), same(loggedUser), same(accessToken)))
      .thenReturn(null);

    ResponseEntity<DebtPositionType> response = debtPositionTypeController.patchDebtPositionType(
      debtPositionTypeId,
      requestBody);

    Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assertions.assertNull(response.getBody());
  }

  @Test
  void givenCorrectRequestWhenDeleteDebtPositionTypeThenOk() {
    long debtPositionTypeId = 123L;

    Mockito.doNothing().when(debtPositionTypeRetrieverServiceMock).deleteDebtPositionType(
      Mockito.eq(debtPositionTypeId),
      same(loggedUser), same(accessToken)
    );

    ResponseEntity<Void> response = debtPositionTypeController.deleteDebtPositionType(debtPositionTypeId);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Mockito.verify(debtPositionTypeRetrieverServiceMock).deleteDebtPositionType(
      Mockito.eq(debtPositionTypeId),
      Mockito.any(),
      Mockito.anyString()
    );
    Mockito.verifyNoMoreInteractions(debtPositionTypeRetrieverServiceMock);
  }

  @Test
  void whenGetDebtPositionTypesByOrganizationIdThenOk() {
    Long organizationId = 1L;
    List<DebtPositionType> expectedList = List.of(debtPositionTypeDTO);

    Mockito.when(debtPositionTypeRetrieverServiceMock.getDebtPositionTypesByOrganizationId(Mockito.eq(organizationId), same(loggedUser), same(accessToken)))
      .thenReturn(expectedList);

    ResponseEntity<List<DebtPositionType>> response = debtPositionTypeController.getDebtPositionTypesByOrganizationId(organizationId);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedList, response.getBody());

    Mockito.verify(debtPositionTypeRetrieverServiceMock).getDebtPositionTypesByOrganizationId(Mockito.eq(organizationId), same(loggedUser), same(accessToken));
  }

}

