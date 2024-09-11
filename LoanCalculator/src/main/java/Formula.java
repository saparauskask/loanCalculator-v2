package application;

import java.text.DecimalFormat;

public class Formula {
	
	private int loanDuration;
	private int type;
	private double amount;
	private double interest;
	
	private int delayStartMonth;
	private int delayDuration;
	private double delayInterest;
	boolean delay;
	
	private double individualPayment[] = new double[600]; // is equal to 50 years in months
	private double individualInterest[] = new double[600];
	private double individualPaymentLeft[] = new double[600];
	
	DecimalFormat numberFormat = new DecimalFormat("#.##");
	
	public void setElements(int loanDuration, int type, double amount, double interest) {
		this.loanDuration = loanDuration;
		this.type = type;
		this.amount = amount;
		this.interest = interest;
	}
	
	public void setDelayInfo(int delayDuration, double delayInterest, int delayStartMonth) {
		this.delayDuration = delayDuration;
		this.delayInterest = delayInterest;
		this.delayStartMonth = delayStartMonth;
		this.loanDuration = loanDuration + this.delayDuration;
		delay = true;
	}
	
	public Payment[] paymentFormula() {
		Payment[] paymentObjects = new Payment[loanDuration];
			if (type == 1) {
				paymentFormulaAnnuity();
			} else {
				paymentFormulaLinear();
			}
				for (int i = 0; i < loanDuration; ++i) {
					Payment individualObject = new Payment(individualPaymentLeft[i], individualInterest[i], individualPayment[i]);
					paymentObjects[i] = individualObject;
				}
				
			return paymentObjects;
	}
	
	private void paymentFormulaAnnuity() {
		double monthlyInterestRate = interest / 1200.0; // convert to monthly rate
	    double annuityPayment = amount * monthlyInterestRate /
	        (1 - Math.pow(1 + monthlyInterestRate, -loanDuration));
	    //double[] monthlyInterest = new double[loanDuration];
	    //double[] remainingBalance = new double[loanDuration];
	    individualPaymentLeft[0] = amount;
	    for (int i = 0; i < loanDuration; i++) {
	    	
	    	if (delay && i == delayStartMonth) {
	    		delayPayment(i);
	    		i += delayDuration;
	    	}
	    	
	        double interest = individualPaymentLeft[i] * monthlyInterestRate;
	        
	        individualPayment[i] = annuityPayment;
	        String formatted1 = numberFormat.format(individualPayment[i]);
	        individualPayment[i] = Double.parseDouble(formatted1);
	        
	        individualInterest[i] = interest;
	        String formatted2 = numberFormat.format(individualInterest[i]);
	        individualInterest[i] = Double.parseDouble(formatted2);
	        
	        double principal = annuityPayment - interest;
	        individualPaymentLeft[i+1] = individualPaymentLeft[i] - principal;
	        String formatted3 = numberFormat.format(individualPaymentLeft[i+1]);
	        individualPaymentLeft[i+1] = Double.parseDouble(formatted3);
	    }
	}
	
	private void paymentFormulaLinear() {
	    double monthlyInterestRate = interest / 1200.0; // convert to monthly rate
	    double principal = amount / loanDuration;
	    individualPaymentLeft[0] = amount;
	    for (int i = 0; i < loanDuration; i++) {
	    	
	    	if (delay && i == delayStartMonth) {
	    		delayPayment(i);
	    		i += delayDuration;
	    	}
	    	
	        double interest = individualPaymentLeft[i] * monthlyInterestRate;
	        individualPayment[i] = principal + interest;
	        String formatted1 = numberFormat.format(individualPayment[i]);
	        individualPayment[i] = Double.parseDouble(formatted1);
	        
	        individualInterest[i] = interest;
	        String formatted2 = numberFormat.format(individualInterest[i]);
	        individualInterest[i] = Double.parseDouble(formatted2);
	        
	        individualPaymentLeft[i+1] = individualPaymentLeft[i] - principal;
	        String formatted3 = numberFormat.format(individualPaymentLeft[i+1]);
	        individualPaymentLeft[i+1] = Double.parseDouble(formatted3);
	    }
	}
	
	private void delayPayment(int i) {
		double previousPaymentLeft = individualPaymentLeft[i-1];
		double payment = (delayInterest * amount) / 100;
		for (int j = 0; j < delayDuration; ++j) {
			individualPayment[i] = payment;
			String formatted = numberFormat.format(individualPayment[i]);
			individualPayment[i] = Double.parseDouble(formatted);
			individualInterest[i] = delayInterest;
			individualPaymentLeft[i] = previousPaymentLeft;
			++i;
		}
	}
	
}
	

