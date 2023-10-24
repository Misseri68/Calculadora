package com.example.calculadora;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;


public class HelloController {
    @FXML
    private TextField pantalla;
    private String resultadoGuardado = "";
    boolean parentesisAbierto = false;

    private char[] operadores = {'+', '-', '/', 'X'};
    private ScriptEngine engine;
    private boolean haSaltadoError = false;
    public HelloController() {
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("js");
        System.out.println("El engine se ha creado correctamente.");
    }


    public void borrarPantalla() {
        pantalla.setText("");
        haSaltadoError = false;
    }

    public void addParentesis(){
        if (haSaltadoError) borrarPantalla();
        if(!parentesisAbierto){
            pantalla.appendText("(");
            parentesisAbierto = true;
        }
        else {
            pantalla.appendText(")");
            parentesisAbierto = false;
        }
    }

    public void addOperador(MouseEvent mouseEvent){
        if (haSaltadoError) borrarPantalla();
        Button botonPulsado = (Button) mouseEvent.getSource();
        boolean operadorAnterior = false;
        if(!pantalla.getText().isEmpty()){
            for (char c : operadores){
                if (c == (pantalla.getText().charAt(pantalla.getText().length() - 1))) {
                    operadorAnterior = true;
                }
            }
            if (operadorAnterior){
                borrarUltimo();
                pantalla.appendText(botonPulsado.getText());
            }
            else {
                pantalla.appendText(botonPulsado.getText());
            }
        }
    }

    public void addNumero(MouseEvent mouseEvent) {
        if (haSaltadoError) borrarPantalla();
       Button botonPulsado = (Button) mouseEvent.getSource();
       pantalla.appendText(botonPulsado.getText());
    }

    public void borrarUltimo() {
        if (haSaltadoError) borrarPantalla();
        if(!pantalla.getText().isEmpty()){
            pantalla.setText(pantalla.getText(0, (pantalla.getLength()-1)));
        }
    }

    public void ultimaRespuesta() {
        if (haSaltadoError) borrarPantalla();
        pantalla.appendText(resultadoGuardado);
    }

    public void calculaResultado() {
        if (haSaltadoError) borrarPantalla();
        String funcion =  pantalla.getText().replaceAll("X", "*");

        try{
            if (pantalla.getText().contains("/0")){
                muestraError("ERR0R");
                throw new ArithmeticException();
            }
            else if (pantalla.getText().isEmpty()){
                throw new IllegalArgumentException();
            }
            Expression expresion = new ExpressionBuilder(funcion).build();
            ValidationResult validacion = expresion.validate();
            if (!validacion.isValid()) {
                muestraError("ERROR");
            } else {
                double result = expresion.evaluate();
                resultadoGuardado = String.valueOf(result);
                pantalla.setText(String.valueOf(result));
            }
        }catch (ArithmeticException e){
            muestraError("N0 SE PUEDE DIVIDIR ENTRE 0.");
        }
        catch (NumberFormatException e){
            muestraError("INGRESE UNA EXPRESI0N VALIDA.");
        }
        catch (IllegalArgumentException e){
            muestraError("INTR0DUZCA NUMER0S PRIMER0");

        }
    }


    public void muestraError(String mensajeError){
        pantalla.setText(mensajeError);
        haSaltadoError = true;
    }
}