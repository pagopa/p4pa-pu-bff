package it.gov.pagopa.pu.bff.connector.organization;

import it.gov.pagopa.pu.bff.connector.organization.client.TaxonomyClient;
import it.gov.pagopa.pu.organization.dto.generated.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxonomyClientServiceTest {

  private final String organizationType = "organizationType";
  private final String macroAreaCode = "macroAreaCode";
  private final String serviceTypeCode = "serviceTypeCode";
  private final String accessToken = "accessToken";

  @Mock
  private TaxonomyClient client;

  private TaxonomyClientService service;

  @BeforeEach
  void setUp() {
    service = new TaxonomyClientServiceImpl(client);
  }

  @Test
  void testGetCollectionReason() {
    CollectionModelTaxonomyCollectionReasonDTO expected = new CollectionModelTaxonomyCollectionReasonDTO();
    when(client.getCollectionReason(organizationType, macroAreaCode, serviceTypeCode, accessToken))
      .thenReturn(expected);
    CollectionModelTaxonomyCollectionReasonDTO result = service.getCollectionReason(organizationType, macroAreaCode, serviceTypeCode, accessToken);
    assertEquals(expected, result);
  }

  @Test
  void testGetMacroArea() {
    CollectionModelTaxonomyMacroAreaCodeDTO expected = new CollectionModelTaxonomyMacroAreaCodeDTO();
    when(client.getMacroArea(organizationType, accessToken))
      .thenReturn(expected);
    CollectionModelTaxonomyMacroAreaCodeDTO result = service.getMacroArea(organizationType, accessToken);
    assertEquals(expected, result);
  }

  @Test
  void testGetOrganizationType() {
    CollectionModelTaxonomyOrganizationTypeDTO expected = new CollectionModelTaxonomyOrganizationTypeDTO();
    when(client.getOrganizationType(accessToken))
      .thenReturn(expected);
    CollectionModelTaxonomyOrganizationTypeDTO result = service.getOrganizationType(accessToken);
    assertEquals(expected, result);
  }

  @Test
  void testGetServiceType() {
    CollectionModelTaxonomyServiceTypeCodeDTO expected = new CollectionModelTaxonomyServiceTypeCodeDTO();
    when(client.getServiceType(organizationType, macroAreaCode, accessToken))
      .thenReturn(expected);
    CollectionModelTaxonomyServiceTypeCodeDTO result = service.getServiceType(organizationType, macroAreaCode, accessToken);
    assertEquals(expected, result);
  }

  @Test
  void testGetTaxonomyCode() {
    CollectionModelTaxonomyCodeDTO expected = new CollectionModelTaxonomyCodeDTO();
    String collectionReason = "collectionReason";
    when(client.getTaxonomyCode(organizationType, macroAreaCode, serviceTypeCode, collectionReason, accessToken))
      .thenReturn(expected);
    CollectionModelTaxonomyCodeDTO result = service.getTaxonomyCode(organizationType, macroAreaCode, serviceTypeCode, collectionReason, accessToken);
    assertEquals(expected, result);
  }
}
