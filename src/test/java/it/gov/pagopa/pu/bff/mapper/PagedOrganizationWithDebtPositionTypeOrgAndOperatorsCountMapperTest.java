package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCount;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgCountByOrganizationId;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrganization;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import it.gov.pagopa.pu.bff.dto.generated.OrganizationWithDebtPositionTypeOrgAndOperatorsCount;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;

import java.util.ArrayList;
import java.util.stream.IntStream;

class PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapperTest {

  private static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper mapper = Mappers.getMapper(PagedOrganizationWithDebtPositionTypeOrgAndOperatorsCountMapper.class);

  @Test
  void givenPagedModelOrganizationWhenMapThenReturnCorrectlyPopulatedDto() {
    // given
    PagedModelOrganization pagedModelOrganization = podamFactory.manufacturePojo(PagedModelOrganization.class);
    List<Organization> organizations = pagedModelOrganization.getEmbedded().getOrganizations();

    List<DebtPositionTypeOrgCountByOrganizationId> dptoCountsByOrgId = new ArrayList<>();
    List<OperatorsPage> allOperatorsPages = new ArrayList<>();

    IntStream.range(0, organizations.size()).forEach(i -> {
      Organization org = organizations.get(i);

      DebtPositionTypeOrgCountByOrganizationId dptoCount = podamFactory.manufacturePojo(DebtPositionTypeOrgCountByOrganizationId.class);
      dptoCount.setOrganizationId(org.getOrganizationId());
      dptoCount.setActiveOrganizations(i + 1);
      dptoCountsByOrgId.add(dptoCount);

      OperatorsPage operatorsPage = podamFactory.manufacturePojo(OperatorsPage.class);
      OperatorDTO operatorDTO = podamFactory.manufacturePojo(OperatorDTO.class);
      operatorDTO.setOrganizationIpaCode(org.getIpaCode());

      operatorsPage.setContent(Collections.singletonList(operatorDTO));
      operatorsPage.setTotalElements((i + 10));
      allOperatorsPages.add(operatorsPage);
    });

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

      assertEquals(Integer.valueOf(i + 1), mappedOrg.getDebtPositionTypeOrgCount());
      assertEquals(1, mappedOrg.getOperatorsCount());
    }

