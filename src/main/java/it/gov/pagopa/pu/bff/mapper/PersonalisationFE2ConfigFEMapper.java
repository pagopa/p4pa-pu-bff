package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.auth.dto.generated.UserInfo;
import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.organization.dto.generated.Broker;
import it.gov.pagopa.pu.organization.dto.generated.PersonalisationFe;
import org.springframework.stereotype.Component;

@Component
public class PersonalisationFE2ConfigFEMapper {
  public final static String DEFAULT_EXTERNAL_ID = "default";

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
      out.setBrokerFiscalCode(broker.getBrokerFiscalCode());
      out.setExternalId(broker.getExternalId());
    } else {
      out.setExternalId(DEFAULT_EXTERNAL_ID);
    }
    out.setCanManageUsers(userInfo!=null && Boolean.TRUE.equals(userInfo.getCanManageUsers()));
    return out;
  }

}
