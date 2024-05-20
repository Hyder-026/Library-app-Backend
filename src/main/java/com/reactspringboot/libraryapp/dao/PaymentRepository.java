package com.reactspringboot.libraryapp.dao;

import com.reactspringboot.libraryapp.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findByUserEmail(@RequestParam("user_email") String userEmail);
}
