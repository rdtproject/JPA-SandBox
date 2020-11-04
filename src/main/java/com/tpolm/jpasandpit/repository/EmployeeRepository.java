package com.tpolm.jpasandpit.repository;

import com.tpolm.jpasandpit.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;


@Repository
@Transactional
public class EmployeeRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepository.class);

    @Autowired
    private EntityManager em;

    public void insert(Employee employee) {
        em.persist(employee);
    }

    public List<Employee> getEmployees() {
        return em.createQuery("select i from Employee i", Employee.class).getResultList();
    }

}
