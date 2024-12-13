package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.controller.generated.BrokersApi;
import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.bff.security.SecurityUtils;
import it.gov.pagopa.pu.bff.service.organization.BrokerServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrganizationController implements BrokersApi {

  private BrokerServiceImpl service;

  public OrganizationController (BrokerServiceImpl service){
    this.service = service;
  }

  @Override
  public ResponseEntity<ConfigFE> getBrokerConfig () {
    return new ResponseEntity<>(service.getBrokerConfig(SecurityUtils.getLoggedUser(),SecurityUtils.getAccessToken()), HttpStatus.OK);
  }

}
