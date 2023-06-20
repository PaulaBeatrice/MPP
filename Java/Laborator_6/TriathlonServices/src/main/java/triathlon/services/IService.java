package triathlon.services;

import triathlon.model.Participant;
import triathlon.model.Referee;
import triathlon.model.Result;

import java.util.List;

public interface IService {
    Referee login(Referee referee, IObserver client) throws Exception;
    public Iterable<Participant> getAllParticipants()throws Exception;
    public Iterable<Result> getAllResults()throws Exception;
    public List<Participant> getNotedParticipants(Referee referee)throws Exception;
    public List<Participant> getNotNoted(Referee referee)throws Exception;
    public Participant getPartById(int id) throws Exception;
    public void addScore(Result result)throws Exception;
    public void logout(Referee referee, IObserver client) throws Exception;
}
