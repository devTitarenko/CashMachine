package com.titarenko.service;

import com.titarenko.entity.Card;

import java.util.Optional;

public interface CardService {

    Optional<Card> findById(Long id);

    void block(Long id);

    Long save(Card card);

    boolean isExistAndActive(Long id);

    boolean checkPass(Long id, String pass);

    Long obtainAmount(Long id, String pass);

    Long updateAmount(Long id, Long amount, String pass);

}
