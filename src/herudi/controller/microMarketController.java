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
import herudi.interfaces.interMicro;
import herudi.model.Customer;
import herudi.model.MicroMarket;
import herudi.model.Product;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.springframework.context.ApplicationContext;

/**
 * FXML Controller class
 *
 * @author Herudi
 */
public class microMarketController implements Initializable {
    @FXML
    private AnchorPane paneTabel;
    @FXML
    private TableView<MicroMarket> tableData;
    @FXML
    private TableColumn colAction;
    @FXML
    private TableColumn<MicroMarket, String> colZip;
    @FXML
    private TableColumn<MicroMarket, String> colRadius;
    @FXML
    private TableColumn<MicroMarket, String> colAreaLength;
    @FXML
    private TableColumn<MicroMarket, String> colAreaWidth;
    @FXML
    private TextField txtZipCode;
    @FXML
    private Button btnCek;
    @FXML
    private VBox vboxCrud;
    @FXML
    private TextField txtRadius;
    @FXML
    private TextField txtAreaLength;
    @FXML
    private TextField txtAreaWidth;
    @FXML
    private Button btnSave;
    Integer status;
    interMicro crud;
    private ObservableList<MicroMarket> listData;
    @FXML
    private ImageView imgLoad;
    @FXML
    private ProgressBar bar;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            ApplicationContext ctx = config.getInstance().getApplicationContext();
            crud = ctx.getBean(interMicro.class);
            listData = FXCollections.observableArrayList();
            status = 0;
            config2.setModelColumn(colAreaLength, "areaLength");
            config2.setModelColumn(colAreaWidth, "areaWidth");
            config2.setModelColumn(colRadius, "radius");
            config2.setModelColumn(colZip, "zipCode");
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
        });
        // TODO
    }  
    
    private void clear(){
        txtRadius.clear();
        txtAreaLength.clear();
        txtAreaWidth.clear();
        txtZipCode.clear();
        vboxCrud.setDisable(true);
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

    @FXML
    private void aksiKlikTableData(MouseEvent event) {
        if (status==1) {
            try {
                MicroMarket klik = tableData.getSelectionModel().getSelectedItem();
                txtAreaLength.setText(String.valueOf(klik.getAreaLength()));
                txtAreaWidth.setText(klik.getAreaWidth().toString());
                txtRadius.setText(klik.getRadius().toString());
                txtZipCode.setText(klik.getZipCode());
            } catch (Exception e) {
            }
        }
    }
    
    @FXML
    private void aksiCek(ActionEvent event) {
        if (txtZipCode.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.WARNING, "Zip Code Not Empty");
            txtZipCode.requestFocus();
        }else{
            int a = crud.chekZip(txtZipCode.getText());
            if (a==1) {
                config2.dialog(Alert.AlertType.ERROR, "Error, Zip Code Is Being Used");
                txtZipCode.clear();
                txtZipCode.requestFocus();
            }else{
                config2.dialog(Alert.AlertType.INFORMATION, "Success, Zip Code Is Available");
                vboxCrud.setDisable(false);
                txtRadius.requestFocus();
            }
        }
    }

    @FXML
    private void aksiSave(ActionEvent event) {
        if (txtZipCode.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "Zip Code Not Empty");
            txtZipCode.requestFocus();
        }else if (txtRadius.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "Radius Not Empty");
            txtRadius.requestFocus();
        }else if (txtAreaLength.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "Area Length Code Not Empty");
            txtAreaLength.requestFocus();
        }else if (txtAreaWidth.getText().isEmpty()) {
            config2.dialog(Alert.AlertType.ERROR, "Area Width Cost Not Empty");
            txtAreaWidth.requestFocus();
        }else{
            MicroMarket m = new MicroMarket();
            m.setAreaLength(Double.valueOf(txtAreaLength.getText()));
            m.setAreaWidth(Double.valueOf(txtAreaWidth.getText()));
            m.setRadius(Double.valueOf(txtRadius.getText()));
            m.setZipCode(txtZipCode.getText());
            crud.saveOrUpdate(m);
            clear();
            selectData();
            config2.dialog(Alert.AlertType.INFORMATION, "Success Save Data");
        }
    }

    @FXML
    private void keyRadius(KeyEvent event) {
        char[] data = txtRadius.getText().toCharArray();
        boolean valid = true;
        for (char c : data) {
            if (!Character.isDigit(c)) {
                valid = false;
                break;
            }
        }
        if (!valid) {
            config2.dialog(Alert.AlertType.ERROR, "Please, Fill With Number");
            txtRadius.clear();
            txtRadius.requestFocus();
        }
    }

    @FXML
    private void keyAreaLength(KeyEvent event) {
        char[] data = txtAreaLength.getText().toCharArray();
        boolean valid = true;
        for (char c : data) {
            if (!Character.isDigit(c)) {
                valid = false;
                break;
            }
        }
        if (!valid) {
            config2.dialog(Alert.AlertType.ERROR, "Please, Fill With Number");
            txtAreaLength.clear();
            txtAreaLength.requestFocus();
        }
    }

    @FXML
    private void keyAreaWidth(KeyEvent event) {
        char[] data = txtAreaWidth.getText().toCharArray();
        boolean valid = true;
        for (char c : data) {
            if (!Character.isDigit(c)) {
                valid = false;
                break;
            }
        }
        if (!valid) {
            config2.dialog(Alert.AlertType.ERROR, "Please, Fill With Number");
            txtAreaWidth.clear();
            txtAreaWidth.requestFocus();
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
                    MicroMarket p = new MicroMarket();
                    p.setZipCode(txtZipCode.getText());
                    crud.delete(p);
                    selectData();
                    clear();
                }else{
                    selectData();
                    clear();
                }
                status = 0;
            });
            cellButtonEdit.setOnAction((ActionEvent event) -> {
                status = 1;
                int row = getTableRow().getIndex();
                tableData.getSelectionModel().select(row);
                aksiKlikTableData(null);
                vboxCrud.setDisable(false);
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
