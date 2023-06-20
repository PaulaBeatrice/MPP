package triathlon.server;

import triathlon.model.Participant;
import triathlon.model.Referee;
import triathlon.model.Result;
import triathlon.persistence.ParticipantRepository;
import triathlon.persistence.RefereeRepository;
import triathlon.persistence.ResultRepository;
import triathlon.services.IObserver;
import triathlon.services.IService;

import java.sql.Ref;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceImpl implements IService {
    private RefereeRepository refRepo;
    private ParticipantRepository partRepo;
    private ResultRepository resRepo;
    private Map<Integer,IObserver> loggedClients;

    public ServiceImpl(RefereeRepository R, ParticipantRepository P, ResultRepository RR){
        refRepo = R;
        partRepo = P;
        resRepo = RR;
        loggedClients = new ConcurrentHashMap<>();
    }


    @Override
    public synchronized Referee login(Referee referee, IObserver client) throws Exception {
        System.out.println("REFEREE PRIMIT: " + referee);
        Referee referee1 = refRepo.findBy(referee.getUsername(),referee.getPassword());
        System.err.println("REFEREE GASIT: "+referee1.getUsername()+ " " + referee1.getPassword() + " " + referee1.getId());
//        if(referee1!=null){
//            if(loggedClients.get(referee.getId())!=null)
//                throw new Exception("Referee already logged in!");
//            loggedClients.put(referee.getId(),client);
//        }
//        else
//            throw new Exception("Authetification failed!");
        loggedClients.put(referee1.getId(),client);
        return referee1;
    }



    private final int defaultThreadsNo=4;

    public synchronized void notifyAddedPoints(Participant participant){
        System.err.println("AM AJUNS AICI");
        Iterable<Referee> referees = refRepo.getAll();
        ExecutorService executorService= Executors.newFixedThreadPool(this.defaultThreadsNo);
        loggedClients.forEach((id,client)-> {
            System.err.println("AM AJUNS IN FOR " + id);
        //    IObserver obs=loggedClients.get(id);
            executorService.execute(()->{
                try{
                    System.err.println("Notifying ["+id+"]");
                    client.notifyAddedPoints(participant);
                }catch (Exception e){
                    System.err.println("Error notifying referee with ID: " + id + " Message: " + e.getMessage());
                }
            });
        });
        executorService.shutdown();
    }

    @Override
    public synchronized Iterable<Participant> getAllParticipants() throws Exception {
        return partRepo.getAll();
    }

    @Override
    public Iterable<Result> getAllResults() throws Exception {
        return resRepo.getAll();
    }

    @Override
    public List<Participant> getNotedParticipants(Referee referee) throws Exception {
        return resRepo.getNotedParticipantsRepo(referee);
    }

    @Override
    public List<Participant> getNotNoted(Referee referee) throws Exception {
        return resRepo.getNotNotedParticipantsRepo(referee);
    }

    @Override
    public Participant getPartById(int id) throws Exception {
        return partRepo.findOne(id);
    }

    @Override
    public void addScore(Result result) throws Exception {
        resRepo.save(result);
        Participant participant = partRepo.findOne(result.getParticipant().getId());
        notifyAddedPoints(participant);
    }

    @Override
    public void logout(Referee referee, IObserver client) throws Exception {
        IObserver localClient=loggedClients.remove(referee.getId());
        if (localClient==null)
            throw new Exception("Referee "+referee.getId()+" is not logged in.");
    }


}
