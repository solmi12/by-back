package com.example.camp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.example.camp.dto.InvoiceDto;
import com.example.camp.entity.Invoice;
import com.example.camp.repository.InvoiceRepository;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }


    public Invoice createInvoice(InvoiceDto invoiceDto) {
        Invoice invoice = new Invoice();
        invoice.setName(invoiceDto.getName());
        invoice.setEmail(invoiceDto.getEmail());
        invoice.setNumPohne(invoiceDto.getNumPohne());
        invoice.setPrice(invoiceDto.getPrice());
        return invoiceRepository.save(invoice);
    }


    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> getInvoiceById(Long invId) {
        return invoiceRepository.findById(invId);
    }



    public Invoice updateInvoice(Long invId, InvoiceDto invoiceDto) {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(invId);
        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            invoice.setName(invoiceDto.getName());
            invoice.setEmail(invoiceDto.getEmail());
            invoice.setNumPohne(invoiceDto.getNumPohne());
            invoice.setPrice(invoiceDto.getPrice());
            return invoiceRepository.save(invoice);
        } else {
            // Handle the case where the invoice is not found
            return null;
        }
    }

    public void deleteInvoice(Long invId) {
        invoiceRepository.deleteById(invId);
    }


}
