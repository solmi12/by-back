package com.example.camp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDto {

    private Long invId;

    private String name;
    private String email;

    private Long numPohne;
    private Double price;
}
