package it.gov.pagopa.pu.bff.service.receipt;

import it.gov.pagopa.pu.organization.dto.generated.Organization;

public interface ReceiptFileService {
    byte[] generateReceiptPdf(it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO receiptDetail, Organization organization);
}
