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
        System.out.println("**********************************");
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
        int[] indices = null;
        boolean selectionValid = false;

        // El bucle no termina hasta que la entrada del usuario sea 100% correcta
        while (!selectionValid) {
            System.out.println("Your cards (choose one or more. Example: 1 3):");
            showHand(p); // Mostramos la mano con las opciones numeradas (1, 2, 3...)
            System.out.println("Write your election:");

            // Eliminamos los espacios vacíos accidentales al principio y al final de la línea
            String input = scan.nextLine().trim();

            // Si el usuario pulsa Enter directamente sin escribir nada
            if (input.isEmpty()) {
                System.out.println("\n[ERROR] You must select at least one card. Try again.\n");
                continue;
            }

            // Usamos "\\s+" para que java lo procese aunque se añadan más espacios intermedios (ej: "1   3")
            String[] parts = input.split("\\s+");
            indices = new int[parts.length];
            boolean syntaxError = false;

            for (int i = 0; i < parts.length; i++) {
                try {
                    // Restamos 1 para convertir la opción al índice de Java (0, 1...)
                    int choice = Integer.parseInt(parts[i]) - 1;

                    // Validamos si el índice existe en la mano del jugador
                    if (choice < 0 || choice >= p.getHand().size()) {
                        System.out.println("\n[ERROR] The option '" + parts[i] + "' is not a valid card. You only have options from 1 to " + p.getHand().size() + ".\n");
                        syntaxError = true;
                        break; // Rompe el bucle for para volver al principio
                    }

                    indices[i] = choice;

                } catch (NumberFormatException e) {
                    // Si el usuario introduce texto, letras o caracteres extraños
                    System.out.println("\n[ERROR] Invalid format. Please write only numbers separated by spaces (Example: 1 3).\n");
                    syntaxError = true;
                    break; // Rompe el mazo de conversión para reiniciar la lectura
                }
            }

            // Si el proceso del for terminó sin activar ninguna alerta de error, la selección es válida
            if (!syntaxError) {
                selectionValid = true;
            }
        }

        return indices;
    }

    public char askDistrust(){ // El jugador elige si quiere o no desconfiar
        System.out.println("Do you want to distrust? y/n");
        String respuesta = scan.nextLine();
        return respuesta.charAt(0);
    }
}
