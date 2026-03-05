package grsu.by.service;

import grsu.by.dto.InvoiceDto;

public interface InvoiceService {
    byte[] createInvoiceByPaymentId(InvoiceDto invoiceDto);
}
