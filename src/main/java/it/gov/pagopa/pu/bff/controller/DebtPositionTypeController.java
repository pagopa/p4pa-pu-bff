package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.DebtPositionTypeApi;
import it.gov.pagopa.pu.bff.dto.generated.PagedDebtPositionTypeWithCount;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.debtposition.DebtPositionTypeService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class DebtPositionTypeController implements DebtPositionTypeApi {

    private final DebtPositionTypeService debtPositionTypeService;

    public DebtPositionTypeController(DebtPositionTypeService debtPositionTypeService) {
      this.debtPositionTypeService = debtPositionTypeService;
    }

    @Override
    public ResponseEntity<PagedDebtPositionTypeWithCount> getDebtPositionTypeWithCount(
      Long organizationId, Integer page, Long size, List<String> sort) {
      return ResponseEntity.ok(debtPositionTypeService.getDebtPositionTypeWithCount(
        organizationId, page, size, sort, SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()));
    }
  }
