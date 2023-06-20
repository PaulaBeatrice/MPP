package triathlon.persistence;


import org.springframework.stereotype.Component;
import triathlon.model.Referee;


public interface RefereeRepository extends Repository<Integer, Referee> {
    Referee findBy(String username, String password);
}
