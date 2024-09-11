package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SceneController {
	
	@FXML private TextField amountBox;
	@FXML private TextField interestBox;
	@FXML private DatePicker dateStart;
	@FXML private DatePicker dateEnd;
	@FXML private RadioButton annuityCheck;
	@FXML private RadioButton linearCheck;
	@FXML private Label amountLabel;
	@FXML private Label interestLabel;
	@FXML private Label dateStartLabel;
	@FXML private Label dateEndLabel;
	@FXML private Label optionLabel;
	@FXML private ToggleGroup choice;
	
	@FXML private DatePicker delayStart;
	@FXML private TextField delayDuration;
	@FXML private TextField delayInterest;
	@FXML private Label delayStartLabel;
	@FXML private Label delayDurationLabel;
	@FXML private Label delayInterestLabel;
	
	@FXML private DatePicker filterStart;
	@FXML private DatePicker filterEnd;
	
	@FXML private Label loanInfoLabel;
	
	@FXML protected Pane calculatePane;
	@FXML protected Pane schemePane;
	@FXML protected Pane graphPane;
	
	@FXML private TableColumn<Payment, Double> amountLeftCol;
	@FXML private TableColumn<Payment, Double> interestCol;
	@FXML private TableColumn<Payment, Double> paymentCol;
	@FXML private TableView<Payment> paymentsTable;
	@FXML private TableColumn<Payment, LocalDate> dateCol;
	
	@FXML private LineChart<String, Number> paymentsChart;
	@FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
	
	private int duration;
	private int delayDurationNumber;
	//private LocalDate delayStartDate;
	private boolean checkDelay;
	private boolean delayIsSet;
	private ObservableList<Payment> originalPayments;
	Formula formula = new Formula();
	
			public void sceneChangeCalculate(ActionEvent event) {
			// we don't have access to stage which was set before in start() method therefore we need to set it up
			try {
				Parent calculateRoot = FXMLLoader.load(getClass().getResource("Calculate.fxml")); // same as before
				Scene calculateScene = new Scene(calculateRoot);
				
				//gets the stage information from ActionEvent and sets for new Stage
				Stage calculateStage = (Stage)((Node)event.getSource()).getScene().getWindow();
				
				calculateStage.setScene(calculateScene);
				calculateStage.show();
				//calculatePane.setVisible(false);
				
				// Initialize schemePane after the calculate scene is loaded
		        schemePane = (Pane) calculateRoot.lookup("#schemePane");
		        if (schemePane == null) {
		            System.out.println("schemePane is null!");
		        } else {
		            schemePane.setVisible(!schemePane.isVisible());
		        }
		        
		        graphPane = (Pane) calculateRoot.lookup("#graphPane");
		        	if (graphPane == null) {
		        		System.out.println("graphPane is null!");
		        	} else {
		        		graphPane.setVisible(false);
		        	}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		public void calculateButtonPushed(ActionEvent event) {
			ValidateNumber validation = new ValidateNumber(amountBox, interestBox, dateStart, dateEnd);
			Plan plan = new Plan();
			//Formula formula = new Formula();
			validation.setLabels(amountLabel, interestLabel, dateStartLabel, dateEndLabel, optionLabel);
			validation.numberInBounds();
			validation.ValidateDate();
			validation.checkSchedule(annuityCheck, linearCheck);
			
				if (validation.dataIsSet()) {
					//plan.changeVisibility();
					calculatePane.setVisible(false);
					graphPane.setVisible(false);
					schemePane.setVisible(true);
					validation.getLoanInfo(plan, formula);
					plan.printInformationLabel(loanInfoLabel);
					
					//here the table should reload
					
					duration = validation.getDuration();
					Payment[] test = new Payment[duration];
					test = formula.paymentFormula();
					validation.setDates(test); // NOTE: takes original dates (months)
					populateTable(test);
					populateChart(test);
					
				}
		}
		
		public void setDelay(ActionEvent event) {
			if (!delayIsSet) {
				
			ValidateDelay delayValidation = new ValidateDelay(amountBox, amountBox, dateEnd, dateEnd);
			delayValidation.setDelayInfo(delayStart, delayDuration, delayInterest, dateStart, dateEnd);
			delayValidation.setDelayLabels(delayStartLabel, delayDurationLabel, delayInterestLabel);
			checkDelay = delayValidation.validateDelayInfo();
		
			if (checkDelay) { // TODO prevent from multiple delays
				delayDurationNumber = delayValidation.getDelayDuration();
				delayValidation.setDelayFormula(formula);
				Payment[] test2 = new Payment[duration + delayDurationNumber];
				test2 = formula.paymentFormula();
				delayValidation.setDates(test2, duration + delayDurationNumber);
				populateTable(test2);
				populateChart(test2);
				delayIsSet = true;
			}
			
			}
		}
		
		private void populateTable(Payment[] payments) {
			originalPayments = FXCollections.observableArrayList(payments);
			ObservableList<Payment> data = FXCollections.observableArrayList(payments);
		    paymentsTable.setItems(data);
		    amountLeftCol.setCellValueFactory(new PropertyValueFactory<>("amountLeft"));
		    interestCol.setCellValueFactory(new PropertyValueFactory<>("interest"));
		    paymentCol.setCellValueFactory(new PropertyValueFactory<>("amountPayment"));
		    dateCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
		}
		
		public void filterTable(ActionEvent event) {
			ObservableList<Payment> data = paymentsTable.getItems();
		    LocalDate start = filterStart.getValue();
		    LocalDate end = filterEnd.getValue();
		    if (start == null || end == null) {
		        return;
		    }
		    ObservableList<Payment> filtered = data.filtered(payment ->
		            payment.getPaymentDate().compareTo(start) >= 0 
		            && payment.getPaymentDate().compareTo(end) <= 0);
		    Payment[] filteredPayments = filtered.toArray(new Payment[filtered.size()]);
		    data.setAll(filteredPayments);
		}
		
		public void restoreTable(ActionEvent event) {
			ObservableList<Payment> data = FXCollections.observableArrayList(originalPayments);
		    paymentsTable.setItems(data);
		}
		
		private void populateChart(Payment[] payments) {
			//XYChart.Series<String, Number> series = new XYChart.Series<>();
			XYChart.Series<String, Number> series2 = new XYChart.Series<>();
			
			for (Payment payment : payments) {
		        String category = payment.getPaymentDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
		        //series.getData().add(new XYChart.Data<>(category, payment.getAmountLeft()));
		        series2.getData().add(new XYChart.Data<>(category, payment.getAmountPayment()));
		    }
			// Add the series to the chart
			//paymentsChart.getData().add(series);
			paymentsChart.getData().add(series2);
		}
		
		public void exportTable(ActionEvent event) {
			
			FileChooser chooseFile = new FileChooser();
			chooseFile.setTitle("Payment_report");
			chooseFile.setInitialFileName("report.csv");
			Stage myStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			File file = chooseFile.showSaveDialog(myStage);
				if (file != null) {
					try {
						BufferedWriter writer = new BufferedWriter(new FileWriter(file));
						StringBuilder stringBuilder = new StringBuilder();
						
							stringBuilder.append("Liko sumoketi:,");
							stringBuilder.append("Palukanos:,");
							stringBuilder.append("Moketina suma:,");
							stringBuilder.append("Data:\n");
						
						for (Payment data : paymentsTable.getItems()) {
							stringBuilder.append(data.getAmountLeft() + ",");
							stringBuilder.append(data.getInterest() + ",");
							stringBuilder.append(data.getAmountPayment() + ",");
							stringBuilder.append(data.getPaymentDate() + ",");
							stringBuilder.setLength(stringBuilder.length() - 1);
							stringBuilder.append("\n");
						}
						writer.write(stringBuilder.toString());
						
						//what should be written here
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		}
		
		public void showGraph(ActionEvent event) {
			calculatePane.setVisible(false);
			graphPane.setVisible(true);
			schemePane.setVisible(false);
		}
		
		public void showScheme(ActionEvent event) {
			calculatePane.setVisible(false);
			graphPane.setVisible(false);
			schemePane.setVisible(true);
		}
		
		public void exitProgram(ActionEvent event) {
			System.exit(0);
		}

}
