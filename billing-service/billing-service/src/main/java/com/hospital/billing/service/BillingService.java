package com.hospital.billing.service;

import com.hospital.billing.entity.Billing;
import com.hospital.billing.repository.BillingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BillingService {

    private final BillingRepository repository;

    public BillingService(BillingRepository repository) {
        this.repository = repository;
    }

    public Billing createBill(Billing bill) {
        // ⚡ Automatically calculate total amount before saving to database
        double consultation = bill.getConsultationFee() != null ? bill.getConsultationFee() : 0.0;
        double pharmacy = bill.getMedicineCharge() != null ? bill.getMedicineCharge() : 0.0;

        bill.setTotalAmount(consultation + pharmacy);
        bill.setBillingDate(LocalDate.now()); // Sets today's date automatically

        return repository.save(bill);
    }

    public List<Billing> getAllBills() {
        return repository.findAll();
    }

    public Billing getBillById(Long id) {
        return repository.findById(id).orElse(null);
    }
}