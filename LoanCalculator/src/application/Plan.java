package application;

import javafx.scene.control.Label;

public class Plan extends SceneController {
	
	private double amount;
	private double interest;
	private int loanDuration;
	private int paymentType; // 1 is for annuity, 2 is for linear
	

	public void changeVisibility () {
		calculatePane.setVisible(false);
		schemePane.setVisible(true);
		//graphPane.setVisible(false);
	}
	
	public void setInformation(double amount, double interest, int loanDuration, int paymentType) {
		this.amount = amount;
		this.interest = interest;
		this.loanDuration = loanDuration;
		this.paymentType = paymentType;
	}
	
	public void printInformationLabel(Label loanInfo) {
		String infoText = "Paskolos suma: " + amount + "€" + "\n";
	    infoText += "Metinis procentas: " + interest + "%" + "\n";
	    infoText += "Paskolos mokėjimo laikotarpis: " + "\n" + loanDuration + "mėnesių(iai)" + "\n\n";
	    	if (paymentType == 1) {
	    		infoText += "Mokėjimo tipas: anuiteto";
	    	} else {
	    		infoText += "Mokėjimo tipas: linijinis";
	    	}
	    //System.out.println(infoText);
	    loanInfo.setText(infoText);
	}

}
