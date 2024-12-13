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
      // TODO add canManageUsers and BrokerId for UserInfo https://pagopa.atlassian.net/browse/P4ADEV-1753
      .canManageUsers(null)
      .brokerId(null)
      .logoFooterImg(personalisationFe.getLogoFooterImg())
      .footerDescText(personalisationFe.getFooterDescText())
      .footerGDPRUrl(personalisationFe.getFooterGDPRUrl())
      .footerPrivacyInfoUrl(personalisationFe.getFooterPrivacyInfoUrl())
      .footerTermsCondUrl(personalisationFe.getFooterTermsCondUrl())
      .headerAssistanceUrl(personalisationFe.getHeaderAssistanceUrl())
      .footerAccessibilityUrl(personalisationFe.getFooterAccessibilityUrl())
      .build();
  }

}
