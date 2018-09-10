package com.titarenko.controller;

import com.titarenko.entity.Card;
import com.titarenko.service.CardService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/card")
public class CardController {

    private final static Logger LOGGER = Logger.getLogger(CardController.class);
    private final CardService service;

    @Autowired
    public CardController(CardService service) {
        this.service = service;
    }

    @RequestMapping(value = "/create", method = POST)
    public String create(@RequestBody @Valid Card card) {
        return String.valueOf(service.save(card));
    }

    @RequestMapping(value = "/exist/{id:\\d+}", method = GET)
    public boolean isExist(@PathVariable("id") Long id) {
        return service.isExistAndActive(id);
    }

    @RequestMapping(value = "/pass/", method = GET)
    public boolean checkPass(@RequestParam Long id, @RequestParam String pass) {
        return service.checkPass(id, pass);
    }

    @RequestMapping(value = "/amount/", method = GET)
    public Long obtainAmount(@RequestParam Long id, @RequestParam String pass) {
        return service.obtainAmount(id, pass);
    }

    @RequestMapping(value = "/cashOut", method = GET)
    public Long cashOut(@RequestParam Long id, @RequestParam Long amount, @RequestParam String pass) {
        return service.updateAmount(id, amount, pass);
    }

    @RequestMapping(value = "/block/{id:\\d+}", method = GET)
    public boolean block(@PathVariable("id") Long id) {
        service.block(id);
        return true;
    }

}