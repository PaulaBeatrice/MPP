package triathlon.model;

public class Result implements Entity<Integer> {
    private Participant participant;
    private Referee referee;
    private String activity;
    private int points;


    public Result(Integer integer, Participant participant, Referee referee, String activity, int points) {
        this.participant = participant;
        this.referee = referee;
        this.activity = activity;
        this.points = points;
        this.setId(integer);
    }

    public Result(){}

    public Result(Participant participant, Referee referee, String activity, int points) {
        this.participant = participant;
        this.referee = referee;
        this.activity = activity;
        this.points = points;
    }


    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Referee getReferee() {
        return referee;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Result{" +
                "participant=" + participant +
                ", referee=" + referee +
                ", activity='" + activity + '\'' +
                ", points=" + points +
                '}';
    }

    private int id;
    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer integer) {
        this.id = integer;
    }
}

