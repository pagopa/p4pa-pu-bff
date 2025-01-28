package it.gov.pagopa.pu.bff.connector.debtposition.client;

import it.gov.pagopa.pu.bff.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.p4pa_debt_positions.dto.generated.PagedModelDebtPositionTypeWithCount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class DebtPositionClient {

    private final DebtPositionApisHolder debtPositionApisHolder;

    public DebtPositionClient(DebtPositionApisHolder debtPositionApisHolder) {
        this.debtPositionApisHolder = debtPositionApisHolder;
    }

  public PagedModelDebtPositionTypeWithCount getDebtPositionTypeWithCount(Long brokerId, Pageable pageable, String accessToken) {
    try {
      return debtPositionApisHolder.getDebtPositionTypeWithCountSearchControllerApi(accessToken)
        .crudDebtPositionTypesWithCountFindByBrokerId( brokerId,pageable.getPageNumber(),
          pageable.getPageSize(),
          pageable.getSort().stream()
            .map(o->o.getProperty()+","+o.getDirection()).toList());
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        log.warn("DebtPositionType with brokerId {} not found", brokerId);
        return null;
      }
      log.error("Error retrieving DebtPositionType by brokerId: {}", brokerId, e);
      throw e;
    } catch (Exception e) {
      log.error("Unexpected error while retrieving DebtPositionType by brokerId: {}", brokerId, e);
      throw e;
    }
  }

}
