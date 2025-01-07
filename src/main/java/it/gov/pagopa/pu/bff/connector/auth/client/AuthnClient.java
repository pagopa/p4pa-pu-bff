package it.gov.pagopa.pu.bff.connector.auth.client;

import it.gov.pagopa.pu.bff.connector.auth.config.AuthApisHolder;
import it.gov.pagopa.pu.p4paauth.dto.generated.UserInfo;
import org.springframework.stereotype.Service;

@Service
public class AuthnClient {

    private final AuthApisHolder authApisHolder;

    public AuthnClient(AuthApisHolder authApisHolder) {
        this.authApisHolder = authApisHolder;
    }

    public UserInfo getUserInfo(String accessToken) {
        return authApisHolder.getAuthnApi(accessToken)
                .getUserInfo();
    }

}
