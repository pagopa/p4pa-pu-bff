package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.BrokersApi;
import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.broker.BrokerRetrieverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class BrokerController implements BrokersApi {

  private final BrokerRetrieverService brokerRetrieverService;

  public BrokerController(BrokerRetrieverService service) {
    this.brokerRetrieverService = service;
  }

  @Override
  public ResponseEntity<ConfigFE> getBrokerConfig() {
    log.info("User requested getBrokerConfig()");
    return new ResponseEntity<>(brokerRetrieverService.getBrokerConfig(SecurityUtils.getLoggedUser(), SecurityUtils.getAccessToken()), HttpStatus.OK);
  }

}
