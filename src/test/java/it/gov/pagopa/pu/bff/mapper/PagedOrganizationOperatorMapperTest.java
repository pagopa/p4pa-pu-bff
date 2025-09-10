package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.bff.dto.generated.OrganizationOperator;
import it.gov.pagopa.pu.bff.dto.generated.PagedOrganizationOperator;
import it.gov.pagopa.pu.bff.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class PagedOrganizationOperatorMapperTest {

  private static final PodamFactory podamFactory = TestUtils.getPodamFactory();
  PagedOrganizationOperatorMapper mapper = Mappers.getMapper(PagedOrganizationOperatorMapper.class);

  @Test
  void givenPopulatedOperatorDptoCountWhenMapToPagedPagoPaRegistryThenCorrectMapping() {
    OperatorsPage operatorsPage = podamFactory.manufacturePojo(
            OperatorsPage.class);
    Map<String,OperatorDTO> operatorMap = operatorsPage.getContent().stream().collect(Collectors.toMap(OperatorDTO::getMappedExternalUserId, Function.identity()));
    Map<String, Long> operatorDptoCount = operatorsPage.getContent().stream().collect(Collectors.toMap(OperatorDTO::getMappedExternalUserId, o -> new Random().nextLong(101)));

    PagedOrganizationOperator result = mapper.mapToPagedOrganizationOperator(
            operatorsPage, operatorDptoCount);

    assertNotNull(result);
    assertEquals(operatorsPage.getPageNo(),
            result.getNumber().intValue());
    assertEquals(operatorsPage.getTotalElements(),
            result.getTotalElements().intValue());
    assertEquals(operatorsPage.getTotalPages(),
            result.getTotalPages().intValue());
    assertEquals(operatorsPage.getPageSize(),
            result.getSize().intValue());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(
            operatorsPage.getContent().size(),
            result.getContent().size());
    for (OrganizationOperator operator : result.getContent()){
      TestUtils.checkNotNullFields(operator);
      String mappedExternalUserId = operator.getMappedExternalUserId();
      assertTrue(operatorMap.containsKey(mappedExternalUserId));
      assertTrue(operatorDptoCount.containsKey(mappedExternalUserId));
      TestUtils.reflectionEqualsByName(operator,operatorMap.get(mappedExternalUserId));
      Assertions.assertEquals(operatorDptoCount.get(mappedExternalUserId),operator.getDebtPositionTypeOrgCount());
    }
  }

  @Test
  void givenEmptyOperatorDptoCountWhenMapToPagedPagoPaRegistryThenCorrectMappingWithZeroDptoCount() {
    OperatorsPage operatorsPage = podamFactory.manufacturePojo(
            OperatorsPage.class);
    OperatorDTO operator = podamFactory.manufacturePojo(
            OperatorDTO.class);
    operatorsPage.setContent(List.of(operator));

    PagedOrganizationOperator result = mapper.mapToPagedOrganizationOperator(
            operatorsPage, Collections.emptyMap());

    assertNotNull(result);
    assertEquals(operatorsPage.getPageNo(),
            result.getNumber().intValue());
    assertEquals(operatorsPage.getTotalElements(),
            result.getTotalElements().intValue());
    assertEquals(operatorsPage.getTotalPages(),
            result.getTotalPages().intValue());
    assertEquals(operatorsPage.getPageSize(),
            result.getSize().intValue());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(
            operatorsPage.getContent().size(),
            result.getContent().size());
    OrganizationOperator resultOperator = result.getContent().getFirst();
    TestUtils.checkNotNullFields(resultOperator);
    TestUtils.reflectionEqualsByName(operator, resultOperator);
    Assertions.assertEquals(0L, resultOperator.getDebtPositionTypeOrgCount());
  }

  @Test
  void givenNullOperatorDptoCountWhenMapToPagedPagoPaRegistryThenCorrectMappingWithZeroDptoCount() {
    OperatorsPage operatorsPage = podamFactory.manufacturePojo(
            OperatorsPage.class);
    OperatorDTO operator = podamFactory.manufacturePojo(
            OperatorDTO.class);
    operatorsPage.setContent(List.of(operator));

    PagedOrganizationOperator result = mapper.mapToPagedOrganizationOperator(
            operatorsPage, null);

    assertNotNull(result);
    assertEquals(operatorsPage.getPageNo(),
            result.getNumber().intValue());
    assertEquals(operatorsPage.getTotalElements(),
            result.getTotalElements().intValue());
    assertEquals(operatorsPage.getTotalPages(),
            result.getTotalPages().intValue());
    assertEquals(operatorsPage.getPageSize(),
            result.getSize().intValue());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(
            operatorsPage.getContent().size(),
            result.getContent().size());
    OrganizationOperator resultOperator = result.getContent().getFirst();
    TestUtils.checkNotNullFields(resultOperator);
    TestUtils.reflectionEqualsByName(operator, resultOperator);
    Assertions.assertEquals(0L, resultOperator.getDebtPositionTypeOrgCount());
  }
}
