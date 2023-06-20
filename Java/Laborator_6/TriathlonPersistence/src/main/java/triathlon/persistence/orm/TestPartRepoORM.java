package triathlon.persistence.orm;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import triathlon.model.Participant;

import java.util.List;

public class TestPartRepoORM {
    private static SessionFactory sessionFactory;


    static void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    public static void main(String ... arg) {
        // initializare
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml") // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch(Exception e){
            StandardServiceRegistryBuilder.destroy(registry);
            throw new RuntimeException(e);
        }

        Participant participant = new Participant("Michael","Brown",0);
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(participant);
            session.getTransaction().commit();
        }

        Iterable<Participant> participants;

        try(Session  session = sessionFactory.openSession()){
            session.beginTransaction();
            participants = session.createQuery("from Participant", Participant.class).list();
            if (!((List<Participant>) participants).isEmpty()) {
                for (Participant part : participants) {
                    if (part != null) {
                        System.out.println("Participant: " + part.getId() + " first_name " + part.getFirst_name() + " last_name " + part.getLast_name() + " points " + part.getPoints());
                    }
                }
            } else {
                System.out.println("Nu exista participanti in baza de date.");
            }
            session.getTransaction().commit();
        }


        close();
    }
}
