// Name: Jiefei Xia, andrew ID: jiefeix

package hw3;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.HashMap;
import java.util.Map;

public abstract class Person {


	ObservableList<RecommendedNutrient> recommendedNutrientsList = FXCollections.observableArrayList();
	ObservableList<Product> dietProductList = FXCollections.observableArrayList();
	ObservableMap<String, RecommendedNutrient> dietNutrientsMap = FXCollections.observableHashMap();

	float age, weight, height, physicalActivityLevel; //age in years, weight in kg, height in cm
	String ingredientsToWatch;
	float[][] nutriConstantsTable = new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT][NutriProfiler.AGE_GROUP_COUNT];

	NutriProfiler.AgeGroupEnum ageGroup;

	abstract void initializeNutriConstantsTable();
	abstract float calculateEnergyRequirement();


	Person(float age, float weight, float height, float physicalActivityLevel, String ingredientsToWatch) {
		this.age = age;
		this.weight = weight;
		this.height = height;
		this.physicalActivityLevel = physicalActivityLevel;
		this.ingredientsToWatch = ingredientsToWatch;
	}

	//returns an array of nutrient values of size NutriProfiler.RECOMMENDED_NUTRI_COUNT.
	//Each value is calculated as follows:
	//For Protein, it multiples the constant with the person's weight.
	//For Carb and Fiber, it simply takes the constant from the
	//nutriConstantsTable based on NutriEnums' nutriIndex and the person's ageGroup
	//For others, it multiples the constant with the person's weight and divides by 1000.
	//Try not to use any literals or hard-coded values for age group, nutrient name, array-index, etc.
	float[] calculateNutriRequirement() {
		HashMap<Integer, Float> nutriRequirements = new HashMap<>();
		for (NutriProfiler.AgeGroupEnum e:ageGroup.values()){
			if (age<e.getAge()){ // ageGroupIndex = e.getAgeGroupIndex();
				for (NutriProfiler.NutriEnum nutriEnum : NutriProfiler.NutriEnum.values()) {
					switch(nutriEnum){
						case PROTEIN: {
							nutriRequirements.put(nutriEnum.getNutriIndex(),
									nutriConstantsTable[nutriEnum.getNutriIndex()][e.getAgeGroupIndex()] * weight);
							break;
						}
						case CARBOHYDRATE: {}
						case FIBER: {
							nutriRequirements.put(nutriEnum.getNutriIndex(),
									nutriConstantsTable[nutriEnum.getNutriIndex()][e.getAgeGroupIndex()]);
							break;
						}
						default:{
							nutriRequirements.put(nutriEnum.getNutriIndex(),
									nutriConstantsTable[nutriEnum.getNutriIndex()][e.getAgeGroupIndex()] * weight / 1000);
						}
					}
				}
				float[] nutriRequirementsArray = new float[nutriRequirements.size()];
				for (int i=0;i<nutriRequirements.size();i++){
					nutriRequirementsArray[i] = nutriRequirements.get(i);
				}
				nutriRequirements.clear();
				return nutriRequirementsArray;
			}
		}
		return null;
	}


	// populate dietNutrientsMap from dietProductList, need to clear dietProductList before use
	void populateDietNutrientMap(){
		for (Product product:dietProductList){ // calculate again
			for (Map.Entry<String, Product.ProductNutrient> productNutrientEntry: product.getProductNutrients().entrySet()){

				if (! dietNutrientsMap.containsKey(productNutrientEntry.getKey())){ // not in the existing productNutrientMap
					dietNutrientsMap.put(productNutrientEntry.getKey(),
							new RecommendedNutrient(productNutrientEntry.getValue().getNutrientCode(), 0));
				}

				dietNutrientsMap.get(productNutrientEntry.getKey()).setNutrientQuantity(
						dietNutrientsMap.get(productNutrientEntry.getKey()).getNutrientQuantity() +
						productNutrientEntry.getValue().getNutrientQuantity() *
								product.getServingSize() / 100);

			}
		}
	}
}
