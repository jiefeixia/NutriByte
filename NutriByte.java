// Name: Jiefei Xia, andrew ID: jiefeix

package hw3;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;


public class NutriByte extends Application{
	static Model model = new Model();  	//made static to make accessible in the controller
	static View view = new View();		//made static to make accessible in the controller
	static Person person;				//made static to make accessible in the controller


	Controller controller = new Controller();	//all event handlers

	/**Uncomment the following three lines if you want to try out the full-size data files */
//	static final String PRODUCT_FILE = "data/Products.csv";
//	static final String NUTRIENT_FILE = "data/Nutrients.csv";
//	static final String SERVING_SIZE_FILE = "data/ServingSize.csv";

	/**The following constants refer to the data files to be used for this application */
	static final String PRODUCT_FILE = "data/Nutri2Products.csv";
	static final String NUTRIENT_FILE = "data/Nutri2Nutrients.csv";
	static final String SERVING_SIZE_FILE = "data/Nutri2ServingSize.csv";

	static final String NUTRIBYTE_IMAGE_FILE = "NutriByteLogo.png"; //Refers to the file holding NutriByte logo image

	static final String NUTRIBYTE_PROFILE_PATH = "profiles";  //folder that has profile data files

	static final int NUTRIBYTE_SCREEN_WIDTH = 1015;
	static final int NUTRIBYTE_SCREEN_HEIGHT = 675;

