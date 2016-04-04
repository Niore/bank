package bank.command.impl;

import bank.Account;
import bank.Bank;
import bank.command.Command;

import java.io.IOException;

/**
 * Created by julian on 29.02.16.
 */
public class GetAccount extends Command{

    private String account;

    public GetAccount(String account){
        this.account = account;
    }

    @Override
    public Boolean getReturnValue() {
        return (Boolean) returnValue;
    }


    @Override
    public void execute(Bank b) throws IOException {
        System.out.println("Get Account : "+account);
        Account acc = b.getAccount(account);
        returnValue = acc;
    }
}
