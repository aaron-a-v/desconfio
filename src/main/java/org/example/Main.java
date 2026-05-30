package org.example;

import io.github.cdimascio.dotenv.Dotenv;

// DISTRAST
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();
        DAO dao = new DAO(dotenv.get("DB_HOST"), dotenv.get("DB_USER"), dotenv.get("DB_PASSWORD"));

        GameController controller = new GameController(dao);
        controller.play();
    }
}
