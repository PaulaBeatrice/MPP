package com.example.laborator_3.service;

import com.example.laborator_3.domain.Participant;
import com.example.laborator_3.domain.Referee;
import com.example.laborator_3.domain.Result;
import com.example.laborator_3.repository.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Service {
    private RefereeRepository refereeRepository;
    private ParticipantRepository participantRepository;
    private ResultRepository resultRepository;

    public Service(RefereeRepository refereeRepository, ParticipantRepository participantRepository, ResultRepository resultRepository) {
        this.refereeRepository = refereeRepository;
        this.participantRepository = participantRepository;
        this.resultRepository = resultRepository;
    }

    public Iterable<Referee> getAllReferees() {
        return refereeRepository.getAll();
    }

    public Iterable<Participant> getAllParticipants(){
        return participantRepository.getAll();
    }

    public Iterable<Result> getAllResults(){
        return resultRepository.getAll();
    }

    public List<Participant> getEvaluatedParticipants(Referee referee){return resultRepository.getEvaluatedParticipants(referee);}

    public void addScore(Result result){resultRepository.save(result);}

    public Participant getParticipantById(int id){return participantRepository.findOne(id);}

    public List<Participant> getTopParticipants(String activity){
        List<Participant> participants = new ArrayList<Participant>();
        for(Participant p: getAllParticipants()){
            int points = 0;
            for(Result r: getAllResults()){
                if(r.getParticipant().getId().equals(p.getId()) && r.getActivity().equals(activity))
                    points += r.getPoints();
            }
            Participant new_p = new Participant(p.getId(),p.getFirst_name(),p.getLast_name(),points);
            participants.add(new_p);
        }
        return participants;
    }

    public List<Participant> getTopParts(Referee referee) {
        List<Participant> participants = new ArrayList<Participant>();
        for (Participant p : getAllParticipants()) {
            for (Result r : getAllResults()) {
                if (r.getReferee().getId().equals(referee.getId()) && r.getParticipant().getId().equals(p.getId())) {
                    Participant new_p = new Participant(p.getId(), p.getFirst_name(), p.getLast_name(), r.getPoints());
                    participants.add(new_p);
                }
            }
        }
        return participants;
    }

}
