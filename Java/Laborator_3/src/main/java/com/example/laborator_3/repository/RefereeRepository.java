package com.example.laborator_3.repository;



import com.example.laborator_3.domain.Referee;

import java.util.List;

public interface RefereeRepository extends Repository<Integer, Referee>{
    List<Referee> findByActivity(String activity);
}
