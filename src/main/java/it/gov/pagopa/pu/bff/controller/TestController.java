package it.gov.pagopa.pu.bff.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping("/hello")
  public String ciao(){
    return "Ciao";
  }

}
