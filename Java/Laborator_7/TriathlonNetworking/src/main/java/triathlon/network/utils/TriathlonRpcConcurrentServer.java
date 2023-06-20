package triathlon.network.utils;

import triathlon.network.rpcprotocol.TriathlonClientRpcWorker;
import triathlon.services.IService;

import java.net.Socket;

public class TriathlonRpcConcurrentServer extends AbsConcurrentServer{
    private IService triathlonServer;

    public TriathlonRpcConcurrentServer(int port,IService triathlonServer) {
        super(port);
        this.triathlonServer = triathlonServer;
        System.out.println("Triathlon - TriathlonRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        TriathlonClientRpcWorker worker = new TriathlonClientRpcWorker(triathlonServer,client);
        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
