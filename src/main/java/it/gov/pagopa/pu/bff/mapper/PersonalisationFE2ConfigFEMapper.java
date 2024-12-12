package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.PersonalisationFe;
import org.springframework.stereotype.Component;

@Component
public class PersonalisationFE2ConfigFEMapper {

  public ConfigFE mapPersonalisationFE2ConfigFE(PersonalisationFe personalisationFe) {
    if(personalisationFe == null){
      return null;
    }
    return ConfigFE.builder()
      .canManageUsers(null)
      .brokerId(null)
      .footerDescText(personalisationFe.getFooterDescText())
      .footerGDPRUrl(personalisationFe.getFooterGDPRUrl())
      .footerPrivacyInfoUrl(personalisationFe.getFooterPrivacyInfoUrl())
      .footerTermsCondUrl(personalisationFe.getFooterTermsCondUrl())
      .headerAssistanceUrl(personalisationFe.getHeaderAssistanceUrl())
      .footerAccessibilityUrl(personalisationFe.getFooterAccessibilityUrl())
      .build();
  }

}
