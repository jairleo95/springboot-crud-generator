/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.utiles;

/**
 *
 * @author Jairleo95
 */
import java.io.File;
import java.io.FileWriter;
import com.alphateam.properties.Global;

public class FileBuilder {

    public static Boolean writeFolderAndFile(String directory, String file, String content) {
        String ubicacion = Global.DEFAULT_PROJECT_LOCATION;
        Boolean x = false;
        try {
            //Crear un objeto File se encarga de crear o abrir acceso a un archivo que se especifica en su constructor
            File directorio = new File(ubicacion + directory);
            directorio.mkdirs();
            File archivo = new File(ubicacion + directory + file);
            //Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
            FileWriter escribir = new FileWriter(archivo, false);
            //Escribimos en el archivo con el metodo write 
            escribir.write(content);
            x = true;
            //Cerramos la conexion
            escribir.close();
        } //Si existe un problema al escribir entra aqui
        catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            x = false;
        }
        return x;
    }

}
