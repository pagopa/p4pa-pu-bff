package it.gov.pagopa.pu.bff.mapper;

import static org.junit.jupiter.api.Assertions.*;

import it.gov.pagopa.pu.bff.dto.generated.TaxonomyMacroAreaCodeDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import org.junit.jupiter.api.Test;

class TaxonomyMacroAreaCodeMapperTest {
  private final TaxonomyMacroAreaCodeMapper mapper = new TaxonomyMacroAreaCodeMapper();

  @Test
  void testMap() {
    it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyMacroAreaCodeDTO input = new it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyMacroAreaCodeDTO();
    input.setOrganizationType("Type1");
    input.setOrganizationTypeDescription("Description1");
    input.setMacroAreaCode("Macro1");
    input.setMacroAreaName("MacroName1");
    input.setMacroAreaDescription("MacroDescription1");

    TaxonomyMacroAreaCodeDTO output = mapper.map(input);

    assertEquals("Type1. Description1", output.getOrganizationTypeDescription());
    assertEquals("Type1", output.getOrganizationType());
    assertEquals("Macro1", output.getMacroAreaCode());
    assertEquals("Macro1. MacroName1", output.getMacroAreaName());
    assertEquals("MacroDescription1", output.getMacroAreaDescription());
    TestUtils.checkNotNullFields(output);
  }
}