    TestUtils.checkNotNullFields(result);
  }

  @Test
  void givenOrganizationWhenMapToOrganizationWithCountsThenReturnCorrectDto() {
    // given
    Organization organization = podamFactory.manufacturePojo(Organization.class);

    List<DebtPositionTypeOrgCountByOrganizationId> dptoCountsByOrgId = new ArrayList<>();
    DebtPositionTypeOrgCountByOrganizationId dptoCount = podamFactory.manufacturePojo(DebtPositionTypeOrgCountByOrganizationId.class);
    dptoCount.setOrganizationId(organization.getOrganizationId());
    dptoCount.setActiveOrganizations(5);
    dptoCountsByOrgId.add(dptoCount);

    List<OperatorsPage> allOperatorsPages = new ArrayList<>();
    OperatorsPage operatorsPage = podamFactory.manufacturePojo(OperatorsPage.class);
    OperatorDTO operatorDTO = podamFactory.manufacturePojo(OperatorDTO.class);
    operatorDTO.setOrganizationIpaCode(organization.getIpaCode());

    operatorsPage.setContent(Collections.singletonList(operatorDTO));
    operatorsPage.setTotalElements(15);
    allOperatorsPages.add(operatorsPage);

    // when
    OrganizationWithDebtPositionTypeOrgAndOperatorsCount result = mapper.mapToOrganizationWithDebtPositionTypeOrgAndOperatorsCount(organization, dptoCountsByOrgId, allOperatorsPages);

    // then
    assertNotNull(result);
    commonCheckFields(organization, result);

    TestUtils.checkNotNullFields(result);

    assertEquals(5, result.getDebtPositionTypeOrgCount());
    assertEquals(1, result.getOperatorsCount());
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

    List<DebtPositionTypeOrgCountByOrganizationId> dptoCounts = new ArrayList<>();
    DebtPositionTypeOrgCountByOrganizationId count1 = new DebtPositionTypeOrgCountByOrganizationId();
    count1.setOrganizationId(54321L);
    count1.setActiveOrganizations(10);

    DebtPositionTypeOrgCountByOrganizationId count2 = new DebtPositionTypeOrgCountByOrganizationId();
    count2.setOrganizationId(12345L);
    count2.setActiveOrganizations(25);

    dptoCounts.add(count1);
    dptoCounts.add(count2);

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

    List<DebtPositionTypeOrgCountByOrganizationId> dptoCounts = Collections.emptyList();

    // when
    Integer result = mapper.dptoCount(organization, dptoCounts);

    // then
    assertEquals(0, result);
  }

  @Test
  void givenDptoCountsWithNullOrganizationIdWhenDptoCountThenReturnZero() {
    // given
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setOrganizationId(12345L);

    DebtPositionTypeOrgCountByOrganizationId debtPositionTypeOrgCountByOrganizationId = podamFactory.manufacturePojo(DebtPositionTypeOrgCountByOrganizationId.class);
    debtPositionTypeOrgCountByOrganizationId.setOrganizationId(null);
    List<DebtPositionTypeOrgCountByOrganizationId> dptoCounts = List.of(debtPositionTypeOrgCountByOrganizationId);

    // when
    Integer result = mapper.dptoCount(organization, dptoCounts);

    // then
    assertEquals(0, result);
  }

  @Test
  void givenDptoCountsWithNullActiveOrganizationsWhenDptoCountThenReturnZero() {
    // given
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setOrganizationId(12345L);

    DebtPositionTypeOrgCountByOrganizationId debtPositionTypeOrgCountByOrganizationId = podamFactory.manufacturePojo(DebtPositionTypeOrgCountByOrganizationId.class);
    debtPositionTypeOrgCountByOrganizationId.setActiveOrganizations(null);
    List<DebtPositionTypeOrgCountByOrganizationId> dptoCounts = List.of(debtPositionTypeOrgCountByOrganizationId);

    // when
    Integer result = mapper.dptoCount(organization, dptoCounts);

    // then
    assertEquals(0, result);
  }

  @Test
  void givenOperatorsPagesWhenOperatorsCountThenReturnCorrectValue() {
    // given
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setIpaCode("IPA_CODE_TEST");

    Organization organization2 = podamFactory.manufacturePojo(Organization.class);
    organization2.setIpaCode("OTHER_IPA");

    List<OperatorsPage> allOperators = new ArrayList<>();

    OperatorsPage page1 = new OperatorsPage();
    OperatorDTO operator1 = new OperatorDTO();
    operator1.setOrganizationIpaCode("OTHER_IPA");
    page1.setContent(Collections.singletonList(operator1));

    OperatorsPage page2 = new OperatorsPage();
    OperatorDTO operator2 = new OperatorDTO();
    operator2.setOrganizationIpaCode("IPA_CODE_TEST");
    OperatorDTO operator3 = new OperatorDTO();
    operator3.setOrganizationIpaCode("IPA_CODE_TEST");
    OperatorDTO operator4 = new OperatorDTO();
    operator4.setOrganizationIpaCode("IPA_CODE_TEST");
    page2.setContent(List.of(operator2, operator3, operator4));

    allOperators.add(page1);
    allOperators.add(page2);

    // when
    Integer result1 = mapper.operatorsCount(organization, allOperators);
    Integer result2 = mapper.operatorsCount(organization2, allOperators);

    // then
    assertEquals(3, result1);
    assertEquals(1, result2);
  }

  @Test
  void givenEmptyOperatorsPagesWhenOperatorsCountThenReturnZero() {
    // given
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setIpaCode("IPA_CODE_TEST");

    List<OperatorsPage> allOperators = Collections.emptyList();

    // when
    Integer result = mapper.operatorsCount(organization, allOperators);

    // then
    assertEquals(0, result);
  }

  @Test
  void givenOperatorsPagesWithEmptyContentWhenOperatorsCountThenReturnZero() {
    // given
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setIpaCode("IPA_CODE_TEST");

    List<OperatorsPage> allOperators = new ArrayList<>();
    OperatorsPage emptyPage = new OperatorsPage();
    emptyPage.setContent(Collections.emptyList());

    allOperators.add(emptyPage);

    // when
    Integer result = mapper.operatorsCount(organization, allOperators);

    // then
    assertEquals(0, result);
  }

  @Test
  void givenNullOperatorsPagesWhenOperatorsCountThenReturnZero() {
    // given
    Organization organization = podamFactory.manufacturePojo(Organization.class);
    organization.setIpaCode("IPA_CODE_TEST");

    List<OperatorsPage> allOperators = new ArrayList<>();
    OperatorsPage nullPage = null;

    allOperators.add(nullPage);

    // when
    Integer result = mapper.operatorsCount(organization, allOperators);

    // then
    assertEquals(0, result);
  }
}
