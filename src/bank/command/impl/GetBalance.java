package bank.command.impl;

import java.io.IOException;

import bank.Bank;
import bank.command.Command;

public class GetBalance extends Command {

	private String accountNumber;
	
	public GetBalance(String num) {
		accountNumber = num;
	}
	
	@Override
	public Double getReturnValue() {
		return (Double) returnValue;
	}

	@Override
	public void execute(Bank b) throws IOException {
		returnValue = b.getAccount(accountNumber).getBalance();
		System.out.println("Checking Balance of Account "+accountNumber+" : "+getReturnValue());
	}

}
