package it.gov.pagopa.pu.bff.mapper;

import it.gov.pagopa.pu.bff.dto.generated.PagedReceiptView;
import it.gov.pagopa.pu.debtpositions.dto.generated.PageMetadata;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptView;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelReceiptViewEmbedded;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReceiptViewMapperTest {

  private final ReceiptViewMapper mapper = new ReceiptViewMapper();

  @Test
  void givenPopulatedPagedModelWhenMapToPagedReceiptViewThenCorrectMapping() {
    PagedModelReceiptView pagedModel = new PagedModelReceiptView();
    PagedModelReceiptViewEmbedded embedded = new PagedModelReceiptViewEmbedded();
    ReceiptView receiptView = new ReceiptView();
    receiptView.setReceiptId(1L);

    embedded.setReceiptViews(List.of(receiptView));
    pagedModel.setEmbedded(embedded);

    PageMetadata page = new PageMetadata();
    page.setSize(10L);
    page.setTotalElements(1L);
    page.setTotalPages(1L);
    page.setNumber(1L);
    pagedModel.setPage(page);

    PagedReceiptView result = mapper.mapToPagedReceiptView(pagedModel);

    assertNotNull(result);
    assertEquals(1L, result.getNumber());
    assertEquals(1L, result.getTotalElements());
    assertEquals(1L, result.getTotalPages());
    assertEquals(10L, result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(1, result.getContent().size());
    assertEquals(receiptView.getReceiptId(), result.getContent().getFirst().getReceiptId());
  }

  @Test
  void givenNoContentWhenMapToPagedReceiptViewThenPartialMapping() {
    PagedModelReceiptView pagedModel = new PagedModelReceiptView();
    PageMetadata page = new PageMetadata();
    page.setSize(10L);
    page.setTotalElements(1L);
    page.setTotalPages(1L);
    page.setNumber(1L);
    pagedModel.setPage(page);

    PagedReceiptView result = mapper.mapToPagedReceiptView(pagedModel);

    assertNotNull(result);
    assertEquals(1L, result.getNumber());
    assertEquals(1L, result.getTotalElements());
    assertEquals(1L, result.getTotalPages());
    assertEquals(10L, result.getSize());
    assertTrue(CollectionUtils.isEmpty(result.getContent()));
  }

  @Test
  void givenNoPageWhenMapToPagedReceiptViewThenPartialMapping() {
    PagedModelReceiptView pagedModel = new PagedModelReceiptView();
    PagedModelReceiptViewEmbedded embedded = new PagedModelReceiptViewEmbedded();
    ReceiptView receiptView = new ReceiptView();
    receiptView.setReceiptId(1L);

    embedded.setReceiptViews(List.of(receiptView));
    pagedModel.setEmbedded(embedded);

    PagedReceiptView result = mapper.mapToPagedReceiptView(pagedModel);

    assertNotNull(result);
    assertNull(result.getNumber());
    assertNull(result.getTotalElements());
    assertNull(result.getTotalPages());
    assertNull(result.getSize());
    assertFalse(CollectionUtils.isEmpty(result.getContent()));
    assertEquals(1, result.getContent().size());
    assertEquals(receiptView.getReceiptId(), result.getContent().getFirst().getReceiptId());
  }

}
