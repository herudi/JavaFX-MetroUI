/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herudi.controller;

import herudi.animations.FadeInUpTransition;
import herudi.config.config;
import herudi.config.config2;
import herudi.interfaces.interCustomer;
import herudi.model.Customer;
import herudi.model.DiscountCode;
import herudi.model.MicroMarket;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.springframework.context.ApplicationContext;

/**
 * FXML Controller class
 *
 * @author Herudi
 */
public class customerController implements Initializable {
    @FXML
    private TableView<Customer> tableData;
    @FXML
    private TableColumn colAction;
    @FXML
    private TableColumn<Customer, String> colCustomerId;
    @FXML
    private TableColumn<Customer, String> colDiscountCode;
    @FXML
    private TableColumn<Customer, String> colZip;
    @FXML
    private TableColumn<Customer, String> colName;
    @FXML
    private TableColumn<Customer, String> colAdderss1;
    @FXML
    private TableColumn<Customer, String> colAddress2;
    @FXML
    private TableColumn<Customer, String> colCity;
    @FXML
    private TableColumn<Customer, String> colState;
    @FXML
    private TableColumn<Customer, String> colPhone;
    @FXML
    private TableColumn<Customer, String> colFax;
    @FXML
    private TableColumn<Customer, String> colEmail;
    @FXML
    private TableColumn<Customer, String> colCreditLimit;
    @FXML
    private Button btnNew;
    @FXML
    private AnchorPane paneTabel;
    interCustomer crud;
    @FXML
    private AnchorPane paneCrud;
    @FXML
    private TextField txtId;
    @FXML
    private ComboBox cbDiscount,cbZip;
    @FXML
    private TextField txtName;
    @FXML
    private TextArea txtAddress1;
    @FXML
    private TextArea txtAddress2;
    @FXML
    private TextField txtCity;
    @FXML
    private TextField txtState;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtFax;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtCredit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnBack;
    Integer status;
    @FXML
    private ImageView imgLoad;
    @FXML
    private ProgressBar bar;
    private ObservableList<Customer> listData;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            ApplicationContext ctx = config.getInstance().getApplicationContext();
            crud = ctx.getBean(interCustomer.class);
            listData = FXCollections.observableArrayList();
            status = 0;
            config2.setModelColumn(colAdderss1, "addressline1");
            config2.setModelColumn(colAddress2, "addressline2");
            config2.setModelColumn(colCity, "city");
            config2.setModelColumn(colCreditLimit, "creditLimit");
            config2.setModelColumn(colCustomerId, "customerId");
            config2.setModelColumn(colDiscountCode, "discountCode");
            config2.setModelColumn(colEmail, "email");
            config2.setModelColumn(colFax, "fax");
            config2.setModelColumn(colName, "name");
            config2.setModelColumn(colPhone, "phone");
            config2.setModelColumn(colState, "state");
            config2.setModelColumn(colZip, "zip");
            colAction.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Object, Boolean>,ObservableValue<Boolean>>() {
                @Override
                public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Object, Boolean> p) {
                    return new SimpleBooleanProperty(p.getValue() != null);
                }
            });
            colAction.setCellFactory(new Callback<TableColumn<Object, Boolean>, TableCell<Object, Boolean>>() {
                @Override
                public TableCell<Object, Boolean> call(TableColumn<Object, Boolean> p) {
                    return new ButtonCell(tableData);
                }
            });
            selectWithService();
            displayDiscountCode();
            displayZip();
        });
