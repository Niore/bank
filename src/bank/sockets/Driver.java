package bank.sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import bank.BankDriver;
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


public class Driver implements BankDriver {
	private Bank bank = null;

	private Socket socket;

	static ObjectOutputStream out;

	static ObjectInputStream in;

	@Override
	public void connect(String[] args) throws IOException {
		socket = new Socket(args[0], Integer.parseInt(args[1]));
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		bank = new Bank();
		System.out.println("connected...");
	}

	@Override
	public void disconnect() throws IOException {
		bank = null;
		socket.close();
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
			Command c = new GetAccountNumbers();
			out.writeObject(c);
			try {
				return (Set<String>) in.readObject();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				throw new Error(e);
			}
		}

		@Override
		public String createAccount(String owner) throws IOException {

			try {
				out.writeObject(new CreateAccount(owner));
				return (String) in.readObject();

			} catch (Exception e) {
				System.out.println(e.getMessage());
				throw new Error(e);
			}
		}

		@Override
		public boolean closeAccount(String number) {
			Command c = new CheckActive(number);
			try {
				out.writeObject(c);

				boolean res = (boolean) in.readObject();
				if (res) {
					Command c2 = new RemoveAccount(number);
					out.writeObject(c2);
					return (boolean) in.readObject();
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
				throw new Error(e);
			}
			return false;
		}

		@Override
		public bank.Account getAccount(String number) throws IOException {
			Command c = new GetOwner(number);
			Account acc = null;
			out.writeObject(c);
			try {
				String owner = (String) in.readObject();
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

			Command c = new Transfer(from.getNumber(), to.getNumber(), amount);
			out.writeObject(c);
			try {
				Exception e = (Exception) in.readObject();
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
			Command c = new GetBalance(number);
			try {
				out.writeObject(c);
				return (Double) in.readObject();
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
			Command c = new Deposit(number, amount);
			out.writeObject(c);
			try {
				String e = (String) in.readObject();
				if (InactiveException.class.getName().equals(e))
					throw new InactiveException();

			} catch (Exception e) {
				System.out.println(e.getMessage());
				throw new Error(e);
			}
		}

		@Override
		public void withdraw(double amount) throws InactiveException, OverdrawException, IOException {
			Command c = new Withdraw(number, amount);
			out.writeObject(c);
			try {
				String e = (String) in.readObject();
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