package client.p2_ui;

import Server.Queries;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AuthController implements Initializable {
    public GridPane loginPane;
    public GridPane signupPane;
    public TextField loginUsername;
    public PasswordField loginPassword;
    public TextField signupLastName;
    public PasswordField signupPassword;
    public DatePicker signupDOB;
    public TextField signupFirstName;
    public TextField signupNID;
    public ChoiceBox<String> signupType;
    public ChoiceBox<String> selectOp;
    public AnchorPane depositPane;
    public AnchorPane withdrawPane;
    public AnchorPane transferPane;
    public AnchorPane balancePane;
    public AnchorPane interestPayment;
    public TextField depositAmount;
    public TextField withdrawAmount;
    public TextField transferAmount;
    public TextField transferDestination;
    public Text balanceText;
    public GridPane activitiesPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signupType.setItems(FXCollections.observableArrayList("Employee", "Client"));
        selectOp.setItems(FXCollections.observableArrayList("Deposit", "Withdraw", "Transfer", "Interest Payment", "Check Balance"));

        selectOp.setOnAction(event -> {
            setPage(selectOp.getValue());
        });
    }

    public void setPage (String val){
        closeTabs();
        char task = val.toCharArray()[0];
        signupPane.setVisible(false);
        loginPane.setVisible(false);
        activitiesPane.setVisible(true);
        switch (task){
            case 'D':
                depositPane.setVisible(true);
                return;
            case 'W':
                withdrawPane.setVisible(true);
                return;
            case 'T':
                transferPane.setVisible(true);
                return;
            case 'I':
                interestPayment.setVisible(true);
                return;
            case 'C':
                balancePane.setVisible(true);
                return;
        }
    }

    public void closeTabs (){
        depositPane.setVisible(false);
        withdrawPane.setVisible(false);
        transferPane.setVisible(false);
        balancePane.setVisible(false);
        interestPayment.setVisible(false);
    }



    public void showSignup(MouseEvent mouseEvent) {
        signupPane.setVisible(true);
        loginPane.setVisible(false);
        clearLogin();
    }

    public void showLogin(MouseEvent mouseEvent) {
        signupPane.setVisible(false);
        loginPane.setVisible(true);
        clearSignup();
    }

    public void clearLogin (){
        loginUsername.clear();
        loginPassword.clear();

    }
    public void clearSignup (){
        signupDOB.setValue(null);
        signupFirstName.clear();
        signupLastName.clear();
        signupNID.clear();
        signupPassword.clear();
    }

    public void loginAction(ActionEvent actionEvent) throws SQLException {
        Queries.login(loginUsername.getText(), loginPassword.getText());
    }

    public void signupAction(ActionEvent actionEvent) throws SQLException {
        int date = Integer.parseInt(signupDOB.getValue().toString().split("-")[0]);

        if ( 2023 - date >= 13 ){
            Queries.signUp(signupFirstName.getText(), signupLastName.getText(), signupPassword.getText(), signupType.getValue(), signupNID.getText(), signupDOB.getValue().toString().replace("-", "/"));
            setPage("D");
        }
    }

    public void depositAction(MouseEvent mouseEvent) throws SQLException {
        if ( depositAmount.getText() != null || depositAmount.equals("")){
            Queries.deposit(Double.parseDouble(depositAmount.getText()));
        }
    }

    public void withdrawAction(MouseEvent mouseEvent) throws SQLException {
        Queries.withdraw(Double.parseDouble(withdrawAmount.getText()));
    }

    public void transferAction(MouseEvent mouseEvent) throws SQLException {
        Queries.transfer(transferDestination.getText(), Double.parseDouble(transferAmount.getText()));
    }

    public void balanceAction(MouseEvent mouseEvent) throws SQLException {
        balanceText.setText("Your balance : " + Queries.checkBalance() + "$");
    }

    public void interestAction(ActionEvent actionEvent) throws SQLException {
        Queries.payInterest();
    }

}