package triathlon.services;

import triathlon.model.Participant;
import triathlon.model.Result;

public interface IObserver {
    void notifyAddedPoints(Participant participant) throws Exception;
}

