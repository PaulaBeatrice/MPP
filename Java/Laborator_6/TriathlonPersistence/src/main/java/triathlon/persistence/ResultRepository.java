package triathlon.persistence;


import triathlon.model.Participant;
import triathlon.model.Referee;
import triathlon.model.Result;

import java.util.List;

public interface ResultRepository extends Repository<Integer, Result> {
    List<Participant> getNotedParticipantsRepo(Referee referee);
    List<Participant> getNotNotedParticipantsRepo(Referee referee);
}
