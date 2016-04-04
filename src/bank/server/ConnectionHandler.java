package bank.server;

import bank.Bank;
import bank.command.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by julian on 24.02.16.
 */
public class ConnectionHandler implements Runnable {

    private Socket clientSocket;

    private Bank bank;

    ObjectOutputStream out;

    ObjectInputStream in;

    public ConnectionHandler(Socket s, Bank b) throws IOException {
        clientSocket = s;
        bank = b;
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
        System.out.println("Client connected to Server");
    }


    @Override
    public void run() {
        boolean active = true;


        while (active) {
            try {
                Command c = (Command) in.readObject();
                c.execute(bank);
                out.writeObject(c.getReturnValue());
                out.flush();
            } catch (Exception e) {
                active = false;
                System.out.println(e.getMessage());
            }
        }
    }
}
