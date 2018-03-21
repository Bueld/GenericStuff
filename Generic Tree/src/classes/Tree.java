package classes;

import java.awt.Point;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Tree extends Application {

	private BorderPane borderPane;
	private Scene scene;

	private Group tree;
	private GridPane controls;

	private Button renderB;
	private Button renderL;
	private Button clear;

	private TextField minVal;
	private TextField maxVal;

	private Slider thickness;
	private Slider color;
	private Slider alpha;
	private Slider length;

	private double stagesCount = 0;

	private double min;
	private double max;
	private double thick;
	private double alph;
	private double leng;

	@Override
	public void init() {
		tree = new Group();

		controls = new GridPane();

		renderB = new Button("Render new Stage");
		renderB.setPrefSize(200, 30);
		renderB.setOnAction(e -> {
			renderNewStage();
		});
		renderL = new Button("Render Leaves");
		renderL.setPrefSize(200, 30);
		renderL.setOnAction(e -> {
			renderLeaves();
		});
		clear = new Button("Clear Tree");
		clear.setPrefSize(200, 30);
		clear.setOnAction(e -> {
			tree.getChildren().clear();
			stagesCount = 0;
			thickness.setValue(thickness.getMax());
			alpha.setValue(alpha.getMax());
			length.setValue(length.getMax()-length.getMin());
		});

		minVal = new TextField("2");
		minVal.setPrefSize(200, 30);
		minVal.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					minVal.setText(newValue.replaceAll("[^\\d]", ""));
				}

				try {
					min = Integer.parseInt(minVal.getText());
				} catch (NumberFormatException e) {
				}
			}

		});

		maxVal = new TextField("5");
		maxVal.setPrefSize(200, 30);
		maxVal.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					maxVal.setText(newValue.replaceAll("[^\\d]", ""));
				}
				try {
					max = Integer.parseInt(maxVal.getText());
				} catch (Exception e) {
				}
			}

		});

		thickness = new Slider();
		thickness.setMin(0.5);
		thickness.setMax(5);
		thickness.setPrefSize(200, 16);
		thickness.setShowTickLabels(true);
		thickness.setShowTickMarks(true);
		thickness.setMinorTickCount(0);
		thickness.setMajorTickUnit(0.5);
		thickness.setBlockIncrement(0.5);
		thickness.setSnapToTicks(true);

		color = new Slider();
		color.setMin(0);
		color.setMax(360);
		color.setPrefSize(202, 16);
		color.setShowTickLabels(true);
		color.setShowTickMarks(true);
		color.setMinorTickCount(20);
		color.setMajorTickUnit(60);
		color.setBlockIncrement(2);
		color.setSnapToTicks(true);
		
		alpha = new Slider();
		alpha.setMin(0);
		alpha.setMax(100);
		alpha.setPrefSize(200, 16);
		alpha.setShowTickLabels(true);
		alpha.setShowTickMarks(true);
		alpha.setMinorTickCount(5);
		alpha.setMajorTickUnit(20);
		alpha.setSnapToTicks(true);
		
		length = new Slider();
		length.setMin(20);
		length.setMax(200);
		length.setPrefSize(200, 16);
		length.setShowTickLabels(true);
		length.setShowTickMarks(true);
		length.setMinorTickCount(10);
		length.setMajorTickUnit(50);
		length.setSnapToTicks(true);

		controls.setPadding(new Insets(20));
		controls.setHgap(15);
		controls.setVgap(25);

		controls.add(renderB, 0, 0);
		controls.add(renderL, 0, 1);
		controls.add(clear, 0, 2);
		controls.add(minVal, 0, 3);
		controls.add(maxVal, 0, 4);
		controls.add(length, 0, 5);
		controls.add(thickness, 0, 6);
		controls.add(color, 0, 7);
		controls.add(alpha, 0, 8);
		
		thickness.setValue(thickness.getMax());
		color.setValue(color.getMax());
		alpha.setValue(alpha.getMax());
		length.setValue(length.getMax());

	}

	private void renderNewStage() {

		min = Integer.parseInt(minVal.getText());
		max = Integer.parseInt(maxVal.getText());
		thick = thickness.getValue();
		alph = alpha.getValue()/100;
		leng = length.getValue();

		Color c = Color.hsb(color.getValue(), 1, 1,alph);

		if (tree.getChildren().size() == 0) {
			Branch b = new Branch(new Point(0, 0), thick, c, leng, Math.PI, (int) (stagesCount));
			tree.getChildren().add(b);
			tree.setManaged(false);

			tree.setTranslateY(scene.getHeight() / 3);
		}

		for (int i = 0; i < tree.getChildren().size(); i++) {
			Node buff = tree.getChildren().get(i);
			if (buff instanceof Branch) {

				if (((Branch) buff).getStage() == stagesCount - 1) {

					int count = (int) Math.round(Math.random() * Math.abs(min - max)) + (int) min;
					for (int j = 0; j < count; j++) {

						Point p = new Point(((Branch) buff).getEnd());
						Branch b = new Branch(p, thick, c, leng, Math.PI, (int) (stagesCount));
						tree.getChildren().add(b);
					}
				}
			}
		}
		try {
			color.setValue(color.getValue() -8);
			thickness.setValue(thickness.getValue() - 0.5);
			alpha.setValue(alpha.getValue()-8);
			length.setValue(length.getValue()-15);
		} catch (Exception e) {
		}

		stagesCount++;

	}

	private void renderLeaves() {

	}

	@Override
	public void start(final Stage stage) {
		borderPane = new BorderPane();
		borderPane.setCenter(tree);
		borderPane.setLeft(controls);
		borderPane.setBackground(null);
		scene = new Scene(borderPane, 666, 666, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.rgb(30, 6, 40));
		stage.setScene(scene);
		stage.setTitle("Generic Tree");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("../img/icon.png")));
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);

	}

}
