package com.hospital.billing.controller;

import com.hospital.billing.entity.Billing;
import com.hospital.billing.service.BillingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/billing")
public class BillingController {

    private final BillingService service;

    public BillingController(BillingService service) {
        this.service = service;
    }

    @PostMapping
    public Billing create(@RequestBody Billing bill) {
        return service.createBill(bill);
    }

    @GetMapping
    public List<Billing> getAll() {
        return service.getAllBills();
    }

    @GetMapping("/{id}")
    public Billing getById(@PathVariable Long id) {
        return service.getBillById(id);
    }
}