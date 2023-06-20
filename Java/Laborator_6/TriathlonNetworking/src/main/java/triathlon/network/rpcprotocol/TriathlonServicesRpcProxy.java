package triathlon.network.rpcprotocol;

import triathlon.model.Participant;
import triathlon.model.Referee;
import triathlon.model.Result;
import triathlon.services.IObserver;
import triathlon.services.IService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class TriathlonServicesRpcProxy implements IService {
    private String host;
    private int port;
    private IObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public TriathlonServicesRpcProxy(String host,int port){
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingDeque<Response>();
    }

    @Override
    public Referee login(Referee referee, IObserver client) throws Exception {
        initializeConnection();
        Request req=new Request.Builder().type(RequestType.LOGIN).data(referee).build();
        sendRequest(req);
        Response response=readResponse();
        if(response.type()==ResponseType.OK){
            this.client=client;
            Referee r = (Referee) response.data();
            return r;
        }
        if(response.type()==ResponseType.ERROR){
            String err = response.data().toString();
            closeConnection();
            throw new Exception(err);
        }
        return null;
    }



    @Override
    public Iterable<Participant> getAllParticipants() throws Exception {
        Request req = new Request.Builder().type(RequestType.GET_TOP_PARTICIPANTS).data(1).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type()==ResponseType.ERROR){
            String err=response.data().toString();
            throw new Exception(err);
        }
        Iterable<Participant> participants=(Iterable<Participant>) response.data();
        return participants;
    }

    @Override
    public Iterable<Result> getAllResults() throws Exception {
        Request req = new Request.Builder().type(RequestType.GET_TOP_PARTICIPANTS).data(1).build();
        sendRequest(req);
        Response response =readResponse();
        if(response.type()==ResponseType.ERROR){
            String err=response.data().toString();
            throw new Exception(err);
        }
        Iterable<Result> results=(Iterable<Result>) response.data();
        return results;
    }

    @Override
    public List<Participant> getNotedParticipants(Referee referee) throws Exception {
        Request req = new Request.Builder().type(RequestType.GET_NOTED).data(referee).build();
        sendRequest(req);
        Response response=readResponse();
        if(response.type()==ResponseType.ERROR){
            String err = response.data().toString();
            throw new Exception(err);
        }
        List<Participant> participants = (List<Participant>) response.data();
        return participants;
    }

    @Override
    public List<Participant> getNotNoted(Referee referee) throws Exception {
        Request req = new Request.Builder().type(RequestType.GET_NOT_NOTED).data(referee).build();
        sendRequest(req);
        Response response=readResponse();
        if(response.type()==ResponseType.ERROR){
            String err = response.data().toString();
            throw new Exception(err);
        }
        List<Participant> participants = (List<Participant>) response.data();
        return participants;
    }

    @Override
    public Participant getPartById(int id) throws Exception {
        Request req = new Request.Builder().type(RequestType.GET_PART).data(id).build();
        sendRequest(req);
        Response response = readResponse();
        if(response.type()==ResponseType.ERROR){
            String err = response.data().toString();
            throw new Exception(err);
        }
        Participant participant = (Participant) response.data();
        return participant;
    }

    @Override
    public void addScore(Result result) throws Exception {
        Request req=new Request.Builder().type(RequestType.ADDSCORE).data(result).build();
        sendRequest(req);
        System.err.println("After sending addScore");
        Response response = readResponse();
        System.err.println("Received addScore" +response);
        if(response.type()==ResponseType.ERROR){
            String err=response.data().toString();
            throw new Exception(err);
        }
    }

    @Override
    public void logout(Referee id, IObserver client) throws Exception {
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(id).build();
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if(response.type()==ResponseType.ERROR){
            String err=response.data().toString();
            throw new Exception(err);
        }
    }

    private void initializeConnection() throws Exception {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Response readResponse() throws Exception {
        Response response=null;
        try{
            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void sendRequest(Request request)throws Exception {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new Exception("Error sending object "+e);
        }

    }

    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Response response) throws Exception {
        if (response.type()== ResponseType.NEW_SCORE){
            Participant participant = (Participant) response.data();
//            Participant part = getPartById(result.getParticipant().getId());
            try {
                client.notifyAddedPoints(participant);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response){
        return response.type()== ResponseType.NEW_SCORE;
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.err.println("response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{
                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Reading error "+e);
                } catch (Exception e) {
                    System.err.println("Reading error "+e);
                }
            }
        }
    }
}
