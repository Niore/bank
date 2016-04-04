package bank.command.impl;

import java.io.IOException;

import bank.Account;
import bank.Bank;
import bank.InactiveException;
import bank.OverdrawException;
import bank.command.Command;

public class Transfer extends Command {

	private String from;
	
	private String to;
	
	private double amount;
	
	public Transfer(String f,String t,double a) {
		from = f;
		to = t;
		amount = a;
	}
	@Override
	public Exception getReturnValue() {
		return (Exception) returnValue;
	}

	@Override
	public void execute(Bank b) throws IOException {
		Account accFrom = b.getAccount(from);
		Account accTo = b.getAccount(to);
		try {
			b.transfer(accFrom, accTo, amount);
			System.out.println("Transfer "+ amount+ " from: "+from+" to: "+to);
		} catch (IllegalArgumentException | OverdrawException | InactiveException e) {
			returnValue = e;
		}

	}

}
