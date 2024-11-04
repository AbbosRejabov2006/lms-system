package com.example.lmssystem.repository;

import com.example.lmssystem.entity.Finance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

    @Repository
    public interface FinanceRepository extends JpaRepository<Finance, Long> {

    }
