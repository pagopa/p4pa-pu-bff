package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.PersonalisationFe;
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
  void testMapPersonalisationFE2ConfigFEWithValidData() {
    PersonalisationFe personalisationFe = new PersonalisationFe();
    personalisationFe.setFooterDescText("Footer Description");
    personalisationFe.setFooterGDPRUrl("GDPR URL");
    personalisationFe.setFooterPrivacyInfoUrl("Privacy Info URL");
    personalisationFe.setFooterTermsCondUrl("Terms and Conditions URL");
    personalisationFe.setHeaderAssistanceUrl("Assistance URL");
    personalisationFe.setFooterAccessibilityUrl("Accessibility URL");
    personalisationFe.setLogoFooterImg("img");

    ConfigFE configFE = mapper.mapPersonalisationFE2ConfigFE(personalisationFe);

    Assertions.assertEquals("img", configFE.getLogoFooterImg());
    Assertions.assertEquals("Footer Description", configFE.getFooterDescText());
    Assertions.assertEquals("GDPR URL", configFE.getFooterGDPRUrl());
    Assertions.assertEquals("Privacy Info URL", configFE.getFooterPrivacyInfoUrl());
    Assertions.assertEquals("Terms and Conditions URL", configFE.getFooterTermsCondUrl());
    Assertions.assertEquals("Assistance URL", configFE.getHeaderAssistanceUrl());
    Assertions.assertEquals("Accessibility URL", configFE.getFooterAccessibilityUrl());
    Assertions.assertNull(configFE.getCanManageUsers());
    Assertions.assertNull(configFE.getBrokerId());
  }

  @Test
  void testMapPersonalisationFE2ConfigFEWithNullInput() {
    ConfigFE configFE = mapper.mapPersonalisationFE2ConfigFE(null);
    Assertions.assertNull(configFE);
  }

}
