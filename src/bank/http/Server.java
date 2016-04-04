package bank.http;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import bank.Bank;
import bank.command.Command;
import bank.local.Driver;

public class Server {
	
	private static Bank bank;

	public static void main(String[] args) throws IOException {

		Driver driver = new Driver();
		driver.connect(null);
		bank = driver.getBank();

		int port = 8888;

		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

		server.createContext("/bank", new CommandHandler());
		server.start();

	}

	static class CommandHandler implements HttpHandler {
		public void handle(HttpExchange exchange) throws IOException {
			ObjectInputStream in = null;
			ObjectOutputStream out = null;
			try {
				in = new ObjectInputStream(exchange.getRequestBody());
				System.out.println("Objekt einlesen");
				Command c = (Command) in.readObject();
				System.out.println("Objekt eingelesen");
				c.execute(bank);
				exchange.getResponseHeaders().add("Content-type", "text/html");
				exchange.sendResponseHeaders(200, 0);
				out = new ObjectOutputStream(exchange.getResponseBody());
				out.writeObject(c);
				out.flush();

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			}

		}
	}

}
