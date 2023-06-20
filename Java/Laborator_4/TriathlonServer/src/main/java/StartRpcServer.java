import triathlon.model.Participant;
import triathlon.model.Referee;
import triathlon.network.utils.AbstractServer;
import triathlon.network.utils.ServerException;
import triathlon.network.utils.TriathlonRpcConcurrentServer;
import triathlon.persistence.ParticipantRepository;
import triathlon.persistence.RefereeRepository;
import triathlon.persistence.ResultRepository;
import triathlon.persistence.jdbc.ParticipantDBRepository;
import triathlon.persistence.jdbc.RefereeDBRepository;
import triathlon.persistence.jdbc.ResultDBRepository;
import triathlon.server.ServiceImpl;
import triathlon.services.IService;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties "+e);
            return;
        }
        RefereeDBRepository refereeDBRepository=new RefereeDBRepository(serverProps);
        ParticipantDBRepository participantDBRepository = new ParticipantDBRepository(serverProps);
        ResultDBRepository resultDBRepository = new ResultDBRepository(serverProps,refereeDBRepository,participantDBRepository);
        IService sv = new ServiceImpl(refereeDBRepository,participantDBRepository,resultDBRepository);




//        for(Referee ref: refereeDBRepository.getAll()){
//            System.out.println(ref);
//        }
//        System.out.println("PARTICIPANTS");
//        for(Participant p: participantDBRepository.getAll()){
//            System.out.println("==>" +  p);
//        }
//        int cnt = 0;
//        Referee referee = null;
//        for(Referee ref: refereeDBRepository.getAll()){
//            if(cnt == 3)
//                referee = ref;
//            cnt++;
//        }
//        System.out.println("REFEREE " + referee);

//        System.out.println("NOTED PARTICIPANTS");
//        for(Participant p: resultDBRepository.getNotedParticipantsRepo(referee)){
//            System.out.println("==>" + p);
//        }
//
//        System.out.println("NOT NOTED PARTICIPANTS");
//        for(Participant p: resultDBRepository.getNotNotedParticipantsRepo(referee)){
//            System.out.println("==>" + p);
//        }



        int trServerPort=defaultPort;
        try {
            trServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+trServerPort);
        AbstractServer server = new TriathlonRpcConcurrentServer(trServerPort, sv);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(ServerException e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }
    }
}

