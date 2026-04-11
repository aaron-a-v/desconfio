package org.example;
import java.util.Scanner;

public class GameView {
    private Scanner scan;

    public GameView(){
        this.scan = new Scanner(System.in);
    }

    public void showWelcome(){ // Mensaje de bienvenida conlas normas
        System.out.println("Welcome to Distrust");
        System.out.println("Rules:");
        System.out.println("1º. There will be 2 players and the computer");
        System.out.println("2º. You will receive your cards");
        System.out.println("3º. The first player will choose which card or cards they wish to play");
        System.out.println("4º. The next player must decide whether to distrust that card or play any cards they wish from their hand, telling the truth or lying");
        System.out.println("5º. The game will continue until one of the 3 runs out of cards");
    }

    public void showGreeting(String playerName) { // Mensaje de bienvenida a los PLayers
        System.out.println("Your welcome player " + playerName);
    }

    public String askPlayerName1(){ // Preguntar por sus nombres
        System.out.println("Write your name Player1: ");
        return scan.nextLine();
    }

    public String askPlayerName2(){ // Preguntar por sus nombres
        System.out.println("Write your name Player2: ");
        return scan.nextLine();
    }

    public void showTable(int cardsRival1, int cardsRival2, int cardsWell){ // Mostrar como está el juego
        System.out.println("********Table status*******");
        System.out.println("Number of opponent's cards 1: "+cardsRival1);
        System.out.println("Number of opponent's cards 2: "+cardsRival2);
        System.out.println("Number of cards of the Well: "+cardsWell);
    }

    public int[] askPlayerChoices(Player p) {
        System.out.println("\n" + p.getName() + ", it's your turn.");
        System.out.println("Your cards (choose one or more. Example: 1 3):");
        
        // Mostramos la mano
        for (int i = 0; i < p.getHand().size(); i++) {
            System.out.println((i + 1) + " - " + p.getHand().get(i).toString());
        }
        
        System.out.println("Write you election:");

        // Leemos la línea completa (ej: "1 2 4")
        String input = scan.nextLine();
        String[] parts = input.split(" "); // Dividimos por espacios
        int[] indices = new int[parts.length];

        for (int i = 0; i < parts.length; i++) {
            // Restamos 1 para convertir la elección del usuario a los índices
            indices[i] = Integer.parseInt(parts[i]) - 1;
        }
        return indices;
    }

    public char askDistrust(){ // El jugador elige si quiere o no desconfiar
        System.out.println("Do you want to distrust? y/n");
        String respuesta = scan.nextLine();
        return respuesta.charAt(0);
    }
}
