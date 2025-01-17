package it.gov.pagopa.pu.bff.service.taxonomy;

import static org.junit.jupiter.api.Assertions.*;

import it.gov.pagopa.pu.bff.connector.organization.client.TaxonomyClient;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyCodeDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyCollectionReasonDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyMacroAreaCodeDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyOrganizationTypeDTO;
import it.gov.pagopa.pu.bff.dto.generated.TaxonomyServiceTypeCodeDTO;
import it.gov.pagopa.pu.bff.mapper.TaxonomyCodeMapper;
import it.gov.pagopa.pu.bff.mapper.TaxonomyCollectionReasonMapper;
import it.gov.pagopa.pu.bff.mapper.TaxonomyMacroAreaCodeMapper;
import it.gov.pagopa.pu.bff.mapper.TaxonomyOrganizationTypeMapper;
import it.gov.pagopa.pu.bff.mapper.TaxonomyServiceTypeCodeMapper;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyCodeDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyCodeDTOEmbedded;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyCollectionReasonDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyCollectionReasonDTOEmbedded;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyMacroAreaCodeDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyMacroAreaCodeDTOEmbedded;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyOrganizationTypeDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyOrganizationTypeDTOEmbedded;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyServiceTypeCodeDTO;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.CollectionModelTaxonomyServiceTypeCodeDTOEmbedded;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaxonomyServiceImplTest {
  @Mock
  private TaxonomyClient taxonomyClientMock;
  @Mock
  private TaxonomyCodeMapper taxonomyCodeMapperMock;
  @Mock
  private TaxonomyServiceTypeCodeMapper taxonomyServiceTypeCodeMapperMock;
  @Mock
  private TaxonomyCollectionReasonMapper taxonomyCollectionReasonMapperMock;
  @Mock
  private TaxonomyOrganizationTypeMapper taxonomyOrganizationTypeMapperMock;
  @Mock
  private TaxonomyMacroAreaCodeMapper taxonomyMacroAreaCodeMapperMock;
  @InjectMocks
  private TaxonomyServiceImpl taxonomyService;
  @Test
  void testGetTaxonomyCode() {
    // Mock input and output DTOs
    CollectionModelTaxonomyCodeDTO taxonomyCodeDTO = new CollectionModelTaxonomyCodeDTO();
    CollectionModelTaxonomyCodeDTOEmbedded collectionModelTaxonomyCodeDTOEmbedded = new CollectionModelTaxonomyCodeDTOEmbedded();

    var inputDTO = new it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyCodeDTO();
    var outputDTO = new TaxonomyCodeDTO();

    collectionModelTaxonomyCodeDTOEmbedded.addTaxonomyCodeDTOesItem(inputDTO);
    taxonomyCodeDTO.setEmbedded(collectionModelTaxonomyCodeDTOEmbedded);

    Mockito.when(taxonomyClientMock.getTaxonomyCode(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
      .thenReturn(taxonomyCodeDTO);
    Mockito.when(taxonomyCodeMapperMock.map(inputDTO)).thenReturn(outputDTO);

    // Call the service method
    List<TaxonomyCodeDTO> result = taxonomyService.getTaxonomyCode("Type1", "Macro1", "ServiceCode1", "Reason1", "token");

    // Assert the result
    assertEquals(1, result.size());
    assertEquals(outputDTO, result.get(0));
  }

  @Test
  void testGetServiceType() {
    // Mock input and output DTOs
    CollectionModelTaxonomyServiceTypeCodeDTO collectionModelTaxonomyServiceTypeCodeDTO = new CollectionModelTaxonomyServiceTypeCodeDTO();
    CollectionModelTaxonomyServiceTypeCodeDTOEmbedded collectionModelTaxonomyServiceTypeCodeDTOEmbedded = new CollectionModelTaxonomyServiceTypeCodeDTOEmbedded();

    var inputDTO = new it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyServiceTypeCodeDTO();
    var outputDTO = new TaxonomyServiceTypeCodeDTO();

    collectionModelTaxonomyServiceTypeCodeDTOEmbedded.addTaxonomyServiceTypeCodeDTOesItem(inputDTO);
    collectionModelTaxonomyServiceTypeCodeDTO.setEmbedded(collectionModelTaxonomyServiceTypeCodeDTOEmbedded);

    Mockito.when(taxonomyClientMock.getServiceType(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
      .thenReturn(collectionModelTaxonomyServiceTypeCodeDTO);
    Mockito.when(taxonomyServiceTypeCodeMapperMock.map(inputDTO)).thenReturn(outputDTO);

    // Call the service method
    List<TaxonomyServiceTypeCodeDTO> result = taxonomyService.getServiceType("Type1", "Macro1", "token");

    // Assert the result
    assertEquals(1, result.size());
    assertEquals(outputDTO, result.get(0));
  }

  @Test
  void testGetOrganizationTypes() {
    // Mock input and output DTOs
    CollectionModelTaxonomyOrganizationTypeDTO collectionModelTaxonomyOrganizationTypeDTO = new CollectionModelTaxonomyOrganizationTypeDTO();
    CollectionModelTaxonomyOrganizationTypeDTOEmbedded collectionModelTaxonomyOrganizationTypeDTOEmbedded = new CollectionModelTaxonomyOrganizationTypeDTOEmbedded();

    var inputDTO = new it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyOrganizationTypeDTO();
    var outputDTO = new TaxonomyOrganizationTypeDTO();

    collectionModelTaxonomyOrganizationTypeDTOEmbedded.addTaxonomyOrganizationTypeDTOesItem(inputDTO);
    collectionModelTaxonomyOrganizationTypeDTO.setEmbedded(collectionModelTaxonomyOrganizationTypeDTOEmbedded);

    Mockito.when(taxonomyClientMock.getOrganizationType(Mockito.anyString()))
      .thenReturn(collectionModelTaxonomyOrganizationTypeDTO);
    Mockito.when(taxonomyOrganizationTypeMapperMock.map(inputDTO)).thenReturn(outputDTO);

    // Call the service method
    List<TaxonomyOrganizationTypeDTO> result = taxonomyService.getOrganizationTypes("token");

    // Assert the result
    assertEquals(1, result.size());
    assertEquals(outputDTO, result.get(0));
  }

  @Test
  void testGetMacroArea() {
    // Mock input and output DTOs
    CollectionModelTaxonomyMacroAreaCodeDTO collectionModelTaxonomyMacroAreaCodeDTO = new CollectionModelTaxonomyMacroAreaCodeDTO();
    CollectionModelTaxonomyMacroAreaCodeDTOEmbedded collectionModelTaxonomyMacroAreaCodeDTOEmbedded = new CollectionModelTaxonomyMacroAreaCodeDTOEmbedded();

    var inputDTO = new it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyMacroAreaCodeDTO();
    var outputDTO = new TaxonomyMacroAreaCodeDTO();

    collectionModelTaxonomyMacroAreaCodeDTOEmbedded.addTaxonomyMacroAreaCodeDTOesItem(inputDTO);
    collectionModelTaxonomyMacroAreaCodeDTO.setEmbedded(collectionModelTaxonomyMacroAreaCodeDTOEmbedded);

    Mockito.when(taxonomyClientMock.getMacroArea(Mockito.anyString(), Mockito.anyString()))
      .thenReturn(collectionModelTaxonomyMacroAreaCodeDTO);
    Mockito.when(taxonomyMacroAreaCodeMapperMock.map(inputDTO)).thenReturn(outputDTO);

    // Call the service method
    List<TaxonomyMacroAreaCodeDTO> result = taxonomyService.getMacroArea("Type1", "token");

    // Assert the result
    assertEquals(1, result.size());
    assertEquals(outputDTO, result.get(0));
  }

  @Test
  void testGetCollectionReason() {
    // Mock input and output DTOs
    CollectionModelTaxonomyCollectionReasonDTO collectionModelTaxonomyCollectionReasonDTO = new CollectionModelTaxonomyCollectionReasonDTO();
    CollectionModelTaxonomyCollectionReasonDTOEmbedded collectionModelTaxonomyCollectionReasonDTOEmbedded = new CollectionModelTaxonomyCollectionReasonDTOEmbedded();

    var inputDTO = new it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyCollectionReasonDTO();
    var outputDTO = new TaxonomyCollectionReasonDTO();

    collectionModelTaxonomyCollectionReasonDTOEmbedded.addTaxonomyCollectionReasonDTOesItem(inputDTO);
    collectionModelTaxonomyCollectionReasonDTO.setEmbedded(collectionModelTaxonomyCollectionReasonDTOEmbedded);

    Mockito.when(taxonomyClientMock.getCollectionReason(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
      .thenReturn(collectionModelTaxonomyCollectionReasonDTO);
    Mockito.when(taxonomyCollectionReasonMapperMock.map(inputDTO)).thenReturn(outputDTO);

    // Call the service method
    List<TaxonomyCollectionReasonDTO> result = taxonomyService.getCollectionReason("Type1", "Macro1", "ServiceCode1", "token");

    // Assert the result
    assertEquals(1, result.size());
    assertEquals(outputDTO, result.get(0));
  }

}
