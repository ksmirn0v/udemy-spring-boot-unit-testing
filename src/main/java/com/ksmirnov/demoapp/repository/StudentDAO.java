package com.ksmirnov.demoapp.repository;

import com.ksmirnov.demoapp.models.CollegeStudent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDAO extends CrudRepository<CollegeStudent, Integer> {

    CollegeStudent findByEmailAddress(String email);
}
