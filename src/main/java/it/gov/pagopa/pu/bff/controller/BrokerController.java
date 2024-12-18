package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.BrokersApi;
import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.broker.BrokerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class BrokerController implements BrokersApi {

  private final BrokerService brokerService;

  public BrokerController(BrokerService service) {
    this.brokerService = service;
  }

  @Override
  public ResponseEntity<ConfigFE> getBrokerConfig() {
    log.info("User requested getBrokerConfig()");
    return new ResponseEntity<>(brokerService.getBrokerConfig(SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()), HttpStatus.OK);
  }

}
