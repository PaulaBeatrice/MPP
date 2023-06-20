package triathlon.persistence.jdbc;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import triathlon.persistence.RefereeRepository;
import triathlon.model.Referee;

import java.net.UnknownServiceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class RefereeDBRepository implements RefereeRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public RefereeDBRepository(Properties props){
        logger.info("Initializing RefereeDBRepository with propertiesL {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Referee findOne(Integer i) throws IllegalArgumentException {
        logger.traceEntry();
        Connection conn = dbUtils.getConnection();
        Referee referee = null;
        try(PreparedStatement preSmt= conn.prepareStatement("select * from Referees where id=?")){
            preSmt.setInt(1,i);
            try(ResultSet resultSet = preSmt.executeQuery()){
                if(!resultSet.next())
                    return null;

                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String activity = resultSet.getString("activity");

                referee = new Referee(id,username,password,first_name,last_name,activity);
            }
        }catch(SQLException e){
            logger.error(e);
            System.err.println("Error DB "+e);
        }
        logger.traceExit(referee);
        return referee;
    }

    @Override
    public Iterable<Referee> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Referee> refereeList = new ArrayList<>();
        try(PreparedStatement preSmt=con.prepareStatement("select * from Referees")){
            try(ResultSet resultSet = preSmt.executeQuery()){
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String first_name = resultSet.getString("first_name");
                    String last_name = resultSet.getString("last_name");
                    String activity = resultSet.getString("activity");

                    Referee referee = new Referee(id,username,password,first_name,last_name,activity);
                    refereeList.add(referee);
                }
            }
        } catch(SQLException e){
            logger.error(e);
            System.err.println("Error DB "+e);
        }
        logger.traceExit(refereeList);
        return refereeList;
    }

    @Override
    public void clear() {

    }

    @Override
    public void save(Referee entity) {
        logger.traceEntry("saving task{} ", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Referees (username,password,first_name,last_name,activity) values (?,?,?,?,?)")){
            preStmt.setString(1,entity.getUsername());
            preStmt.setString(2,entity.getPassword());
            preStmt.setString(3,entity.getFirst_name());
            preStmt.setString(4,entity.getLast_name());
            preStmt.setString(5,entity.getActivity());
            int result=preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Referee entity) {
        logger.traceEntry();
        Connection conn = dbUtils.getConnection();
        try(PreparedStatement preSmt= conn.prepareStatement("update Referees set username = ?, password = ?, first_name = ?, last_name = ?, activity = ? where id=?")){
            preSmt.setString(1,entity.getUsername());
            preSmt.setString(2,entity.getPassword());
            preSmt.setString(3,entity.getFirst_name());
            preSmt.setString(4,entity.getLast_name());
            preSmt.setString(5,entity.getActivity());
            preSmt.setInt(6,entity.getId());
            preSmt.executeUpdate();
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
    public Referee findBy(String username, String password) {
        logger.traceEntry();
        Connection conn = dbUtils.getConnection();
        Referee referee = null;
        System.out.println("REPOSITORY " + username + " " + password);
        try(PreparedStatement preSmt= conn.prepareStatement("select * from Referees where username=? and password=?")){
            preSmt.setString(1,username);
            preSmt.setString(2,password);
            try(ResultSet resultSet = preSmt.executeQuery()){
                if(!resultSet.next())
                    return null;

                int id = resultSet.getInt("id");
             //   String username = resultSet.getString("username");
             //   String password = resultSet.getString("password");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String activity = resultSet.getString("activity");

                referee = new Referee(id,username,password,first_name,last_name,activity);
            }
        }catch(SQLException e){
            logger.error(e);
            System.err.println("Error DB "+e);
        }
        logger.traceExit(referee);
        System.out.println("REFEREE REPOSITORY GASIT: " + referee);
        return referee;



//        logger.traceEntry();
//        Connection conn = dbUtils.getConnection();
//        Referee referee = null;
//        System.out.println("REPOSITORY " + username + " " + password);
//        try(PreparedStatement preSmt= conn.prepareStatement("select * from Referees where username=? and password=?")){
//            preSmt.setString(1,username);
//            preSmt.setString(1,password);
//            try(ResultSet resultSet = preSmt.executeQuery()){
//                if(!resultSet.next())
//                    return null;
//                int id = resultSet.getInt("id");
//                String first_name = resultSet.getString("first_name");
//                String last_name = resultSet.getString("last_name");
//                String activity = resultSet.getString("activity");
//                referee = new Referee(id,username,password,first_name,last_name,activity);
//            }
//        }catch(SQLException e){
//            logger.error(e);
//            System.err.println("Error DB "+e);
//        }
//        logger.traceExit(referee);
//        System.out.println("REFEREE REPOSITORY GASIT: " + referee);
//        return referee;
    }
}
