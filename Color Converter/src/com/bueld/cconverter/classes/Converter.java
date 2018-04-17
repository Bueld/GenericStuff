package com.bueld.cconverter.classes;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Converter extends Application {

	private BorderPane borderPane;
	private Scene scene;

	private GridPane controls;
	private StackPane translation;

	private int[] r;
	private int[] g;
	private int[] b;

	public void init() {

		r = new int[26];
		g = new int[26];
		b = new int[26];

		createControls();

		initialRefresh();
	}

	private void createControls() {

		controls = new GridPane();

		controls.setVgap(6);
		controls.setHgap(6);
		controls.setPadding(new Insets(12));

		Font f = new Font(18);

		Text t1 = new Text("RED");

		t1.setFill(Color.WHITESMOKE);
		t1.setFont(f);

		controls.add(t1, 1, 0);
		Text t2 = new Text("GREEN");

		t2.setFill(Color.WHITESMOKE);
		t2.setFont(f);

		controls.add(t2, 2, 0);
		Text t3 = new Text("BLUE");

		t3.setFill(Color.WHITESMOKE);
		t3.setFont(f);
		controls.add(t3, 3, 0);

		for (int i = 0; i < 26; i++) {

			Text t = new Text(Character.toString((char) ((i) + 'A')));

			t.setFill(Color.WHITESMOKE);
			t.setFont(f);

			controls.add(t, 0, i + 1);

			addTextFields(i + 1);
		}

		createPreview();
	}

	private void addTextFields(int row) {

		for (int i = 0; i < 3; i++) {

			String str = calculateInitialValue(row - 1, i);

			TextField tf = new TextField(str);
			tf.setPrefSize(45, 18);

			int j = i;

			tf.textProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("\\d*")) {
						tf.setText(newValue.replaceAll("[^\\d]", ""));
					}

					try {
						setNewWorth(j, row - 1, Integer.parseInt(tf.getText()));
					} catch (NumberFormatException e) {
					}
				}

			});

			controls.add(tf, i + 1, row);
		}
	}

	private void setNewWorth(int column, int row, int worth) {

		switch (column) {
		case 0:
			r[row] = worth;
			break;
		case 1:
			g[row] = worth;
			break;
		case 2:
			b[row] = worth;
			break;
		}

		refreshPreview(row);
	}

	private void createPreview() {

		for (int i = 1; i < 27; i++) {
			Rectangle rect = new Rectangle();
			rect.setFill(Color.rgb(r[i - 1], g[i - 1], b[i - 1]));

			rect.setHeight(18);
			rect.setWidth(36);

			controls.add(rect, 4, i);
		}
	}

	private void refreshPreview(int row) {

		for (Node buff : controls.getChildren()) {
			if (buff instanceof Rectangle) {
				if (GridPane.getRowIndex(buff) == row + 1) {
					((Rectangle) buff).setFill(Color.rgb(r[row], g[row], b[row]));
				}
			}
		}
	}

	private String calculateInitialValue(int row, int column) {
		int a;
		switch (column) {
		case 0:
			double b = (255 * row) / 25;
			if (row < 13) {
				a = (int) Math.round(b) + 127;
			} else {
				a = (int) (255 - Math.round(b) + 127);
			}
			break;
		case 1:
			double c = (255 * row) / 25;
			a = (int) (255 - Math.round(c));
			break;
		default:
			double d = (255 * row) / 25;
			a = (int) Math.round(d);
			break;
		}
		return a + "";
	}

	private void initialRefresh() {

		for (Node buff : controls.getChildren()) {
			if (buff instanceof TextField) {
				switch (GridPane.getColumnIndex(buff)) {
				case 1:
					r[GridPane.getRowIndex(buff) - 1] = Integer.parseInt(((TextField) buff).getText());
					break;
				case 2:
					g[GridPane.getRowIndex(buff) - 1] = Integer.parseInt(((TextField) buff).getText());
					break;
				case 3:
					b[GridPane.getRowIndex(buff) - 1] = Integer.parseInt(((TextField) buff).getText());
					break;
				}
			}
		}

		for (Node buff : controls.getChildren()) {
			if (buff instanceof Rectangle) {
				((Rectangle) buff).setFill(Color.rgb(r[GridPane.getRowIndex(buff) - 1],
						g[GridPane.getRowIndex(buff) - 1], b[GridPane.getRowIndex(buff) - 1]));
			}
		}
	}

	@Override
	public void start(Stage stage) {

		borderPane = new BorderPane();
		borderPane.setCenter(translation);
		borderPane.setLeft(controls);
		borderPane.setBackground(null);
		scene = new Scene(borderPane, 666, 666, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.rgb(30, 6, 40));
		stage.setScene(scene);
		stage.setTitle("Color Converter");
		try {
			stage.getIcons().add(new Image(getClass().getResourceAsStream("../img/icon.png")));
		} catch (Exception e) {
		}

		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.F11) {
				stage.setFullScreen(!stage.isFullScreen());
			}
		});

		stage.show();
	}

	public static void main(String[] args) {

		launch(args);
	}

}
