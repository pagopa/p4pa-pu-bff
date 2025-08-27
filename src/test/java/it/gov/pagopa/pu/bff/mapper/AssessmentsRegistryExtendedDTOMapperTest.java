package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.AssessmentsRegistryExtendedDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedAssessmentsRegistry;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelAssessmentsRegistry;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class AssessmentsRegistryExtendedDTOMapperTest {

  private final AssessmentsRegistryExtendedDTOMapper mapper = Mappers.getMapper(AssessmentsRegistryExtendedDTOMapper.class);
  private final PodamFactory podamFactory= TestUtils.getPodamFactory();

  @Test
  void givenPopulatedPagedModelWhenMapToPagedAssessmentsRegistryThenCorrectMapping() {
    PagedModelAssessmentsRegistry pagedModelAssessmentsRegistry = podamFactory.manufacturePojo(
            PagedModelAssessmentsRegistry.class);
    Map<String, DebtPositionTypeOrg> debtPositionTypeOrgMap = new HashMap<>();
    Map<Long,AssessmentsRegistry> assessmentsRegistries = new HashMap<>();
    for (AssessmentsRegistry assessmentsRegistry : pagedModelAssessmentsRegistry.getEmbedded().getAssessmentsRegistries()) {
      debtPositionTypeOrgMap.computeIfAbsent(assessmentsRegistry.getDebtPositionTypeOrgCode(),c-> {
          DebtPositionTypeOrg dpto = new DebtPositionTypeOrg();
          dpto.setCode(c);
          dpto.setDescription(c+"description");
          return dpto;
        }
      );
      assessmentsRegistries.put(assessmentsRegistry.getAssessmentRegistryId(),assessmentsRegistry);
    }


    PagedAssessmentsRegistry result = mapper.mapToPagedAssessmentsRegistry(
            pagedModelAssessmentsRegistry, debtPositionTypeOrgMap);

    assertNotNull(result);
    assertEquals(pagedModelAssessmentsRegistry.getPage().getNumber(),
            result.getNumber());
    assertEquals(pagedModelAssessmentsRegistry.getPage().getTotalElements(),
            result.getTotalElements());
    assertEquals(pagedModelAssessmentsRegistry.getPage().getTotalPages(),
            result.getTotalPages());
    assertEquals(pagedModelAssessmentsRegistry.getPage().getSize(),
            result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    for (AssessmentsRegistryExtendedDTO assessmentsRegistryExtendedDTO : result.getContent()) {
      TestUtils.checkNotNullFields(assessmentsRegistryExtendedDTO);
      TestUtils.reflectionEqualsByName(assessmentsRegistries.get(assessmentsRegistryExtendedDTO.getAssessmentRegistryId()),assessmentsRegistryExtendedDTO);
      assertEquals(debtPositionTypeOrgMap.get(assessmentsRegistryExtendedDTO.getDebtPositionTypeOrgCode()).getDescription(),assessmentsRegistryExtendedDTO.getDebtPositionTypeOrgDescription());
    }
  }

  @Test
  void givenEmptyDebtPositionTypeOrgMapPopulatedPagedModelWhenMapToPagedAssessmentsRegistryThenNoDebtPositionTypeOrgDescription() {
    PagedModelAssessmentsRegistry pagedModelAssessmentsRegistry = podamFactory.manufacturePojo(
            PagedModelAssessmentsRegistry.class);
    Map<Long,AssessmentsRegistry> assessmentsRegistries = pagedModelAssessmentsRegistry.getEmbedded().getAssessmentsRegistries().stream().collect(Collectors.toMap(AssessmentsRegistry::getAssessmentRegistryId, Function.identity()));

    PagedAssessmentsRegistry result = mapper.mapToPagedAssessmentsRegistry(
            pagedModelAssessmentsRegistry, Collections.emptyMap());

    assertNotNull(result);
    assertEquals(pagedModelAssessmentsRegistry.getPage().getNumber(),
            result.getNumber());
    assertEquals(pagedModelAssessmentsRegistry.getPage().getTotalElements(),
            result.getTotalElements());
    assertEquals(pagedModelAssessmentsRegistry.getPage().getTotalPages(),
            result.getTotalPages());
    assertEquals(pagedModelAssessmentsRegistry.getPage().getSize(),
            result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    for (AssessmentsRegistryExtendedDTO assessmentsRegistryExtendedDTO : result.getContent()) {
      TestUtils.checkNotNullFields(assessmentsRegistryExtendedDTO,"debtPositionTypeOrgDescription");
      TestUtils.reflectionEqualsByName(assessmentsRegistries.get(assessmentsRegistryExtendedDTO.getAssessmentRegistryId()),assessmentsRegistryExtendedDTO);
      assertNull(assessmentsRegistryExtendedDTO.getDebtPositionTypeOrgDescription());
    }
  }

  @Test
  void givenNoDebtPositionTypeOrgMapPopulatedPagedModelWhenMapToPagedAssessmentsRegistryThenCorrectMapping() {
    PagedModelAssessmentsRegistry pagedModelAssessmentsRegistry = podamFactory.manufacturePojo(
            PagedModelAssessmentsRegistry.class);
    Map<Long,AssessmentsRegistry> assessmentsRegistries = pagedModelAssessmentsRegistry.getEmbedded().getAssessmentsRegistries().stream().collect(Collectors.toMap(AssessmentsRegistry::getAssessmentRegistryId, Function.identity()));

    PagedAssessmentsRegistry result = mapper.mapToPagedAssessmentsRegistry(
            pagedModelAssessmentsRegistry, null);

    assertNotNull(result);
    assertEquals(pagedModelAssessmentsRegistry.getPage().getNumber(),
            result.getNumber());
    assertEquals(pagedModelAssessmentsRegistry.getPage().getTotalElements(),
            result.getTotalElements());
    assertEquals(pagedModelAssessmentsRegistry.getPage().getTotalPages(),
            result.getTotalPages());
    assertEquals(pagedModelAssessmentsRegistry.getPage().getSize(),
            result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    for (AssessmentsRegistryExtendedDTO assessmentsRegistryExtendedDTO : result.getContent()) {
      TestUtils.checkNotNullFields(assessmentsRegistryExtendedDTO,"debtPositionTypeOrgDescription");
      TestUtils.reflectionEqualsByName(assessmentsRegistries.get(assessmentsRegistryExtendedDTO.getAssessmentRegistryId()),assessmentsRegistryExtendedDTO);
      assertNull(assessmentsRegistryExtendedDTO.getDebtPositionTypeOrgDescription());
    }
  }

  @Test
  void givenNoContentWhenMapToPagedAssessmentsRegistryThenPartialMapping() {
    PagedModelAssessmentsRegistry pagedModelAssessmentsRegistry = podamFactory.manufacturePojo(
            PagedModelAssessmentsRegistry.class);
    pagedModelAssessmentsRegistry.getEmbedded()
            .setAssessmentsRegistries(Collections.emptyList());

    PagedAssessmentsRegistry result = mapper.mapToPagedAssessmentsRegistry(
            pagedModelAssessmentsRegistry, Collections.emptyMap());

    assertNotNull(result);
    assertEquals(pagedModelAssessmentsRegistry.getPage().getNumber(),
            result.getNumber());
    assertEquals(pagedModelAssessmentsRegistry.getPage().getTotalElements(),
            result.getTotalElements());
    assertEquals(pagedModelAssessmentsRegistry.getPage().getTotalPages(),
            result.getTotalPages());
    assertEquals(pagedModelAssessmentsRegistry.getPage().getSize(),
            result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }

  @Test
  void givenNoPageWhenMapToPagedAssessmentsRegistryThenPartialMapping() {
    PagedModelAssessmentsRegistry pagedModelAssessmentsRegistry = podamFactory.manufacturePojo(
            PagedModelAssessmentsRegistry.class);
    pagedModelAssessmentsRegistry.setPage(null);
    Map<Long,AssessmentsRegistry> assessmentsRegistries = pagedModelAssessmentsRegistry.getEmbedded().getAssessmentsRegistries().stream().collect(Collectors.toMap(AssessmentsRegistry::getAssessmentRegistryId, Function.identity()));

    PagedAssessmentsRegistry result = mapper.mapToPagedAssessmentsRegistry(
            pagedModelAssessmentsRegistry,null);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    for (AssessmentsRegistryExtendedDTO assessmentsRegistryExtendedDTO : result.getContent()) {
      TestUtils.checkNotNullFields(assessmentsRegistryExtendedDTO,"debtPositionTypeOrgDescription");
      TestUtils.reflectionEqualsByName(assessmentsRegistries.get(assessmentsRegistryExtendedDTO.getAssessmentRegistryId()),assessmentsRegistryExtendedDTO);
      assertNull(assessmentsRegistryExtendedDTO.getDebtPositionTypeOrgDescription());
    }
  }
}
