package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.organization.dto.generated.Broker;
import it.gov.pagopa.pu.organization.dto.generated.PersonalisationFe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PersonalisationFE2ConfigFEMapperTest {

  private PersonalisationFE2ConfigFEMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new PersonalisationFE2ConfigFEMapper();
  }

  @Test
  void givenCompleteInputsWhenMapPersonalisationFE2ConfigFEThenOk() {
    PersonalisationFe personalisationFe = new PersonalisationFe();
    personalisationFe.setFooterDescText("Footer Description");
    personalisationFe.setFooterGDPRUrl("GDPR URL");
    personalisationFe.setHeaderAssistanceUrl("Assistance URL");
    personalisationFe.setFooterAccessibilityUrl("Accessibility URL");
    personalisationFe.setLogoFooterImg("img");

    Broker broker = new Broker();
    broker.setBrokerId(1L);
    broker.setBrokerFiscalCode("brokerFiscalCode");
    broker.setExternalId("brokerExternalId");

    UserInfo userInfo = new UserInfo();
    userInfo.setCanManageUsers(true);

    ConfigFE configFE = mapper.mapPersonalisationFE2ConfigFE(personalisationFe, broker, userInfo);

    Assertions.assertEquals("img", configFE.getLogoFooterImg());
    Assertions.assertEquals("Footer Description", configFE.getFooterDescText());
    Assertions.assertEquals("GDPR URL", configFE.getFooterGDPRUrl());
    Assertions.assertEquals("Assistance URL", configFE.getHeaderAssistanceUrl());
    Assertions.assertEquals("Accessibility URL", configFE.getFooterAccessibilityUrl());
    Assertions.assertEquals(String.valueOf(broker.getBrokerId()), configFE.getBrokerId());
    Assertions.assertEquals(broker.getBrokerFiscalCode(), configFE.getBrokerFiscalCode());
    Assertions.assertEquals(broker.getExternalId(), configFE.getExternalId());
    Assertions.assertTrue(configFE.getCanManageUsers());
    TestUtils.checkNotNullFields(configFE, "headerAssistanceUrl", "logoFooterImg", "footerDescText", "footerGDPRUrl", "footerAccessibilityUrl");
  }

  @Test
  void givenNullPersonalizationFeWhenMapPersonalisationFE2ConfigFEThenNull() {
    ConfigFE configFE = mapper.mapPersonalisationFE2ConfigFE(null, null, null);
    Assertions.assertNull(configFE);
  }

  @Test
  void givenNullBrokerAndNullUserInfoWhenMapPersonalisationFE2ConfigFEThenOk() {
    ConfigFE configFE = mapper.mapPersonalisationFE2ConfigFE(new PersonalisationFe(), null, null);
    Assertions.assertNotNull(configFE);
    Assertions.assertNull(configFE.getBrokerId());
    Assertions.assertEquals("default", configFE.getExternalId());
    Assertions.assertFalse(configFE.getCanManageUsers());
  }

}
