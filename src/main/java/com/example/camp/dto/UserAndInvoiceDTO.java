package com.example.camp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UserAndInvoiceDTO {
    private UserDTO userDTO;
    private InvoiceDto invoiceDto;
}
