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
import java.util.ArrayList;
import java.util.List;

import static triathlon.network.rpcprotocol.ResponseType.*;

public class TriathlonClientRpcWorker implements  Runnable, IObserver {
    private IService server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public TriathlonClientRpcWorker(IService server, Socket connection){
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private Response handleRequest(Request request) {
        Response response = null;
        if (request.type()== RequestType.LOGIN){
            System.out.println("Login request ..."+request.type());
            Referee referee = (Referee)request.data();
            System.out.println(referee);
            try {
                Referee foundRefere = server.login(referee, this);
                return new Response.Builder().type(OK).data(foundRefere).build();
            } catch (Exception e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.LOGOUT){
            System.out.println("Logout request");
            int referee = (int) request.data();
            try {
                server.logout(referee, this);
                connected=false;
                return okResponse;

            } catch (Exception e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.ADDSCORE){
            System.err.println("AddedScoreRequest ...");
            Result result = (Result)request.data();
            try {
                server.addScore(result);
                return new Response.Builder().type(OK).data(result).build();
            } catch (Exception e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if(request.type() == RequestType.GET_TOP_PARTICIPANTS){
            System.out.println("GetTopParticipants...");
            int ref=(int)request.data();
            try{
                Iterable<Participant> topParticipants = server.getAllParticipants();
                return new Response.Builder().type(GET_TOP_PARTICIPANTS).data(topParticipants).build();
            }catch(Exception e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if(request.type() == RequestType.GET_NOTED){
            System.out.println("GetNotedParticipants...");
            Referee ref=(Referee)request.data();
            try{
                List<Participant> participants = server.getNotedParticipants(ref);
                return new Response.Builder().type(GET_NOTED).data(participants).build();
            }catch(Exception e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if(request.type() == RequestType.GET_NOT_NOTED){
            System.out.println("GetNotNotedParticipants...");
            Referee ref=(Referee)request.data();
            try{
                List<Participant> participants = server.getNotNoted(ref);
                return new Response.Builder().type(GET_NOT_NOTED).data(participants).build();
            }catch(Exception e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if(request.type() == RequestType.GET_PART){
            System.out.println("Participant by id...");
            int id = (int)request.data();
            try{
                Participant participant=server.getPartById(id);
                return new Response.Builder().type(GET_PART).data(participant).build();
            }catch(Exception e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        return response;
    }

    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();
    private static Response errorResponse=new Response.Builder().type(ResponseType.ERROR).build();


    private void sendResponse(Response resp) throws IOException{
        System.out.println("Adding score"+resp);
        synchronized (output){
         output.writeObject(resp);
         output.flush();
        }
    }


    @Override
    public void notifyAddedPoints(Participant participant) throws Exception {
        Response resp = new Response.Builder().type(ResponseType.NEW_SCORE).data(participant).build();
        System.out.println("Score added");
        try{
            sendResponse(resp);
        }catch(IOException e){
            throw new Exception("Adding score error");
        }
    }
}
