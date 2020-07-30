package com.alphateam;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CRUDGeneratorApplication implements CommandLineRunner {
     public static void main(String[] args) {
          SpringApplication.run(CRUDGeneratorApplication.class, args);
     }

     @Override
     public void run(String... args) throws Exception {
          //new Process().build();
     }
}