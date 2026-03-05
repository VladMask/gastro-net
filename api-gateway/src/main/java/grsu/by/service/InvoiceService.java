package grsu.by.service;

public interface InvoiceService {
    byte[] getInvoiceByPaymentId(Long paymentId);
}
