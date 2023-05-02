package tms.tms;


import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController {

    @FXML
    private Button clearEmailText;
    @FXML
    private Button clearPasswordText;
    @FXML
    private TextField yearText;
    @FXML
    private ComboBox day;
    @FXML
    private ComboBox month;
    @FXML
    private Pane CreateAccountPane;
    @FXML
    private TextArea Surname;
    @FXML
    private TextArea FirstName;
    @FXML
    private JFXButton createAccountButton;
    @FXML
    private Label dateErrorLabel;
    @FXML
    private Label surnameErrorLabel;
    @FXML
    private Label nameErrorLabel;

    @FXML
    private Pane ForgottenPasswordPane;
    @FXML
    private Label forgottenEmailErrorLabel;
    @FXML
    private Hyperlink forgottenLogin;
    @FXML
    private TextArea forgottenEmailField;
    @FXML
    private JFXButton forgottenButton;

    @FXML
    private Pane RegisterPane;
    @FXML
    private Label registerPasswordErrorLabel;
    @FXML
    private Label registerEmailErrorLabel;
    @FXML
    private PasswordField registerPasswordText;
    @FXML
    private TextArea registerEmailText;
    @FXML
    private JFXButton registerNowButton;
    @FXML
    private Hyperlink LoginNow;

    @FXML
    private Pane LogInPane;
    @FXML
    private JFXButton ContinueEmail;
    @FXML
    private TextArea emailTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Hyperlink RegisterNow;
    @FXML
    private Label RegisterText;
    @FXML
    private Label emailErrorLabel;
    @FXML
    private Label passwordErrorLabel;
    @FXML
    private Hyperlink ForgotPassword;
    private int monthNumber;
    private static final String INVALID_EMAIL_ERROR = "Invalid email address";
    private static String oneTimePassword;
    private static final String INVALID_PASSWORD_ERROR = "Invalid password";
    private static final String WRONG_EMAIL_OR_PASSWORD_ERROR = "Wrong email or password";
    private static final String USER_ALREADY_EXISTS_ERROR = "User already exists";
    private static final String USER_DOES_NOT_EXIST_ERROR = "User does not exist";
    private static final String INVALID_NAME_ERROR = "Invalid input, it needs to be at least 3 characters long";
    private static final String INVALID_DATE_ERROR = "Invalid date, make sure to choose from every option";

    public void openDashboard(ActionEvent actionEvent) throws IOException {
        String email = emailTextField.getText().toLowerCase().trim();
        String password = passwordTextField.getText();

        if (!isValidEmail(email)) {
            emailErrorLabel.setText(INVALID_EMAIL_ERROR);
            passwordTextField.setText("");
            return;
        }
        emailErrorLabel.setText("");

        if (!DB.checkUser(email)) {
            passwordTextField.setText("");
            passwordErrorLabel.setText(WRONG_EMAIL_OR_PASSWORD_ERROR);
            return;
        }

        if (!password.equals(oneTimePassword) && !DB.checkUser(email, password)) {
            passwordTextField.setText("");
            passwordErrorLabel.setText(WRONG_EMAIL_OR_PASSWORD_ERROR);
            return;
        }

        launchDashboardWindow(email);
    }

    private void launchDashboardWindow(String email) throws IOException {
        DashboardController.email = email;

        Stage currentStage = (Stage) emailErrorLabel.getScene().getWindow();
        currentStage.close();

        try {
            new Dashboard().start(new Stage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void toRegister(ActionEvent actionEvent) throws IOException {
        emailErrorLabel.setText("");
        passwordErrorLabel.setText("");
        emailTextField.setText("");
        passwordTextField.setText("");
        LogInPane.setVisible(false);
        RegisterPane.setVisible(true);
    }

    public void toForgotten(ActionEvent actionEvent) {
        emailErrorLabel.setText("");
        passwordErrorLabel.setText("");
        emailTextField.setText("");
        passwordTextField.setText("");
        registerPasswordText.setText("");
        registerEmailText.setText("");
        LogInPane.setVisible(false);
        RegisterPane.setVisible(false);
        ForgottenPasswordPane.setVisible(true);
    }

    public void toLogin() {
        registerPasswordErrorLabel.setText("");
        registerEmailErrorLabel.setText("");
        forgottenEmailErrorLabel.setText("");
        registerEmailErrorLabel.setText("");
        dateErrorLabel.setText("");
        surnameErrorLabel.setText("");
        nameErrorLabel.setText("");
        registerEmailText.setText("");
        registerPasswordText.setText("");
        forgottenEmailField.setText("");
        yearText.setText("");
        RegisterPane.setVisible(false);
        ForgottenPasswordPane.setVisible(false);
        CreateAccountPane.setVisible(false);
        LogInPane.setVisible(true);
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean isValidName(String name) {
        return name != null && name.length() >= 3;
    }
    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    private boolean isComboBoxValueSet(ComboBox comboBox) {
        return comboBox.getValue() != null;
    }
    void changeTab() {
        Node[] logInNodes = {emailTextField, passwordTextField, ContinueEmail, ForgotPassword, RegisterNow};
        Node[] registerNodes = {registerEmailText, registerPasswordText, registerNowButton, LoginNow};
        Node[] forgottenNodes = {forgottenEmailField, forgottenButton, forgottenLogin};
        Node[] createNodes = {FirstName, Surname, day, month, yearText, createAccountButton};
        addTabTraversal(logInNodes);
        addTabTraversal(registerNodes);
        addTabTraversal(forgottenNodes);
        addTabTraversal(createNodes);
    }

    void addTabTraversal(Node[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            Node current = nodes[i];
            int finalI = i;
            current.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.TAB) {
                    if (event.isShiftDown()) { // Check if Shift key is pressed
                        event.consume(); // Consume the event to prevent default behavior
                        int prevIndex = (finalI - 1 + nodes.length) % nodes.length;
                        Node prevNode = nodes[prevIndex];
                        prevNode.requestFocus(); // Switch focus to previous node in array
                    } else {
                        event.consume(); // Consume the event to prevent default behavior
                        Node nextNode = nodes[(finalI + 1) % nodes.length];
                        nextNode.requestFocus(); // Switch focus to next node in array
                    }
                }
            });
        }
    }

    private void addEmailValidator(TextArea textField, Label errorLabel) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String text = textField.getText().trim();
            if (!isValidEmail(text) && text.length() == 0) errorLabel.setText("");
            else if (!isValidEmail(text) && text.length() > 0) errorLabel.setText(INVALID_EMAIL_ERROR);
            else {
                errorLabel.setText("");
            }
        });
    }

    private void addPasswordValidator(PasswordField textField, Label errorLabel) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String passwordText = textField.getText();
            if (!isValidPassword(passwordText) && passwordText.length() == 0) errorLabel.setText("");
            else if (!isValidPassword(passwordText) && passwordText.length() > 0)
                errorLabel.setText(INVALID_PASSWORD_ERROR);
            else {
                errorLabel.setText("");
            }
        });
    }
    private void addEnterKeyListener(Node node, Button button) {
        node.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
                button.fire();
            }
        });
    }

    public void loadFilters() {
        setClearButtonVisibility(clearEmailText, emailTextField);
        setClearButtonVisibility(clearPasswordText, passwordTextField);

        Parent parent = clearPasswordText.getParent();
        parent.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (isWithinBounds(event, clearPasswordText)) {
                clearPassword();
            }
        });
        parent.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (isWithinBounds(event, clearEmailText)) {
                clearEmail();
            }
        });
        addEmailValidator(emailTextField, emailErrorLabel);
        addEmailValidator(forgottenEmailField, forgottenEmailErrorLabel);
        addEmailValidator(registerEmailText, registerEmailErrorLabel);

        addPasswordValidator(registerPasswordText, registerPasswordErrorLabel);

        yearText.textProperty().addListener((observable, oldValue, newValue) -> {
            // remove all non-digit characters from the input
            String cleanInput = newValue.replaceAll("[^\\d]", "");
            // check if the input is more than 4 digits, and if so, truncate it to 4 digits
            if (cleanInput.length() > 4) {
                cleanInput = cleanInput.substring(0, 4);
            }
            // pad the input with underscores to make it 4 digits
            String paddedInput = String.format("%4s", cleanInput).replaceAll(" ", "_");
            // set the text of the yearText field to the padded input
            yearText.setText(paddedInput);
        });
        addEnterKeyListener(passwordTextField, ContinueEmail);
        addEnterKeyListener(emailTextField, ContinueEmail);
        addEnterKeyListener(registerEmailText, registerNowButton);
        addEnterKeyListener(registerPasswordText, registerNowButton);
        addEnterKeyListener(FirstName, createAccountButton);
        addEnterKeyListener(forgottenEmailField, forgottenButton);
    }

    public void toCreate(ActionEvent actionEvent) {
        String email = registerEmailText.getText().trim();
        String password = registerPasswordText.getText();
        if (!isValidEmail(email)) {
            registerEmailErrorLabel.setText(INVALID_EMAIL_ERROR);
            return;
        }

        if (!isValidPassword(password)) {
            registerPasswordErrorLabel.setText(INVALID_PASSWORD_ERROR);
            return;
        }

        if (DB.checkUser(email)) {
            registerEmailErrorLabel.setText(USER_ALREADY_EXISTS_ERROR);
        } else {
            registerPasswordErrorLabel.setText("");
            RegisterPane.setVisible(false);
            CreateAccountPane.setVisible(true);
        }
    }

    private String capitalizeFirstLetter(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        }
    }

    public void backToLogin(ActionEvent actionEvent) {
        String email = registerEmailText.getText().toLowerCase().trim();
        String password = registerPasswordText.getText();
        String fname = capitalizeFirstLetter(FirstName.getText().trim());
        String sname = capitalizeFirstLetter(Surname.getText().trim());
        Object value = month.getValue();

        if (!isValidName(fname)) {
            nameErrorLabel.setText(INVALID_NAME_ERROR);
            return;
        }
        nameErrorLabel.setText("");
        if (!isValidName(sname)) {
            surnameErrorLabel.setText(INVALID_NAME_ERROR);
            return;
        }
        surnameErrorLabel.setText("");
        if (!isComboBoxValueSet(day)) {
            dateErrorLabel.setText(INVALID_DATE_ERROR);
            return;
        }
        dateErrorLabel.setText("");
        if (!isComboBoxValueSet(month)) {
            dateErrorLabel.setText(INVALID_DATE_ERROR);
            return;
        }
        dateErrorLabel.setText("");
        /*if (!isComboBoxValueSet(year)) {
            dateErrorLabel.setText(INVALID_DATE_ERROR);
            return;
        }
        dateErrorLabel.setText("");*/

        if (value.equals("Jan")) {
            monthNumber = 1;
        } else if (value.equals("Feb")) {
            monthNumber = 2;
        } else if (value.equals("Mar")) {
            monthNumber = 3;
        } else if (value.equals("Apr")) {
            monthNumber = 4;
        } else if (value.equals("May")) {
            monthNumber = 5;
        } else if (value.equals("Jun")) {
            monthNumber = 6;
        } else if (value.equals("Jul")) {
            monthNumber = 7;
        } else if (value.equals("Aug")) {
            monthNumber = 8;
        } else if (value.equals("Sep")) {
            monthNumber = 9;
        } else if (value.equals("Oct")) {
            monthNumber = 10;
        } else if (value.equals("Nov")) {
            monthNumber = 11;
        } else if (value.equals("Dec")) {
            monthNumber = 12;
        } else {
            monthNumber = 1;
        }
        String yearTextStr = yearText.getText();
        System.out.println(yearTextStr);
        if (!yearTextStr.matches("\\d{4}")) {
            dateErrorLabel.setText("Invalid year format. Please enter a 4-digit year.");
            return;
        }
        Date birthdate = Date.valueOf(yearTextStr + "-" + monthNumber + "-" + day.getValue());
        LocalDate today = LocalDate.now();
        LocalDate birthLocalDate = birthdate.toLocalDate();
        int age = Period.between(birthLocalDate, today).getYears();
        if (age < 18 || age > 99) {
            dateErrorLabel.setText("You must be between 18 and 99 years old to register.");
            return;
        }
        User obj = new User(fname, sname, birthdate, email, password);

        obj.addMember(fname, sname, birthdate, email, password);
        toLogin();
    }

    public void setData() {
        emailTextField.setText("chedly.shabou1@gmail.com");
        passwordTextField.setText("PassPass");
        String yearFieldStyle = "-fx-background-color: linear-gradient(to bottom, #f9f9f9, #c9c9c9);" +
                "-fx-background-radius: 3;" +
                "-fx-border-color: #b0b0b0;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 3;" +
                "-fx-padding: 0.16667em 0.583em 0.16667em 0.583em; /* 2 7 2 7 */" +
                "-fx-background-insets: 0, 1;";
        yearText.setStyle(yearFieldStyle);
        yearText.setPromptText("YYYY");
        day.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31);
        month.getItems().addAll("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep",
                "Oct", "Nov", "Dec");
    }

    public void sendPassword(ActionEvent actionEvent) throws GeneralSecurityException, IOException, MessagingException {
        String email = forgottenEmailField.getText().trim();
        if (!isValidEmail(email)) {
            forgottenEmailErrorLabel.setText(INVALID_EMAIL_ERROR);
            return;
        }
        forgottenEmailErrorLabel.setText("");
        if (!DB.checkUser(email)) {
            forgottenEmailErrorLabel.setText(USER_DOES_NOT_EXIST_ERROR);
        } else {
            sendEmail();
        }
    }

    public void sendEmail() {
        try {
            String email = forgottenEmailField.getText().toLowerCase().trim();
            User obj = new User("", "", Date.valueOf("1990-09-09"), "", "");
            String password = obj.getMemberPassword(email);
            // Create a new email message
            GmailAPI sendEmail = new GmailAPI();
            oneTimePassword = PasswordGenerator.generatePassword();
            System.out.println("Generated password: " + oneTimePassword);
            String message = String.format("Dear %s,\n\n" +
                    "You have requested to recover your password. Here is your one time password in order to login:\n\n" +
                    "%s\n\n" +
                    "If you did not request this, please ignore this email.\n\n" +
                    "Best regards,\n" +
                    "TMS Team", email, oneTimePassword);
            sendEmail.main(email, message);
            System.out.println("Email sent successfully!");
            forgottenEmailErrorLabel.setStyle("-fx-text-fill: green;");
            forgottenEmailErrorLabel.setText("Email sent successfully.");
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
        } catch (GeneralSecurityException e) {
            System.out.println("Error sending email: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Error sending email: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void setClearButtonVisibility(Node clearButton, TextInputControl textInput) {
        clearButton.setVisible(false);
        textInput.focusedProperty().addListener((obs, oldVal, newVal) -> {
            clearButton.setVisible(newVal);
        });
    }

    private boolean isWithinBounds(MouseEvent event, Node node) {
        double mouseX = event.getSceneX();
        double mouseY = event.getSceneY();
        Bounds bounds = node.getBoundsInParent();
        return mouseX >= bounds.getMinX() && mouseX <= bounds.getMaxX() && mouseY >= bounds.getMinY() && mouseY <= bounds.getMaxY();
    }

    public void clearPassword() {
        passwordTextField.clear();
        passwordTextField.requestFocus();
    }

    public void clearEmail() {
        emailTextField.clear();
        emailTextField.requestFocus();
    }
}
