/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herudi.controller;

import herudi.animations.FadeInUpTransition;
import herudi.config.config;
import herudi.config.config2;
import herudi.interfaces.interProduct;
import herudi.model.Customer;
import herudi.model.Manufacturer;
import herudi.model.MicroMarket;
import herudi.model.Product;
import herudi.model.ProductCode;
import java.math.BigDecimal;
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
import javafx.scene.control.CheckBox;
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
public class productController implements Initializable {
    @FXML
    private AnchorPane paneCrud;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtPurchaseCode;
    @FXML
    private TextField txtMarkup;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnBack;
    @FXML
    private ComboBox cbManufacturer;
    @FXML
    private ComboBox cbProductCode;
    @FXML
    private TextField txtQuantityCode;
    @FXML
    private CheckBox avaTrue;
    @FXML
    private CheckBox avaFalse;
    @FXML
    private TextArea txtDescription;
    @FXML
    private AnchorPane paneTabel;
    @FXML
    private TableView<Product> tableData;
    @FXML
    private TableColumn colAction;
    @FXML
    private TableColumn<Product, String> colProductId;
    @FXML
    private TableColumn<Product, String> colManufacturerIid;
    @FXML
    private TableColumn<Product, String> colProductCode;
    @FXML
    private TableColumn<Product, String> colPurchaseCost;
    @FXML
    private TableColumn<Product, String> colQuantityOnHand;
    @FXML
    private TableColumn<Product, String> colMarkup;
    @FXML
    private TableColumn<Product, String> colAvailable;
    @FXML
    private TableColumn<Product, String> colDescription;
    @FXML
    private Button btnNew;
    Integer status;
    interProduct crud;
    String available;
    @FXML
    private ProgressBar bar;
    @FXML
    private ImageView imgLoad;
    private ObservableList<Product> listData;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            ApplicationContext ctx = config.getInstance().getApplicationContext();
            crud = ctx.getBean(interProduct.class);
            listData = FXCollections.observableArrayList();
            status = 0;
            available = "";
            config2.setModelColumn(colAvailable, "available");
            config2.setModelColumn(colDescription, "description");
            config2.setModelColumn(colManufacturerIid, "manufacturerId");
            config2.setModelColumn(colMarkup, "markup");
            config2.setModelColumn(colProductCode, "productCode");
            config2.setModelColumn(colProductId, "productId");
            config2.setModelColumn(colPurchaseCost, "formatCost");
            config2.setModelColumn(colQuantityOnHand, "quantityOnHand");
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
            displayManufacturer();
            displayProductCode();
        });
        // TODO
    }    
    
    private void selectData(){
        if(listData == null){
            listData = FXCollections.observableArrayList(crud.selectData());
        }else {
            listData.clear();
            listData.addAll(crud.selectData());
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
                        Integer max = crud.selectData().size();
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
    
    private void displayManufacturer(){
        Service<ObservableList<Manufacturer>> service = new Service<ObservableList<Manufacturer>>() {
            @Override
            protected Task<ObservableList<Manufacturer>> createTask() {
                return new Task<ObservableList<Manufacturer>>() {           
                    @Override
                    protected ObservableList<Manufacturer> call() throws Exception {
                        ObservableList<Manufacturer> listTask = FXCollections.observableArrayList();
                        if(listTask == null){
                            listTask = FXCollections.observableArrayList(crud.selectManufacturerID());
                        }else {
                            listTask.clear();
                            listTask.addAll(crud.selectManufacturerID());
                        }
                        cbManufacturer.setItems(listTask);
                        return listTask;
                    }
                };
            }
        };
        service.start();
    }
    
    private void displayProductCode(){
        Service<ObservableList<ProductCode>> service = new Service<ObservableList<ProductCode>>() {
            @Override
            protected Task<ObservableList<ProductCode>> createTask() {
                return new Task<ObservableList<ProductCode>>() {           
                    @Override
                    protected ObservableList<ProductCode> call() throws Exception {
                        ObservableList<ProductCode> listTask = FXCollections.observableArrayList();
                        if(listTask == null){
                            listTask = FXCollections.observableArrayList(crud.selectProductCode());
                        }else {
                            listTask.clear();
                            listTask.addAll(crud.selectProductCode());
                        }
                        cbProductCode.setItems(listTask);
                        return listTask;
                    }
                };
            }
        };
        service.start();
    }
    
    private void auto(){
        if (crud.selectData().isEmpty()) {
            txtId.setText("1");
        }else{
            txtId.setText(String.valueOf(crud.auto()));
        }
    }
    
    private void clear(){
        txtDescription.clear();
        txtMarkup.clear();
        txtPurchaseCode.clear();
        txtQuantityCode.clear();
        cbManufacturer.setValue("");
        cbProductCode.setValue("");
        available = "";
        avaFalse.setSelected(false);
        avaTrue.setSelected(false);
    }
    
    

    @FXML
    private void aksiSave(ActionEvent event) {
        if (txtId.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "ID Not Empty");
            txtId.requestFocus();
        }else if (cbManufacturer.getValue().equals("")) {
            config2.dialog(Alert.AlertType.ERROR, "Manufacturer ID Not Empty");
            cbManufacturer.requestFocus();
        }else if (cbProductCode.getValue().equals("")) {
            config2.dialog(Alert.AlertType.ERROR, "Product Code Not Empty");
            cbProductCode.requestFocus();
        }else if (txtPurchaseCode.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "Purchase Cost Not Empty");
            txtPurchaseCode.requestFocus();
        }else if (txtQuantityCode.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "Quantity On Hand Not Empty");
            txtQuantityCode.requestFocus();
        }else if (txtMarkup.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "Markup Not Empty");
            txtMarkup.requestFocus();
        }else if (available.isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "Available Not Empty");
        }else if (txtDescription.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "Description Not Empty");
            txtDescription.requestFocus();
        }else{
            Product p = new Product();
            p.setAvailable(available);
            p.setDescription(txtDescription.getText());
            p.setManufacturerId(Integer.valueOf(cbManufacturer.getValue().toString()));
            p.setMarkup(BigDecimal.valueOf(Double.valueOf(txtMarkup.getText())));
            p.setProductCode(cbProductCode.getValue().toString());
            p.setProductId(Integer.valueOf(txtId.getText()));
            p.setPurchaseCost(BigDecimal.valueOf(Double.valueOf(txtPurchaseCode.getText())));
            p.setQuantityOnHand(Integer.valueOf(txtQuantityCode.getText()));
            crud.saveOrUpdate(p);
            clear();
            auto();
            config2.dialog(Alert.AlertType.INFORMATION, "Success Save Data..");
        }
        
    }

    @FXML
    private void aksiBack(ActionEvent event) {
        paneCrud.setOpacity(0);
        new FadeInUpTransition(paneTabel).play();
        selectData();
    }

    @FXML
    private void aksiKlikTableData(MouseEvent event) {
        if (status==1) {
            try {
                Product klik = tableData.getSelectionModel().getSelectedItem();
                txtDescription.setText(klik.getDescription());
                txtId.setText(String.valueOf(klik.getProductId()));
                txtMarkup.setText(klik.getMarkup().toString());
                txtPurchaseCode.setText(klik.getPurchaseCost().toString());
                txtQuantityCode.setText(klik.getQuantityOnHand().toString());
                cbProductCode.setValue(klik.getProductCode());
                cbManufacturer.setValue(klik.getManufacturerId());
                available = klik.getAvailable();
                if (available.equals("TRUE")) {
                    avaTrue.setSelected(true);
                    avaFalse.setSelected(false);
                }else{
                    avaFalse.setSelected(true);
                    avaTrue.setSelected(false);
                }
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
    private void aksiTrue(ActionEvent event) {
        available = "TRUE";
        avaFalse.setSelected(false);
    }

    @FXML
    private void aksiFalse(ActionEvent event) {
        available = "FALSE";
        avaTrue.setSelected(false);
    }

    @FXML
    private void aksiQuantity(KeyEvent event) {
        char[] data = txtQuantityCode.getText().toCharArray();
        boolean valid = true;
        for (char c : data) {
            if (!Character.isDigit(c)) {
                valid = false;
                break;
            }
        }
        if (!valid) {
            config2.dialog(Alert.AlertType.ERROR, "Please, Fill With Number");
            txtQuantityCode.clear();
            txtQuantityCode.requestFocus();
        }
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
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure Delete Data ?");
                alert.initStyle(StageStyle.UTILITY);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Product p = new Product();
                    p.setProductId(Integer.valueOf(txtId.getText()));
                    crud.delete(p);
                    selectData();
                }else{
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
