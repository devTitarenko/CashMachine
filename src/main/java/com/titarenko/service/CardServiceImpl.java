package com.titarenko.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.titarenko.entity.Card;
import com.titarenko.entity.Operation;
import com.titarenko.repository.CardRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {

    private final static org.apache.log4j.Logger LOGGER = Logger.getLogger(CardServiceImpl.class);
    private final CardRepository repository;

    @Autowired
    public CardServiceImpl(CardRepository repository) {
        this.repository = repository;
    }

    public Long save(Card card) {
        card.setPassword(DigestUtils.md5Hex(card.getPassword()));
        return repository.save(card).getId();
    }

    public boolean isExistAndActive(Long id) {
        return findById(id).map(Card::isActive).orElse(false);
    }

    public boolean checkPass(Long id, String pin) {
        String pass = DigestUtils.md5Hex(pin);
        return findById(id).map(Card::getPassword).map(pass::equals).orElse(false);
    }

    public Long obtainAmount(Long id, String pin) {
        String pass = DigestUtils.md5Hex(pin);
        return findById(id).filter(card -> pass.equals(card.getPassword()))
                .map(Card::getAmount).orElse(-1L);
    }

    public Long updateAmount(Long id, Long amount, String pin) {
        String pass = DigestUtils.md5Hex(pin);
        Optional<Card> optionalCard = findById(id)
                .filter(card -> pass.equals(card.getPassword()))
                .filter(card -> amount <= card.getAmount());
        if (optionalCard.isPresent()) {
            Card card = optionalCard.get();
            card.setAmount(card.getAmount() - amount);
            if (CollectionUtils.isEmpty(card.getOperations())) {
                card.setOperations(new HashSet<>());
            }
            Operation operation = new Operation();
            operation.setAmount(amount);
            operation.setRest(card.getAmount());
            operation.setDate(new Date());
            card.getOperations().add(operation);
            repository.save(card);
            return card.getAmount();
        } else {
            return -1L;
        }
    }

    public void transferMoney(Long fromId, Long amount, String pin, Long toId) {
        String pass = DigestUtils.md5Hex(pin);
        Optional<Card> fromCard = findById(fromId).filter(card -> pass.equals(card.getPassword()))
                .filter(card -> card.getAmount() >= amount);
        Optional<Card> toCard = findById(toId);
        if (fromCard.isPresent() && toCard.isPresent()) {
            Card from = fromCard.get();
            from.setAmount(from.getAmount() - amount);
            repository.save(from);
            Card to = toCard.get();
            to.setAmount(to.getAmount() + amount);
            repository.save(to);
        }
    }

    @Override
    public void block(Long id) {
        findById(id).ifPresent(card -> {
            card.setActive(false);
            repository.save(card);
        });
    }

    public void unBlock(Long id) {
        findById(id).ifPresent(card -> card.setActive(true));
    }

    public Optional<Card> findById(Long id) {
        return Optional.ofNullable(repository.findOne(id));
    }

    public String toJson(Card card) throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(card);
    }

}
