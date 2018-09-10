package com.titarenko.entity;


import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Entity
public class Card {

    @Id
    private Long id;
    @NotNull
    private Long amount;
    private boolean isActive;
    @NotBlank
    private String password;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "FK_CARD_ID", referencedColumnName = "ID")
    private Set<Operation> operations;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Set<Operation> getOperations() {
        return operations;
    }

    public void setOperations(Set<Operation> operations) {
        this.operations = operations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return isActive == card.isActive &&
                Objects.equals(id, card.id) &&
                Objects.equals(amount, card.amount) &&
                Objects.equals(password, card.password) &&
                Objects.equals(operations, card.operations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, isActive, password, operations);
    }
}
