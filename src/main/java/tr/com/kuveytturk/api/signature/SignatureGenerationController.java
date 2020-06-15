/*
 * Copyright (c) 2020
 * KUVEYT TÜRK PARTICIPATION BANK INC.
 *
 * Author: Fikri Aydemir
 *
 * Project: API Request SignatureGenerator
 */

package tr.com.kuveytturk.api.signature;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;

import java.net.URL;
import java.util.ResourceBundle;

public class SignatureGenerationController implements Initializable {

    @FXML
    ComboBox<String> httpMethodComboBox = new ComboBox<>();

    @FXML
    TextField apiEndpointUrlTextField = new TextField();

    @FXML
    TextField accessTokenTextField = new TextField();

    @FXML
    TextArea privateKeyTextArea = new TextArea();

    @FXML
    TextArea postRequestBodyTextArea = new TextArea();

    @FXML
    TextArea signatureTextArea = new TextArea();

    @FXML
    Button generateSignatureButton = new Button();

    @FXML
    Button copyToClipBoardButton = new Button();

    @FXML
    Button cleanFormButton = new Button();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        httpMethodComboBox.getItems().add("Select");
        httpMethodComboBox.getItems().add("GET");
        httpMethodComboBox.getItems().add("POST");
        httpMethodComboBox.getSelectionModel().selectFirst();
        signatureTextArea.setEditable(false);
    }

    @FXML
    public void onHttpMethodChanged(ActionEvent event) {
        String selectedHttpMethod = httpMethodComboBox.getSelectionModel().getSelectedItem();

        if(selectedHttpMethod.equals("POST")){
            apiEndpointUrlTextField.setEditable(false);
            apiEndpointUrlTextField.setText("");
            postRequestBodyTextArea.setEditable(true);
        } else if (selectedHttpMethod.equals("GET")){
            apiEndpointUrlTextField.setEditable(true);
            postRequestBodyTextArea.setText("");
            postRequestBodyTextArea.setEditable(false);
        } else {
            apiEndpointUrlTextField.setEditable(false);
            apiEndpointUrlTextField.setText("");
            postRequestBodyTextArea.setText("");
            postRequestBodyTextArea.setEditable(false);
        }
    }

    @FXML
    public void onGenerateSignatureClicked(ActionEvent event) {
         String selectedHttpMethod = httpMethodComboBox.getSelectionModel().getSelectedItem();

        if(selectedHttpMethod.equals("Select")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("HTTP Method must be selected!");
            alert.showAndWait();
            return;

        } else if(selectedHttpMethod.equals("POST")){
            if(accessTokenTextField.getText().isBlank() || accessTokenTextField.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("AccessToken field cannot be left empty!");
                alert.showAndWait();
                return;
            }

            if(privateKeyTextArea.getText().isBlank() || privateKeyTextArea.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Private Key field cannot be left empty!");
                return;
            }

            if(postRequestBodyTextArea.getText().isBlank() || postRequestBodyTextArea.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("POST Request Body field cannot be left empty when the selected HTTP method is POST!");
                alert.showAndWait();
                return;
            }

            try {
                String signatureText = SignatureGeneratorUtility.generateSignatureForPostRequest(accessTokenTextField.getText().trim(),
                                                                          privateKeyTextArea.getText().trim(),
                                                                          postRequestBodyTextArea.getText().trim());
                signatureTextArea.setText(signatureText);
            } catch (SignatureGenerationException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Error occurred while generating the post request signature!");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }

        } else if (selectedHttpMethod.equals("GET")){
            if(accessTokenTextField.getText().isBlank() || accessTokenTextField.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("AccessToken field cannot be left empty!");
                alert.showAndWait();
                return;
            }

            if(apiEndpointUrlTextField.getText().isBlank() || apiEndpointUrlTextField.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("API Endpoint URL field cannot be left empty when the selected HTTP method is GET!");
                alert.showAndWait();
                return;
            }

            if(privateKeyTextArea.getText().isBlank() || privateKeyTextArea.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Private Key field cannot be left empty!");
                return;
            }

            if(apiEndpointUrlTextField.getText().contains("?")) {
                QueryParameterListBean queryParamListBean;
                try {
                    queryParamListBean = new QueryParameterListBean(apiEndpointUrlTextField.getText().trim());
                } catch (SignatureGenerationException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Error occurred while parsing the query parameters within the API Endpoint URL field!");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    return;
                }

                try {
                    String signatureText =
                            SignatureGeneratorUtility.generateSignatureForGetRequest(
                                    accessTokenTextField.getText().trim(),
                                    privateKeyTextArea.getText().trim(),
                            queryParamListBean.toList());
                    signatureTextArea.setText(signatureText);
                } catch (SignatureGenerationException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Error occurred while generating the post request signature!");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    return;
                }
            } else {

                try {
                    String signatureText =
                            SignatureGeneratorUtility.generateSignatureForGetRequest(
                                    accessTokenTextField.getText().trim(),
                                    privateKeyTextArea.getText().trim());
                    signatureTextArea.setText(signatureText);
                } catch (SignatureGenerationException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("Error occurred while generating the post request signature!");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    return;
                }
            }

        }

    }

    @FXML
    public void onCleanFormClicked(ActionEvent event) {
        httpMethodComboBox.getSelectionModel().selectFirst();
        apiEndpointUrlTextField.setText("");
        accessTokenTextField.setText("");
        privateKeyTextArea.setText("");
        postRequestBodyTextArea.setText("");
    }

    @FXML
    public void onCopyToClipBoardClicked(ActionEvent event) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.put(DataFormat.PLAIN_TEXT, signatureTextArea.getText());
        clipboard.setContent(content);
    }
}
