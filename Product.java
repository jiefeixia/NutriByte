// Name: Jiefei Xia, andrew ID: jiefeix


package hw3;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class Product {
    private StringProperty nbdNumber = new SimpleStringProperty();
    private StringProperty productName = new SimpleStringProperty();
    private StringProperty  manufacturer = new SimpleStringProperty();
    private StringProperty  ingredients = new SimpleStringProperty();
    private FloatProperty servingSize = new SimpleFloatProperty();
    private StringProperty  servingUom = new SimpleStringProperty();
    private FloatProperty householdSize = new SimpleFloatProperty();
    private StringProperty  householdUom = new SimpleStringProperty();
    private ObservableMap<String, ProductNutrient> productNutrients = FXCollections.observableHashMap();

    public Product() {
        this.nbdNumber.set("");
        this.productName.set("");
        this.manufacturer.set("");
        this.ingredients.set("");
    }

    public Product(String nbdNumber, String productName, String manufacturer, String ingredients) {
        this.nbdNumber.set(nbdNumber);
        this.productName.set(productName);
        this.manufacturer.set(manufacturer);
        this.ingredients.set(ingredients);

    }

    // deep copy constructor
    public Product(Product original){
        this(original.getNbdNumber(), original.getProductName(), original.getManufacturer(), original.getIngredients());
        this.setServingSize(original.getServingSize());
        this.setServingUom(original.getServingUom());
        this.setHouseholdSize(original.getHouseholdSize());
        this.setHouseholdUom(original.getHouseholdUom());
        this.setProductNutrients(FXCollections.observableMap(original.getProductNutrients()));
    }

    @Override
    public String toString() {
        return (productName.get() + " by " + manufacturer.get());
    }

    public final String getNbdNumber() {
        return nbdNumber.get();
    }

    public final StringProperty nbdNumberProperty() {
        return nbdNumber;
    }

    public final void setNbdNumber(String nbdNumber) {
        this.nbdNumber.set(nbdNumber);
    }

    public final String getProductName() {
        return productName.get();
    }

    public final StringProperty productNameProperty() {
        return productName;
    }

    public final void setProductName(String productName) {
        this.productName.set(productName);
    }

    public final String getManufacturer() {
        return manufacturer.get();
    }

    public final StringProperty manufacturerProperty() {
        return manufacturer;
    }

    public final void setManufacturer(String manufacturer) {
        this.manufacturer.set(manufacturer);
    }

    public final String getIngredients() {
        return ingredients.get();
    }

    public final StringProperty ingredientsProperty() {
        return ingredients;
    }

    public final void setIngredients(String ingredients) {
        this.ingredients.set(ingredients);
    }

    public final float getServingSize() {
        return servingSize.get();
    }

    public final FloatProperty servingSizeProperty() {
        return servingSize;
    }

    public final void setServingSize(float servingSize) {
        this.servingSize.set(servingSize);
    }

    public final String getServingUom() {
        return servingUom.get();
    }

    public final StringProperty servingUomProperty() {
        return servingUom;
    }

    public final void setServingUom(String servingUom) {
        this.servingUom.set(servingUom);
    }

    public final float getHouseholdSize() {
        return householdSize.get();
    }

    public final FloatProperty householdSizeProperty() {
        return householdSize;
    }

    public final void setHouseholdSize(float householdSize) {
        this.householdSize.set(householdSize);
    }

    public final String getHouseholdUom() {
        return householdUom.get();
    }

    public final StringProperty householdUomProperty() {
        return householdUom;
    }

    public final void setHouseholdUom(String householdUom) {
        this.householdUom.set(householdUom);
    }

    public final ObservableMap<String, ProductNutrient> getProductNutrients() {
        return productNutrients;
    }

    public final void setProductNutrients(ObservableMap<String, ProductNutrient> productNutrients) {
        this.productNutrients = productNutrients;
    }

    public final void addProductNutrients(String nutrientCode, float nutrientQuantity) {
        this.productNutrients.put(nutrientCode, new ProductNutrient(nutrientCode, nutrientQuantity));
    }

    public class ProductNutrient{
        private StringProperty nutrientCode = new SimpleStringProperty();
        private FloatProperty nutrientQuantity = new SimpleFloatProperty();

        public ProductNutrient() {
            nutrientCode.set("");
        }

        public ProductNutrient(String nutrientCode, float nutrientQuantity){
            this.nutrientCode.set(nutrientCode);
            this.nutrientQuantity.set(nutrientQuantity);
        }

        public final String getNutrientCode() {
            return nutrientCode.get();
        }

        public final StringProperty nutrientCodeProperty() {
            return nutrientCode;
        }

        public final void setNutrientCode(String nutrientCode) {
            this.nutrientCode.set(nutrientCode);
        }

        public final float getNutrientQuantity() {
            return nutrientQuantity.get();
        }

        public final FloatProperty nutrientQuantityProperty() {
            return nutrientQuantity;
        }

        public final void setNutrientQuantity(float nutrientQuantity) {
            this.nutrientQuantity.set(nutrientQuantity);
        }
    }
}
