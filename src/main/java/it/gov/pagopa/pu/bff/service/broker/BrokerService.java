package it.gov.pagopa.pu.bff.service.broker;

import it.gov.pagopa.pu.bff.dto.generated.ConfigFE;
import it.gov.pagopa.pu.p4paauth.model.generated.UserInfo;

public interface BrokerService {

  ConfigFE getBrokerConfig(UserInfo user, String accessToken);

}
