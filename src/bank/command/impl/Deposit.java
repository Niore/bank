package bank.command.impl;

import java.io.IOException;

import bank.Bank;
import bank.InactiveException;
import bank.command.Command;

public class Deposit extends Command {

	private String accountNumber;

	private double amount;

	public Deposit(String acc, double a) {
		accountNumber = acc;
		amount = a;
	}

	@Override
	public Exception getReturnValue() {
		return (Exception) returnValue;
	}

	@Override
	public void execute(Bank b) throws IOException {
		try {
			b.getAccount(accountNumber).deposit(amount);
			System.out.println("Make Deposit from " + accountNumber + " : " + amount);
		} catch (IllegalArgumentException | InactiveException e) {
			returnValue = e;
		}

	}

}
