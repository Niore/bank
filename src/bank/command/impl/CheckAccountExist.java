package bank.command.impl;

import java.io.IOException;

import javax.print.attribute.standard.RequestingUserName;

import bank.Account;
import bank.Bank;
import bank.command.Command;

public class CheckAccountExist extends Command {

	private String owner;

	private String accNumber;

	public CheckAccountExist(String owner, String number) {
		this.owner = owner;
		accNumber = number;
	}

	@Override
	public Boolean getReturnValue() {
		return (Boolean) returnValue;
	}

	@Override
	public void execute(Bank b) throws IOException {
		Account acc = b.getAccount(accNumber+"");

		// check if Account exists
		if (acc != null) {

			// check if Owner is the same
			if (acc.getOwner().equals(owner))
				returnValue = true;

		}
		returnValue = false;
		System.out.println(owner+" Account with number "+accNumber+ " existis: "+returnValue);
	}

}
