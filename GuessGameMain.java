import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class GuessGameMain extends Application
{
	private static int playerCount = 0;
	public static int goalGuess = 0;
	private static int trialCount = 0;
	private static HashMap<Integer, GuessGameTrial> trialHash = new HashMap<Integer, GuessGameTrial>();
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception
	{
		VBox rootVBox = new VBox();
		Text introText = new Text("GUESS NUMBER SIMULATOR");
		Label introLabel = new Label("How many AI guessers are participating?");
		SpinnerValueFactory<Integer> introSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, Integer.MAX_VALUE);
		Spinner<Integer> introSpinner = new Spinner<Integer>();
		Button introButton = new Button("Go!");
		
		introButton.setPrefSize(100, 20);
		introSpinner.setMaxWidth(100);
		introSpinner.setValueFactory(introSVF);
		introText.setFont(Font.font("Ebrima", 30));
		
		introButton.setOnAction(e -> {
			playerCount = introSpinner.getValue();
			primaryStage.close();
			askGoal();
		});
		
		rootVBox.setAlignment(Pos.CENTER);
		rootVBox.setPadding(new Insets(10));
		rootVBox.setSpacing(10);
		rootVBox.getChildren().addAll(introText, introLabel, introSpinner, introButton);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Guess Simulator");
		primaryStage.setScene(new Scene(rootVBox));
		primaryStage.show();
	}
	
	public void askGoal()
	{
		Stage secondaryStage = new Stage();
		HBox goalHBox = new HBox();
		VBox goalVBox = new VBox();
		VBox goalVBoxLeft = new VBox();
		VBox goalVBoxRight = new VBox();
		Text goalText = new Text("SELECT A GOAL");
		Label goalLabel = new Label("Pick a number for the AI players to try to guess. (0-9)");
		Label goalLabelSeperator = new Label("|\n|\n|\n|\n|\n|");
		Label goalLabelLeft = new Label("Manual selection:");
		Label goalLabelRight = new Label("Automatic generation:");
		SpinnerValueFactory<Integer> goalSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 9, 0);
		Spinner<Integer> goalSpinner = new Spinner<Integer>();
		Button goalButtonManual = new Button("Go!");
		Button goalButtonAuto = new Button("Random number!");
		
		goalButtonAuto.setPrefSize(150, 20);
		goalButtonManual.setPrefSize(150, 20);
		goalSpinner.setMaxWidth(150);
		goalSpinner.setValueFactory(goalSVF);
		goalText.setFont(Font.font("Ebrima", 30));
		
		goalButtonAuto.setOnAction(e -> {
			Random r = new Random();
			goalGuess = r.nextInt(10);
			secondaryStage.close();
			askTrial();
		});
		
		goalButtonManual.setOnAction(e -> {
			goalGuess = goalSpinner.getValue();
			secondaryStage.close();
			askTrial();
		});
		
		goalVBoxLeft.setAlignment(Pos.CENTER);
		goalVBoxLeft.setPadding(new Insets(10));
		goalVBoxLeft.setSpacing(10);
		goalVBoxLeft.getChildren().addAll(goalLabelLeft, goalSpinner, goalButtonManual);
		goalVBoxRight.setAlignment(Pos.CENTER);
		goalVBoxRight.setPadding(new Insets(10));
		goalVBoxRight.setSpacing(10);
		goalVBoxRight.getChildren().addAll(goalLabelRight, goalButtonAuto);
		goalHBox.setAlignment(Pos.CENTER);
		goalHBox.getChildren().addAll(goalVBoxLeft, goalLabelSeperator, goalVBoxRight);
		goalVBox.setAlignment(Pos.CENTER);
		goalVBox.setPadding(new Insets(10));
		goalVBox.setSpacing(10);
		goalVBox.getChildren().addAll(goalText, goalLabel, goalHBox);
		secondaryStage.setResizable(false);
		secondaryStage.setTitle("Guess Simulator");
		secondaryStage.setScene(new Scene(goalVBox));
		secondaryStage.show();
	}

	private void askTrial()
	{
		Stage tertiaryStage = new Stage();
		VBox trialVBox = new VBox();
		Text trialText = new Text("SELECT A NUMBER OF TRIALS");
		Label trialLabel = new Label("How many trials will the simulation run for?");
		SpinnerValueFactory<Integer> trialSVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
		Spinner<Integer> trialSpinner = new Spinner<Integer>();
		Button trialButton = new Button("Go!");
		
		trialButton.setPrefSize(100, 20);
		trialSpinner.setPrefWidth(100);
		trialSpinner.setValueFactory(trialSVF);
		trialText.setFont(Font.font("Ebrima", 30));
		
		trialButton.setOnAction(e -> {
			trialCount = trialSpinner.getValue();
			tertiaryStage.close();
			startSimulation();
		});
		
		trialVBox.setAlignment(Pos.CENTER);
		trialVBox.setPadding(new Insets(10));
		trialVBox.setSpacing(10);
		trialVBox.getChildren().addAll(trialText, trialLabel, trialSpinner, trialButton);
		tertiaryStage.setResizable(false);
		tertiaryStage.setTitle("Guess Simulator");
		tertiaryStage.setScene(new Scene(trialVBox));
		tertiaryStage.show();
	}
	
	private void startSimulation()
	{
		Stage quaternaryStage = new Stage();
		VBox quaternaryVBox = new VBox();
		Button simulateButton = new Button("Simulate " + trialCount + " Trial(s)");
		Button reportButton = new Button("No Simulation to Report");
		
		reportButton.setDisable(true);
		reportButton.setPrefSize(250, 50);
		simulateButton.setPrefSize(250, 50);
		quaternaryVBox.getChildren().addAll(simulateButton, reportButton);
		quaternaryVBox.setAlignment(Pos.CENTER);
		quaternaryVBox.setPadding(new Insets(10));
		quaternaryVBox.setSpacing(10);
		
		simulateButton.setOnAction(e -> {
			
			simulateButton.setDisable(true);
			simulateButton.setText("Simulation Complete");
			reportButton.setText("Simulation Report");
			reportButton.setDisable(false);
			try
			{
				simulate();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		});
		
		reportButton.setOnAction(e -> {
			displayReport();
		});
		
		quaternaryStage.setOnCloseRequest(e -> {
			Platform.exit();
		});
		
		quaternaryStage.setResizable(false);
		quaternaryStage.setScene(new Scene(quaternaryVBox));
		quaternaryStage.setTitle("Guess Simulator");
		quaternaryStage.show();
	}
	
	private void displayReport()
	{
		Stage reportStage   = new Stage();
		VBox reportVBox	 = new VBox();
		Text reportText	 = new Text("Simulation Report");
		Label reportLabel   = new Label();
		TextArea reportArea = new TextArea();
		Button reportClsBtn = new Button("Close");
		
		reportClsBtn.setOnAction(re -> reportStage.close());
		reportArea.setPrefSize(210, 300);
		reportArea.setEditable(false);
		reportText.setFont(Font.font("Ebrima", 20));
		reportText.setTextAlignment(TextAlignment.CENTER);
		
		HashMap<String, Integer> winnerHash = new HashMap<String, Integer>();
		
		for(int p = 0; p < playerCount; p++)
		{
			winnerHash.put("Player " + (p+1), 0);
		}
		
		for(int t = 0; t < trialCount; t++)
		{
			for(int p = 0; p < playerCount; p++)
			{
				if(trialHash.get(t+1).getThreadHash().get(p+1).getWin()==true)
				{
					winnerHash.put("Player " + (p+1), winnerHash.get("Player " + (p+1)) + 1);
				}
			}
		}
		
		for(int p = 0; p < playerCount; p++)
		{
			reportLabel.setText(reportLabel.getText() + "Player " + (p+1) + " wins: " + winnerHash.get("Player " + (p+1)) + "\n");
		}
		
		reportLabel.setText(reportLabel.getText() + "*Ties are included as wins\n------------------------------------------");
		
		try(DataInputStream in = new DataInputStream(new FileInputStream("results.dat")))
		{
			while(true)
			{
				reportArea.appendText(in.readUTF() + "\n");
			}
		}
		catch(Exception ex)
		{
			
		}
		
		reportVBox.getChildren().addAll(reportText, reportLabel, reportArea, reportClsBtn);
		reportVBox.setAlignment(Pos.CENTER);
		reportVBox.setPadding(new Insets(10));
		reportVBox.setSpacing(10);
		reportStage.setTitle("Report");
		reportStage.setY(50);
		reportStage.setScene(new Scene(reportVBox));
		reportStage.show();
		reportArea.setScrollTop(0);
		reportClsBtn.requestFocus();
	}

	private void simulate() throws IOException
	{
		CountDownLatch startTrials = new CountDownLatch(1);
		CountDownLatch endTrials = new CountDownLatch(trialCount);
		
		for(int t = 0; t < trialCount; t++)
		{
			GuessGameTrial ggt = new GuessGameTrial(playerCount, t+1, startTrials, endTrials);
			trialHash.put(t+1, ggt);
			trialHash.get(t+1).start();
		}
		
		startTrials.countDown();
		
		try
		{
			endTrials.await();
		}
		catch(InterruptedException e1)
		{
			e1.printStackTrace();
		}
		
		File file = new File("results.dat");
		file.deleteOnExit();
		
		for(int t = 0; t < trialCount; t++)
		{
			trialHash.get(t+1).saveResults(file);
		}
	}
}