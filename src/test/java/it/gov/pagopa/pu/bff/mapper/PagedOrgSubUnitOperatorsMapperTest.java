package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.bff.dto.generated.OrgSubUnitOperator;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrgSubUnitOperators;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.OrgSubUnitOperators;
import it.gov.pagopa.pu.organization.dto.generated.PagedModelOrgSubUnitOperators;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PagedOrgSubUnitOperatorsMapperTest {

  private static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  private final PagedOrgSubUnitOperatorsMapper mapper = new PagedOrgSubUnitOperatorsMapperImpl();


  // ---------- toOrgSubUnitOperator(OrgSubUnitOperators sourceOperator, OperatorDTO operator) ----------
  @Test
  void toOrgSubUnitOperator_shouldMapAllFields_whenSourceAndOperatorArePresent() {
    OrgSubUnitOperators sourceOperator = podamFactory.manufacturePojo(OrgSubUnitOperators.class);

    OperatorDTO operator = podamFactory.manufacturePojo(OperatorDTO.class);

    OrgSubUnitOperator result = mapper.toOrgSubUnitOperator(sourceOperator, operator);

    assertNotNull(result);
    assertEquals(sourceOperator.getOperatorExternalUserId(), result.getMappedExternalUserId());
    assertEquals(operator.getFirstName(), result.getFirstName());
    assertEquals(operator.getLastName(), result.getLastName());
    assertEquals(operator.getFiscalCode(), result.getFiscalCode());
  }

  @Test
  void toOrgSubUnitOperator_shouldLeaveFieldsNull_whenOperatorIsNull() {
    OrgSubUnitOperators sourceOperator = podamFactory.manufacturePojo(OrgSubUnitOperators.class);

    OrgSubUnitOperator result = mapper.toOrgSubUnitOperator(sourceOperator, null);

    assertNotNull(result);
    assertEquals(sourceOperator.getOperatorExternalUserId(), result.getMappedExternalUserId());
    assertNull(result.getFirstName());
    assertNull(result.getLastName());
    assertNull(result.getFiscalCode());
  }

  @Test
  void toOrgSubUnitOperator_shouldMapOperatorFields_whenSourceOperatorIsNull() {
    OperatorDTO operator = podamFactory.manufacturePojo(OperatorDTO.class);

    OrgSubUnitOperator result = mapper.toOrgSubUnitOperator(null, operator);

    assertNotNull(result);
    assertNull(result.getMappedExternalUserId());
    assertEquals(operator.getFirstName(), result.getFirstName());
    assertEquals(operator.getLastName(), result.getLastName());
    assertEquals(operator.getFiscalCode(), result.getFiscalCode());
  }

  @Test
  void toOrgSubUnitOperator_shouldReturnNull_whenBothParamsAreNull() {
    OrgSubUnitOperator result = mapper.toOrgSubUnitOperator(null, null);

    assertNull(result);
  }

  // ---------- toOrgSubUnitOperator(OrgSubUnitOperators sourceOperator, OperatorDTO operator) ----------
  @Test
  void toOrgSubUnitOperator_shouldMapFields_whenOperatorIsPresent() {
    OperatorDTO operator = podamFactory.manufacturePojo(OperatorDTO.class);

    OrgSubUnitOperator result = mapper.toOrgSubUnitOperator(operator);

    assertNotNull(result);
    assertEquals(operator.getMappedExternalUserId(), result.getMappedExternalUserId());
    assertEquals(operator.getFirstName(), result.getFirstName());
    assertEquals(operator.getLastName(), result.getLastName());
    assertEquals(operator.getFiscalCode(), result.getFiscalCode());
    TestUtils.checkNotNullFields(result);
  }

  @Test
  void toOrgSubUnitOperator_shouldReturnNull_whenOperatorIsNull() {
    OrgSubUnitOperator result = mapper.toOrgSubUnitOperator(null);

    assertNull(result);
  }

  // ---------- map(List<OrgSubUnitOperator> content,PagedModelOrgSubUnitOperators source) ----------

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

  // ---------- map(Page<OrgSubUnitOperator> page) ----------

  @Test
  void map_shouldSetContentAndMetadata_whenPageIsPresent() {
    OrgSubUnitOperator op1 = podamFactory.manufacturePojo(OrgSubUnitOperator.class);
    OrgSubUnitOperator op2 = podamFactory.manufacturePojo(OrgSubUnitOperator.class);
    List<OrgSubUnitOperator> content = List.of(op1, op2);

    Page<OrgSubUnitOperator> page = new PageImpl<>(content, PageRequest.of(1, 10), 20);

    PagedOrgSubUnitOperators result = mapper.map(page);

    assertEquals(content, result.getContent());
    assertEquals(1L, result.getNumber());
    assertEquals(10L, result.getSize());
    assertEquals(20L, result.getTotalElements());
    assertEquals(2L, result.getTotalPages());
    TestUtils.checkNotNullFields(result);
  }

  @Test
  void map_shouldSetEmptyContentAndZeros_whenPageIsNull() {
    PagedOrgSubUnitOperators result = mapper.map(null);

    assertTrue(result.getContent().isEmpty());
    assertEquals(0L, result.getNumber());
    assertEquals(0L, result.getSize());
    assertEquals(0L, result.getTotalElements());
    assertEquals(0L, result.getTotalPages());
    TestUtils.checkNotNullFields(result);
  }
}
