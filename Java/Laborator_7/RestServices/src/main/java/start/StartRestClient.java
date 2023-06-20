package start;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import rest.client.ResultClient;
import triathlon.model.Referee;
import triathlon.model.Result;
import triathlon.model.Participant;

public class StartRestClient {
    private final static ResultClient resultClient =new ResultClient();
    public static void main(String[] args) {
        RestTemplate restTemplate=new RestTemplate();
        Result result=new Result(new Participant(13,"A","A",0),new Referee(14,"B","B","B","B","swimming"),"swimming",10);
        try{
              Result res= restTemplate.postForObject("http://localhost:8080/triathlon/results",result, Result.class);

              System.out.println("Result received "+res);

            // System.out.println(restTemplate.postForObject("http://localhost:8080/chat/users",userT, User.class));
            //System.out.println( restTemplate.postForObject("http://localhost:8080/chat/users",userT, User.class));

            show(()-> {
                try {
                    System.out.println(resultClient.create(result));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            show(()->{
                Result[] results= new Result[0];
                try {
                    results = resultClient.getAll();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                for(Result r:results){
                    System.out.println(r.getId()+": "+r.getPoints());
                }
            });
        }catch(RestClientException ex){
            System.out.println("Exception ... "+ex.getMessage());
        }

        show(()-> {
            try {
                System.out.println(resultClient.getById("10"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }



    private static void show(Runnable task) {
        try {
            task.run();
        } catch (Exception e) {
            System.out.println("Exception "+ e);
        }
    }
}
