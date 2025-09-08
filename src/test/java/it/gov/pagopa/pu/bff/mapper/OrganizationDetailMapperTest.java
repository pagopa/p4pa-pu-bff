package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationDetail;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.OrganizationDetailDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class OrganizationDetailMapperTest {
  private OrganizationDetailMapperImpl mapper;

  @BeforeEach
  void setUp() {
    mapper = new OrganizationDetailMapperImpl();
  }

  @Test
  void givenValidInputWhenMapToBffDtoThenReturnOrganizationDetail() {
    OrganizationDetailDTO source = TestUtils.getPodamFactory().manufacturePojo(OrganizationDetailDTO.class);
    OrganizationDetail result = mapper.mapToBffDTO(source);

    TestUtils.reflectionEqualsByName(source, result);
    TestUtils.checkNotNullFields(result);
  }

  @Test
  void givenNullSourceWhenMapToBffDtoThenReturnNull() {
    OrganizationDetail result = mapper.mapToBffDTO(null);
    assertNull(result);
  }
}
