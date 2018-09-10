package com.titarenko.controller;

import com.titarenko.entity.Operation;
import com.titarenko.service.OperationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/operations")
public class OperationsController {

    private final static Logger LOGGER = Logger.getLogger(OperationsController.class);
    private final OperationService service;

    @Autowired
    public OperationsController(OperationService service) {
        this.service = service;
    }

    @RequestMapping(value = "/getAll", method = GET)
    public Set<Operation> getOperations(@RequestParam Long id, @RequestParam String pass) {
        return service.getOperations(id, pass);
    }

}