	@Override
	public void start(Stage stage) throws Exception {
		model.readProducts(PRODUCT_FILE);
		model.readNutrients(NUTRIENT_FILE);
		model.readServingSizes(SERVING_SIZE_FILE );
		view.setupMenus();
		view.setupNutriTrackerGrid();
		view.root.setCenter(view.setupWelcomeScene());
		Background b = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
		view.root.setBackground(b);
		Scene scene = new Scene (view.root, NUTRIBYTE_SCREEN_WIDTH, NUTRIBYTE_SCREEN_HEIGHT);
		view.root.requestFocus();  //this keeps focus on entire window and allows the textfield-prompt to be visible

		setupBindings();
		stage.setTitle("NutriByte 3.0");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	void setupBindings() {

		// region1 recommend nutrient
		view.genderComboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				checkProfileInput();
			}
		});
		view.ageTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				checkProfileInput();
			}
		});
		view.weightTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				checkProfileInput();
			}
		});
		view.heightTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				checkProfileInput();
			}
		});
		view.physicalActivityComboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				checkProfileInput();
			}
		});

		view.recommendedNutrientNameColumn.setCellValueFactory(recommendedNutrientNameCallback);
		view.recommendedNutrientQuantityColumn.setCellValueFactory(recommendedNutrientQuantityCallback);
		view.recommendedNutrientUomColumn.setCellValueFactory(recommendedNutrientUomCallback);
		view.createProfileButton.setOnAction(controller.new RecommendNutrientsButtonHandler());

		// region 2 search product
		view.searchButton.setOnAction(controller.new SearchButtonHandler());
		view.clearButton.setOnAction(controller. new ClearButtonHandler());

		view.productNutrientNameColumn.setCellValueFactory(productNutrientNameCallback);
		view.productNutrientQuantityColumn.setCellValueFactory(productNutrientQuantityCallback);
		view.productNutrientUomColumn.setCellValueFactory(productNutrientUomCallback);
		view.productsComboBox.valueProperty().addListener(new ChangeListener<Product>() {
			@Override
			public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
				if (newValue != null){
					//update product nutrients table
					view.productNutrientsTableView.setItems(
							FXCollections.observableList(
									new ArrayList<Product.ProductNutrient>(newValue.getProductNutrients().values())));

					// update size and Uom
					String servingUom = newValue.getServingUom();
					String householdUom = newValue.getHouseholdUom();
					view.servingSizeLabel.setText(String.format("%.2f %s",
							model.productsMap.get(newValue.getNbdNumber()).getServingSize(), servingUom));
					view.householdSizeLabel.setText(String.format("%.2f %s",
							model.productsMap.get(newValue.getNbdNumber()).getHouseholdSize(), householdUom));
					view.dietServingUomLabel.setText(servingUom);
					view.dietHouseholdUomLabel.setText(householdUom);
					view.productIngredientsTextArea.setText("Product ingredients: " + newValue.getIngredients());
				}
			}
		});

		// region 3 add product
		view.addDietButton.setOnAction(controller. new AddDietButtonHandler());
		view.removeDietButton.setOnAction(controller. new RemoveDietButtonHandler());

		// menu item
		view.newNutriProfileMenuItem.setOnAction(controller.new NewMenuItemHandler());
		view.openNutriProfileMenuItem.setOnAction(controller.new OpenMenuItemHandler());
		view.exitNutriProfileMenuItem.setOnAction(event -> Platform.exit());
		view.aboutMenuItem.setOnAction(controller.new AboutMenuItemHandler());
		view.saveNutriProfileMenuItem.setOnAction(controller.new SaveMenuItemHandler());
		view.closeNutriProfileMenuItem.setOnAction(controller.new CloseMenuItemHandler());
	}


	// check whether the profile should be created or updated
	void checkProfileInput(){
		float age = validateTextField(view.ageTextField);
		float height = validateTextField(view.heightTextField);
		float weight = validateTextField(view.weightTextField);

		float physicalLevel = 1;
		for (NutriProfiler.PhysicalActivityEnum e : NutriProfiler.PhysicalActivityEnum.values()){
			if (e.getName().equals(NutriByte.view.physicalActivityComboBox.getValue())) {
				physicalLevel = e.getPhysicalActivityLevel();
			}
		}

		if (view.genderComboBox.getValue()!=null && age!=-1 && weight!=-1 && height!=-1){
			if (view.genderComboBox.getValue().equals("Male")){
				person = new Male(age,weight,height,physicalLevel,view.ingredientsToWatchTextArea.getText());
			}else{
				person = new Female(age,weight,height,physicalLevel,view.ingredientsToWatchTextArea.getText());
			}
			NutriProfiler.createNutriProfile(person);
			view.recommendedNutrientsTableView.setItems(person.recommendedNutrientsList);
		}
	}


	// check whether the input of age, height and weight is float and larger than zero
	private float validateTextField(TextField textField){
		try {
			float value = Float.parseFloat(textField.getText());
			textField.setStyle("-fx-text-fill: black");
			if (value<0) {
				textField.setStyle("-fx-text-fill: red");
				return -1;
			}
			return value;
		}
		catch (NumberFormatException e){
			textField.setStyle("-fx-text-fill: red");
			return -1;
		}

	}


	// ***************************************Call back Class for table view******************************************//
	// recommendedNutrientTable
	// recommendedNutrientNameCallback
	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientNameCallback =
			new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			return Model.nutrientsMap.get(arg0.getValue().getNutrientCode()).nutrientNameProperty();
		}
	};
	// recommendedNutrientQuantityCallback


	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientQuantityCallback =
			new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			return new SimpleStringProperty(String.format("%.2f", arg0.getValue().getNutrientQuantity()));
		}
	};
	// recommendedNutrientUomCallback


	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientUomCallback =
			new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			return Model.nutrientsMap.get(arg0.getValue().getNutrientCode()).nutrientUomProperty();
		}
	};


	// productNutrientNameCallback
	Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientNameCallback =
			new Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Product.ProductNutrient, String> arg0) {
					return Model.nutrientsMap.get(arg0.getValue().getNutrientCode()).nutrientNameProperty();
				}
			};


	// productNutrientQuantityCallback
	Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientQuantityCallback =
			new Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Product.ProductNutrient, String> arg0) {
					return new SimpleStringProperty(String.format("%.2f", arg0.getValue().getNutrientQuantity()));
				}
			};


	// productProductNutrientUomCallback
	Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientUomCallback =
			new Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Product.ProductNutrient, String> arg0) {
					return Model.nutrientsMap.get(arg0.getValue().getNutrientCode()).nutrientUomProperty();
				}
			};


}
