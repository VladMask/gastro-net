package grsu.by.service.impl;

import grsu.by.dto.InvoiceDto;
import grsu.by.exception.InvoiceGenerationException;
import grsu.by.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    @Override
    public byte[] createInvoiceByPaymentId(InvoiceDto invoiceDto) {
        InputStream reportStream = InvoiceServiceImpl.class.getResourceAsStream("/reports/Invoice.jasper");

        JasperReport invoiceReport;
        try {
            invoiceReport = (JasperReport) JRLoader.loadObject(reportStream);
        } catch (JRException e) {
            throw new InvoiceGenerationException("Something went wrong while loading the report!", e);
        }

        maskData(invoiceDto);

        Map<String, Object> params = new HashMap<>();
        params.put("firstName", invoiceDto.getFirstName());
        params.put("lastName", invoiceDto.getLastName());
        params.put("userEmail", invoiceDto.getUserEmail());
        params.put("restaurantName", invoiceDto.getRestaurantName());
        params.put("orderId", invoiceDto.getOrderId());
        params.put("createdAt", invoiceDto.getCreatedAt());
        params.put("totalPrice", invoiceDto.getTotalPrice());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(
                invoiceDto.getItems()
        );

        byte[] invoice;
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(invoiceReport, params, dataSource);
            invoice = JasperExportManager.exportReportToPdf(jasperPrint);
        }
        catch (JRException e) {
            throw new InvoiceGenerationException("Cannot create invoice", e);
        }
        return invoice;
    }

    private void maskData(InvoiceDto invoiceDto) {
        invoiceDto.setUserEmail(maskEmail(invoiceDto.getUserEmail()));
        invoiceDto.setFirstName(maskName(invoiceDto.getFirstName()));
        invoiceDto.setLastName(maskName(invoiceDto.getLastName()));
    }

    private String maskName(String name) {
        if (name == null || name.length() < 2) return name;
        if (name.length() == 2) return name.charAt(0) + "*";
        return name.charAt(0) + "*".repeat(name.length() - 2) + name.charAt(name.length() - 1);
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email;

        String[] parts = email.split("@", 2);
        String local = parts[0];
        String domain = parts[1];

        if (local.length() <= 2) {
            return local + "@" + domain;
        }

        return local.substring(0, 2) + "*".repeat(local.length() - 2) + "@" + domain;
    }
}
