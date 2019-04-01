// Name: Jiefei Xia, andrew ID: jiefeix

package hw3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Nutrient {
    private StringProperty nutrientCode = new SimpleStringProperty();
    private StringProperty nutrientName = new SimpleStringProperty();
    private StringProperty nutrientUom = new SimpleStringProperty();

    public Nutrient(){
        nutrientCode.set("");
        nutrientName.set("");
        nutrientUom.set("");
    }

    public Nutrient(String nutrientCode, String nutrientName, String nutrientUom){
        this.nutrientCode.set(nutrientCode);
        this.nutrientName.set(nutrientName);
        this.nutrientUom.set(nutrientUom);
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

    public String getNutrientName() {
        return nutrientName.get();
    }

    public StringProperty nutrientNameProperty() {
        return nutrientName;
    }

    public void setNutrientName(String nutrientName) {
        this.nutrientName.set(nutrientName);
    }

    public String getNutrientUom() {
        return nutrientUom.get();
    }

    public StringProperty nutrientUomProperty() {
        return nutrientUom;
    }

    public void setNutrientUom(String nutrientUom) {
        this.nutrientUom.set(nutrientUom);
    }
}
