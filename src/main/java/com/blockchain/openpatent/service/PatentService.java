package com.blockchain.openpatent.service;

import com.blockchain.openpatent.model.PatentData;
import com.blockchain.openpatent.repository.PatentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatentService {

    @Autowired
    private PatentRepository patentRepository;

    public void createPatent(PatentData patentData) {
        patentRepository.save(patentData);
    }

    public List<PatentData> getAllPatents() {
        return patentRepository.findAll();
    }
}
