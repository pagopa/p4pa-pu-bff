package it.gov.pagopa.pu.bff.service.broker;

import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.auth.dto.generated.UserInfo;

public interface BrokerRetrieverService {

  ConfigFE getBrokerConfig(UserInfo user, String accessToken);

}