// TODO
    }   
    
    private void clear(){
        txtAddress1.clear();
        txtAddress2.clear();
        txtCity.clear();
        txtCredit.clear();
        cbDiscount.setValue("");
        txtEmail.clear();
        txtFax.clear();
        txtName.clear();
        txtPhone.clear();
        txtState.clear();
        cbZip.setValue("");
    }
    
    private void displayDiscountCode(){
        Service<ObservableList<DiscountCode>> service = new Service<ObservableList<DiscountCode>>() {
            @Override
            protected Task<ObservableList<DiscountCode>> createTask() {
                return new Task<ObservableList<DiscountCode>>() {           
                    @Override
                    protected ObservableList<DiscountCode> call() throws Exception {
                        ObservableList<DiscountCode> listTask = FXCollections.observableArrayList();
                        if(listTask == null){
                            listTask = FXCollections.observableArrayList(crud.selectCode());
                        }else {
                            listTask.clear();
                            listTask.addAll(crud.selectCode());
                        }
                        cbDiscount.setItems(listTask);
                        return listTask;
                    }
                };
            }
        };
        service.start();
    }
    
    private void displayZip(){
        Service<ObservableList<MicroMarket>> service = new Service<ObservableList<MicroMarket>>() {
            @Override
            protected Task<ObservableList<MicroMarket>> createTask() {
                return new Task<ObservableList<MicroMarket>>() {           
                    @Override
                    protected ObservableList<MicroMarket> call() throws Exception {
                        ObservableList<MicroMarket> listTask = FXCollections.observableArrayList();
                        if(listTask == null){
                            listTask = FXCollections.observableArrayList(crud.selectZip());
                        }else {
                            listTask.clear();
                            listTask.addAll(crud.selectZip());
                        }
                        cbZip.setItems(listTask);
                        return listTask;
                    }
                };
            }
        };
        service.start();
    }
    
    private void auto(){
        if (crud.select().isEmpty()) {
            txtId.setText("1");
        }else{
            txtId.setText(String.valueOf(crud.auto()));
        }
    }
    
    private void selectData(){
        if(listData == null){
            listData = FXCollections.observableArrayList(crud.select());
        }else {
            listData.clear();
            listData.addAll(crud.select());
        }
        tableData.setItems(listData);
    }
    
    private void selectWithService(){
        Service<Integer> service = new Service<Integer>() {
            @Override
            protected Task<Integer> createTask() {
                selectData();
                return new Task<Integer>() {           
                    @Override
                    protected Integer call() throws Exception {
                        Integer max = crud.select().size();
                        if (max > 35) {
                            max = 30;
                        }
                        updateProgress(0, max);
                        for (int k = 0; k < max; k++) {
                            Thread.sleep(40);
                            updateProgress(k+1, max);
                        }
                        return max;
                    }
                };
            }
        };
        service.start();
        bar.progressProperty().bind(service.progressProperty());
        service.setOnRunning((WorkerStateEvent event) -> {
            imgLoad.setVisible(true);
        });
        service.setOnSucceeded((WorkerStateEvent event) -> {
            imgLoad.setVisible(false);
            new FadeInUpTransition(paneTabel).play();
        });
    }
    
    
    
    @FXML
    private void keyState(KeyEvent e){
        if (txtState.getText().length() > 2) {
            config2.dialog(Alert.AlertType.INFORMATION, "State Must 2 Char");
            txtState.clear();
        }
    }

    @FXML
    private void aksiKlikTableData(MouseEvent event) {
        if (status==1) {
            try {
                Customer klik = tableData.getSelectionModel().getSelectedItem();
                txtAddress1.setText(klik.getAddressline1());
                txtAddress2.setText(klik.getAddressline2());
                txtCity.setText(klik.getCity());
                txtCredit.setText(klik.getCreditLimit().toString());
                txtEmail.setText(klik.getEmail());
                txtFax.setText(klik.getFax());
                txtId.setText(klik.getCustomerId().toString());
                txtName.setText(klik.getName());
                txtPhone.setText(klik.getPhone());
                txtState.setText(klik.getState());
                cbDiscount.setValue(klik.getDiscountCode());
                cbZip.setValue(klik.getZip());
            } catch (Exception e) {
            }
        }
    }

    @FXML
    private void aksiNew(ActionEvent event) {
        paneTabel.setOpacity(0);
        new FadeInUpTransition(paneCrud).play();
        Platform.runLater(() -> {
            clear();
            auto();
        });
    }
    
    @FXML
    private void aksiSave(ActionEvent event) {
        if (txtId.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "ID Not Empty");
            txtId.requestFocus();
        }else if (cbDiscount.getValue().equals("")) {
            config2.dialog(Alert.AlertType.ERROR, "Discount Not Empty");
            cbDiscount.requestFocus();
        }else if (cbZip.getValue().equals("")) {
            config2.dialog(Alert.AlertType.ERROR, "Zip Not Empty");
            cbZip.requestFocus();
        }else if (txtName.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "Name Not Empty");
            txtName.requestFocus();
        }else if (txtAddress1.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "Address1 Not Empty");
            txtAddress1.requestFocus();
        }else if (txtAddress2.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "Address2 Not Empty");
            txtAddress2.requestFocus();
        }else if (txtCity.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "City Not Empty");
            txtCity.requestFocus();
        }else if (txtState.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "State Not Empty");
            txtState.requestFocus();
        }else if (txtPhone.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "Phone Not Empty");
            txtPhone.requestFocus();
        }else if (txtFax.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "Fax Not Empty");
            txtFax.requestFocus();
        }else if (txtEmail.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "Email Not Empty");
            txtEmail.requestFocus();
        }else if (txtCredit.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "Credit Not Empty");
            txtCredit.requestFocus();
        }else{
            Customer a = new Customer();
            a.setAddressline1(txtAddress1.getText());
            a.setAddressline2(txtAddress2.getText());
            a.setCity(txtCity.getText());
            a.setCreditLimit(Integer.valueOf(txtCredit.getText()));
            a.setCustomerId(Integer.valueOf(txtId.getText()));
            a.setDiscountCode(cbDiscount.getValue().toString());
            a.setEmail(txtEmail.getText());
            a.setFax(txtFax.getText());
            a.setName(txtName.getText());
            a.setPhone(txtPhone.getText());
            a.setState(txtState.getText());
            a.setZip(cbZip.getValue().toString());
            crud.saveOrUpdate(a);
            clear();
            selectData();
            auto();
            config2.dialog(Alert.AlertType.INFORMATION, "Success Save Data. . .");
        }
    }

    @FXML
    private void aksiBack(ActionEvent event) {
        paneCrud.setOpacity(0);
        new FadeInUpTransition(paneTabel).play();
    }
    
    private class ButtonCell extends TableCell<Object, Boolean> {
        final Hyperlink cellButtonDelete = new Hyperlink("Delete");
        final Hyperlink cellButtonEdit = new Hyperlink("Edit");
        final HBox hb = new HBox(cellButtonDelete,cellButtonEdit);
        ButtonCell(final TableView tblView){
            hb.setSpacing(4);
            cellButtonDelete.setOnAction((ActionEvent t) -> {
                status = 1;
                int row = getTableRow().getIndex();
                tableData.getSelectionModel().select(row);
                aksiKlikTableData(null);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure Delete Data "+txtName.getText()+" ?");
                alert.initStyle(StageStyle.UTILITY);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Customer p = new Customer();
                    p.setCustomerId(Integer.valueOf(txtId.getText()));
                    crud.delete(p);
                    clear();
                    selectData();
                }else{
                    clear();
                    selectData();
                    auto();
                }
                status = 0;
            });
            cellButtonEdit.setOnAction((ActionEvent event) -> {
                status = 1;
                int row = getTableRow().getIndex();
                tableData.getSelectionModel().select(row);
                aksiKlikTableData(null);
                paneTabel.setOpacity(0);
                new FadeInUpTransition(paneCrud).play();
                status = 0;
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(hb);
            }else{
                setGraphic(null);
            }
        }
    }
}
