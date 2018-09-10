package com.titarenko.service;

import com.titarenko.entity.Operation;

import java.util.Set;

public interface OperationService {

    Set<Operation> getOperations(Long id, String pass);
}
