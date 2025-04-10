package it.gov.pagopa.pu.bff.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationWithDebtPositionTypeOrgCount;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgCount;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.PageMetadata;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class OrganizationWithDebtPositionTypeOrgCountMapperTest {

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  private final OrganizationWithDebtPositionTypeOrgCountMapper mapper = new OrganizationWithDebtPositionTypeOrgCountMapper();

  @Test
  void testMapToPagedOrganizationWithDebtPositionTypeOrgCount() {
    // given
    Organization org1 = podamFactory.manufacturePojo(Organization.class);
    Organization org2 = podamFactory.manufacturePojo(Organization.class);
    List<Organization> organizations = List.of(org1, org2);

    DebtPositionTypeOrgCountByOrganizationId dpto1 = DebtPositionTypeOrgCountByOrganizationId.builder()
      .organizationId(org1.getOrganizationId()).activeOrganizations(3).build();
    DebtPositionTypeOrgCountByOrganizationId dpto2 = DebtPositionTypeOrgCountByOrganizationId.builder()
      .organizationId(org2.getOrganizationId()).activeOrganizations(10).build();
    List<DebtPositionTypeOrgCountByOrganizationId> dptos = List.of(dpto1, dpto2);

    PageMetadata pageMetadata = new PageMetadata(2L, 2L, 1L, 1L);

    OrganizationWithDebtPositionTypeOrgCount expectedItem1 = OrganizationWithDebtPositionTypeOrgCount.builder()
      .organizationId(org1.getOrganizationId())
      .organizationName(org1.getOrgName())
      .ipaCode(org1.getIpaCode())
      .debtPositionTypeOrgCount(dpto1.getActiveOrganizations())
      .build();
    OrganizationWithDebtPositionTypeOrgCount expectedItem2 = OrganizationWithDebtPositionTypeOrgCount.builder()
      .organizationId(org2.getOrganizationId())
      .organizationName(org2.getOrgName())
      .ipaCode(org2.getIpaCode())
      .debtPositionTypeOrgCount(dpto2.getActiveOrganizations())
      .build();
    PagedOrganizationWithDebtPositionTypeOrgCount expectedResult = PagedOrganizationWithDebtPositionTypeOrgCount.builder()
      .content(List.of(expectedItem1, expectedItem2))
      .size(pageMetadata.getSize())
      .totalPages(pageMetadata.getTotalPages())
      .totalElements(pageMetadata.getTotalElements())
      .number(pageMetadata.getNumber())
      .build();

    // when
    PagedOrganizationWithDebtPositionTypeOrgCount result = mapper.mapToPagedOrganizationWithDebtPositionTypeOrgCount(organizations, dptos, pageMetadata);

    // then
    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

}
