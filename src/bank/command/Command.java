package bank.command;

import bank.Bank;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by julian on 24.02.16.
 */
public abstract class Command implements Serializable {

    public Object returnValue;

    public abstract Object getReturnValue();

    public abstract void execute(Bank b) throws IOException;
}
