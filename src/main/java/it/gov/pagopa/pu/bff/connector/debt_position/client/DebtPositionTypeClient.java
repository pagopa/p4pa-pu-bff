package it.gov.pagopa.pu.bff.connector.debt_position.client;

import it.gov.pagopa.pu.bff.connector.debt_position.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.bff.exception.ResourceNotFoundException;
import it.gov.pagopa.pu.bff.util.PageUtils;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionType;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeRequestBody;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeWithCount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class DebtPositionTypeClient {

  private final DebtPositionApisHolder debtPositionApisHolder;

  public DebtPositionTypeClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public DebtPositionType getDebtPositionTypeById(Long id, String accessToken) {
    try {
      return debtPositionApisHolder.getDebtPositionTypeControllerApi(accessToken)
        .crudGetDebtpositiontype(String.valueOf(id));
    } catch (HttpClientErrorException.NotFound e) {
      log.info("Debt Position Type with ID {} not found", id);
      return null;
    }
  }

  public PagedModelDebtPositionTypeWithCount getDebtPositionTypeWithCount(Long brokerId, String description, Pageable pageable, String accessToken) {
    try {
      return debtPositionApisHolder.getDebtPositionTypeWithCountSearchControllerApi(accessToken)
        .crudDebtPositionTypesWithCountFindByBrokerId(brokerId,
          description,
          PageUtils.getPageNumber(pageable),
          PageUtils.getPageSize(pageable),
          PageUtils.getSortList(pageable));
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("DebtPositionType with brokerId {} not found", brokerId);
      return null;
    }
  }

  public DebtPositionType createDebtPositionType(
    DebtPositionTypeRequestBody debtPositionType, String accessToken) {
    return debtPositionApisHolder.getDebtPositionTypeControllerApi(accessToken)
      .crudCreateDebtpositiontype(debtPositionType);
  }

  public DebtPositionType patchDebtPositionType(
    Long debtPositionTypeId, DebtPositionTypeRequestBody debtPositionType, String accessToken) {
    try {
      return debtPositionApisHolder.getDebtPositionTypeControllerApi(accessToken)
        .crudPatchDebtpositiontype(debtPositionTypeId.toString(), debtPositionType);
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("DebtPositionType with debtPositionTypeId {} not found", debtPositionTypeId);
      return null;
    }
  }

  public void deleteDebtPositionType(Long debtPositionTypeId, String accessToken) {
    try {
      debtPositionApisHolder.getDebtPositionTypeControllerApi(accessToken)
        .crudDeleteDebtpositiontype(String.valueOf(debtPositionTypeId));
    } catch (HttpClientErrorException.NotFound e) {
      throw new ResourceNotFoundException("DebtPositionType with ID %d not found".formatted(debtPositionTypeId));
    }
  }

  public CollectionModelDebtPositionType getDebtPositionTypesByBrokerIdAndOrgType(Long brokerId, String orgType, String accessToken) {
    return debtPositionApisHolder.getDebtPositionTypeSearchControllerApi(accessToken)
      .crudDebtPositionTypesFindAllByBrokerIdAndOrgType(brokerId, orgType);
  }

  public List<DebtPositionType> findByDebtPositionTypeIds(Set<Long> debtPositionTypeIds, String accessToken) {
    CollectionModelDebtPositionType collectionModelDebtPositionType = debtPositionApisHolder.getDebtPositionTypeSearchControllerApi(accessToken)
            .crudDebtPositionTypesFindByDebtPositionTypeIdIn(debtPositionTypeIds);
    return collectionModelDebtPositionType!=null && collectionModelDebtPositionType.getEmbedded()!=null?collectionModelDebtPositionType.getEmbedded().getDebtPositionTypes() : Collections.emptyList();
  }

}

