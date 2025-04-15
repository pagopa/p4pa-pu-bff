package it.gov.pagopa.pu.bff.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.gov.pagopa.pu.auth.dto.generated.OperatorsPage;
import it.gov.pagopa.pu.bff.dto.generated.DebtPositionTypeOrgOperatorDTO;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeOrgOperatorDTO;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgOperators;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgOperators;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrgOperatorsEmbedded;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgOperatorsMapperTest {

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  private final DebtPositionTypeOrgOperatorsMapper mapper = new DebtPositionTypeOrgOperatorsMapper();

  @Test
  void givenOperatorsAndDebtPositionTypeOrgOperatorsWhenMapToPagedDebtPositionTypeOrgOperatorDTOThenCorrectMapping() {
    OperatorsPage operators = podamFactory.manufacturePojo(OperatorsPage.class);
    DebtPositionTypeOrgOperators debtPositionTypeOrgOperator = podamFactory.manufacturePojo(DebtPositionTypeOrgOperators.class);
    debtPositionTypeOrgOperator.setOperatorExternalUserId(operators.getContent().getFirst().getOperatorId());
    CollectionModelDebtPositionTypeOrgOperators debtPositionTypeOrgOperators = CollectionModelDebtPositionTypeOrgOperators.builder()
      .embedded(PagedModelDebtPositionTypeOrgOperatorsEmbedded.builder()
        .debtPositionTypeOrgOperatorses(List.of(debtPositionTypeOrgOperator))
        .build())
      .build();

    DebtPositionTypeOrgOperatorDTO expectedItem = DebtPositionTypeOrgOperatorDTO.builder()
      .operatorId(operators.getContent().getFirst().getOperatorId())
      .firstName(operators.getContent().getFirst().getFirstName())
      .lastName(operators.getContent().getFirst().getLastName())
      .mappedExternalUserId(operators.getContent().getFirst().getMappedExternalUserId())
      .enabled(true)
      .build();

    // when
    PagedDebtPositionTypeOrgOperatorDTO result = mapper.mapToPagedDebtPositionTypeOrgOperatorDTO(operators, debtPositionTypeOrgOperators);

    // then
    assertNotNull(result);
    assertFalse(result.getContent().isEmpty());
    assertEquals(expectedItem, result.getContent().getFirst());
    assertEquals(operators.getPageSize().longValue(), result.getSize());
    assertEquals(operators.getTotalPages().longValue(), result.getTotalPages());
    assertEquals(operators.getTotalElements().longValue(), result.getTotalElements());
    assertEquals(operators.getPageNo().longValue(), result.getNumber());
  }

  @Test
  void givenDebtPositionTypeOrgOperatorsEmptyWhenMapToPagedDebtPositionTypeOrgOperatorDTOThenReturnEmptyContent() {
    OperatorsPage operators = podamFactory.manufacturePojo(OperatorsPage.class);
    CollectionModelDebtPositionTypeOrgOperators debtPositionTypeOrgOperators = CollectionModelDebtPositionTypeOrgOperators.builder()
      .embedded(null)
      .build();

    // when
    PagedDebtPositionTypeOrgOperatorDTO result = mapper.mapToPagedDebtPositionTypeOrgOperatorDTO(operators, debtPositionTypeOrgOperators);

    // then
    assertNotNull(result);
    assertTrue(result.getContent().isEmpty());
    assertEquals(operators.getPageSize().longValue(), result.getSize());
    assertEquals(operators.getTotalPages().longValue(), result.getTotalPages());
    assertEquals(operators.getTotalElements().longValue(), result.getTotalElements());
    assertEquals(operators.getPageNo().longValue(), result.getNumber());
  }
}
