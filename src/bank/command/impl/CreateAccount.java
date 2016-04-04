package bank.command.impl;

import bank.Account;
import bank.Bank;
import bank.command.Command;

import java.io.IOException;
import java.util.Set;

/**
 * Created by julian on 29.02.16.
 */
public class CreateAccount extends Command {

    private String owner;

    public CreateAccount(String account){
        owner = account;
    }

    @Override
    public String getReturnValue() {
        return (String) returnValue;
    }


    @Override
    public void execute(Bank b) throws IOException {
        String accNumber = b.createAccount(owner);
        System.out.println("Created Account for Owner: "+b.getAccount(accNumber).getOwner() + " with Accountnumber: "+accNumber);
        returnValue = accNumber;
    }
}
