package bank.http;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import bank.Bank;
import bank.command.Command;

public class RequestHandler implements HttpHandler {

		private Bank bank;

		public RequestHandler(Bank b) {
			this.bank = b;
		}

		@Override
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
				out.writeObject(c.getReturnValue());
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