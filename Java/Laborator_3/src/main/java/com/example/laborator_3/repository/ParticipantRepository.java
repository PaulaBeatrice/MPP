package com.example.laborator_3.repository;


import com.example.laborator_3.domain.Participant;

import java.util.List;

public interface ParticipantRepository extends Repository<Integer, Participant>{
    List<Participant> findByActivityOrderedDescByPoints(String activity);
}
