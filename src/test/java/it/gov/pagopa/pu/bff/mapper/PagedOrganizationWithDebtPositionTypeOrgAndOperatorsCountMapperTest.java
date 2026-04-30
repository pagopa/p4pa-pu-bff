package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationWithDebtPositionTypeOrgAndOperatorsCount;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrganization;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapperTest {

  private static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper mapper = Mappers.getMapper(PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper.class);

  @Test
  void givenPagedModelOrganizationWhenMapThenReturnCorrectlyPopulatedDto() {
    // given
    PagedModelOrganization pagedModelOrganization = podamFactory.manufacturePojo(PagedModelOrganization.class);
    List<Organization> organizations = pagedModelOrganization.getEmbedded().getOrganizations();

    Map<Long, Integer> dptoCountsByOrgId = organizations.stream()
      .collect(Collectors.toMap(
        Organization::getOrganizationId,
        org -> 1
      ));

    Map<Long, OperatorsPage> allOperatorsPages = organizations.stream()
      .collect(Collectors.toMap(
        Organization::getOrganizationId,
        org -> podamFactory.manufacturePojo(OperatorsPage.class)
      ));

    // when
    PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount result = mapper.map(pagedModelOrganization, dptoCountsByOrgId, allOperatorsPages);

    // then
    assertNotNull(result);
    assertEquals(pagedModelOrganization.getPage().getNumber(), result.getNumber());
    assertEquals(pagedModelOrganization.getPage().getTotalElements(), result.getTotalElements());
    assertEquals(pagedModelOrganization.getPage().getTotalPages(), result.getTotalPages());
    assertEquals(pagedModelOrganization.getPage().getSize(), result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));

    List<OrganizationWithDebtPositionTypeOrgAndOperatorsCount> content = result.getContent();

    for (int i = 0; i < organizations.size(); i++) {
      Organization originalOrg = organizations.get(i);
      OrganizationWithDebtPositionTypeOrgAndOperatorsCount mappedOrg = content.get(i);

      commonCheckFields(originalOrg, mappedOrg);

      assertEquals(mapper.dptoCount(originalOrg, dptoCountsByOrgId), mappedOrg.getDebtPositionTypeOrgCount());
      assertEquals(mapper.operatorsCount(originalOrg, allOperatorsPages), mappedOrg.getOperatorsCount());
    }

    TestUtils.checkNotNullFields(result);
  }

  @Test
  void givenOrganizationWhenMapToOrganizationWithCountsThenReturnCorrectDto() {
    // given
    Organization organization = podamFactory.manufacturePojo(Organization.class);

    Map<Long, Integer> dptoCountsByOrgId = Map.of(
      organization.getOrganizationId(), 5
    );

    OperatorsPage operatorsPage = podamFactory.manufacturePojo(OperatorsPage.class);
    operatorsPage.setTotalElements(15);
    Map<Long, OperatorsPage> allOperatorsPages = Map.of(
      organization.getOrganizationId(), operatorsPage
    );

    // when
    OrganizationWithDebtPositionTypeOrgAndOperatorsCount result = mapper.mapToOrganizationWithDebtPositionTypeOrgAndOperatorsCount(organization, dptoCountsByOrgId, allOperatorsPages);

    // then
    assertNotNull(result);
    commonCheckFields(organization, result);

    assertEquals(5, result.getDebtPositionTypeOrgCount());
    assertEquals(15, result.getOperatorsCount());

    TestUtils.checkNotNullFields(result);
  }

  void commonCheckFields(Organization organization, OrganizationWithDebtPositionTypeOrgAndOperatorsCount result) {
    assertEquals(organization.getOrganizationId(), result.getOrganizationId());
    assertEquals(organization.getIpaCode(), result.getIpaCode());
    assertEquals(organization.getOrgName(), result.getOrgName());
    assertEquals(organization.getOrgFiscalCode(), result.getOrgFiscalCode());
  }

  @Test
  void givenDptoCountsWhenDptoCountThenReturnCorrectValue() {
    // given
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setOrganizationId(12345L);

    Map<Long, Integer> dptoCounts = Map.of(
      54321L, 10,
      12345L, 25
    );

    // when
    Integer result = mapper.dptoCount(organization, dptoCounts);

    // then
    assertEquals(25, result);
  }

  @Test
  void givenEmptyDptoCountsWhenDptoCountThenReturnZero() {
    // given
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setOrganizationId(12345L);

    Map<Long, Integer> dptoCounts = Collections.emptyMap();

    // when
    Integer result = mapper.dptoCount(organization, dptoCounts);

    // then
    assertEquals(0, result);
  }

  @Test
  void givenNullDptoCountsWhenDptoCountThenReturnZero() {
    // given
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setOrganizationId(12345L);

    Map<Long, Integer> dptoCounts = null;

    // when
    Integer result = mapper.dptoCount(organization, dptoCounts);

    // then
    assertNotNull(result);
    assertEquals(0,result);
  }

  @Test
  void givenOperatorsPagesWhenOperatorsCountThenReturnCorrectValue() {
    // given
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setOrganizationId(12345L);

    OperatorsPage page1 = new OperatorsPage();
    page1.setTotalElements(10);

    OperatorsPage page2 = new OperatorsPage();
    page2.setTotalElements(25);
    page2.setContent(Collections.singletonList(new OperatorDTO()));

    Map<Long, OperatorsPage> allOperators = Map.of(
      111L, page1,
      12345L, page2
    );

    // when
    Integer result = mapper.operatorsCount(organization, allOperators);

    // then
    assertEquals(25, result);
  }

  @Test
  void givenNoOperatorsPageForOrgWhenOperatorsCountThenReturnZero() {
    // given
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setOrganizationId(12345L);

    Map<Long, OperatorsPage> allOperators = Collections.emptyMap();

    // when
    Integer result = mapper.operatorsCount(organization, allOperators);

    // then
    assertEquals(0, result);
  }

  @Test
  void givenNullOperatorsPageForOrgWhenOperatorsCountThenReturnZero() {
    // given
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setOrganizationId(12345L);

    Map<Long, OperatorsPage> allOperators = null;

    // when
    Integer result = mapper.operatorsCount(organization, allOperators);

    // then
    assertNotNull(result);
    assertEquals(0,result);
  }

  @Test
  void givenOperatorsPageWithEmptyContentWhenOperatorsCountThenReturnZero() {
    // given
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setOrganizationId(12345L);

    OperatorsPage operatorsPage = new OperatorsPage();
    operatorsPage.setTotalElements(15);
    operatorsPage.setContent(Collections.emptyList());

    Map<Long, OperatorsPage> allOperators = Map.of(
      12345L, operatorsPage
    );

    // when
    Integer result = mapper.operatorsCount(organization, allOperators);

    // then
    assertEquals(0, result);
  }
}
