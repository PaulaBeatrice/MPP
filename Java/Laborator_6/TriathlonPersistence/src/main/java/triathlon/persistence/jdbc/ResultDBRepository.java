package triathlon.persistence.jdbc;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import triathlon.model.Participant;
import triathlon.model.Referee;
import triathlon.model.Result;
import triathlon.persistence.ParticipantRepository;
import triathlon.persistence.RefereeRepository;
import triathlon.persistence.ResultRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ResultDBRepository implements ResultRepository {
    private JdbcUtils dbUtils;

    private RefereeRepository refereeRepository;
    private ParticipantRepository participantRepository;

    private static final Logger logger = LogManager.getLogger();

    public ResultDBRepository(Properties props, RefereeDBRepository refereeDBRepository1, ParticipantRepository participantDBRepository1){
        logger.info("Initializing RefereeDBRepository with propertiesL {} ",props);
        dbUtils=new JdbcUtils(props);
        refereeRepository = refereeDBRepository1;
        participantRepository = participantDBRepository1;
    }

    @Override
    public Result findOne(Integer integer) throws IllegalArgumentException {
        logger.traceEntry();
        Connection conn = dbUtils.getConnection();
        Result result = null;
        try(PreparedStatement preSmt= conn.prepareStatement("select * from Results where id=?")){
            preSmt.setInt(1,integer);
            try(ResultSet resultSet = preSmt.executeQuery()){
                if(!resultSet.next())
                    return null;

                int id = resultSet.getInt("id"); // id-ul resultului
                int id_referee = resultSet.getInt("id_referee");
                Referee referee = this.refereeRepository.findOne(id_referee);
                int id_participant = resultSet.getInt("id_participant");
                Participant participant = this.participantRepository.findOne(id_participant);

                String activity = resultSet.getString("activity");
                int points = resultSet.getInt("points");


                result = new Result(id, participant,referee,activity,points);
            }
        }catch(SQLException e){
            logger.error(e);
            System.err.println("Error DB "+e);
        }
        logger.traceExit(result);
        return result;
    }

    @Override
    public Iterable<Result> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Result> resultList = new ArrayList<>();
        try(PreparedStatement preSmt=con.prepareStatement("select * from Results")){
            try(ResultSet resultSet = preSmt.executeQuery()){
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    int id_ref = resultSet.getInt("id_referee");
                    Referee referee = this.refereeRepository.findOne(id_ref);
                    int id_part = resultSet.getInt("id_participant");
                    Participant participant = this.participantRepository.findOne(id_part);
                    int points = resultSet.getInt("points");
                    String activity = resultSet.getString("activity");
                    Result result = new Result(id,participant,referee,activity,points);
                    resultList.add(result);
                }
            }
        } catch(SQLException e){
            logger.error(e);
            System.err.println("Error DB "+e);
        }
        logger.traceExit(resultList);
        return resultList;
    }

    @Override
    public void clear() {

    }

    @Override
    public void save(Result entity) {
        logger.traceEntry("saving task{} ", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Results (id_referee,id_participant,points,activity) values (?,?,?,?)")){
            preStmt.setInt(1,entity.getReferee().getId());
            preStmt.setInt(2,entity.getParticipant().getId());
            preStmt.setInt(3,entity.getPoints());
            preStmt.setString(4,entity.getActivity());
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
            // actualizam si punctajul participantului
            int new_points = entity.getParticipant().getPoints() + entity.getPoints();
            Participant new_participant = new Participant(entity.getParticipant().getId(),entity.getParticipant().getFirst_name(),entity.getParticipant().getLast_name(),new_points);
            this.participantRepository.update(new_participant);

        } catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Result entity) {
        logger.traceEntry();
        Connection conn = dbUtils.getConnection();
        int old_points = this.participantRepository.findOne(entity.getParticipant().getId()).getPoints(); // puntajul vechi
        try(PreparedStatement preSmt= conn.prepareStatement("update Results set id_referee = ?, id_participant = ?, points = ?, activity = ? where id=?")){
            preSmt.setInt(1,entity.getReferee().getId());
            preSmt.setInt(2,entity.getParticipant().getId());
            preSmt.setInt(3,entity.getPoints());
            preSmt.setString(4,entity.getActivity());
            preSmt.setInt(5,entity.getId());
            preSmt.executeUpdate();
            // actualizam si punctajul participantului (scadem punctajul vechi, adunam punctajul nou)
            int new_points = entity.getParticipant().getPoints() - old_points + entity.getPoints();
            Participant new_participant = new Participant(entity.getParticipant().getId(),entity.getParticipant().getFirst_name(),entity.getParticipant().getLast_name(),new_points);
            this.participantRepository.update(new_participant);
        }catch(SQLException e){
            logger.error(e);
            System.err.println("Error DB "+e);
        }
        logger.traceExit(entity);
    }

    @Override
    public void delete(Integer id) {

    }


    @Override
    public List<Participant> getNotedParticipantsRepo(Referee referee) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Participant> participantList = new ArrayList<>();
        try(PreparedStatement preSmt=con.prepareStatement("select * from Results where id_referee=?")){
            preSmt.setInt(1,referee.getId());
            try(ResultSet resultSet = preSmt.executeQuery()){
                while(resultSet.next()){
                    int id_part = resultSet.getInt("id_participant");
                    Participant participant = this.participantRepository.findOne(id_part);
                    Participant new_part = new Participant(participant.getId(),participant.getFirst_name(),participant.getLast_name(),resultSet.getInt("points"));
                    participantList.add(new_part);
                }
            }
        } catch(SQLException e){
            logger.error(e);
            System.err.println("Error DB "+e);
        }
        logger.traceExit(participantList);
        return participantList;
    }

    @Override
    public List<Participant> getNotNotedParticipantsRepo(Referee referee) {
        List<Participant> participantList = new ArrayList<>();
        for(Participant p:participantRepository.getAll()){
            // pentru fiecare participant verific daca exista un result cu participantul p si referee r
            // daca nu exista il adaug in lista
            int gasit = 0;
            for (Result r:getAll()){
                if(r.getParticipant().getId().equals(p.getId()) && r.getReferee().getId().equals(referee.getId()))
                    gasit = 1;
            }
            if(gasit == 0)
                participantList.add(p);
        }
        return participantList;
    }
}
