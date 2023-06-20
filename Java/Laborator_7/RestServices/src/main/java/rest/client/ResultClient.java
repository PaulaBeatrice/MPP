package rest.client;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import triathlon.model.Result;

import java.util.concurrent.Callable;

public class ResultClient {
    public static final String URL = "http://localhost:8080/triathlon/results";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) throws Exception {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new Exception(e);
        }
    }

    public Result[] getAll() throws Exception {
        return execute(() -> restTemplate.getForObject(URL, Result[].class));
    }

    public Result getById(String id) throws Exception {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), Result.class));
    }

    public Result create(Result result) throws Exception {
        return execute(() -> restTemplate.postForObject(URL, result, Result.class));
    }

    public void update(Result result) throws Exception {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, result.getId()), result);
            return null;
        });
    }

    public void delete(String id) throws Exception {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }

}
