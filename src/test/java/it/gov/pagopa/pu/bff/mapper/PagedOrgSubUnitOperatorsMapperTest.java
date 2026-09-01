package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.OrgSubUnitOperator;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnitOperators;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitOperators;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperators;
import org.junit.jupiter.api.Test;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PagedOrgSubUnitOperatorsMapperTest {

  private static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  private final PagedOrgSubUnitOperatorsMapper mapper = new PagedOrgSubUnitOperatorsMapperImpl();


  // ---------- toOrgSubUnitOperator ----------
  @Test
  void toOrgSubUnitOperator_shouldMapAllFields_whenSourceAndUserInfoArePresent() {
    OrgSubUnitOperators sourceOperator = podamFactory.manufacturePojo(OrgSubUnitOperators.class);
    UserInfo userInfo = podamFactory.manufacturePojo(UserInfo.class);

    OrgSubUnitOperator result = mapper.toOrgSubUnitOperator(sourceOperator, userInfo);

    assertNotNull(result);
    assertEquals(sourceOperator.getOperatorExternalUserId(), result.getMappedExternalUserId());
    assertEquals(userInfo.getName(), result.getFirstName());
    assertEquals(userInfo.getFamilyName(), result.getLastName());
    assertEquals(userInfo.getFiscalCode(), result.getFiscalCode());
  }

  @Test
  void toOrgSubUnitOperator_shouldLeaveUserFieldsNull_whenUserInfoIsNull() {
    OrgSubUnitOperators sourceOperator = podamFactory.manufacturePojo(OrgSubUnitOperators.class);

    OrgSubUnitOperator result = mapper.toOrgSubUnitOperator(sourceOperator, null);

    assertNotNull(result);
    assertEquals(sourceOperator.getOperatorExternalUserId(), result.getMappedExternalUserId());
    assertNull(result.getFirstName());
    assertNull(result.getLastName());
    assertNull(result.getFiscalCode());
  }

  @Test
  void toOrgSubUnitOperator_shouldLeaveMappedExternalUserIdNull_whenSourceOperatorIsNull() {
    UserInfo userInfo = podamFactory.manufacturePojo(UserInfo.class);

    OrgSubUnitOperator result = mapper.toOrgSubUnitOperator(null, userInfo);

    assertNotNull(result);
    assertNull(result.getMappedExternalUserId());
    assertEquals(userInfo.getName(), result.getFirstName());
    assertEquals(userInfo.getFamilyName(), result.getLastName());
    assertEquals(userInfo.getFiscalCode(), result.getFiscalCode());
  }

  @Test
  void toOrgSubUnitOperator_shouldReturnObjectWithAllNullFields_whenBothParamsAreNull() {
    OrgSubUnitOperator result = mapper.toOrgSubUnitOperator(null, null);

    assertNull(result);
  }

  // ---------- map ----------

  @Test
  void map_shouldReturnEmptyContentAndNullMetadata_whenSourceIsNull() {
    OrgSubUnitOperator orgSubUnitOperator = podamFactory.manufacturePojo(OrgSubUnitOperator.class);

    PagedOrgSubUnitOperators result = mapper.map(List.of(orgSubUnitOperator), null);

    assertNotNull(result);
    assertTrue(result.getContent().isEmpty());
    assertNull(result.getSize());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getNumber());
  }

  @Test
  void map_shouldSetEmptyContent_whenContentIsNull() {
    PagedModelOrgSubUnitOperators source = podamFactory.manufacturePojo(PagedModelOrgSubUnitOperators.class);

    PagedOrgSubUnitOperators result = mapper.map(null, source);

    assertTrue(result.getContent().isEmpty());
  }

  @Test
  void map_shouldSetEmptyContent_whenContentIsEmptyList() {
    PagedModelOrgSubUnitOperators source = podamFactory.manufacturePojo(PagedModelOrgSubUnitOperators.class);

    PagedOrgSubUnitOperators result = mapper.map(Collections.emptyList(), source);

    assertTrue(result.getContent().isEmpty());
  }

  @Test
  void map_shouldLeavePageMetadataNull_whenPageIsNull() {
    PagedModelOrgSubUnitOperators source = podamFactory.manufacturePojo(PagedModelOrgSubUnitOperators.class);
    source.setPage(null);

    OrgSubUnitOperator orgSubUnitOperator = podamFactory.manufacturePojo(OrgSubUnitOperator.class);
    List<OrgSubUnitOperator> content = List.of(orgSubUnitOperator);

    PagedOrgSubUnitOperators result = mapper.map(content, source);

    assertEquals(content, result.getContent());
    assertNull(result.getSize());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getNumber());
  }

  @Test
  void map_shouldSetContentAndPageMetadata_whenSourceAndPageArePresent() {
    PagedModelOrgSubUnitOperators source = podamFactory.manufacturePojo(PagedModelOrgSubUnitOperators.class);

    OrgSubUnitOperator orgSubUnitOperator = podamFactory.manufacturePojo(OrgSubUnitOperator.class);
    List<OrgSubUnitOperator> content = List.of(orgSubUnitOperator);

    PagedOrgSubUnitOperators result = mapper.map(content, source);

    assertEquals(content, result.getContent());
    assertEquals(source.getPage().getSize(), result.getSize());
    assertEquals(source.getPage().getTotalElements(), result.getTotalElements());
    assertEquals(source.getPage().getTotalPages(), result.getTotalPages());
    assertEquals(source.getPage().getNumber(), result.getNumber());
  }
}
