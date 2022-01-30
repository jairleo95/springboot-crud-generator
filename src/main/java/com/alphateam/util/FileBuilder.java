/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphateam.util;

/**
 *
 * @author Jairleo95
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.alphateam.app.base.ApplicationClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;

public class FileBuilder {
    private final Logger log = LogManager.getLogger(getClass().getName());

    public static String readAllBytes(String filePath){
        String content = "";
        try {
            content = new String (Files.readAllBytes(Paths.get(filePath)));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    @Async
    public Boolean writeFolderAndFile(String directory, String file, String content) {
        log.info("writeFolderAndFile thread");

        String location = ApplicationClass.instance().getConfig().getOutputLocation();

        //String location = Global.DEFAULT_PROJECT_LOCATION;
        Boolean x = false;
        try {
            //Crear un objeto File se encarga de crear o abrir acceso a un archivo que se especifica en su constructor
            File directorio = new File(location + directory);
            directorio.mkdirs();
            File archivo = new File(location + directory + file);
            //Crear objeto FileWriter que sera el que nos ayude a escribir sobre archivo
            FileWriter escribir = new FileWriter(archivo, false);
            //Escribimos en el archivo con el metodo write 
            escribir.write(content);
            x = true;
            //Cerramos la conexion
            escribir.close();
            log.debug("file generated:"+location + directory + file);

        } //Si existe un problema al escribir entra aqui
        catch (Exception e) {
            log.error("Error " + e.getMessage());
            x = false;
        }
        return x;
    }

}
