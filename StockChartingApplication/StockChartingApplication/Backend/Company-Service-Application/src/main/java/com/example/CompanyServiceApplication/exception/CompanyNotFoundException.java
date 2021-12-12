package com.example.CompanyServiceApplication.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyNotFoundException extends Throwable
{
    private String message;
}