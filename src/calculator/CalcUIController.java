package calculator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.lang.String;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Math.min;

/**
 * FXML Controller class
 *
 * @author rajesh
 */
public class CalcUIController implements Initializable {

    Double temp = 0.0, sum = 0.0;
    boolean shouldClear;
    boolean isDecimal;
    String operatorPressed = "";
    String lastOperatorPressed = "";

    @FXML
    TextField outputTF;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        outputTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*([\\.]\\d*)?")) {
                    outputTF.setText(oldValue);
                }
            }
        });
    }

    //If operator is pressed set outputTF to the button press, else set outputTF to what's in outputTF and button press
    @FXML
    private void onNumberClick(ActionEvent event) {
        // if empty, then set first number to button pressed. else, concatenate
        if(outputEmpty() || shouldClear)
        {
            outputTF.setText(((Button)event.getSource()).getText());
            if(shouldClear){
                shouldClear = false;
            }
        }
        else
        {
            String tempDisplay = "";
            tempDisplay = outputTF.getText();
            tempDisplay += ((Button)event.getSource()).getText();
            outputTF.setText(tempDisplay);
        }
    }

    /*
        Checks if outputTF is not empty
        Checks which button was pressed and performs the correct operation on sum
        Checks if button was "=" or "%" if so sets outputTF to the sum & operatorPressed to default
            if not sets outputTF to default and operatorPressed to the button pressed
            flags operator pressed to true
     */
    @FXML
    private void onOperatorClick(ActionEvent event) {
            String tempDisplay = "";
            String answer;
            BigDecimal format;

            operatorPressed = ((Button)event.getSource()).getText();
            if(operatorPressed.equals("=") || operatorPressed.equals("%")){
                if(operatorPressed.equals("%")){
                    if(!lastOperatorPressed.equals("+") && !lastOperatorPressed.equals("-")){
                        outputTF.setText(tempDisplay.valueOf(0.01 * Double.parseDouble(outputTF.getText())));
                    } else{
                        outputTF.setText(tempDisplay.valueOf(temp * (0.01 * Double.parseDouble(outputTF.getText()))));
                    }
                }
                switch(lastOperatorPressed) {
                    case "/":
                        answer = tempDisplay.valueOf(temp / Double.parseDouble(outputTF.getText()));
                        outputTF.setText(formatAnswer(answer));
                        break;
                    case "*":
                        answer = tempDisplay.valueOf(temp * Double.parseDouble(outputTF.getText()));
                        outputTF.setText(formatAnswer(answer));
                        break;
                    case "-":
                        answer = tempDisplay.valueOf(temp - Double.parseDouble(outputTF.getText()));
                        outputTF.setText(formatAnswer(answer));
                        break;
                    case "+":
                        answer = tempDisplay.valueOf(temp + Double.parseDouble(outputTF.getText()));
                        outputTF.setText(formatAnswer(answer));
                        break;


                }
                shouldClear = true;
            } else if(operatorPressed.equals(".")){
                if(!isDecimal){
                    tempDisplay = outputTF.getText();

                    if(outputEmpty() || shouldClear){
                        tempDisplay = "0";
                    }

                    tempDisplay += (".");
                    outputTF.setText(tempDisplay);
                    isDecimal = true;
                    shouldClear = false;
                }
                return;
            } else{
                temp = Double.parseDouble(outputTF.getText());
                shouldClear = true;
            }
            isDecimal = false;
            lastOperatorPressed = operatorPressed;
    }

    //If outputTF is not empty remove 1 char from the outputTF
    @FXML
    private void onDELClick(ActionEvent event) {

        if(!outputEmpty()){
            String tempDisplay = "";
            tempDisplay = outputTF.getText();
            tempDisplay = tempDisplay.substring(0,tempDisplay.length()-1);
            outputTF.setText(tempDisplay);
            if(outputTF.getText().equals("")){
                outputTF.setText("0.0");
            }
        }
    }

    //Reset all values to their default states
    @FXML
    private void onCEClick(ActionEvent event) {
        if(!outputEmpty()){
            outputTF.setText("0.0");
        }
        isDecimal = false;
    }



    private boolean outputEmpty(){
        if(Double.parseDouble(outputTF.getText()) == 0.0 && !isDecimal) {
            return true;
        } else{
            return false;
        }
    }

    private String formatAnswer(String answer){
        BigDecimal format;

        format = BigDecimal.valueOf(Double.parseDouble(answer));
        MathContext mc = new MathContext(15);
        format = format.round(mc);

        /*for(int i = 0; i < format.toString().length(); i++){
            if(indexOf(format.toString())){

            }
        }
         */




        return format.toString();
    }
}
