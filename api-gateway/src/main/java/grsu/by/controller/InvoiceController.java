package grsu.by.controller;

import grsu.by.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-gateway/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService service;

    @GetMapping("/{paymentId}")
    public ResponseEntity<byte[]> findByPaymentId(@PathVariable Long paymentId) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(service.getInvoiceByPaymentId(paymentId));
    }
}
