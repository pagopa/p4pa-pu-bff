package it.gov.pagopa.pu.bff.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeDetailDTO;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.service.debt_position.DebtPositionTypeRetrieverService;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.organization.dto.generated.Taxonomy;
import java.time.OffsetDateTime;
import java.util.List;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeControllerTest {

  @Mock
  private DebtPositionTypeRetrieverService debtPositionTypeRetrieverServiceMock;

  @InjectMocks
  private DebtPositionTypeController debtPositionTypeController;

  private DebtPositionType debtPositionTypeDTO;

  @BeforeEach
  void setUp() {
    Authentication authentication = new UsernamePasswordAuthenticationToken(
      "fakeUser", "fakeAccessToken");
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);

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


  @Test
  void testGetDebtPositionType() {
    when(debtPositionTypeRetrieverServiceMock.getDebtPositionTypeById(any(),
      anyLong())).thenReturn(debtPositionTypeDTO);

    ResponseEntity<DebtPositionType> response = debtPositionTypeController.getDebtPositionType(
      "123");

    assertEquals(HttpStatus.OK, response.getStatusCode());
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

    verify(debtPositionTypeRetrieverServiceMock,
      times(1)).getDebtPositionTypeById(any(), eq(123L));
  }

  @Test
  void givenCorrectRequestWhenGetDebtPositionTypeWithCountThenOk() {
    long organizationId = 1L;
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
          Mockito.eq(organizationId),
          Mockito.argThat(
            p -> p.getPageNumber() == 0 && p.getPageSize() == 10 && p.getSort()
              .isUnsorted()),
          Mockito.any(), anyString()))
      .thenReturn(expectedResult);

    ResponseEntity<PagedDebtPositionTypeWithCount> response = debtPositionTypeController.getDebtPositionTypeWithCount(
      organizationId,
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
      .organizationTypeDescription(taxonomy.getOrganizationTypeDescription())
      .macroAreaName(taxonomy.getMacroAreaName())
      .serviceType(taxonomy.getServiceType())
      .collectionReason(taxonomy.getCollectionReason())
      .taxonomyCode(debtPositionTypeDTO.getTaxonomyCode())
      .flagAnonymousFiscalCode(debtPositionTypeDTO.getFlagAnonymousFiscalCode())
      .flagMandatoryDueDate(debtPositionTypeDTO.getFlagMandatoryDueDate())
      .flagNotifyIo(debtPositionTypeDTO.getFlagNotifyIo())
      .ioTemplateMessage(debtPositionTypeDTO.getIoTemplateMessage())
      .build();

    Mockito.when(
        debtPositionTypeRetrieverServiceMock.getDebtPositionTypeDetail(anyLong(),
          Mockito.eq(debtPositionTypeDTO.getDebtPositionTypeId()), any(),
          anyString()))
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
          Mockito.eq(debtPositionTypeDTO.getDebtPositionTypeId()), any(),
          anyString()))
      .thenReturn(null);

    ResponseEntity<DebtPositionTypeDetailDTO> response = debtPositionTypeController.getDebtPositionTypeDetail(
      1L,
      debtPositionTypeDTO.getDebtPositionTypeId());

    Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}

