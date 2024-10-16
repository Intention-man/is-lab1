package com.example.prac.repository.data;

import com.example.prac.model.dataEntity.Dragon;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DragonRepository extends CrudRepository<Dragon, Long>,
        PagingAndSortingRepository<Dragon, Long> {
}
