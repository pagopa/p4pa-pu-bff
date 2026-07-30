package it.gov.pagopa.pu.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostTokenRequest {
    private String clientId;
    private String grantType;
    private String scope;
    private String subjectToken;
    private String subjectIssuer;
    private String subjectTokenType;
    private String refreshToken;
}
