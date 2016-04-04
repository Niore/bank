package bank.soap;

import javax.jws.WebParam;
import javax.jws.WebService;

import bank.command.Command;

@WebService
public interface SoapBankService {

	Command handle(@WebParam(name = "object") Command object);
}
