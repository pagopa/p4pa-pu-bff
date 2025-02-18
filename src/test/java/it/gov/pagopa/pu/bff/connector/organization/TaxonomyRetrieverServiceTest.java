package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.TaxonomyClient;
import it.gov.pagopa.pu.organization.dto.generated.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxonomyRetrieverServiceTest {

  private final String organizationType = "organizationType";
  private final String macroAreaCode = "macroAreaCode";
  private final String serviceTypeCode = "serviceTypeCode";
  private final String accessToken = "accessToken";

  @Mock
  private TaxonomyClient client;

  private TaxonomyService service;

  @BeforeEach
  void setUp() {
    service = new TaxonomyServiceImpl(client);
  }

  @Test
  void testGetCollectionReason() {
    CollectionModelTaxonomyCollectionReasonDTO expected = new CollectionModelTaxonomyCollectionReasonDTO();
    when(client.getCollectionReason(Mockito.same(organizationType), Mockito.same(macroAreaCode), Mockito.same(serviceTypeCode), Mockito.same(accessToken)))
      .thenReturn(expected);
    CollectionModelTaxonomyCollectionReasonDTO result = service.getCollectionReason(organizationType, macroAreaCode, serviceTypeCode, accessToken);
    assertSame(expected, result);
  }

  @Test
  void testGetMacroArea() {
    CollectionModelTaxonomyMacroAreaCodeDTO expected = new CollectionModelTaxonomyMacroAreaCodeDTO();
    when(client.getMacroArea(Mockito.same(organizationType), Mockito.same(accessToken)))
      .thenReturn(expected);
    CollectionModelTaxonomyMacroAreaCodeDTO result = service.getMacroArea(organizationType, accessToken);
    assertSame(expected, result);
  }

  @Test
  void testGetOrganizationType() {
    CollectionModelTaxonomyOrganizationTypeDTO expected = new CollectionModelTaxonomyOrganizationTypeDTO();
    when(client.getOrganizationType(Mockito.same(accessToken)))
      .thenReturn(expected);
    CollectionModelTaxonomyOrganizationTypeDTO result = service.getOrganizationType(accessToken);
    assertSame(expected, result);
  }

  @Test
  void testGetServiceType() {
    CollectionModelTaxonomyServiceTypeCodeDTO expected = new CollectionModelTaxonomyServiceTypeCodeDTO();
    when(client.getServiceType(Mockito.same(organizationType), Mockito.same(macroAreaCode), Mockito.same(accessToken)))
      .thenReturn(expected);
    CollectionModelTaxonomyServiceTypeCodeDTO result = service.getServiceType(organizationType, macroAreaCode, accessToken);
    assertSame(expected, result);
  }

  @Test
  void testGetTaxonomyCode() {
    CollectionModelTaxonomyCodeDTO expected = new CollectionModelTaxonomyCodeDTO();
    String collectionReason = "collectionReason";
    when(client.getTaxonomyCode(Mockito.same(organizationType), Mockito.same(macroAreaCode), Mockito.same(serviceTypeCode), Mockito.same(collectionReason), Mockito.same(accessToken)))
      .thenReturn(expected);
    CollectionModelTaxonomyCodeDTO result = service.getTaxonomyCode(organizationType, macroAreaCode, serviceTypeCode, collectionReason, accessToken);
    assertSame(expected, result);
  }
}
