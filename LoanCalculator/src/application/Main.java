package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class Main extends Application {
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setResizable(false);
			Image icon = new Image("bank-meta.png");
			Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			//BorderPane root = new BorderPane();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Būsto paskolos skaičiuoklė");
			primaryStage.getIcons().add(icon);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
