package com.titarenko.service;

import com.titarenko.entity.Card;
import com.titarenko.entity.Operation;
import com.titarenko.repository.OperationRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class OperationServiceImpl implements OperationService {

    private final CardService cardService;
    private final OperationRepository repository;

    @Autowired
    public OperationServiceImpl(OperationRepository repository, CardService cardService) {
        this.repository = repository;
        this.cardService = cardService;
    }

    @Override
    public Set<Operation> getOperations(Long id, String pin) {
        String pass = DigestUtils.md5Hex(pin);
        return cardService.findById(id)
                .filter(card -> pass.equals(card.getPassword()))
                .map(Card::getOperations)
                .orElse(new HashSet<>());
    }

}
