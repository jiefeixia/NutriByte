// Name: Jiefei Xia, andrew ID: jiefeix

package hw3;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Map;

public class Controller {

	class NewMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
			NutriByte.view.initializePrompts();
			NutriByte.view.recommendedNutrientsTableView.getItems().clear();
		}
	}

	class OpenMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			if (NutriByte.person != null) 	clear(); // not in the welcome page

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select file");
			fileChooser.setInitialDirectory(new File(NutriByte.NUTRIBYTE_PROFILE_PATH));
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
					new FileChooser.ExtensionFilter("XML Files", "*.xml"));
			File file = null;
			if ((file = fileChooser.showOpenDialog(null))!=null){
				if (NutriByte.model.readProfiles(file.getAbsolutePath())){
					// store NytriByte.person into a intermediate variable to be assigned to NytriByte.person later,
					// because the pointer of Nutribyte.person will change (I don't know why)
					Person person = NutriByte.person;

					NutriByte.view.genderComboBox.setValue(NutriByte.person instanceof Male ? "Male" : "Female");
					NutriByte.view.ageTextField.setText(String.valueOf(NutriByte.person.age));
					NutriByte.view.weightTextField.setText(String.valueOf(NutriByte.person.weight));
					NutriByte.view.heightTextField.setText(String.valueOf(NutriByte.person.height));
					for (NutriProfiler.PhysicalActivityEnum e : NutriProfiler.PhysicalActivityEnum.values()){
						if (NutriByte.person.physicalActivityLevel == e.getPhysicalActivityLevel()) {
							NutriByte.view.physicalActivityComboBox.setValue(e.getName());
						}
					}

					NutriByte.person = person; // assign nutribyte person with its original value
					NutriByte.view.ingredientsToWatchTextArea.setText(NutriByte.person.ingredientsToWatch);
					NutriProfiler.createNutriProfile(NutriByte.person);
					NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);

					NutriByte.view.searchResultSizeLabel.setText(NutriByte.person.dietProductList.size() + " product(s) found");
					NutriByte.view.productsComboBox.setItems(NutriByte.person.dietProductList);
					NutriByte.view.productsComboBox.getSelectionModel().selectFirst();

					NutriByte.view.dietProductsTableView.setItems(NutriByte.person.dietProductList);

					NutriByte.person.populateDietNutrientMap();
					NutriByte.view.nutriChart.updateChart();
				}
			}

		}
	}

	class AboutMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("About");
			alert.setHeaderText("NutriByte");
			alert.setContentText("Version 2.0 \nRelease 1.0\nCopyleft Java Nerds\nThis software is designed purely for educational purposes.\nNo commercial use intended");
			Image image = new Image(getClass().getClassLoader().getResource(NutriByte.NUTRIBYTE_IMAGE_FILE).toString());
			ImageView imageView = new ImageView();
			imageView.setImage(image);
			imageView.setFitWidth(300);
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			alert.setGraphic(imageView);
			alert.showAndWait();
		}
	}

	class CloseMenuItemHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event){
			clear();
			NutriByte.view.root.setCenter(NutriByte.view.setupWelcomeScene());
		}
	}

	class SaveMenuItemHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event){
			// the try-catch is used to throw the invalida ProfileException before the file chooser pop up
			try{
				String physicalActivityLevel = "1";
				for (NutriProfiler.PhysicalActivityEnum e: NutriProfiler.PhysicalActivityEnum.values()){
					if (e.getName().equals(NutriByte.view.physicalActivityComboBox.getValue())) {
						physicalActivityLevel = String.format("%.2f", e.getPhysicalActivityLevel());
					}
				}


				String profile = String.format("%s, %s, %s, %s, %s, %s",
						NutriByte.view.genderComboBox.getValue(),
						NutriByte.view.ageTextField.getText(),
						NutriByte.view.weightTextField.getText(),
						NutriByte.view.heightTextField.getText(),
						physicalActivityLevel,
						NutriByte.view.ingredientsToWatchTextArea.getText());

				Person foo = CSVFiler.validatePersonData(profile);
			}
			catch (InvalidProfileException e){
				return;
			}

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select file");
			fileChooser.setInitialDirectory(new File(NutriByte.NUTRIBYTE_PROFILE_PATH));
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
					new FileChooser.ExtensionFilter("XML Files", "*.xml"));
			File file = null;
			if ((file = fileChooser.showSaveDialog(null))!=null){
				NutriByte.model.writeProfile(file.getAbsolutePath());
			}
		}
	}

	class SearchButtonHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event){
			NutriByte.model.searchResultList.clear();
			for (Map.Entry<String, Product> productEntry : NutriByte.model.productsMap.entrySet()){ // for each product
				boolean add = true;
				// search product
				if (!NutriByte.view.productSearchTextField.getText().isEmpty() &&
						!productEntry.getValue().getProductName().toLowerCase().contains(NutriByte.view.productSearchTextField.getText().toLowerCase())){
					add = false;
				}

				// search nutrient
				if (add && !NutriByte.view.nutrientSearchTextField.getText().isEmpty()){
					add = false;
					for (Map.Entry<String, Product.ProductNutrient> productNutrientEntry:
							productEntry.getValue().getProductNutrients().entrySet()){ // for each nutrient in the product
						if (NutriByte.view.nutrientSearchTextField.getText().equals( // compare nutrient name
								NutriByte.model.nutrientsMap.get(productNutrientEntry.getKey()).getNutrientName().toLowerCase())){
							add = true;
							break;
						}
					}
				}

				if (add && !NutriByte.view.ingredientSearchTextField.getText().isEmpty() &&
						!productEntry.getValue().getIngredients().toLowerCase().contains(NutriByte.view.ingredientSearchTextField.getText().toLowerCase())){
					add = false;
				}
				if (add) NutriByte.model.searchResultList.add(productEntry.getValue());
			}

			NutriByte.view.searchResultSizeLabel.setText(NutriByte.model.searchResultList.size() + " product(s) found");
			NutriByte.view.productsComboBox.setItems(NutriByte.model.searchResultList);
			NutriByte.view.productsComboBox.getSelectionModel().selectFirst();
		}
	}

	class ClearButtonHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event){
			// clear variable
			NutriByte.model.searchResultList.clear();

			// reset GUI
			NutriByte.view.productSearchTextField.setText("");
			NutriByte.view.nutrientSearchTextField.setText("");
			NutriByte.view.ingredientSearchTextField.setText("");

			NutriByte.view.servingSizeLabel.setText("0.00");
			NutriByte.view.servingUom.setText("");
			NutriByte.view.householdSizeLabel.setText("0.00");
			NutriByte.view.householdServingUom.setText("");

			NutriByte.view.productNutrientsTableView.setItems(null);
		}
	}

	class AddDietButtonHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event){
			Product product = new Product(NutriByte.view.productsComboBox.getValue()); // deep copy constructor

			if(! NutriByte.view.dietServingSizeTextField.getText().isEmpty()){ // if input serving size
				product.setServingSize(Float.parseFloat(NutriByte.view.dietServingSizeTextField.getText()));
				product.setHouseholdSize(NutriByte.model.productsMap.get(product.getNbdNumber()).getHouseholdSize() *
						product.getServingSize() / NutriByte.model.productsMap.get(product.getNbdNumber()).getServingSize());
			}else if(! NutriByte.view.dietHouseholdSizeTextField.getText().isEmpty()){ // if input household size but not input serving size
				product.setHouseholdSize(Float.parseFloat(NutriByte.view.dietHouseholdSizeTextField.getText()));

				if (NutriByte.model.productsMap.get(product.getNbdNumber()).getHouseholdSize()!=0){
					// in case calculate household/serving size is infinity (household size is zero)
					product.setServingSize(NutriByte.model.productsMap.get(product.getNbdNumber()).getServingSize() *
							product.getHouseholdSize() / NutriByte.model.productsMap.get(product.getNbdNumber()).getHouseholdSize());

				}
			}

			NutriByte.person.dietProductList.add(product);
			NutriByte.view.dietProductsTableView.setItems(NutriByte.person.dietProductList);

			NutriByte.person.dietNutrientsMap.clear();
			NutriByte.person.populateDietNutrientMap();
			NutriByte.view.nutriChart.updateChart();
		}
	}

	class RemoveDietButtonHandler implements EventHandler<ActionEvent>{
		public void handle(ActionEvent event){
			Product removedProduct = NutriByte.view.dietProductsTableView.getSelectionModel().getSelectedItem();
			if (removedProduct != null){
				// first delete nutrients
				for (Map.Entry<String, Product.ProductNutrient> productNutrientEntry:
						removedProduct.getProductNutrients().entrySet()){
					NutriByte.person.dietNutrientsMap.get(productNutrientEntry.getKey()).setNutrientQuantity(
							NutriByte.person.dietNutrientsMap.get(productNutrientEntry.getKey()).getNutrientQuantity() -
									productNutrientEntry.getValue().getNutrientQuantity() *
											removedProduct.getServingSize() / 100);
				}

				// then delete product
				NutriByte.person.dietProductList.remove(removedProduct);

				NutriByte.view.nutriChart.updateChart();
			}
		}

	}

	class RecommendNutrientsButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			float physicalLevel = 1;
			for (NutriProfiler.PhysicalActivityEnum e : NutriProfiler.PhysicalActivityEnum.values()){
				if (e.getName().equals(NutriByte.view.physicalActivityComboBox.getValue())) {
					physicalLevel = e.getPhysicalActivityLevel();
				}
			}

			try{
				String profile = String.format("%s, %s, %s, %s, %s, %s", NutriByte.view.genderComboBox.getValue(),
						NutriByte.view.ageTextField.getText(), NutriByte.view.weightTextField.getText(),
						NutriByte.view.heightTextField.getText(), physicalLevel,
						NutriByte.view.ingredientsToWatchTextArea.getText());

				NutriByte.person = CSVFiler.validatePersonData(profile);
			}
			catch (InvalidProfileException e){
				return;
			}

			NutriByte.view.recommendedNutrientsTableView.setItems(NutriByte.person.recommendedNutrientsList);
		}
	}

	private void clear(){
		// clear variable
		NutriByte.person.dietProductList.clear();
		NutriByte.person.dietNutrientsMap.clear();
		NutriByte.person.recommendedNutrientsList.clear();

		// reset GUI
		NutriByte.view.productNutrientsTableView.setItems(null);
		NutriByte.view.servingSizeLabel.setText("0.00");
		NutriByte.view.servingUom.setText("");
		NutriByte.view.householdSizeLabel.setText("0.00");
		NutriByte.view.householdServingUom.setText("");
		NutriByte.view.nutriChart.clearChart();
	}
}
