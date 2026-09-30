package it.gov.pagopa.pu.bff.service.org_sub_unit_operators;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.connector.organization.OrgSubUnitOperatorsService;
import it.gov.pagopa.pu.bff.connector.organization.OrganizationService;
import it.gov.pagopa.pu.bff.dto.OrgSubUnitOperatorsFilters;
import it.gov.pagopa.pu.bff.dto.generated.OrgSubUnitOperator;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnitOperators;
import it.gov.pagopa.pu.bff.mapper.PagedOrgSubUnitOperatorsMapper;
import it.gov.pagopa.pu.bff.service.AuthorizationService;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitOperators;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperators;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperatorsEmbedded;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrgSubUnitOperatorsRetrieverServiceImplTest {

  @Mock
  private AuthorizationService authorizationServiceMock;
  @Mock
  private OrgSubUnitOperatorsService orgSubUnitOperatorsServiceMock;
  @Mock
  private PagedOrgSubUnitOperatorsMapper pagedOrgSubUnitOperatorsMapperMock;
  @Mock
  private AuthzService authzServiceMock;
  @Mock
  private OrganizationService organizationServiceMock;

  private OrgSubUnitOperatorsRetrieverServiceImpl service;
  private UserInfo loggedUser;

  public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  private static final Long ORGANIZATION_ID = 1L;
  private static final String SUB_UNIT_CODE = "subUnitCode";
  private static final String ACCESS_TOKEN = "accessToken";
  private static final Pageable PAGEABLE = PageRequest.of(0, 10);
  private static final String ORGANIZATION_IPA_CODE = "ipaCode";
  private static final Integer PAGE_MAX_SIZE = 1000;

  @BeforeEach
  void setUp() {
    loggedUser = podamFactory.manufacturePojo(UserInfo.class);

    service = new OrgSubUnitOperatorsRetrieverServiceImpl(
      authorizationServiceMock,
      orgSubUnitOperatorsServiceMock,
      pagedOrgSubUnitOperatorsMapperMock,
      authzServiceMock,
      organizationServiceMock,
      PAGE_MAX_SIZE
    );
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      authorizationServiceMock,
      orgSubUnitOperatorsServiceMock,
      pagedOrgSubUnitOperatorsMapperMock,
      authzServiceMock,
      organizationServiceMock);
  }

  @Test
  void getOrgSubUnitOperators_shouldPropagateException_whenAuthorizationFails() {
    OrgSubUnitOperatorsFilters filters = new OrgSubUnitOperatorsFilters(null, null, null, null);

    doThrow(new RuntimeException("Not authorized")).when(authorizationServiceMock)
      .validateAdminRole(ORGANIZATION_ID, loggedUser);

    assertThrows(RuntimeException.class, () ->
      service.getOrgSubUnitOperators(ORGANIZATION_ID, SUB_UNIT_CODE, filters, PAGEABLE, loggedUser, ACCESS_TOKEN));
  }

  @Test
  void getOrgSubUnitOperators_shouldReturnEmptyContent_whenEmbeddedIsNull() {
    OrgSubUnitOperatorsFilters filters = new OrgSubUnitOperatorsFilters(null, null, null, null);

    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setIpaCode(ORGANIZATION_IPA_CODE);

    OperatorDTO operatorDTO = podamFactory.manufacturePojo(OperatorDTO.class);
    operatorDTO.setMappedExternalUserId("mappedExternalUserId");

    OperatorsPage operatorsPage = podamFactory.manufacturePojo(OperatorsPage.class);
    operatorsPage.setContent(List.of(operatorDTO));

    PagedModelOrgSubUnitOperators pagedModel = podamFactory.manufacturePojo(PagedModelOrgSubUnitOperators.class);
    pagedModel.setEmbedded(null);

    doNothing().when(authorizationServiceMock)
      .validateAdminRole(ORGANIZATION_ID, loggedUser);

    when(organizationServiceMock.getOrganizationByOrganizationId(ORGANIZATION_ID, ACCESS_TOKEN))
      .thenReturn(organization);

    when(authzServiceMock.getOrganizationOperators(ORGANIZATION_IPA_CODE, null, null, null, 0, PAGE_MAX_SIZE, ACCESS_TOKEN))
      .thenReturn(operatorsPage);

    Set<String> mappedExternalUserIds = Set.of(operatorDTO.getMappedExternalUserId());

    when(orgSubUnitOperatorsServiceMock.findByOrganizationIdAndSubUnitCodeAndOperatorExternalUserIdIn(ORGANIZATION_ID, SUB_UNIT_CODE, mappedExternalUserIds, PAGEABLE, ACCESS_TOKEN))
      .thenReturn(pagedModel);

    PagedOrgSubUnitOperators expectedResult = podamFactory.manufacturePojo(PagedOrgSubUnitOperators.class);

    when(pagedOrgSubUnitOperatorsMapperMock.map(Collections.emptyList(), pagedModel))
      .thenReturn(expectedResult);

    PagedOrgSubUnitOperators result = service.getOrgSubUnitOperators(ORGANIZATION_ID, SUB_UNIT_CODE, filters, PAGEABLE, loggedUser, ACCESS_TOKEN);

    assertEquals(expectedResult, result);
  }

  @Test
  void getOrgSubUnitOperators_shouldReturnEmptyContent_whenOperatorsListIsEmpty() {
    OrgSubUnitOperatorsFilters filters = new OrgSubUnitOperatorsFilters(null, null, null, null);

    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setIpaCode(ORGANIZATION_IPA_CODE);

    OperatorDTO operatorDTO = podamFactory.manufacturePojo(OperatorDTO.class);
    operatorDTO.setMappedExternalUserId("mappedExternalUserId");

    OperatorsPage operatorsPage = podamFactory.manufacturePojo(OperatorsPage.class);
    operatorsPage.setContent(List.of(operatorDTO));

    PagedModelOrgSubUnitOperators pagedModel = buildPagedModelWithEmbedded(Collections.emptyList());

    doNothing().when(authorizationServiceMock)
      .validateAdminRole(ORGANIZATION_ID, loggedUser);

    when(organizationServiceMock.getOrganizationByOrganizationId(ORGANIZATION_ID, ACCESS_TOKEN))
      .thenReturn(organization);

    when(authzServiceMock.getOrganizationOperators(ORGANIZATION_IPA_CODE, null, null, null, 0, PAGE_MAX_SIZE, ACCESS_TOKEN))
      .thenReturn(operatorsPage);

    Set<String> mappedExternalUserIds = Set.of(operatorDTO.getMappedExternalUserId());

    when(orgSubUnitOperatorsServiceMock.findByOrganizationIdAndSubUnitCodeAndOperatorExternalUserIdIn(ORGANIZATION_ID, SUB_UNIT_CODE, mappedExternalUserIds, PAGEABLE, ACCESS_TOKEN))
      .thenReturn(pagedModel);

    PagedOrgSubUnitOperators expectedResult = podamFactory.manufacturePojo(PagedOrgSubUnitOperators.class);

    when(pagedOrgSubUnitOperatorsMapperMock.map(Collections.emptyList(), pagedModel))
      .thenReturn(expectedResult);

    PagedOrgSubUnitOperators result = service.getOrgSubUnitOperators(ORGANIZATION_ID, SUB_UNIT_CODE, filters, PAGEABLE, loggedUser, ACCESS_TOKEN);

    assertEquals(expectedResult, result);
  }

  @Test
  void getOrgSubUnitOperators_shouldEnrichEachOperator_withOperatorInfoFromAuthz() {
    OrgSubUnitOperatorsFilters filters = new OrgSubUnitOperatorsFilters(null, null, null, null);

    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setIpaCode(ORGANIZATION_IPA_CODE);

    OperatorDTO operatorDTO1 = podamFactory.manufacturePojo(OperatorDTO.class);
    operatorDTO1.setMappedExternalUserId("mappedExternalUserId1");

    OperatorDTO operatorDTO2 = podamFactory.manufacturePojo(OperatorDTO.class);
    operatorDTO2.setMappedExternalUserId("mappedExternalUserId2");

    OperatorsPage operatorsPage = podamFactory.manufacturePojo(OperatorsPage.class);
    operatorsPage.setContent(List.of(operatorDTO1, operatorDTO2));

    OrgSubUnitOperators operator1 = podamFactory.manufacturePojo(OrgSubUnitOperators.class);
    operator1.setOperatorExternalUserId(operatorDTO1.getMappedExternalUserId());

    OrgSubUnitOperators operator2 = podamFactory.manufacturePojo(OrgSubUnitOperators.class);
    operator2.setOperatorExternalUserId(operatorDTO2.getMappedExternalUserId());

    List<OrgSubUnitOperators> sourceOperators = List.of(operator1, operator2);

    PagedModelOrgSubUnitOperators pagedModel = buildPagedModelWithEmbedded(sourceOperators);

    doNothing().when(authorizationServiceMock)
      .validateAdminRole(ORGANIZATION_ID, loggedUser);

    when(organizationServiceMock.getOrganizationByOrganizationId(ORGANIZATION_ID, ACCESS_TOKEN))
      .thenReturn(organization);

    when(authzServiceMock.getOrganizationOperators(ORGANIZATION_IPA_CODE, null, null, null, 0, PAGE_MAX_SIZE, ACCESS_TOKEN))
      .thenReturn(operatorsPage);

    Set<String> mappedExternalUserIds = Set.of(operatorDTO1.getMappedExternalUserId(), operatorDTO2.getMappedExternalUserId());

    when(orgSubUnitOperatorsServiceMock.findByOrganizationIdAndSubUnitCodeAndOperatorExternalUserIdIn(ORGANIZATION_ID, SUB_UNIT_CODE, mappedExternalUserIds, PAGEABLE, ACCESS_TOKEN))
      .thenReturn(pagedModel);

    OrgSubUnitOperator mapped1 = podamFactory.manufacturePojo(OrgSubUnitOperator.class);
    OrgSubUnitOperator mapped2 = podamFactory.manufacturePojo(OrgSubUnitOperator.class);

    when(pagedOrgSubUnitOperatorsMapperMock.toOrgSubUnitOperator(operator1, operatorDTO1))
      .thenReturn(mapped1);

    when(pagedOrgSubUnitOperatorsMapperMock.toOrgSubUnitOperator(operator2, operatorDTO2))
      .thenReturn(mapped2);

    List<OrgSubUnitOperator> expectedContent = List.of(mapped1, mapped2);

    PagedOrgSubUnitOperators expectedResult = podamFactory.manufacturePojo(PagedOrgSubUnitOperators.class);

    when(pagedOrgSubUnitOperatorsMapperMock.map(expectedContent, pagedModel))
      .thenReturn(expectedResult);

    PagedOrgSubUnitOperators result = service.getOrgSubUnitOperators(ORGANIZATION_ID, SUB_UNIT_CODE, filters, PAGEABLE, loggedUser, ACCESS_TOKEN);

    assertEquals(expectedResult, result);
  }

  @Test
  void getOrgSubUnitOperators_shouldRetrieveSingleOperator_whenMappedExternalUserIdIsProvided() {
    String mappedExternalUserId = "mappedExternalUserId";

    OrgSubUnitOperatorsFilters filters = new OrgSubUnitOperatorsFilters(mappedExternalUserId, null, null, null);

    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setIpaCode(ORGANIZATION_IPA_CODE);

    OperatorDTO operatorDTO = podamFactory.manufacturePojo(OperatorDTO.class);
    operatorDTO.setMappedExternalUserId(mappedExternalUserId);

    OrgSubUnitOperators sourceOperator = podamFactory.manufacturePojo(OrgSubUnitOperators.class);
    sourceOperator.setOperatorExternalUserId(mappedExternalUserId);

    PagedModelOrgSubUnitOperators pagedModel = buildPagedModelWithEmbedded(List.of(sourceOperator));

    doNothing().when(authorizationServiceMock)
      .validateAdminRole(ORGANIZATION_ID, loggedUser);

    when(organizationServiceMock.getOrganizationByOrganizationId(ORGANIZATION_ID, ACCESS_TOKEN))
      .thenReturn(organization);

    when(authzServiceMock.getOrganizationOperator(ORGANIZATION_IPA_CODE, mappedExternalUserId, ACCESS_TOKEN))
      .thenReturn(operatorDTO);

    when(orgSubUnitOperatorsServiceMock.findByOrganizationIdAndSubUnitCodeAndOperatorExternalUserIdIn(ORGANIZATION_ID, SUB_UNIT_CODE, Set.of(mappedExternalUserId), PAGEABLE, ACCESS_TOKEN))
      .thenReturn(pagedModel);

    OrgSubUnitOperator mappedOperator = podamFactory.manufacturePojo(OrgSubUnitOperator.class);

    when(pagedOrgSubUnitOperatorsMapperMock.toOrgSubUnitOperator(sourceOperator, operatorDTO))
      .thenReturn(mappedOperator);

    PagedOrgSubUnitOperators expectedResult = podamFactory.manufacturePojo(PagedOrgSubUnitOperators.class);

    when(pagedOrgSubUnitOperatorsMapperMock.map(List.of(mappedOperator), pagedModel))
      .thenReturn(expectedResult);

    PagedOrgSubUnitOperators result = service.getOrgSubUnitOperators(ORGANIZATION_ID, SUB_UNIT_CODE, filters, PAGEABLE, loggedUser, ACCESS_TOKEN);

    assertEquals(expectedResult, result);
  }

  @Test
  void getOrgSubUnitOperators_shouldMatchFiltersIgnoringCase() {
    String mappedExternalUserId = "mappedExternalUserId";

    OrgSubUnitOperatorsFilters filters = new OrgSubUnitOperatorsFilters(mappedExternalUserId, "fiscalcode", "mario", "rossi");

    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setIpaCode(ORGANIZATION_IPA_CODE);

    OperatorDTO operatorDTO = podamFactory.manufacturePojo(OperatorDTO.class);
    operatorDTO.setMappedExternalUserId(mappedExternalUserId);
    operatorDTO.setFiscalCode("FISCALCODE");
    operatorDTO.setFirstName("MARIO");
    operatorDTO.setLastName("ROSSI");

    OrgSubUnitOperators sourceOperator = podamFactory.manufacturePojo(OrgSubUnitOperators.class);
    sourceOperator.setOperatorExternalUserId(mappedExternalUserId);

    PagedModelOrgSubUnitOperators pagedModel = buildPagedModelWithEmbedded(List.of(sourceOperator));

    doNothing().when(authorizationServiceMock)
      .validateAdminRole(ORGANIZATION_ID, loggedUser);

    when(organizationServiceMock.getOrganizationByOrganizationId(ORGANIZATION_ID, ACCESS_TOKEN))
      .thenReturn(organization);

    when(authzServiceMock.getOrganizationOperator(ORGANIZATION_IPA_CODE, mappedExternalUserId, ACCESS_TOKEN))
      .thenReturn(operatorDTO);

    when(orgSubUnitOperatorsServiceMock.findByOrganizationIdAndSubUnitCodeAndOperatorExternalUserIdIn(ORGANIZATION_ID, SUB_UNIT_CODE, Set.of(mappedExternalUserId), PAGEABLE, ACCESS_TOKEN))
      .thenReturn(pagedModel);

    OrgSubUnitOperator mappedOperator = podamFactory.manufacturePojo(OrgSubUnitOperator.class);

    when(pagedOrgSubUnitOperatorsMapperMock.toOrgSubUnitOperator(sourceOperator, operatorDTO))
      .thenReturn(mappedOperator);

    PagedOrgSubUnitOperators expectedResult = podamFactory.manufacturePojo(PagedOrgSubUnitOperators.class);

    when(pagedOrgSubUnitOperatorsMapperMock.map(List.of(mappedOperator), pagedModel))
      .thenReturn(expectedResult);

    PagedOrgSubUnitOperators result = service.getOrgSubUnitOperators(ORGANIZATION_ID, SUB_UNIT_CODE, filters, PAGEABLE, loggedUser, ACCESS_TOKEN);

    assertEquals(expectedResult, result);
  }

  @Test
  void getOrgSubUnitOperators_shouldReturnEmptyOperators_whenFiscalCodeDoesNotMatch() {
    String mappedExternalUserId = "mappedExternalUserId";

    OrgSubUnitOperatorsFilters filters = new OrgSubUnitOperatorsFilters(mappedExternalUserId, "DIFFERENT_FISCAL_CODE", null, null);

    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setIpaCode(ORGANIZATION_IPA_CODE);

    OperatorDTO operatorDTO = podamFactory.manufacturePojo(OperatorDTO.class);
    operatorDTO.setMappedExternalUserId(mappedExternalUserId);
    operatorDTO.setFiscalCode("FISCAL_CODE");

    PagedModelOrgSubUnitOperators pagedModel = buildPagedModelWithEmbedded(Collections.emptyList());

    doNothing().when(authorizationServiceMock)
      .validateAdminRole(ORGANIZATION_ID, loggedUser);

    when(organizationServiceMock.getOrganizationByOrganizationId(ORGANIZATION_ID, ACCESS_TOKEN))
      .thenReturn(organization);

    when(authzServiceMock.getOrganizationOperator(ORGANIZATION_IPA_CODE, mappedExternalUserId, ACCESS_TOKEN))
      .thenReturn(operatorDTO);

    when(orgSubUnitOperatorsServiceMock.findByOrganizationIdAndSubUnitCodeAndOperatorExternalUserIdIn(ORGANIZATION_ID, SUB_UNIT_CODE, Collections.emptySet(), PAGEABLE, ACCESS_TOKEN))
      .thenReturn(pagedModel);

    PagedOrgSubUnitOperators expectedResult = podamFactory.manufacturePojo(PagedOrgSubUnitOperators.class);

    when(pagedOrgSubUnitOperatorsMapperMock.map(Collections.emptyList(), pagedModel))
      .thenReturn(expectedResult);

    PagedOrgSubUnitOperators result = service.getOrgSubUnitOperators(ORGANIZATION_ID, SUB_UNIT_CODE, filters, PAGEABLE, loggedUser, ACCESS_TOKEN);

    assertEquals(expectedResult, result);
  }

  @Test
  void getOrgSubUnitOperators_shouldReturnEmptyOperators_whenFirstNameIsNull() {
    String mappedExternalUserId = "mappedExternalUserId";

    OrgSubUnitOperatorsFilters filters = new OrgSubUnitOperatorsFilters(mappedExternalUserId, null, "Mario", null);

    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setIpaCode(ORGANIZATION_IPA_CODE);

    OperatorDTO operatorDTO = podamFactory.manufacturePojo(OperatorDTO.class);
    operatorDTO.setMappedExternalUserId(mappedExternalUserId);
    operatorDTO.setFirstName(null);

    PagedModelOrgSubUnitOperators pagedModel = buildPagedModelWithEmbedded(Collections.emptyList());

    doNothing().when(authorizationServiceMock)
      .validateAdminRole(ORGANIZATION_ID, loggedUser);

    when(organizationServiceMock.getOrganizationByOrganizationId(ORGANIZATION_ID, ACCESS_TOKEN))
      .thenReturn(organization);

    when(authzServiceMock.getOrganizationOperator(ORGANIZATION_IPA_CODE, mappedExternalUserId, ACCESS_TOKEN))
      .thenReturn(operatorDTO);

    when(orgSubUnitOperatorsServiceMock.findByOrganizationIdAndSubUnitCodeAndOperatorExternalUserIdIn(ORGANIZATION_ID, SUB_UNIT_CODE, Collections.emptySet(), PAGEABLE, ACCESS_TOKEN))
      .thenReturn(pagedModel);

    PagedOrgSubUnitOperators expectedResult = podamFactory.manufacturePojo(PagedOrgSubUnitOperators.class);

    when(pagedOrgSubUnitOperatorsMapperMock.map(Collections.emptyList(), pagedModel))
      .thenReturn(expectedResult);

    PagedOrgSubUnitOperators result = service.getOrgSubUnitOperators(ORGANIZATION_ID, SUB_UNIT_CODE, filters, PAGEABLE, loggedUser, ACCESS_TOKEN);

    assertEquals(expectedResult, result);
  }

  private PagedModelOrgSubUnitOperators buildPagedModelWithEmbedded(List<OrgSubUnitOperators> operators) {
    PagedModelOrgSubUnitOperators pagedModel = podamFactory.manufacturePojo(PagedModelOrgSubUnitOperators.class);
    PagedModelOrgSubUnitOperatorsEmbedded embedded =
      podamFactory.manufacturePojo(PagedModelOrgSubUnitOperatorsEmbedded.class);
    embedded.setOrgSubUnitOperatorses(operators);
    pagedModel.setEmbedded(embedded);
    return pagedModel;
  }

  @Test
  void givenCorrectRequestWhenAddOrgSubUnitsToOperatorThenOk() {
    String mappedExternalUserId = "mappedExternalUserId";
    List<String> orgSubUnitCodes = List.of("SUB_UNIT_1", "SUB_UNIT_2");

    service.addOrgSubUnitsToOperator(ORGANIZATION_ID, mappedExternalUserId, orgSubUnitCodes, loggedUser, ACCESS_TOKEN);

    verify(authorizationServiceMock).validateAdminRole(ORGANIZATION_ID, loggedUser);
    verify(orgSubUnitOperatorsServiceMock).addOrgSubUnitsToOperator(ORGANIZATION_ID, mappedExternalUserId, orgSubUnitCodes, ACCESS_TOKEN);
  }

  @Test
  void givenCorrectRequestWhenDeleteOrgSubUnitFromOperatorThenOk() {
    String mappedExternalUserId = "mappedExternalUserId";
    String subUnitCode = "SUB_UNIT_1";

    service.deleteOrgSubUnitFromOperator(ORGANIZATION_ID, mappedExternalUserId, subUnitCode, loggedUser, ACCESS_TOKEN);

    verify(authorizationServiceMock).validateAdminRole(ORGANIZATION_ID, loggedUser);
    verify(orgSubUnitOperatorsServiceMock).deleteOrgSubUnitFromOperator(ORGANIZATION_ID, mappedExternalUserId, subUnitCode, ACCESS_TOKEN);
  }

  @Test
  void givenCorrectRequestWhenAddOperatorsToOrgSubUnitThenOk() {
    String subUnitCode = "SUB_UNIT_1";
    List<String> mappedExternalUserIds = List.of("mappedExternalUserId1", "mappedExternalUserId2");

    service.addOperatorsToOrgSubUnit(ORGANIZATION_ID, subUnitCode, mappedExternalUserIds, loggedUser, ACCESS_TOKEN);

    verify(authorizationServiceMock).validateAdminRole(ORGANIZATION_ID, loggedUser);
    verify(orgSubUnitOperatorsServiceMock).addOperatorsToOrgSubUnit(ORGANIZATION_ID, subUnitCode, mappedExternalUserIds, ACCESS_TOKEN);
  }

  @Test
  void givenCorrectRequestWhenDeleteOperatorsFromOrgSubUnitThenOk() {
    String subUnitCode = "SUB_UNIT_1";
    List<String> mappedExternalUserIds = List.of("mappedExternalUserId1", "mappedExternalUserId2");

    service.deleteOperatorsFromOrgSubUnit(ORGANIZATION_ID, subUnitCode, mappedExternalUserIds, loggedUser, ACCESS_TOKEN);

    verify(authorizationServiceMock).validateAdminRole(ORGANIZATION_ID, loggedUser);
    verify(orgSubUnitOperatorsServiceMock).deleteOperatorsFromOrgSubUnit(ORGANIZATION_ID, subUnitCode, mappedExternalUserIds, ACCESS_TOKEN);
  }
}
