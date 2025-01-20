package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.bff.util.TestUtils;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.Broker;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.PersonalisationFe;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
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
    personalisationFe.setFooterPrivacyInfoUrl("Privacy Info URL");
    personalisationFe.setFooterTermsCondUrl("Terms and Conditions URL");
    personalisationFe.setHeaderAssistanceUrl("Assistance URL");
    personalisationFe.setFooterAccessibilityUrl("Accessibility URL");
    personalisationFe.setLogoFooterImg("img");

    Broker broker = new Broker();
    broker.setBrokerId(1L);

    UserInfo userInfo = new UserInfo();
    userInfo.setCanManageUsers(true);

    ConfigFE configFE = mapper.mapPersonalisationFE2ConfigFE(personalisationFe, broker, userInfo);

    Assertions.assertEquals("img", configFE.getLogoFooterImg());
    Assertions.assertEquals("Footer Description", configFE.getFooterDescText());
    Assertions.assertEquals("GDPR URL", configFE.getFooterGDPRUrl());
    Assertions.assertEquals("Privacy Info URL", configFE.getFooterPrivacyInfoUrl());
    Assertions.assertEquals("Terms and Conditions URL", configFE.getFooterTermsCondUrl());
    Assertions.assertEquals("Assistance URL", configFE.getHeaderAssistanceUrl());
    Assertions.assertEquals("Accessibility URL", configFE.getFooterAccessibilityUrl());
    Assertions.assertEquals(String.valueOf(broker.getBrokerId()), configFE.getBrokerId());
    Assertions.assertTrue(configFE.getCanManageUsers());
    TestUtils.checkNotNullFields(configFE);
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
    Assertions.assertFalse(configFE.getCanManageUsers());
  }

}
