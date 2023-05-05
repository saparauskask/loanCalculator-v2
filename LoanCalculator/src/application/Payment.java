package application;

import java.time.LocalDate;

public class Payment {
	
	private double amountLeft;
	private double interest;
	private double amountPayment;
	private LocalDate paymentDate;
	
	public Payment(double amountLeft, double interest, double amountPayment) {
		this.amountLeft = amountLeft;
		this.interest = interest;
		this.amountPayment = amountPayment;
	}
	
	public double getAmountLeft() {
		return amountLeft;
	}
	
	public double getInterest() {
		return interest;
	}
	
	public double getAmountPayment() {
		return amountPayment;
	}
	
	public LocalDate getPaymentDate() {
		return paymentDate;
	}
	
	public void setAmountLeft() {
		this.amountLeft = 0.00;
	}
	
	public void setDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	public void show() {
		System.out.println("Liko: " + amountLeft);
		System.out.println("Menesinis procentas: " + interest);
		System.out.println("Moketi si menesi: " + amountPayment + "\n");
	}
	
}
