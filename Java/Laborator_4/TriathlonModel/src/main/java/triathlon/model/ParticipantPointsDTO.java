package triathlon.model;

public class ParticipantPointsDTO extends Entity<Integer>{
    private Participant participant;
    private int points;

    public ParticipantPointsDTO(Integer integer, Participant participant, int points) {
        super(integer);
        this.participant = participant;
        this.points = points;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return  participant.getFirst_name() + " "+participant.getLast_name()+ " has "+points+" points.";

    }

}