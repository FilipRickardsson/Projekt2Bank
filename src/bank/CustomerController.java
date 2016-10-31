package bank;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class CustomerController extends BaseController {

    private ObservableList<String> accounts;
    private int typeOfOperation = 0;

    @FXML
    private Label lblSsn;

    @FXML
    private TextField changeFirstName;

    @FXML
    private TextField changeLastName;

    @FXML
    private ListView listOfAccounts;

    @FXML
    private RadioButton saving;

    @FXML
    private RadioButton credit;

    @FXML
    private Label message;
    @FXML
    private Label message2;
    @FXML
    private Button btnAccount;

    private final ToggleGroup group = new ToggleGroup();

    @FXML
    private void buttonChange(ActionEvent event) {
        long newSsn = BaseController.selectedCustomerSSN;
        if (changeFirstName.getText().length() > 1 && changeLastName.getText().length() > 1) {

            if (bankLogic.changeCustomer(changeFirstName.getText() + " "
                           + changeLastName.getText(), newSsn)) {
                message.setText("Change success");
                System.out.println("Change saved to: " + changeFirstName.getText() + changeLastName.getText());
            } else {
                message.setText("Invalid symbols");
            }
        }else {
           message.setText("Input too short!");
        }
    }

    @FXML
    private void buttonRemove(ActionEvent event) {
        if(listOfAccounts.getSelectionModel().getSelectedItem()!=null){
            typeOfOperation = 0;
        setPopupMessage("Are you sure you want to\nremove the account?");
        showPopup();
        }else{
            message2.setText("Nothing selected");
        }
        
    }

    @FXML
    private void buttonCreate(ActionEvent event) {
        typeOfOperation = 1;
        if (saving.isSelected()) {
            setPopupMessage("Create saving account?");
        } else {
            setPopupMessage("Create credit account?");
        }
        showPopup();
    }

    @FXML
    private void buttonSelect(ActionEvent event) throws IOException {
        
        if(listOfAccounts.getSelectionModel().getSelectedItem()!=null){
           selectedCustomerAccountID = bankLogic.getCustomerAccountIdViaIndex(listOfAccounts.getSelectionModel().getSelectedIndex());
        loadScene("Account.fxml"); 
        }else
        {
           message2.setText("Nothing selected");
        }
    }

    @Override
    protected void popupYes() {
        if (typeOfOperation == 0) {
            selectedCustomerAccountID = bankLogic.getCustomerAccountIdViaIndex(listOfAccounts.getSelectionModel().getSelectedIndex());
            loadScene("Info.fxml");
        } else {
            if (saving.isSelected()) {
                bankLogic.addSavingsAccount(selectedCustomerSSN);
            } else {
                bankLogic.addCreditAccount(selectedCustomerSSN);
            }
            updateInfo();
        }
        popup.close();
    }

    private void updateInfo() {
        ArrayList<String> info = (ArrayList<String>) bankLogic.getCustomer(selectedCustomerSSN);

        String[] parts = info.get(0).split(" ");
        changeFirstName.setText(parts[0]);
        changeLastName.setText(parts[1]);
        lblSsn.setText(info.get(1));
        info.remove(0);
        info.remove(0);
        accounts = FXCollections.observableArrayList(info);
        listOfAccounts.setItems(accounts);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnAccount.setVisible(false);
        saving.setSelected(true);
        saving.setToggleGroup(group);
        credit.setToggleGroup(group);

        updateInfo();

        try {
            loadPopup();
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
