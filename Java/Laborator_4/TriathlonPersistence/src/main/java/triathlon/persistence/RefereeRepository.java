package triathlon.persistence;


import triathlon.model.Referee;

public interface RefereeRepository extends Repository<Integer, Referee> {
    Referee findBy(String username, String password);
}
