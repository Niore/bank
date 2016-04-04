package bank.command.impl;

import java.io.IOException;

import bank.Bank;
import bank.command.Command;

public class GetOwner extends Command {

	private String accountNumber;
	
	public GetOwner(String accN) {
		accountNumber = accN;
	}
	
	@Override
	public String getReturnValue() {
		return (String) returnValue;
	}

	@Override
	public void execute(Bank b) throws IOException {
		returnValue = b.getAccount(accountNumber).getOwner();
		System.out.println("Load Owner of Accountnumber: "+accountNumber + " ----> "+ getReturnValue());
	}

}
