package it.gov.pagopa.pu.bff.connector.classification.client;

import it.gov.pagopa.pu.bff.connector.classification.config.ClassificationApisHolder;
import it.gov.pagopa.pu.classification.dto.generated.AssessmentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AssessmentsEntityExtendedClient {

    private final ClassificationApisHolder classificationApisHolder;

    public AssessmentsEntityExtendedClient(ClassificationApisHolder classificationApisHolder) {
        this.classificationApisHolder = classificationApisHolder;
    }

    public void updateStatus(Long organizationId, Long assessmentId, AssessmentStatus status, String accessToken){
        classificationApisHolder.getAssessmentsEntityExtendedControllerApi(accessToken)
                .updateStatus(assessmentId,organizationId,status);
    }
}
