package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.bff.dto.generated.OperatorRole;
import it.gov.pagopa.pu.bff.dto.generated.OperatorsDetail;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgDTO;
import it.gov.pagopa.pu.bff.exception.InvalidOperatorRoleException;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrg;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OperatorDetailMapperTest {

  private PodamFactory podamFactory =  TestUtils.getPodamFactory();
  private PagedDebtPositionTypeOrgDTOMapper pagedDebtPositionTypeOrgDTOMapperMock = Mockito.mock(PagedDebtPositionTypeOrgDTOMapper.class);
  private OperatorDetailMapper mapper = Mappers.getMapper(OperatorDetailMapper.class);

  @Test
  void givenPagedModelDebtPositionTypeOrgAndOperatorDTOWhenMapThenReturnOperatorsDetail() {
    //given
    PagedModelDebtPositionTypeOrg pagedModelDebtPositionTypeOrg = podamFactory.manufacturePojo(PagedModelDebtPositionTypeOrg.class);
    PagedDebtPositionTypeOrgDTO pagedDebtPositionTypeOrg = podamFactory.manufacturePojo(PagedDebtPositionTypeOrgDTO.class);
    OperatorDTO operatorDTO = podamFactory.manufacturePojo(OperatorDTO.class);
    operatorDTO.setRoles(List.of("ROLE_ADMIN"));
    try (MockedStatic<Mappers> mappersMockedStatic = Mockito.mockStatic(Mappers.class)) {
      Mockito.when(pagedDebtPositionTypeOrgDTOMapperMock.map(pagedModelDebtPositionTypeOrg,Collections.emptyMap())).thenReturn(pagedDebtPositionTypeOrg);
      mappersMockedStatic
              .when(() -> Mappers.getMapper(PagedDebtPositionTypeOrgDTOMapper.class))
              .thenAnswer(a -> pagedDebtPositionTypeOrgDTOMapperMock);
      //when
      OperatorsDetail result = mapper.map(pagedModelDebtPositionTypeOrg, operatorDTO, Collections.emptyMap());
      //then
      Assertions.assertNotNull(result);
      Assertions.assertEquals(operatorDTO.getOperatorId(), result.getOperatorId());
      Assertions.assertEquals(operatorDTO.getFiscalCode(), result.getOperatorFiscalCode());
      Assertions.assertEquals(OperatorRole.ROLE_ADMIN, result.getOperatorRole());
      Assertions.assertEquals(pagedDebtPositionTypeOrg, result.getPagedDebtPositionTypeOrg());
      Assertions.assertEquals(operatorDTO.getEmail(), result.getOperatorEmail());

      TestUtils.checkNotNullFields(result);
      mappersMockedStatic.verify(() -> Mappers.getMapper(PagedDebtPositionTypeOrgDTOMapper.class));
    }
  }

  @Test
  void testMap_InvalidRole() {
    PagedModelDebtPositionTypeOrg pagedModelDebtPositionTypeOrg = podamFactory.manufacturePojo(PagedModelDebtPositionTypeOrg.class);
    OperatorDTO operatorDTO = podamFactory.manufacturePojo(OperatorDTO.class);
    List<String> roles = Collections.singletonList("INVALID_ROLE");
    operatorDTO.setRoles(roles);
    Map<Long, DebtPositionType> debtPositionTypes = new HashMap<>();

    Exception exception = assertThrows(InvalidOperatorRoleException.class, () ->
      mapper.map(pagedModelDebtPositionTypeOrg, operatorDTO, debtPositionTypes));

    assertEquals("INVALID_OPERATOR_ROLE: INVALID_ROLE", exception.getMessage());
  }

  @Test
  void testMap_EmptyRoles() {
    PagedModelDebtPositionTypeOrg pagedModelDebtPositionTypeOrg = podamFactory.manufacturePojo(PagedModelDebtPositionTypeOrg.class);
    OperatorDTO operatorDTO = podamFactory.manufacturePojo(OperatorDTO.class);
    List<String> roles = Collections.emptyList();
    operatorDTO.setRoles(roles);

    OperatorsDetail result = mapper.map(pagedModelDebtPositionTypeOrg, operatorDTO, Collections.emptyMap());

    assertNull(result.getOperatorRole());
  }
}
