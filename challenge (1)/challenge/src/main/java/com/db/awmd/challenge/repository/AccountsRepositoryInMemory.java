package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.NotificationService;
import com.db.awmd.challenge.exception.InSufficientBalanceException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {
	
	@Getter
	private final NotificationService notificationService;

	  @Autowired
	  public AccountsRepositoryInMemory(NotificationService notificationService) {
	    this.notificationService = notificationService;
	  }

  private final Map<String, Account> accounts = new ConcurrentHashMap<>();

  @Override
  public void createAccount(Account account) throws DuplicateAccountIdException {
    Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
    if (previousAccount != null) {
      throw new DuplicateAccountIdException(
        "Account id " + account.getAccountId() + " already exists!");
    }
  }

  @Override
  public Account getAccount(String accountId) {
    return accounts.get(accountId);
  }

  @Override
  public void clearAccounts() {
    accounts.clear();
  }

@Override
public void transferAmount(Account accountFrom, Account accountTo,
		BigDecimal amount) throws InSufficientBalanceException {
	// TODO Auto-generated method stub
	if(accountFrom.getAccountId()!=null && accountTo.getAccountId()!=null){
		if(accountFrom.getBalance()!=null 
				&& accountFrom.getBalance().compareTo(BigDecimal.ZERO) > 0
				&& (accountFrom.getBalance().subtract(amount)).compareTo(BigDecimal.ZERO) > 0) {
		accountTo.setBalance(accountTo.getBalance().add(amount));
		accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
		String desFrom = amount.toString() + "has been transferred to"
				 + accountTo.getAccountId();
		notificationService.notifyAboutTransfer(accountFrom, desFrom);
		String desTo = amount.toString() + "deposited from" + accountFrom.getAccountId();
		notificationService.notifyAboutTransfer(accountTo, desTo);
		
		}
		else {
			throw new InSufficientBalanceException("In-sufficient balance");
		}
	}
}

}
