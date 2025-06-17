package com.blockchain.openpatent.repository;

import com.blockchain.openpatent.model.PatentData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatentRepository extends JpaRepository<PatentData, Long> {}
