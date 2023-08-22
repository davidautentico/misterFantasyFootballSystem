package com.drosa.misterfantasysystem.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.drosa.misterfantasysystem"})
public class Application {

  /**
   * Main entry point.
   *
   * @param args the args
   */
  public static void main(final String[] args) {

    SpringApplication.run(Application.class, args);
  }

}
