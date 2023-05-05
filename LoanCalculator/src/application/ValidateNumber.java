package application;

import java.time.Period;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;


public class ValidateNumber extends Validate {
	
	private int timeResult = 0;
	private Period period;
	private int months;
	private int paymentType;
	
	private boolean amountOk;
	private boolean interestOk;
	private boolean dateOk;
	private boolean scheduleOk;
	
	
		
		public ValidateNumber(TextField numberAmount, TextField numberInterest, DatePicker dateStart, DatePicker dateEnd) {
			super(numberAmount, numberInterest, dateStart, dateEnd);
		}
		
		public void setLabels(Label amountLabel,Label interestLabel,Label dateStartLabel,Label dateEndLabel,Label optionLabel) {
			super.setLabels(amountLabel, interestLabel, dateStartLabel, dateEndLabel, optionLabel);
		}
		
		public void numberInBounds() {
			
			super.initialValidationNumber(); // should add that booleans should be false in the start
			amountOk = false;
			interestOk = false;
				
				if (amount >= 1000.0 && amount <= 1000000.0) {
					amountLabel.setText(""); // IS CORRECT
					amountOk = true;
				} else {
					amountLabel.setText("Tokios sumos bankas neteikia");
				}
				
				if (interest > 0.0 && interest <= 100.0) {
					interestLabel.setText("");// IS CORRECT
					interestOk = true;
				} else {
					interestLabel.setText("Netinkamas metinis procentas");
				}
		}
		
		public void ValidateDate() {
			
			super.initialValidationDate();
			dateOk = false;
			
				if (selectedStart == null || selectedEnd == null) {
					return; // TODO
				}
			timeResult = selectedStart.compareTo(selectedEnd);
				if (timeResult >= 0) {
					//System.out.println("error");
					dateStartLabel.setText("");
					dateEndLabel.setText("Netinkamas terminas");
					return;
				}
				
				period = Period.between(selectedStart.withDayOfMonth(1), selectedEnd.withDayOfMonth(1));
				months = period.getYears() * 12 + period.getMonths();
				
				if (months >= 3) {
					dateStartLabel.setText("");
					dateEndLabel.setText(""); // IS CORRECT
					dateOk = true;
				} else {
					dateStartLabel.setText("");
					dateEndLabel.setText("Minimali trukmė - 3 mėnesiai");
				}
				//timeResult = 7889238; // 3 months in seconds
				
		}
		
		public void checkSchedule(RadioButton annuityCheck, RadioButton linearCheck) {
			super.setSchedule(annuityCheck, linearCheck);
			scheduleOk = false;
			
				if (annuityCheck.isSelected() || linearCheck.isSelected()) {
					optionLabel.setText("");
						if (annuityCheck.isSelected()) {
							//CORRECT
							paymentType = 1;
							scheduleOk = true;
						} else {
							//CORRECT
							paymentType = 2;
							scheduleOk = true;
						}
				} else {
					optionLabel.setText("Pasirinkite vieną iš būdų");
				}
		}
		
		public boolean dataIsSet() {
			
				if (amountOk && interestOk && dateOk && scheduleOk) {
					return true;
				} else {
					return false;
				}
		}
		
		public void getLoanInfo(Plan plan, Formula formula) {
			
			plan.setInformation(amount, interest, months, paymentType);
			formula.setElements(months, paymentType, amount, interest);
		}
		
		public int getDuration () {
			return months;
		}
		
		public void setDates(Payment[] test) {
				for (int i = 0; i < months; ++i) {
					test[i].setDate(selectedStart.plusMonths(i+1).withDayOfMonth(1));
				}
		}
		
}
