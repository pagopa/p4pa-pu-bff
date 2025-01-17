package it.gov.pagopa.pu.bff.mapper;

import static org.junit.jupiter.api.Assertions.*;

import it.gov.pagopa.pu.bff.dto.generated.TaxonomyCodeDTO;
import org.junit.jupiter.api.Test;

class TaxonomyCodeMapperTest {

  private final TaxonomyCodeMapper mapper = new TaxonomyCodeMapper();

  @Test
  void testMap() {
    it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyCodeDTO input = new it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyCodeDTO();
    input.setOrganizationType("Type1");
    input.setOrganizationTypeDescription("Description1");
    input.setMacroAreaCode("Macro1");
    input.setMacroAreaName("MacroName1");
    input.setMacroAreaDescription("MacroDescription1");
    input.setServiceTypeCode("ServiceCode1");
    input.setServiceType("Service1");
    input.setServiceTypeDescription("ServiceDescription1");
    input.setCollectionReason("Reason1");
    input.setTaxonomyCode("Taxonomy1");

    TaxonomyCodeDTO output = mapper.map(input);

    assertEquals("Type1. Description1", output.getOrganizationTypeDescription());
    assertEquals("Type1", output.getOrganizationType());
    assertEquals("Macro1", output.getMacroAreaCode());
    assertEquals("Macro1. MacroName1", output.getMacroAreaName());
    assertEquals("MacroDescription1", output.getMacroAreaDescription());
    assertEquals("ServiceCode1", output.getServiceTypeCode());
    assertEquals("ServiceCode1. Service1", output.getServiceType());
    assertEquals("ServiceDescription1", output.getServiceTypeDescription());
    assertEquals("Reason1", output.getCollectionReason());
    assertEquals("Taxonomy1", output.getTaxonomyCode());
  }

}
