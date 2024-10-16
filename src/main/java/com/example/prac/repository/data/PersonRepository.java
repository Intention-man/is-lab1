package com.example.prac.repository.data;

import com.example.prac.model.dataEntity.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonRepository extends CrudRepository<Person, Long>,
        PagingAndSortingRepository<Person, Long> {
}
