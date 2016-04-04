package bank.soap;

import javax.xml.ws.Endpoint;

import bank.Bank;
import bank.local.Driver;

/**
 * Created by julian on 24.02.16.
 */
public class Server {

	private Bank bank;

	private final int PORT = 1234;

	public static void main(String[] args) {
		Server s = new Server();
		s.runServer();
	}

	public Server() {
		Driver driver = new Driver();
		driver.connect(null);
		bank = driver.getBank();
		runServer();
	}

	public void runServer() {
		String url = "http://127.0.0.1:" + PORT + "/bankHandler";
		SoapBankServiceImpl service = new SoapBankServiceImpl(bank);
		Endpoint.publish(url, service);
		System.out.println("service published");
	}
}
