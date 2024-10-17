package com.example.prac.repository.data;

import com.example.prac.model.dataEntity.Dragon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DragonRepository extends JpaRepository<Dragon, Long> {
}