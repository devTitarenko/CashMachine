package com.titarenko.repository;

import com.titarenko.entity.Card;
import com.titarenko.entity.Operation;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface CardRepository extends CrudRepository<Card, Long> {
}
