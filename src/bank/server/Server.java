package bank.server;

import bank.Bank;
import bank.local.Driver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by julian on 24.02.16.
 */
public class Server {

    private static ServerSocket serverSocket;

    private Bank bank;

    private List<ConnectionHandler> connections = new ArrayList<ConnectionHandler>();


    public static void main(String[] args) {
        Server s = new Server();
        s.runServer();
    }

    public Server() {
        Driver driver = new Driver();
        driver.connect(null);
        bank = driver.getBank();

        try {
            serverSocket = new ServerSocket(6789);
            System.out.println("server started on Port 6789");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        runServer();
    }

    public void runServer() {
        ExecutorService pool = Executors.newCachedThreadPool();
        while (true) {
            try {
                Socket s = serverSocket.accept();
                pool.execute(new ConnectionHandler(s, bank));
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
