package application;

import java.time.LocalDate;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class Validate {
	
	private TextField numberAmount;
	private TextField numberInterest;
	protected DatePicker dateStart;
	protected DatePicker dateEnd;
	
	protected Label amountLabel;
	protected Label interestLabel;
	protected Label dateStartLabel;
	protected Label dateEndLabel;
	protected Label optionLabel;
	
	protected RadioButton annuityCheck;
	protected RadioButton linearCheck;
	
	protected LocalDate selectedStart;
	protected LocalDate selectedEnd;
	
	protected double amount = 0.0;
	protected double interest = 0.0;
	
	//private RadioButton paymentType;
	//TODO implement Radio buttons
	
	
		public Validate(TextField numberAmount, TextField numberInterest, DatePicker dateStart, DatePicker dateEnd) {
			
			this.numberAmount = numberAmount;
			this.numberInterest = numberInterest;
			this.dateStart = dateStart;
			this.dateEnd = dateEnd;
		}
		
		protected void setLabels(Label amountLabel,Label interestLabel, Label dateStartLabel, Label dateEndLabel, Label optionLabel) {
			
			this.amountLabel = amountLabel;
			this.interestLabel = interestLabel;
			this.dateStartLabel = dateStartLabel;
			this.dateEndLabel = dateEndLabel;
			this.optionLabel = optionLabel;
		}
		
		protected void setSchedule(RadioButton annuityCheck, RadioButton linearCheck) {
			
			this.annuityCheck = annuityCheck;
			this.linearCheck = linearCheck;
		}
		
		public void initialValidationNumber() {
			
			try {
				amount = Double.parseDouble(numberAmount.getText());
			} catch (NumberFormatException e) {
				//handle the exception
			}
			
			try {
				interest = Double.parseDouble(numberInterest.getText());
			} catch(NumberFormatException e) {
				//handle the exception
			}
		}
		
		public void initialValidationDate() {
			
				if (dateStart.getValue() != null) {
					selectedStart = dateStart.getValue();
					//dateStartLabel.setText("");
				} else {
					dateStartLabel.setText("Įveskite datą");
				}
			
				if (dateEnd.getValue() != null) {
					selectedEnd = dateEnd.getValue();
					//dateEndLabel.setText("");
				} else {
					dateEndLabel.setText("Įveskite datą");
				}
			
		}

}
