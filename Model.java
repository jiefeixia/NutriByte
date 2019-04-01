// Name: Jiefei Xia, andrew ID: jiefeix


package hw3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Model {
    static ObservableMap<String, Product> productsMap = FXCollections.observableHashMap();
    static ObservableMap<String, Nutrient> nutrientsMap = FXCollections.observableHashMap();
    ObservableList<Product> searchResultList = FXCollections.observableArrayList();

    public void readProducts(String filename){
        try {
            Scanner input = new Scanner(new File(filename));
            input.nextLine(); // read the column names
            while(input.hasNextLine()){
                String[] line = input.nextLine().split("(\\.\")|^\"|\"\\r|(\",\")"); // first string is null
                productsMap.put(line[1], new Product(line[1],line[2],line[5],line[8]));
            }
            input.close();
        } catch (FileNotFoundException e){}
    }

    public void readNutrients(String filename){
        try {
            Scanner input = new Scanner(new File(filename));
            input.nextLine(); // read the column names
            while(input.hasNextLine()){
                String[] line = input.nextLine().split("(\\.\")|^\"|\"\\r|(\",\")");
                nutrientsMap.put(line[2], new Nutrient(line[1], line[3], line[6].replace("\"","")));
                if (Float.valueOf(line[5])>0) productsMap.get(line[1]).addProductNutrients(line[2],Float.parseFloat(line[5]));
            }
            input.close();
        } catch (FileNotFoundException e){}
    }

    public void readServingSizes(String filename){
        try {
            Scanner input = new Scanner(new File(filename));
            input.nextLine(); // read the column names
            while(input.hasNextLine()){
                String[] line = input.nextLine().split("(\\.\")|^\"|\"\\r|(\",\")");
                Product product = productsMap.get(line[1]);
                product.setServingSize(Float.parseFloat(line[2].isEmpty()? "0" : line[2]));
                product.setServingUom(line[3]);
                product.setHouseholdSize(Float.parseFloat(line[4].isEmpty()? "0" : line[4]));
                product.setHouseholdUom(line[5]);
            }
            input.close();
        } catch (FileNotFoundException e){}
    }

    public boolean readProfiles(String filename){
        return filename.contains(".csv") ? new CSVFiler().readFile(filename) : new XMLFiler().readFile(filename);
    }

    void writeProfile(String filename){
        new CSVFiler().writeFile(filename);
    }

}
