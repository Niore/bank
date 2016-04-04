package bank.command.impl;

import java.io.IOException;

import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;
import bank.command.Command;

public class Withdraw extends Command {

	private String accountNumber;
	
	private double amount;
	public Withdraw(String acc,double am) {
		accountNumber = acc;
		amount = am;
	}
	
	@Override
	public Exception getReturnValue() {
		// TODO Auto-generated method stub
		return (Exception) returnValue;
	}

	@Override
	public void execute(Bank b) throws IOException {
		try {
			b.getAccount(accountNumber).withdraw(amount);
		} catch (IllegalArgumentException | OverdrawException | InactiveException e) {
			returnValue = e;
		}

	}

}
