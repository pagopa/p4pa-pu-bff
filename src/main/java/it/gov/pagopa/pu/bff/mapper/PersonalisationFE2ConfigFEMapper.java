package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.Broker;
import it.gov.pagopa.pu.p4pa_organization.dto.generated.PersonalisationFe;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import org.springframework.stereotype.Component;

@Component
public class PersonalisationFE2ConfigFEMapper {

  public ConfigFE mapPersonalisationFE2ConfigFE(PersonalisationFe personalisationFe, Broker broker, UserInfo userInfo) {
    if(personalisationFe == null){
      return null;
    }
    ConfigFE out = ConfigFE.builder()
      .logoFooterImg(personalisationFe.getLogoFooterImg())
      .footerDescText(personalisationFe.getFooterDescText())
      .footerGDPRUrl(personalisationFe.getFooterGDPRUrl())
      .footerPrivacyInfoUrl(personalisationFe.getFooterPrivacyInfoUrl())
      .footerTermsCondUrl(personalisationFe.getFooterTermsCondUrl())
      .headerAssistanceUrl(personalisationFe.getHeaderAssistanceUrl())
      .footerAccessibilityUrl(personalisationFe.getFooterAccessibilityUrl())
      .build();
    if(broker!=null){
      out.setBrokerId(String.valueOf(broker.getBrokerId()));
    }
    if(userInfo!=null){
      out.setCanManageUsers(Boolean.TRUE.equals(userInfo.getCanManageUsers()));
    }
    return out;
  }

}
