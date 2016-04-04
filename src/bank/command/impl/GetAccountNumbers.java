package bank.command.impl;

import java.io.IOException;
import java.util.Set;

import bank.Bank;
import bank.command.Command;

public class GetAccountNumbers extends Command {

	@Override
	public Set<String> getReturnValue() {
		return (Set<String>) returnValue;
	}

	@Override
	public void execute(Bank b) throws IOException {
		returnValue = (Set<String>) b.getAccountNumbers();
		System.out.println("Count Accountnumbers: "+ getReturnValue().size());
	}

}
