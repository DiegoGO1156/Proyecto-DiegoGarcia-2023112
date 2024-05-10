/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegogarcia.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.diegogarcia.dao.Conexion;
import org.diegogarcia.dto.CargoDTO;
import org.diegogarcia.model.Cargos;
import org.diegogarcia.system.Main;
import org.diegogarcia.utils.SuperKinalAlert;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class FormCargoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private static Connection conexion;
    private static PreparedStatement statement;
    private static ResultSet resultSet;
    
    private Main stage;
    private int op;

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    @FXML
    Button button_CancelarCargo, button_Acept;
    
    @FXML
    TextField tf_CargId, tf_CargNom;
    
    @FXML
    TextArea ta_cargDes;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(CargoDTO.getCargoDTO().getCargos() != null){
        cargarDatos(CargoDTO.getCargoDTO().getCargos());
        }
    }    
    
    public void cargarDatos(Cargos cargo){
        tf_CargId.setText(Integer.toString(cargo.getCargoId()));
        tf_CargNom.setText(cargo.getNombreCargo());
        ta_cargDes.setText(cargo.getDescripcionCargo());
    }
    
    
    public void agregarCargo(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarCargo(?, ?);";
            statement = conexion.prepareStatement(sql);
            
            statement.setString(1, tf_CargNom.getText());
            statement.setString(2, ta_cargDes.getText());
            statement.execute();
            
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(conexion != null){
                    conexion.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void editarCargo(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarCargo(?, ? ,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tf_CargId.getText()));
            statement.setString(2, tf_CargNom.getText());
            statement.setString(3, ta_cargDes.getText());
            statement.execute();
            
            
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(conexion != null){
                    conexion.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    

    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == button_CancelarCargo){
            stage.menuCargosView();
        }else if(event.getSource() == button_Acept){
            if(op == 1){
                
                if(!tf_CargNom.getText().equals("") && !ta_cargDes.getText().equals("")){
                    agregarCargo();
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(600);
                    stage.menuCargosView();
                }else {
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                        if(tf_CargNom.getText().equals("")) {
                            tf_CargNom.requestFocus();
                        }else if(ta_cargDes.getText().equals("")){
                            ta_cargDes.requestFocus();
                        }
                    }
                }else if(op == 2){
                
                if(!tf_CargNom.getText().equals("") && !ta_cargDes.getText().equals("")){  

                    if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(800).get() == ButtonType.OK){
                        editarCargo();
                        SuperKinalAlert.getInstance().mostrarAlertaInfo(500);
                        CargoDTO.getCargoDTO().setCargos(null);
                        stage.menuCargosView();
                    }else{
                        stage.menuCargosView();
                    }
                 }else{
                    SuperKinalAlert.getInstance().mostrarAlertaInfo(400);
                     if(tf_CargNom.getText().equals("")) {
                        tf_CargNom.requestFocus();
                     } else if(ta_cargDes.getText().equals("")){
                        ta_cargDes.requestFocus();
                    }
                }
            }
        }
    }
    public void setOp(int op) {
        this.op = op;
    }  
    
}
