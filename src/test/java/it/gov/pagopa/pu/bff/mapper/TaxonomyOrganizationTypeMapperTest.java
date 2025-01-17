package it.gov.pagopa.pu.bff.mapper;

import static org.junit.jupiter.api.Assertions.*;

import it.gov.pagopa.pu.bff.dto.generated.TaxonomyOrganizationTypeDTO;
import org.junit.jupiter.api.Test;

class TaxonomyOrganizationTypeMapperTest {
  private final TaxonomyOrganizationTypeMapper mapper = new TaxonomyOrganizationTypeMapper();

  @Test
  void testMap() {
    it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyOrganizationTypeDTO input = new it.gov.pagopa.pu.p4pa_organization.dto.generated.TaxonomyOrganizationTypeDTO();
    input.setOrganizationType("Type1");
    input.setOrganizationTypeDescription("Description1");

    TaxonomyOrganizationTypeDTO output = mapper.map(input);

    assertEquals("Type1. Description1", output.getOrganizationTypeDescription());
    assertEquals("Type1", output.getOrganizationType());
  }
}
