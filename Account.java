package com.abc;

import java.util.ArrayList;
import java.util.List;

public class Account {
    
    private double SAVINGS_THRESHOLD = 1000;
    private double MAXI_SAVINGS_THRESHOLD = 1000;
    private double CHECKING_FLAT_RATE = 0.001;
    private double SAVINGS_RATE = 0.001;
    private double SAVINGS_RATE_ABOVE_THRESHOLD = 0.002;
    private double MAXI_SAVINGS_RATE_BELOW_THRESHOLD = 0.02;
    private double MAXI_SAVINGS_RATE_AT_THRESHOLD = 0.05;
    private double MAXI_SAVINGS_RATE_ABOVE_THRESHOLD = 0.1;
    
    private double currentBalance = 0;   
        
    private AccountType accountType;
    public List<Transaction> transactions;
    
    public Account(AccountType accountType) {
        this.accountType = accountType;
        this.transactions = new ArrayList<Transaction>();
    }


    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        } else {
        	currentBalance += amount;
            transactions.add(new Transaction(amount));
        }
    }
    
    public double getCurrentBalance() {
    	return currentBalance;
    }

	public void withdraw(double amount) {
	    if (amount <= 0) {
	        throw new IllegalArgumentException("amount must be greater than zero");
	    } else {
	    	currentBalance -= amount;
	        transactions.add(new Transaction(-amount));
	    }
	}
	
	public void transfer(Customer customer, int fromAcct, int toAcct, double amount) throws Exception {
		if (amount <= 0){
            throw new IllegalArgumentException("transfer amount must be greater than zero");
		}
		
		Account sourceAcct = customer.getAccount(fromAcct);
		Account targetAcct = customer.getAccount(toAcct);
		if(sourceAcct != null && targetAcct != null) {
			if (amount < sourceAcct.getCurrentBalance())
				sourceAcct.withdraw(amount);
			else
				throw new Exception("Insufficient funds. Transfer amount must be less than current balance");
		}
		targetAcct.deposit(amount);
	}
	
	

    public double interestEarned() {
        
        switch(accountType){
        	case CHECKING:
        		return getCheckinAcctInt();        	
            case SAVINGS:
            	return getSavingsAcctInt();
            case MAXI_SAVINGS:
            	return getMaxiSavingsAcctInt();
            default:
            	throw new IllegalArgumentException("Invalid account type");
        }
    }

    public double sumTransactions() {
        double amount = 0.0;
        for (Transaction t: transactions)
            amount += t.amount;
        return amount;    
    }

    public AccountType getAccountType() {
        return accountType;
    }
    
    private double getSavingsAcctInt() {
        if (currentBalance <= SAVINGS_THRESHOLD)
            return currentBalance * SAVINGS_RATE;
        else
            return SAVINGS_THRESHOLD*SAVINGS_RATE + 
            		(currentBalance-SAVINGS_THRESHOLD) * SAVINGS_RATE_ABOVE_THRESHOLD;   	
    }
    
    private double getMaxiSavingsAcctInt() {
    	double interest = 0;
        if (currentBalance <= MAXI_SAVINGS_THRESHOLD)
        	interest = currentBalance * MAXI_SAVINGS_RATE_BELOW_THRESHOLD;
        if (currentBalance >= MAXI_SAVINGS_THRESHOLD && currentBalance < 2*MAXI_SAVINGS_THRESHOLD) {
        	interest = MAXI_SAVINGS_THRESHOLD*MAXI_SAVINGS_RATE_BELOW_THRESHOLD + 
        				(currentBalance-MAXI_SAVINGS_THRESHOLD) * MAXI_SAVINGS_RATE_AT_THRESHOLD;  
        }
        if (currentBalance > 2*MAXI_SAVINGS_THRESHOLD) {
        	interest = MAXI_SAVINGS_THRESHOLD*MAXI_SAVINGS_RATE_BELOW_THRESHOLD +
        			MAXI_SAVINGS_THRESHOLD*MAXI_SAVINGS_RATE_AT_THRESHOLD +
        			(currentBalance-2*MAXI_SAVINGS_THRESHOLD) * MAXI_SAVINGS_RATE_ABOVE_THRESHOLD;
        }
        return interest;
    }
    
    private double getCheckinAcctInt() {
    	return currentBalance * CHECKING_FLAT_RATE;
    	
    }
    


}
