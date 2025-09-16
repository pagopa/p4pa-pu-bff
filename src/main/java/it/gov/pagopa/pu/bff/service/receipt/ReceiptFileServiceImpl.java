package it.gov.pagopa.pu.bff.service.receipt;

import freemarker.template.TemplateException;
import it.gov.pagopa.pu.bff.util.DocumentComposition;
import it.gov.pagopa.pu.bff.util.Utilities;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@Slf4j
public class ReceiptFileServiceImpl implements ReceiptFileService{
    private final DocumentComposition documentComposition;

    public static final String RECEIPT_LOGO = "logo";
    public static final String RECEIPT_ORG_NAME = "orgName";
    public static final String RECEIPT_IUV = "iuv";
    public static final String RECEIPT_DEBTOR_NAME = "debtorName";
    public static final String RECEIPT_DEBTOR_FISCAL_CODE = "debtorFiscalCode";
    public static final String RECEIPT_TOTAL_AMOUNT = "totalAmount";
    public static final String RECEIPT_PAYMENT_DATE = "paymentDate";
    public static final String RECEIPT_PSP_NAME = "pspName";
    public static final String RECEIPT_FEE_AMOUNT = "feeAmount";
    public static final String RECEIPT_AMOUNT = "amount";
    public static final String RECEIPT_ORG_FISCAL_CODE = "orgFiscalCode";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss", Locale.ITALIAN);
    public static final String REMITTANCE_INFORMATION = "remittanceInformation";

    public ReceiptFileServiceImpl(DocumentComposition documentComposition) {
        this.documentComposition = documentComposition;
    }


    public byte[] generateReceiptPdf(it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO receiptDetail, Organization organization) {
        byte[] receiptPdf;
        try {
            receiptPdf = documentComposition.executePdfTemplate(DocumentComposition.TemplateType.RECEIPT, buildTemplateModel(receiptDetail, organization));
        } catch (IOException | TemplateException e) {
            throw new IllegalStateException(e);
        }
        return receiptPdf;
    }

    private Map<String, Object> buildTemplateModel(it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptDetailDTO receiptDetail, Organization organization) {
        Map<String, Object> templateModel = new HashMap<>();

        Long feeCents = receiptDetail.getFeeCents() != null ? receiptDetail.getFeeCents() : 0L;

        templateModel.put(RECEIPT_LOGO, StringUtils.defaultString(organization.getOrgLogo()));
        templateModel.put(RECEIPT_ORG_NAME,organization.getOrgName());
        templateModel.put(RECEIPT_IUV,StringUtils.defaultString(receiptDetail.getIuv()));
        templateModel.put(RECEIPT_DEBTOR_NAME,receiptDetail.getDebtor().getFullName());
        templateModel.put(RECEIPT_DEBTOR_FISCAL_CODE,receiptDetail.getDebtor().getFiscalCode());
        templateModel.put(RECEIPT_TOTAL_AMOUNT, Utilities.formatPrice(receiptDetail.getPaymentAmountCents()+feeCents));
        templateModel.put(RECEIPT_PAYMENT_DATE,receiptDetail.getPaymentDateTime()!=null?receiptDetail.getPaymentDateTime().format(DATE_TIME_FORMATTER):"");
        templateModel.put(RECEIPT_PSP_NAME,receiptDetail.getPspCompanyName());
        templateModel.put(RECEIPT_FEE_AMOUNT, Utilities.formatPrice(receiptDetail.getFeeCents()));
        templateModel.put(RECEIPT_AMOUNT, Utilities.formatPrice(receiptDetail.getPaymentAmountCents()));
        templateModel.put(RECEIPT_ORG_FISCAL_CODE,organization.getOrgFiscalCode());
        templateModel.put(REMITTANCE_INFORMATION,StringUtils.defaultString(receiptDetail.getRemittanceInformation()));
        return templateModel;
    }
}
