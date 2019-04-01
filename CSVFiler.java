// Name: Jiefei Xia, andrew ID: jiefeix

package hw3;

import javafx.scene.control.Alert;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class CSVFiler {

    void writeFile(String filename) {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(filename))) {
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

                Person person = validatePersonData(profile);

                printWriter.printf("%s, %.2f, %.2f, %.2f, %.2f, %s\n",
                        person instanceof Male? "Male" : "Female", person.age, person.weight, person.height,
                        person.physicalActivityLevel, person.ingredientsToWatch);
            }
            catch (InvalidProfileException e){
                return;
            }
            for (Product product:NutriByte.person.dietProductList){
                printWriter.printf("%s, %.2f, %.2f\n", product.getNbdNumber(), product.getServingSize(), product.getHouseholdSize());
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    boolean readFile(String filename){
        if (filename == null) return false;
        try (Scanner input = new Scanner(new File(filename))){

            if (input.hasNextLine()){//read person
                try {
                    NutriByte.person = validatePersonData(input.nextLine());
                }
                catch (InvalidProfileException e){
                    return false;
                }
            }
            while(input.hasNextLine()){//read products
                Product profileProduct = validateProductData(input.nextLine());
                if (profileProduct != null) NutriByte.person.dietProductList.add(profileProduct);
            }
            return true;
        } catch (FileNotFoundException e){
            return false;
        }
    }


    // the method is static in order to be called in Controller.SaveMenuItemHandler
    static Person validatePersonData (String data) throws InvalidProfileException {
        String[] dataList = data.split(", ", 6);

        if (dataList.length==1){ // delimiter doesn't contain space
            dataList = dataList[0].split(",", 6);
        }


        if (dataList[0].equals("null"))  throw new InvalidProfileException("Missing gender information");
        if (!Arrays.asList("Male", "Female").contains(dataList[0])) {
            throw new InvalidProfileException("The profile must have gender; Female or Male as first word");
        }


        // check whether is empty or not float or <0
        String[] profileNameList = {"Age", "Weight", "Height", "physical activity level"};
        for (int i=1;i<4;i++){//loop from Age to physical Activity
            if (dataList[i].isEmpty())  {
                throw new InvalidProfileException("Missing " + profileNameList[i-1].toLowerCase() + " information");
            }
            try {
                float foo = Float.parseFloat(dataList[i]); // for test
                if (foo<0) throw new InvalidProfileException(profileNameList[i-1] + " must be a positive number");
            }
            catch (NumberFormatException e){
                throw new InvalidProfileException("Incorrect " + profileNameList[i-1] + "input" + "\n"
                        + "Must be a number");
            }
        }


        boolean validPhysicalActivity = false;
        for (NutriProfiler.PhysicalActivityEnum e: NutriProfiler.PhysicalActivityEnum.values()){
            if(e.getPhysicalActivityLevel()==Float.valueOf(dataList[4])) validPhysicalActivity = true;
        }
        if (!validPhysicalActivity) {
            throw new InvalidProfileException("Invalid physical activity level: " + dataList[4]
                    + "\nMust be 1.0, 1.1, 1.25, or 1.48");
        }

        switch (dataList[0]){// decide gender
            case "Male":
                return new Male(Float.parseFloat(dataList[1]), Float.parseFloat(dataList[2]),
                        Float.parseFloat(dataList[3]), Float.parseFloat(dataList[4]), dataList[5]);
            default: // Female
                return new Female(Float.parseFloat(dataList[1]), Float.parseFloat(dataList[2]),
                        Float.parseFloat(dataList[3]), Float.parseFloat(dataList[4]), dataList[5]);
        }
    }


    // validate the product input
    Product validateProductData(String data){
        String[] dataList = data.split(", ");

        if(dataList.length<3){
            alert("Cannot read " + data
                    + "\nThe data must be - String, number, number - for ndb number, \nserving size, household size");
            return null;
        }

        if(! Model.productsMap.containsKey(dataList[0])){
            alert("No product found with this code: " + dataList[0]);
            return null;
        }

        // if product exists
        try{
            Product product = new Product(Model.productsMap.get(dataList[0])); // use deep copy constructor

            product.setServingSize(Float.valueOf(dataList[1]));
            product.setHouseholdSize(Float.valueOf(dataList[2]));
            return product;
        }
        catch (NumberFormatException e){
            alert("Cannot read " + data
                    + "\nThe data must be - String, number, number - for ndb number, \nserving size, household size");
            return null;
        }
    }

    // alert popup for invalid input
    private void alert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Profile Data Error" );
        alert.setTitle("NutriByte 3.0");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
