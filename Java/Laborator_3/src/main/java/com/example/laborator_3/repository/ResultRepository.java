package com.example.laborator_3.repository;



import com.example.laborator_3.domain.Participant;
import com.example.laborator_3.domain.Referee;
import com.example.laborator_3.domain.Result;

import java.util.List;

public interface ResultRepository extends Repository<Integer, Result> {
    List<Participant> getEvaluatedParticipants(Referee referee);
}
