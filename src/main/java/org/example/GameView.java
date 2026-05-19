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

    public void showTable(java.util.ArrayList<Player> allPlayers, Player currentPlayer, int cardsWell) {
        System.out.println("\n******** ESTADO DE LA MESA ********");

        // Recorremos la lista de todos los jugadores
        for (Player p : allPlayers) {
            // Solo mostramos las cartas de los rivales (los que no son el jugador actual)
            if (p != currentPlayer) {
                System.out.println("Cartas de " + p.getName() + ": " + p.getHand().size());
            }
        }

        System.out.println("Cartas en el Pozo: " + cardsWell);
        System.out.println("**********************************\n");
    }

    public void showHand(Player p) {
       // Mostramos la mano
        for (int i = 0; i < p.getHand().size(); i++) {
            System.out.println((i + 1) + " - " + p.getHand().get(i).toString());
        }
    }

    public void startRound(Player p){
        System.out.println("\n" + p.getName() + ", it's your turn.");
        System.out.println("This are your cards:");
        showHand(p);
    }

    public int[] askPlayerChoices(Player p) {

        System.out.println("Your cards (choose one or more. Example: 1 3):");
        showHand(p); // Mostramos la mano
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
