package bank.http;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import bank.InactiveException;
import bank.OverdrawException;
import bank.command.Command;
import bank.command.impl.CheckActive;
import bank.command.impl.CreateAccount;
import bank.command.impl.Deposit;
import bank.command.impl.GetAccountNumbers;
import bank.command.impl.GetBalance;
import bank.command.impl.GetOwner;
import bank.command.impl.RemoveAccount;
import bank.command.impl.Transfer;
import bank.command.impl.Withdraw;

public class Driver implements bank.BankDriver {
	private Bank bank = null;

	static ObjectOutputStream out;

	static ObjectInputStream in;

	private static String postUrl;

	@Override
	public void connect(String[] args) throws IOException {
		String hostname = args[0];
		int port = Integer.parseInt(args[1]);
		postUrl = String.format("http://%s:%s/bank", hostname, port);

		// socket = new Socket(args[0], Integer.parseInt(args[1]));
		// out = new ObjectOutputStream(socket.getOutputStream());
		// in = new ObjectInputStream(socket.getInputStream());
		// out = new ObjectOutputStream(c.getOutputStream());
		// in = new ObjectInputStream(c.getInputStream());
		bank = new Bank();
		System.out.println("connected...");
	}

	public static Command getResponse(String addr, Command cmd) throws IOException, ClassNotFoundException {
		HttpURLConnection c = (HttpURLConnection) new URL(addr).openConnection();
		c.setRequestMethod("POST");
		c.setDoOutput(true);
		c.setDoInput(true);
		c.connect();

		ObjectOutputStream out = new ObjectOutputStream(c.getOutputStream());

		out.writeObject(cmd);
		out.flush();

		ObjectInputStream in = new ObjectInputStream(c.getInputStream());

		return (Command) in.readObject();
	}

	@Override
	public void disconnect() throws IOException {
		bank = null;
		System.out.println("disconnected...");
	}

	@Override
	public Bank getBank() {
		return bank;
	}

	static class Bank implements bank.Bank {

		private final Map<String, Account> accounts = new HashMap<>();

		@SuppressWarnings("unchecked")
		@Override
		public Set<String> getAccountNumbers() throws IOException {
			// out.writeObject(c);
			try {
				Command c = getResponse(postUrl, new GetAccountNumbers());
				return (Set<String>) c.getReturnValue();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				throw new Error(e);
			}
		}

		@Override
		public String createAccount(String owner) throws IOException {

			try {
				Command c = getResponse(postUrl, new CreateAccount(owner));
				return (String) c.getReturnValue();

			} catch (Exception e) {
				System.out.println(e.getMessage());
				throw new Error(e);
			}
		}

		@Override
		public boolean closeAccount(String number) {
			try {
				Command c = getResponse(postUrl, new CheckActive(number));

				boolean res = (boolean) c.getReturnValue();
				if (res) {
					Command c2 = getResponse(postUrl, new RemoveAccount(number));
					return (boolean) c2.getReturnValue();
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
				throw new Error(e);
			}
			return false;
		}

		@Override
		public bank.Account getAccount(String number) throws IOException {

			Account acc = null;
			try {
				Command c = getResponse(postUrl, new GetOwner(number));
				String owner = (String) c.getReturnValue();
				if (!accounts.containsKey(number)) {
					acc = new Account(owner);
					acc.number = number;
				}
			} catch (ClassNotFoundException e) {
				System.out.println(e.getMessage());
				throw new Error(e);
			}
			return acc;
		}

		@Override
		public void transfer(bank.Account from, bank.Account to, double amount)
				throws IOException, InactiveException, OverdrawException {

			try {
				Command c = getResponse(postUrl, new Transfer(from.getNumber(), to.getNumber(), amount));
				Exception e = (Exception) c.getReturnValue();
				if (e != null)
					throw e;

			} catch (Exception e) {
				System.out.println(e.getMessage());
				throw new Error(e);
			}
		}

	}

	static class Account implements bank.Account {
		private String number;
		private String owner;
		private double balance;
		private boolean active = true;

		Account(String owner) {
			this.owner = owner;
		}

		@Override
		public double getBalance() {
			try {
				Command c = getResponse(postUrl, new GetBalance(number));
				return (Double) c.getReturnValue();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				// throw new Error(e);
			}

			return -1;
		}

		@Override
		public String getOwner() {
			return owner;
		}

		@Override
		public String getNumber() {
			return number;
		}

		@Override
		public boolean isActive() {
			return active;
		}

		@Override
		public void deposit(double amount) throws InactiveException, IOException {
			try {
				Command c = getResponse(postUrl, new Deposit(number, amount));
				String e = (String) c.getReturnValue();
				if (InactiveException.class.getName().equals(e))
					throw new InactiveException();

			} catch (Exception e) {
				System.out.println(e.getMessage());
				throw new Error(e);
			}
		}

		@Override
		public void withdraw(double amount) throws InactiveException, OverdrawException, IOException {

			try {
				Command c = getResponse(postUrl, new Withdraw(number, amount));
				String e = (String) c.getReturnValue();
				if (InactiveException.class.getName().equals(e))
					throw new InactiveException();

				if (OverdrawException.class.getName().equals(e))
					throw new InactiveException();

			} catch (Exception e) {
				System.out.println(e.getMessage());
				throw new Error(e);
			}
		}

	}

}