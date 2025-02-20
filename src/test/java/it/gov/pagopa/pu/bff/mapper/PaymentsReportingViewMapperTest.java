package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedPaymentsReportingView;
import it.gov.pagopa.pu.classification.dto.generated.PageMetadata;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingView;
import it.gov.pagopa.pu.classification.dto.generated.PagedModelPaymentsReportingViewEmbedded;
import it.gov.pagopa.pu.classification.dto.generated.PaymentsReportingView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentsReportingViewMapperTest {

  private final PaymentsReportingViewMapper mapper = new PaymentsReportingViewMapper();

  @Test
  void givenPopulatedPagedModelWhenMapToPagedPaymentsReportingThenCorrectMapping() {
    PagedModelPaymentsReportingView pagedModel = new PagedModelPaymentsReportingView();
    PagedModelPaymentsReportingViewEmbedded embedded = new PagedModelPaymentsReportingViewEmbedded();
    PaymentsReportingView paymentsReportingView = new PaymentsReportingView();
    paymentsReportingView.setIngestionFlowFileId(1L);

    embedded.setPaymentsReportingViews(List.of(paymentsReportingView));
    pagedModel.setEmbedded(embedded);

    PageMetadata page = new PageMetadata();
    page.setSize(10L);
    page.setTotalElements(1L);
    page.setTotalPages(1L);
    page.setNumber(1L);
    pagedModel.setPage(page);

    PagedPaymentsReportingView result = mapper.mapToPagedPaymentsReporting(pagedModel);

    assertNotNull(result);
    assertEquals(1L, result.getNumber());
    assertEquals(1L, result.getTotalElements());
    assertEquals(1L, result.getTotalPages());
    assertEquals(10L, result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(1, result.getContent().size());
    assertEquals(paymentsReportingView.getIngestionFlowFileId(), result.getContent().get(0).getIngestionFlowFileId());
  }

  @Test
  void givenNoContentWhenMapToPagedPaymentsReportingThenPartialMapping() {
    PagedModelPaymentsReportingView pagedModel = new PagedModelPaymentsReportingView();
    PageMetadata page = new PageMetadata();
    page.setSize(10L);
    page.setTotalElements(1L);
    page.setTotalPages(1L);
    page.setNumber(1L);
    pagedModel.setPage(page);

    PagedPaymentsReportingView result = mapper.mapToPagedPaymentsReporting(pagedModel);

    assertNotNull(result);
    assertEquals(1L, result.getNumber());
    assertEquals(1L, result.getTotalElements());
    assertEquals(1L, result.getTotalPages());
    assertEquals(10L, result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }

  @Test
  void givenNoPageWhenMapToPagedPaymentsReportingThenPartialMapping() {
    PagedModelPaymentsReportingView pagedModel = new PagedModelPaymentsReportingView();
    PagedModelPaymentsReportingViewEmbedded embedded = new PagedModelPaymentsReportingViewEmbedded();
    PaymentsReportingView paymentsReportingView = new PaymentsReportingView();
    paymentsReportingView.setIngestionFlowFileId(1L);

    embedded.setPaymentsReportingViews(List.of(paymentsReportingView));
    pagedModel.setEmbedded(embedded);

    PagedPaymentsReportingView result = mapper.mapToPagedPaymentsReporting(pagedModel);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(1, result.getContent().size());
    assertEquals(paymentsReportingView.getIngestionFlowFileId(), result.getContent().get(0).getIngestionFlowFileId());
  }
}
