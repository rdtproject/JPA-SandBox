package com.tpolm.jpasandpit.entity;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class FullTimeEmployee extends Employee {

    private BigDecimal salary;

    public FullTimeEmployee() {
    }

    public FullTimeEmployee(String name, BigDecimal salary) {
        super(name);
        this.salary = salary;
    }
}
