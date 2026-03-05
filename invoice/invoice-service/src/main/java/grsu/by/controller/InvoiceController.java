package grsu.by.controller;

import grsu.by.dto.InvoiceDto;
import grsu.by.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
@Slf4j
public class InvoiceController {
    private final InvoiceService service;

    @PostMapping
    public ResponseEntity<byte[]> createByPaymentId(@RequestBody InvoiceDto invoiceDto) {
        log.info("createByPaymentId {}", invoiceDto);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(service.createInvoiceByPaymentId(invoiceDto));
    }
}
