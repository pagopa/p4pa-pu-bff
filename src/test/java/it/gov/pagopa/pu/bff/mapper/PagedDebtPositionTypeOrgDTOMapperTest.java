package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PageMetadata;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrg;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

class PagedDebtPositionTypeOrgDTOMapperTest {

  private final PodamFactory podamFactory =  TestUtils.getPodamFactory();
  private PagedDebtPositionTypeOrgDTOMapper mapper = Mappers.getMapper(PagedDebtPositionTypeOrgDTOMapper.class);
  private DebtPositionTypeOrgDTOMapper debtPositionTypeOrgDTOMapperMock = Mockito.mock(DebtPositionTypeOrgDTOMapper.class);

  @Test
  void givenEmptyDebtPositionTypesPagedModelDebtPositionTypeOrgWhenMapThenReturnPagedOperatorsDetails() {
    //given
    PagedModelDebtPositionTypeOrg pagedModelDebtPositionTypeOrg = podamFactory.manufacturePojo(PagedModelDebtPositionTypeOrg.class);
    DebtPositionTypeOrg dpto = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);
    DebtPositionTypeOrgDTO expectedDpto = podamFactory.manufacturePojo(DebtPositionTypeOrgDTO.class);
    pagedModelDebtPositionTypeOrg.getEmbedded().setDebtPositionTypeOrgs(List.of(dpto));
    //when
    try (MockedStatic<Mappers> mappersMockedStatic = Mockito.mockStatic(Mappers.class)) {
      Mockito.when(debtPositionTypeOrgDTOMapperMock.map(dpto, null, null, null, null, Collections.emptyList()))
              .thenReturn(expectedDpto);
      mappersMockedStatic
              .when(() -> Mappers.getMapper(DebtPositionTypeOrgDTOMapper.class))
              .thenAnswer(a -> debtPositionTypeOrgDTOMapperMock);

      PagedDebtPositionTypeOrgDTO result = mapper.map(pagedModelDebtPositionTypeOrg, Collections.emptyMap());
      //then
      Assertions.assertNotNull(result);
      Assertions.assertNotNull(result.getContent());
      PageMetadata page = pagedModelDebtPositionTypeOrg.getPage();
      Assertions.assertEquals(page.getSize(), result.getSize());
      Assertions.assertEquals(page.getTotalPages(), result.getTotalPages());
      Assertions.assertEquals(page.getTotalElements(), result.getTotalElements());
      Assertions.assertEquals(page.getNumber(), result.getNumber());
      Assertions.assertEquals(1,result.getContent().size());
      Assertions.assertEquals(expectedDpto,result.getContent().getFirst());
    }
  }

  @Test
  void givenNullDebtPositionTypesPagedModelDebtPositionTypeOrgWhenMapThenReturnPagedOperatorsDetails() {
    //given
    PagedModelDebtPositionTypeOrg pagedModelDebtPositionTypeOrg = podamFactory.manufacturePojo(PagedModelDebtPositionTypeOrg.class);
    DebtPositionTypeOrg dpto = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);
    DebtPositionTypeOrgDTO expectedDpto = podamFactory.manufacturePojo(DebtPositionTypeOrgDTO.class);
    pagedModelDebtPositionTypeOrg.getEmbedded().setDebtPositionTypeOrgs(List.of(dpto));
    //when
    try (MockedStatic<Mappers> mappersMockedStatic = Mockito.mockStatic(Mappers.class)) {
      Mockito.when(debtPositionTypeOrgDTOMapperMock.map(dpto, null, null, null, null, Collections.emptyList()))
              .thenReturn(expectedDpto);
      mappersMockedStatic
              .when(() -> Mappers.getMapper(DebtPositionTypeOrgDTOMapper.class))
              .thenAnswer(a -> debtPositionTypeOrgDTOMapperMock);

      PagedDebtPositionTypeOrgDTO result = mapper.map(pagedModelDebtPositionTypeOrg, null);
      //then
      Assertions.assertNotNull(result);
      Assertions.assertNotNull(result.getContent());
      PageMetadata page = pagedModelDebtPositionTypeOrg.getPage();
      Assertions.assertEquals(page.getSize(), result.getSize());
      Assertions.assertEquals(page.getTotalPages(), result.getTotalPages());
      Assertions.assertEquals(page.getTotalElements(), result.getTotalElements());
      Assertions.assertEquals(page.getNumber(), result.getNumber());
      Assertions.assertEquals(1, result.getContent().size());
      Assertions.assertEquals(expectedDpto, result.getContent().getFirst());
    }
  }

  @Test
  void givenPopulatedDebtPositionTypesPagedModelDebtPositionTypeOrgWhenMapThenReturnFullPagedOperatorsDetails() {
    //given
    PagedModelDebtPositionTypeOrg pagedModelDebtPositionTypeOrg = podamFactory.manufacturePojo(PagedModelDebtPositionTypeOrg.class);
    DebtPositionType dpt = podamFactory.manufacturePojo(DebtPositionType.class);
    DebtPositionTypeOrg dpto = podamFactory.manufacturePojo(DebtPositionTypeOrg.class);
    dpto.setDebtPositionTypeId(dpt.getDebtPositionTypeId());
    DebtPositionTypeOrgDTO expectedDpto = podamFactory.manufacturePojo(DebtPositionTypeOrgDTO.class);
    pagedModelDebtPositionTypeOrg.getEmbedded().setDebtPositionTypeOrgs(List.of(dpto));
    //when
    try (MockedStatic<Mappers> mappersMockedStatic = Mockito.mockStatic(Mappers.class)) {
      Mockito.when(debtPositionTypeOrgDTOMapperMock.map(dpto, dpt, null, null, null, Collections.emptyList()))
              .thenReturn(expectedDpto);
      mappersMockedStatic
              .when(() -> Mappers.getMapper(DebtPositionTypeOrgDTOMapper.class))
              .thenAnswer(a -> debtPositionTypeOrgDTOMapperMock);

      PagedDebtPositionTypeOrgDTO result = mapper.map(pagedModelDebtPositionTypeOrg, Map.of(dpt.getDebtPositionTypeId(),dpt));
      //then
      Assertions.assertNotNull(result);
      Assertions.assertNotNull(result.getContent());
      PageMetadata page = pagedModelDebtPositionTypeOrg.getPage();
      Assertions.assertEquals(page.getSize(), result.getSize());
      Assertions.assertEquals(page.getTotalPages(), result.getTotalPages());
      Assertions.assertEquals(page.getTotalElements(), result.getTotalElements());
      Assertions.assertEquals(page.getNumber(), result.getNumber());
      Assertions.assertEquals(1, result.getContent().size());
      Assertions.assertEquals(expectedDpto, result.getContent().getFirst());
    }
  }
}
