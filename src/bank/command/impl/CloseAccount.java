package bank.command.impl;

import java.io.IOException;

import bank.Bank;
import bank.command.Command;

public class CloseAccount extends Command {

	@Override
	public Boolean getReturnValue() {
		return (Boolean) returnValue;
	}

	@Override
	public void execute(Bank b) throws IOException {
		// TODO Auto-generated method stub

	}

}
