// Name: Jiefei Xia, andrew ID: jiefeix

// Name: Jiefei Xia, andrew ID: jiefeix

package hw3;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RecommendedNutrient {
    private StringProperty nutrientCode = new SimpleStringProperty();
    private FloatProperty nutrientQuantity = new SimpleFloatProperty();

    public RecommendedNutrient() {
        nutrientCode.set("");
    }

    public RecommendedNutrient(String nutrientCode, float nutrientQuantity) {
        this.nutrientCode.set(nutrientCode);
        this.nutrientQuantity.set(nutrientQuantity);
    }

    public String getNutrientCode() {
        return nutrientCode.get();
    }

    public StringProperty nutrientCodeProperty() {
        return nutrientCode;
    }

    public void setNutrientCode(String nutrientCode) {
        this.nutrientCode.set(nutrientCode);
    }

    public float getNutrientQuantity() {
        return nutrientQuantity.get();
    }

    public FloatProperty nutrientQuantityProperty() {
        return nutrientQuantity;
    }

    public void setNutrientQuantity(float nutrientQuantity) {
        this.nutrientQuantity.set(nutrientQuantity);
    }
}
