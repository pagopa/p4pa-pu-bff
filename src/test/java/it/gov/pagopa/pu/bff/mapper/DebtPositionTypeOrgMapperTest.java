package it.gov.pagopa.pu.bff.mapper;


import it.gov.pagopa.pu.auth.dto.generated.OperatorDTO;
import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.bff.connector.auth.AuthzService;
import it.gov.pagopa.pu.bff.dto.generated.OperatorsSelection;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.SaveDebtPositionTypeOrgDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgMapperTest {

  public static final int PAGE_MAX_SIZE = 10;
  @Mock
  private AuthzService authzServiceMock;
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  private DebtPositionTypeOrgMapper mapper;

  @BeforeEach
  void init(){
    mapper = new DebtPositionTypeOrgMapper(authzServiceMock, PAGE_MAX_SIZE);
  }

  @Test
  void givenNullSaveDebtPositionTypeOrgDTOWhenToSaveDebtPositionTypeOrgDTOThenNull() {
    String accessToken = "accessToken";

    SaveDebtPositionTypeOrgDTO result = mapper.mapToSaveDebtPositionTypeOrgDTO(null,null,null,accessToken);

    //verify
    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(authzServiceMock);
  }

  @Test
  void givenAllOperatorSelectionWhenToSaveDebtPositionTypeOrgDTOThenAllOperators() {
    String accessToken = "accessToken";
    String operatorExternalUserId = "operatorExternalUserId";
    String organizationIpaCode = "organizationIpaCode";
    it.gov.pagopa.pu.bff.dto.generated.SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrgDTO = podamFactory.manufacturePojo(it.gov.pagopa.pu.bff.dto.generated.SaveDebtPositionTypeOrgDTO.class);
    saveDebtPositionTypeOrgDTO.setOperatorsSelection(OperatorsSelection.ALL);
    OperatorsPage operatorsPage = podamFactory.manufacturePojo(OperatorsPage.class);
    Set<String> operatorsSet = operatorsPage.getContent().stream().map(
      OperatorDTO::getMappedExternalUserId).collect(Collectors.toSet());
    operatorsSet.add(operatorExternalUserId);
    Mockito.when(authzServiceMock.getOrganizationOperators(organizationIpaCode,null,null,null,0,PAGE_MAX_SIZE,accessToken))
      .thenReturn(operatorsPage);

    SaveDebtPositionTypeOrgDTO result = mapper.mapToSaveDebtPositionTypeOrgDTO(saveDebtPositionTypeOrgDTO,operatorExternalUserId,organizationIpaCode,accessToken);

    //verify
    Assertions.assertNotNull(result);
    Assertions.assertEquals(saveDebtPositionTypeOrgDTO.getDebtPositionTypeOrg(),result.getDebtPositionTypeOrg());
    Assertions.assertEquals(operatorsSet.size(),result.getEnabledOperators().size());
    for (String operator : result.getEnabledOperators()) {
      Assertions.assertTrue(operatorsSet.contains(operator));
      operatorsSet.remove(operator);
    }
    Assertions.assertNull(result.getDisabledOperators());
    Assertions.assertFalse(result.getRemoveEnabledOperators());
  }

  @Test
  void givenSelectedOperatorSelectionWhenToSaveDebtPositionTypeOrgDTOThenEnabledAndLoggedOperators() {
    String accessToken = "accessToken";
    String operatorExternalUserId = "operatorExternalUserId";
    String organizationIpaCode = "organizationIpaCode";
    it.gov.pagopa.pu.bff.dto.generated.SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrgDTO = podamFactory.manufacturePojo(it.gov.pagopa.pu.bff.dto.generated.SaveDebtPositionTypeOrgDTO.class);
    saveDebtPositionTypeOrgDTO.setOperatorsSelection(OperatorsSelection.SELECTED);

    SaveDebtPositionTypeOrgDTO result = mapper.mapToSaveDebtPositionTypeOrgDTO(saveDebtPositionTypeOrgDTO,operatorExternalUserId,organizationIpaCode,accessToken);

    //verify
    Assertions.assertNotNull(result);
    Assertions.assertEquals(saveDebtPositionTypeOrgDTO.getDebtPositionTypeOrg(),result.getDebtPositionTypeOrg());
    Assertions.assertEquals(saveDebtPositionTypeOrgDTO.getEnabledOperators(),result.getEnabledOperators());
    Assertions.assertEquals(saveDebtPositionTypeOrgDTO.getDisabledOperators(),result.getDisabledOperators());
    Assertions.assertFalse(result.getRemoveEnabledOperators());
    Mockito.verifyNoInteractions(authzServiceMock);
  }

  @Test
  void givenNoneOperatorSelectionWhenToSaveDebtPositionTypeOrgDTOThenOnlyLoggedOperator() {
    String accessToken = "accessToken";
    String operatorExternalUserId = "operatorExternalUserId";
    String organizationIpaCode = "organizationIpaCode";
    it.gov.pagopa.pu.bff.dto.generated.SaveDebtPositionTypeOrgDTO saveDebtPositionTypeOrgDTO = podamFactory.manufacturePojo(it.gov.pagopa.pu.bff.dto.generated.SaveDebtPositionTypeOrgDTO.class);
    saveDebtPositionTypeOrgDTO.setOperatorsSelection(OperatorsSelection.NONE);

    SaveDebtPositionTypeOrgDTO result = mapper.mapToSaveDebtPositionTypeOrgDTO(saveDebtPositionTypeOrgDTO,operatorExternalUserId,organizationIpaCode,accessToken);

    //verify
    Assertions.assertNotNull(result);
    Assertions.assertEquals(saveDebtPositionTypeOrgDTO.getDebtPositionTypeOrg(),result.getDebtPositionTypeOrg());
    Assertions.assertEquals(1,result.getEnabledOperators().size());
    Assertions.assertTrue(result.getEnabledOperators().contains(operatorExternalUserId));
    Assertions.assertNull(result.getDisabledOperators());
    Assertions.assertTrue(result.getRemoveEnabledOperators());
    Mockito.verifyNoInteractions(authzServiceMock);
  }

}
