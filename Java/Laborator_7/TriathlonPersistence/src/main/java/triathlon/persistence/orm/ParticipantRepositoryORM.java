package triathlon.persistence.orm;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import triathlon.model.Participant;
import triathlon.persistence.ParticipantRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ParticipantRepositoryORM implements ParticipantRepository {
    private static SessionFactory sessionFactory;

    public ParticipantRepositoryORM(){
        initialize();
    }

    static void initialize(){
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exceptie " + e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close(){
        if(sessionFactory != null){
            sessionFactory.close();
        }
    }

    @Override
    public Participant findOne(Integer integer) throws IllegalArgumentException {
        //initialize();

        Participant participant = null;

        try(Session  session = sessionFactory.openSession()){
            session.beginTransaction();
            List<Participant> participants = session.createQuery("from Participant", Participant.class).list();
            for (Participant part : participants) {
                if(part.getId().equals(integer))
                    participant = new Participant(part.getId(),part.getFirst_name(), part.getLast_name(),part.getPoints());
            }
            session.getTransaction().commit();
        }

        // close();

        if(participant != null)
            System.out.println("Participant: first_name" + participant.getFirst_name() + " last_name " + participant.getLast_name() + " points " + participant.getPoints());
        else
            System.out.println("Participant null!!");

        return participant;
    }

    @Override
    public Iterable<Participant> getAll() {
        Iterable<Participant> participants;

        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            Stream<Participant> stream = session.createQuery("from Participant", Participant.class).stream();
//            participants = stream::iterator;
            participants = StreamSupport.stream(stream.spliterator(),false).toList();
            session.getTransaction().commit();
//            participants = StreamSupport.stream(stream.spliterator(),false).toList();
        }

        System.out.println("GET ALL: " + participants);
        participants.forEach(System.out::println);

        return participants;

    }

    @Override
    public void clear() {

    }

    @Override
    public void save(Participant entity) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Participant entity) {
        System.out.println("Am ajuns in update PARTICIPANT ");
        System.out.println("Pct nou: " + entity.getPoints());
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                Participant participant = session.load(Participant.class,entity.getId());
                participant.setPoints(entity.getPoints());
                tx.commit();

            } catch(RuntimeException ex){
                System.err.println("Eroare la update "+ex);
                if (tx!=null)
                    tx.rollback();
            }
        }
    }

    @Override
    public void delete(Integer integer) {

    }
}
