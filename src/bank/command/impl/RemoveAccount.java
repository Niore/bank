package bank.command.impl;

import java.io.IOException;

import bank.Bank;
import bank.command.Command;

public class RemoveAccount extends Command {

	private String accountNumber;
	
	public RemoveAccount(String acc) {
		accountNumber = acc;
	}
	
	@Override
	public Boolean getReturnValue() {
		return (Boolean) returnValue;
	}

	@Override
	public void execute(Bank b) throws IOException {
		returnValue = b.closeAccount(accountNumber);
		System.out.println("Deactivate Account "+accountNumber+" : "+getReturnValue());
	}

}
