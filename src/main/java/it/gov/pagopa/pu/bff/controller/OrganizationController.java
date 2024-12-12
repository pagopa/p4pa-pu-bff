package it.gov.pagopa.pu.bff.controller;

import it.gov.pagopa.pu.bff.service.organization.BrokerServiceImpl;
import it.gov.pagopa.pu.p4pa_organization.model.generated.PersonalisationFe;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrganizationController {

  private BrokerServiceImpl service;

  public OrganizationController (BrokerServiceImpl service){
    this.service = service;
  }

  @GetMapping(path = "getBrokerConfig")
  public PersonalisationFe getBrokerConf(){
    return service.getBrokerConfig();
  }

}
