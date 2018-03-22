package classes;

import java.awt.Point;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Tree extends Application {

	private BorderPane borderPane;
	private Scene scene;

	private Group tree;
	private GridPane controls;
	private GridPane stats;

	private Button renderB;
	private Button renderL;
	private Button clear;

	private TextField minVal;
	private TextField maxVal;

	private Slider thickness;
	private Slider color;
	private Slider alpha;
	private Slider length;
	private Slider leafThick;

	private Label leafC;
	private Label branchC;
	private Label leafCN;
	private Label branchCN;
	private Label totalEver;
	private Label totalEverN;
	
	private CheckBox alphC;
	private CheckBox lengthC;
	private CheckBox thickC;
	private CheckBox colorC;

	private double stagesCount = 0;
	private double leavesCount = 0;

	private double min;
	private double max;
	private double thick;
	private double alph;
	private double leng;
	private double leavesThick;

	private int branchesCount;
	private int leafCount;
	private int totalEv;

	@Override
	public void init() {
		tree = new Group();

		controls = new GridPane();
		stats = new GridPane();

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
			length.setValue(length.getMax() - length.getMin());

			branchesCount = 0;
			leafCount = 0;

			leafC.setText("" + leafCount);
			branchC.setText("" + branchesCount);
		});

		minVal = new TextField("2");
		minVal.setPrefSize(90, 30);
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

		maxVal = new TextField("4");
		maxVal.setPrefSize(90, 30);
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
		color.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				if(Double.parseDouble(arg2.toString()) <= color.getMin()) {
					color.setValue(color.getMax());
				}
			}
			
		});

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

		leafThick = new Slider();
		leafThick.setMin(4);
		leafThick.setMax(32);
		leafThick.setPrefSize(200, 16);
		leafThick.setShowTickLabels(true);
		leafThick.setShowTickMarks(true);
		leafThick.setMinorTickCount(4);
		leafThick.setMajorTickUnit(8);
		leafThick.setSnapToTicks(true);
		
		Font f = new Font("Roboto Black", 12);
		Color c = Color.WHITESMOKE;
		
		alphC = new CheckBox("Alpha Static");
		alphC.setFont(f);
		alphC.setTextFill(c);
		alphC.setSelected(true);
		colorC = new CheckBox("Color Static");
		colorC.setFont(f);
		colorC.setTextFill(c);
		lengthC = new CheckBox("Length Static");
		lengthC.setFont(f);
		lengthC.setTextFill(c);
		thickC = new CheckBox("Thickness Static");
		thickC.setFont(f);
		thickC.setTextFill(c);
		
		

		controls.setPadding(new Insets(20));
		controls.setHgap(15);
		controls.setVgap(25);
		
		//controls.setGridLinesVisible(true);
		
		

		controls.add(renderB, 0, 0);
		controls.add(renderL, 0, 1);
		controls.add(clear, 0, 2);
		controls.add(minVal, 0, 3);
		controls.add(maxVal, 1, 3);
		controls.add(length, 0, 4);
		controls.add(thickness, 0, 5);
		controls.add(color, 0, 6);
		controls.add(alpha, 0, 7);
		controls.add(leafThick, 0, 8);
		controls.add(colorC, 0, 9);
		controls.add(alphC, 1, 9);
		controls.add(lengthC, 0, 10);
		controls.add(thickC, 1, 10);
		
		GridPane.setConstraints(renderB, 0, 0,2,1,HPos.CENTER,VPos.CENTER);
		GridPane.setConstraints(renderL, 0, 1,2,1,HPos.CENTER,VPos.CENTER);
		GridPane.setConstraints(clear, 0, 2,2,1,HPos.CENTER,VPos.CENTER);
		GridPane.setConstraints(length, 0, 4,2,1);
		GridPane.setConstraints(thickness, 0, 5,2,1);
		GridPane.setConstraints(color, 0, 6,2,1);
		GridPane.setConstraints(alpha, 0, 7,2,1);
		GridPane.setConstraints(leafThick, 0, 8,2,1);

		thickness.setValue(thickness.getMax());
		color.setValue(color.getMax());
		alpha.setValue(alpha.getMax());
		length.setValue(length.getMax());
		leafThick.setValue(leafThick.getMax() - leafThick.getMin());

		stats.setPadding(new Insets(20));
		stats.setHgap(15);
		stats.setVgap(25);

		f = new Font("Roboto Black", 16);
		

		branchCN = new Label("Branches generated:");
		branchCN.setFont(f);
		branchCN.setTextFill(c);
		branchC = new Label(branchesCount + "");
		branchC.setFont(f);
		branchC.setTextFill(c);
		leafCN = new Label("Leaves generated:");
		leafCN.setFont(f);
		leafCN.setTextFill(c);
		leafC = new Label(leafCount + "");
		leafC.setFont(f);
		leafC.setTextFill(c);
		totalEverN = new Label("Total generated:");
		totalEverN.setFont(f);
		totalEverN.setTextFill(c);
		totalEver = new Label(totalEv + "");
		totalEver.setFont(f);
		totalEver.setTextFill(c);

		stats.add(branchCN, 0, 0);
		stats.add(branchC, 1, 0);
		stats.add(leafCN, 0, 1);
		stats.add(leafC, 1, 1);
		stats.add(totalEverN, 0, 2);
		stats.add(totalEver, 1, 2);

	}

	private void renderNewStage() {

		min = Integer.parseInt(minVal.getText());
		max = Integer.parseInt(maxVal.getText());
		thick = thickness.getValue();
		alph = alpha.getValue() / 100;
		leng = length.getValue();

		Color c = Color.hsb(color.getValue(), 1, 1, alph);

		if (tree.getChildren().size() == 0) {
			Branch b = new Branch(new Point(0, 0), thick, c, leng, Math.PI, (int) (stagesCount));
			tree.getChildren().add(b);
			tree.setManaged(false);
			branchesCount++;
			tree.setTranslateY(scene.getHeight() / 2);
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
						branchesCount++;
					}
				}
			}
		}
		try {
			if (!colorC.isSelected()) {
				color.setValue(color.getValue() - 8);
			}
			if (!thickC.isSelected()) {
				thickness.setValue(thickness.getValue() - 0.5);
			}
			if (!alphC.isSelected()) {
				alpha.setValue(alpha.getValue() - 8);
			}
			if (!lengthC.isSelected()) {
				length.setValue(length.getValue() - 15);
			}
		} catch (Exception e) {
		}

		stagesCount++;
		leavesCount = 20;

		branchC.setText("" + branchesCount);

		totalEv = branchesCount + leafCount;

		totalEver.setText("" + totalEv);

	}

	private void renderLeaves() {

		min = Integer.parseInt(minVal.getText());
		max = Integer.parseInt(maxVal.getText());
		Color c = Color.hsb(color.getValue(), 1, 1, 0.5);
		leavesThick = leafThick.getValue();

		double buffC = stagesCount + leavesCount;

		for (int i = 0; i < tree.getChildren().size(); i++) {
			Node buff = tree.getChildren().get(i);
			if (buff instanceof Branch) {

				if (((Branch) buff).getStage() == buffC - leavesCount - 1) {

					int count = (int) Math.round(Math.random() * Math.abs(min - max + 2)) + (int) min;
					for (int j = 0; j < count; j++) {

						Point p = new Point(((Branch) buff).getEnd());
						Branch b = new Branch(p, leavesThick / 2, c, leavesThick, 2 * Math.PI, (int) (buffC));
						tree.getChildren().add(b);

						leafCount++;
					}
				}
			}
		}
		try {
			if (!colorC.isSelected()) {
				color.setValue(color.getValue() - 8);
			}
		} catch (Exception e) {
		}

		leavesCount += 20;

		leafC.setText("" + leafCount);

		totalEv = branchesCount + leafCount;

		totalEver.setText("" + totalEv);
	}

	@Override
	public void start(final Stage stage) {
		borderPane = new BorderPane();
		borderPane.setCenter(tree);
		borderPane.setLeft(controls);
		borderPane.setRight(stats);
		borderPane.setBackground(null);
		scene = new Scene(borderPane, 666, 666, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.rgb(30, 6, 40));
		stage.setScene(scene);
		stage.setTitle("Generic Tree");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("../img/icon.png")));

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.F11) {
					stage.setFullScreen(!stage.isFullScreen());
				}

			}

		});

		stage.show();
	}

	public static void main(String[] args) {
		launch(args);

	}

}
