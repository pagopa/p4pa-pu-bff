package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.TaxonomyClient;
import it.gov.pagopa.pu.organization.dto.generated.*;
import it.gov.pagopa.pu.workflowhub.dto.generated.WorkflowCreatedDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxonomyServiceTest {

  private final String organizationType = "organizationType";
  private final String macroAreaCode = "macroAreaCode";
  private final String serviceTypeCode = "serviceTypeCode";
  private final String accessToken = "accessToken";

  @Mock
  private TaxonomyClient taxonomyClientMock;

  private TaxonomyService service;

  @BeforeEach
  void setUp() {
    service = new TaxonomyServiceImpl(taxonomyClientMock);
  }

  @Test
  void testGetTaxonomyDetail() {
    Long taxonomyId = 123L;
    Taxonomy expected = new Taxonomy();

    when(taxonomyClientMock.getTaxonomyDetail(Mockito.same(taxonomyId), Mockito.same(accessToken)))
      .thenReturn(expected);

    Taxonomy result = service.getTaxonomyDetail(taxonomyId, accessToken);

    assertSame(expected, result);
  }

  @Test
  void testGetCollectionReason() {
    CollectionModelTaxonomyCollectionReasonDTO expected = new CollectionModelTaxonomyCollectionReasonDTO();
    when(taxonomyClientMock.getCollectionReason(Mockito.same(organizationType), Mockito.same(macroAreaCode), Mockito.same(serviceTypeCode), Mockito.same(accessToken)))
      .thenReturn(expected);
    CollectionModelTaxonomyCollectionReasonDTO result = service.getCollectionReason(organizationType, macroAreaCode, serviceTypeCode, accessToken);
    assertSame(expected, result);
  }

  @Test
  void testGetMacroArea() {
    CollectionModelTaxonomyMacroAreaCodeDTO expected = new CollectionModelTaxonomyMacroAreaCodeDTO();
    when(taxonomyClientMock.getMacroArea(Mockito.same(organizationType), Mockito.same(accessToken)))
      .thenReturn(expected);
    CollectionModelTaxonomyMacroAreaCodeDTO result = service.getMacroArea(organizationType, accessToken);
    assertSame(expected, result);
  }

  @Test
  void testGetOrganizationType() {
    CollectionModelTaxonomyOrganizationTypeDTO expected = new CollectionModelTaxonomyOrganizationTypeDTO();
    when(taxonomyClientMock.getOrganizationType(Mockito.same(accessToken)))
      .thenReturn(expected);
    CollectionModelTaxonomyOrganizationTypeDTO result = service.getOrganizationType(accessToken);
    assertSame(expected, result);
  }

  @Test
  void testGetServiceType() {
    CollectionModelTaxonomyServiceTypeCodeDTO expected = new CollectionModelTaxonomyServiceTypeCodeDTO();
    when(taxonomyClientMock.getServiceType(Mockito.same(organizationType), Mockito.same(macroAreaCode), Mockito.same(accessToken)))
      .thenReturn(expected);
    CollectionModelTaxonomyServiceTypeCodeDTO result = service.getServiceType(organizationType, macroAreaCode, accessToken);
    assertSame(expected, result);
  }

  @Test
  void testGetTaxonomyCode() {
    CollectionModelTaxonomyCodeDTO expected = new CollectionModelTaxonomyCodeDTO();
    String collectionReason = "collectionReason";
    when(taxonomyClientMock.getTaxonomyCode(Mockito.same(organizationType), Mockito.same(macroAreaCode), Mockito.same(serviceTypeCode), Mockito.same(collectionReason), Mockito.same(accessToken)))
      .thenReturn(expected);
    CollectionModelTaxonomyCodeDTO result = service.getTaxonomyCode(organizationType, macroAreaCode, serviceTypeCode, collectionReason, accessToken);
    assertSame(expected, result);
  }

  @Test
  void testGetByTaxonomyCode() {
    Taxonomy expected = new Taxonomy();
    when(taxonomyClientMock.getTaxonomyByTaxonomyCode("TAX", accessToken))
      .thenReturn(expected);
    Taxonomy result = service.getTaxonomyByTaxonomyCode("TAX", accessToken);
    assertSame(expected, result);
  }

  @Test
  void testGetTaxonomies() {
    PagedModelTaxonomy expected = new PagedModelTaxonomy();
    String collectionReason = "collectionReason";
    Pageable pageable = PageRequest.of(0, 10);
    when(taxonomyClientMock.getTaxonomies(Mockito.same(organizationType), Mockito.same(macroAreaCode), Mockito.same(serviceTypeCode), Mockito.same(collectionReason), Mockito.same(pageable), Mockito.same(accessToken)))
      .thenReturn(expected);
    PagedModelTaxonomy result = service.getTaxonomies(organizationType, macroAreaCode, serviceTypeCode, collectionReason, pageable, accessToken);
    assertSame(expected, result);
  }

  @Test
  void testSynchronizeTaxonomy() {
    WorkflowCreatedDTO expected = new WorkflowCreatedDTO();
    when(taxonomyClientMock.synchronizeTaxonomy(Mockito.same(accessToken)))
      .thenReturn(expected);

    WorkflowCreatedDTO result = service.synchronizeTaxonomy(accessToken);

    assertSame(expected, result);
  }
}
