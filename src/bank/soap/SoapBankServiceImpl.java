package bank.soap;

import java.io.IOException;

import javax.jws.WebService;

import bank.Bank;
import bank.command.Command;

@WebService
public class SoapBankServiceImpl implements SoapBankService {

	private Bank bank;
	
	public SoapBankServiceImpl(Bank bank) {
		this.bank = bank;
	}
	
	@Override
	public Command handle(Command object) {
		try {
			object.execute(bank);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
	}

	// wsgen -cp bin -keep -s src -d bin bank.soap.SoapBankServiceImpl
}
