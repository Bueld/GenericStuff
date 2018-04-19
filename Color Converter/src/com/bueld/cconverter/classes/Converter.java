package com.bueld.cconverter.classes;

import java.io.File;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Converter extends Application {

	private BorderPane borderPane;
	private Scene scene;

	private GridPane controls;
	private GridPane translation;

	private GridPane translatedRects;

	private TextArea input;

	private Button save;
	private Button saveTable;

	private int[] r;
	private int[] g;
	private int[] b;

	public void init() {

		r = new int[26];
		g = new int[26];
		b = new int[26];

		createControls();

		initialRefresh();

		initInput();
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

	private void initInput() {

		input = new TextArea();
		input.setPrefSize(400, 300);

		input.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				translate(input.getText());

			}
		});

		translatedRects = new GridPane();
		translatedRects.setPadding(new Insets(12));
		translatedRects.setHgap(2);
		translatedRects.setVgap(5);

		translation = new GridPane();

		translation.setVgap(12);
		translation.setHgap(12);
		translation.setPadding(new Insets(24));

		translation.add(input, 0, 0);
		translation.add(translatedRects, 0, 1);

		save = new Button("Save Writing as PNG");
		save.setOnAction(e -> {
			saveAsPng(translatedRects);
		});
		translation.add(save, 0, 2);

		saveTable = new Button("Save Table as PNG");
		saveTable.setOnAction(e -> {
			saveAsPng(controls);
		});
		translation.add(saveTable, 0, 3);
	}

	private void translate(String str) {

		translatedRects.getChildren().clear();

		str = str.toUpperCase();

		int coords[] = new int[2];

		coords[0] = 0;
		coords[1] = 0;

		for (int i = 0; i < str.length(); i++) {
			switch (str.substring(i, i + 1)) {
			case " ":
				coords[0]++;
				break;
			default:
				if (str.substring(i, i + 1).hashCode() == 10) {
					coords[0] = 0;
					coords[1]++;
				} else {
					translatedRects.add(getColoredRect(str.substring(i, i + 1)), coords[0], coords[1]);
					coords[0]++;
				}
				break;
			}
		}

	}

	private Rectangle getColoredRect(String str) {

		Rectangle r = new Rectangle();
		r.setWidth(20);
		r.setHeight(20);

		for (Node buff : controls.getChildren()) {
			if (buff instanceof Text) {
				if (((Text) buff).getText().length() == 1) {

					if (((Text) buff).getText().equals(str)) {
						for (Node buff2 : controls.getChildren()) {
							if (buff2 instanceof Rectangle
									&& GridPane.getRowIndex(buff).equals(GridPane.getRowIndex(buff2))) {
								r.setFill(((Rectangle) buff2).getFill());
							}
						}
					}
				}
			}
		}

		return r;

	}

	private void saveAsPng(GridPane gP) {

		FileChooser fChooser = new FileChooser();

		fChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.PNG)", ".PNG"));
		fChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", ".png"));

		File f = fChooser.showSaveDialog(null);

		if (f != null) {
			try {

				SnapshotParameters sSP = new SnapshotParameters();
				sSP.setFill(Color.TRANSPARENT);

				WritableImage wImage = gP.snapshot(sSP, null);

				ImageIO.write(SwingFXUtils.fromFXImage(wImage, null), "png", f);

			} catch (Exception e) {
				System.out.println("Save failed");
				e.printStackTrace();
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
