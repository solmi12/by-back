package com.example.camp.controllers;

import com.example.camp.dto.InvoiceDto;
import com.example.camp.entity.Invoice;
import com.example.camp.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok().body(invoices);
    }

    @GetMapping("/{invId}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long invId) {
        Optional<Invoice> invoice = invoiceService.getInvoiceById(invId);
        return invoice.map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/{invId}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long invId, @RequestBody InvoiceDto invoiceDto) {
        Invoice updatedInvoice = invoiceService.updateInvoice(invId, invoiceDto);
        if (updatedInvoice != null) {
            return ResponseEntity.ok().body(updatedInvoice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{invId}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long invId) {
        invoiceService.deleteInvoice(invId);
        return ResponseEntity.noContent().build();
    }
}
