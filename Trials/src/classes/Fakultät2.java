package classes;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Fakultät2 extends Application {

	private Button calc;
	private TextArea input;
	private GridPane pane;
	private Text output;
	private int a;

	public void init() {

		output = new Text("Ergebnis:  ");
		output.setFill(Color.WHITESMOKE);
		output.setFont(new Font(30));

		calc = new Button("Calculate");
		calc.setPrefSize(100, 40);
		calc.setMinWidth(100);
		calc.setOnAction(e -> {
			output.setText("Ergebnis:  " + check(a));
		});

		input = new TextArea();
		input.setPrefSize(100, 40);
		input.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					input.setText(newValue.replaceAll("[^\\d]", ""));
				}
				try {
					a = Integer.parseInt(input.getText());
				} catch (Exception e) {
				}
			}

		});

		pane = new GridPane();
		pane.setPadding(new Insets(30));
		pane.setHgap(20);
		pane.setVgap(20);
		pane.setBackground(null);

		pane.add(input, 0, 0);
		pane.add(calc, 1, 0);
		pane.add(output, 0, 1);

		GridPane.setConstraints(output, 0, 1, 2, 1);
	}

	private int check(int i) {
		int j = i;
		if (j < 0) {
			return j;
		} else {
			return fak(j, j);
		}

	}

	private int fak(int i, int j) {
		i--;
		if (i > 0) {
			j *= i;
			return fak(i, j);
		} else {
			return j;
		}
	}

	@Override
	public void start(Stage stage) {
		Scene scene = new Scene(pane);
		scene.setFill(Color.rgb(30, 6, 40));
		stage.setScene(scene);

		stage.setTitle("Fakultät");
		stage.getIcons().add(new Image(("img/icon2.png")));

		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
