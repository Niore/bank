/*
 * Copyright (c) 2000-2016 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved. 
 */

package bank.local;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.SynchronousQueue;

import bank.InactiveException;
import bank.OverdrawException;

public class Driver implements bank.BankDriver {
	private Bank bank = null;

	@Override
	public void connect(String[] args) {
		bank = new Bank();
		System.out.println("connected...");
	}

	@Override
	public void disconnect() {
		bank = null;
		System.out.println("disconnected...");
	}

	@Override
	public Bank getBank() {
		return bank;
	}

	static class Bank implements bank.Bank {

		private final Map<String, Account> accounts = new HashMap<>();

		@Override
		public Set<String> getAccountNumbers() {
			Set<String> numbers = new HashSet<String>();

			for (Map.Entry<String, Account> entry : accounts.entrySet()) {
				if (entry.getValue().isActive())
					numbers.add(entry.getKey());
			}

			return numbers;
		}

		@Override
		public String createAccount(String owner) {
			UUID uuid = UUID.randomUUID();

			while (accounts.containsKey(uuid))
				uuid = UUID.randomUUID();

			Account acc = new Account(owner, uuid.toString());
			accounts.put(acc.getNumber(), acc);
			return acc.getNumber();
		}

		@Override
		public boolean closeAccount(String number) {
			Account acc = accounts.get(number);
			return acc.deactivate();
		}

		@Override
		public bank.Account getAccount(String number) {
			return accounts.get(number);
		}

		@Override
		public void transfer(bank.Account from, bank.Account to, double amount)
				throws IOException, InactiveException, OverdrawException {

			from.withdraw(amount);
			to.deposit(amount);

		}

	}

	static class Account implements bank.Account {
		private String number;
		private String owner;
		private double balance;
		private boolean active = true;

		Account(String owner, String num) {
			this.owner = owner;
			number = num;
		}

		@Override
		public double getBalance() {
			return balance;
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
			if (!isActive())
				throw new InactiveException("Account not active");

			if (amount < 0)
				throw new IOException("negative amount");

			balance += amount;
		}

		@Override
		public void withdraw(double amount) throws InactiveException, OverdrawException, IOException {
			if (balance - amount < 0)
				throw new OverdrawException("Balance gets negative after withdraw.Not allowed");

			if (!isActive())
				throw new InactiveException("Account not active");

			if (amount < 0)
				throw new IOException("negative amount");

			balance -= amount;
		}

		public boolean deactivate() {
			if (balance == 0 && isActive()) {
				active = false;
				return true;
			}
			return false;
		}

	}

}