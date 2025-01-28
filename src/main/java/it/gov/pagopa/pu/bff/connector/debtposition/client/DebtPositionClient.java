package it.gov.pagopa.pu.bff.connector.debtposition.client;

import it.gov.pagopa.pu.bff.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeWithCount;
import java.util.Collections;
import java.util.List;
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
      Integer pageNumber = pageable.isPaged()? pageable.getPageNumber() : 0;
      Integer pageSize = pageable.isPaged()? pageable.getPageSize() : null;
      return debtPositionApisHolder.getDebtPositionTypeWithCountSearchControllerApi(accessToken)
        .crudDebtPositionTypesWithCountFindByBrokerId( brokerId, pageNumber,
          pageSize,
          getSortList(pageable));
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

  private static List<String> getSortList(Pageable pageable) {
    return pageable.getSort().isSorted()?
      pageable.getSort().stream()
        .map(o -> o.getProperty() + "," + o.getDirection()).toList()
      : Collections.emptyList();
  }

}
