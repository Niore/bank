package bank.command.impl;

import java.io.IOException;

import bank.Bank;
import bank.command.Command;

public class CheckActive extends Command {

	private String accountNumber;

	public CheckActive(String acc) {
		accountNumber = acc;
	}

	@Override
	public Boolean getReturnValue() {
		// TODO Auto-generated method stub
		return (Boolean) returnValue;
	}

	@Override
	public void execute(Bank b) throws IOException {
		returnValue = b.getAccount(accountNumber).isActive();
		System.out.println("Check if "+accountNumber+" is active: "+getReturnValue());
	}

}
