package org.example;
import java.util.Scanner;

public class GameController {
    private Table table;
    private GameView view;
    private Scanner scan;
    private Player p1, p2, p3;
    private int currentCardNumber; // El número que se está jugando (ej. todos juegan "Ases")

    public GameController() {
        this.view = new GameView();
        this.scan = new Scanner(System.in);
    }

    public void play(){
        view.showWelcome();
        String name1 = view.askPlayerName1(); // Guardamos el nombre del jugador 1
        String name2 = view.askPlayerName2(); // Guardamos el nombre del jugador 2

        p1 = new Player(name1, false); // Le decimos que el p1 es humano
        p2 = new Player(name2, false); // Le decimos que el p2 es humano
        p3 = new Player("Machine", true); // Le decimos que el p3 es la máquina

        this.table = new Table(); 
        view.showGreeting(name1); // Le damos la bienvenida a los jugadores
        view.showGreeting(name2);

        table.shuffleDeck(); // Barajamos la baraja
        table.distribute(p1, p2, p3); // Repartimos las cartas a cada jugador

        // LÓGICA DE JUEGO
        Player[] players = {p1, p2, p3}; // Guardamos los jugadores en un Array para rotarlos
        int turn = 0; // Iniciamos con el jugador 1
        boolean gameOver = false; // Establecemos una variable que nos indicará cuando acaba el bucle
        int currentCardNumber = -1; // -1 indica que la ronda aún no tiene un número asignado (el pozo está vacío)
        while (!gameOver) {
            Player currentPlayer = players[turn]; // Identificamos quién juega ahora
            Player nextPlayer = players[(turn + 1) % 3]; // Identificamos quién es el siguiente (el que puede desconfiar)

            // Mostramos el estado incial con la cantidad de cartas de los oponentes y la cantidad en el pozo
            view.showTable(players[(turn + 1) % 3].getHand().size(), players[(turn + 2) % 3].getHand().size(), table.getTotalWell());

            // LÓGICA DE INICIO DE RONDA
            if (table.getTotalWell() == 0) { // Si no hay cartas en el pozo, el jugador actual elige el número
                if (currentPlayer.isComputer()) { // Comprobamos si es el turno de la máquina
                    currentCardNumber = currentPlayer.getHand().get(0).getNum(); // La máquina elige el número de su primera carta
                } else { // Comprobamos si es el turno de un jugador
                    boolean validNumber = false; // Variable para validar la carta elegida
                    while (!validNumber) { // Permanecemos en el bucle hasta que se elija un número válido de la baraja
                        System.out.println(currentPlayer.getName() + ", choose the card for this round (1-7, 10-12):");
                        currentCardNumber = scan.nextInt(); // Elegimos el número de la carta con la que vamos a empezar
                        scan.nextLine();
                        // Validamos que el número exista (del 1 al 12, saltando 8 y 9)
                        if (currentCardNumber >= 1 && currentCardNumber <= 12 && currentCardNumber != 8 && currentCardNumber != 9) {
                            validNumber = true; // Salimos del bucle de elección
                        } else {
                            System.out.println("Invalid number!");
                        }
                    }
                }
                System.out.println(">>> ROUND THEME: " + currentCardNumber + "s <<<"); // Informamos el número de la ronda
            }

            // LÓGICA DE TIRAR CARTA
            int cardIndex; // Índice de la carta elegida de la mano
            if (currentPlayer.isComputer()) { // Si es el turno de la máquina
                cardIndex = currentPlayer.selectCardComputer(currentCardNumber); // Usa su lógica para decidir qué tirar
            } else { // Si es el turno de un jugador
                cardIndex = view.askPlayerChoice(currentPlayer); // Elige que carta de su mano va a tirar
            }

            Card playedCard = currentPlayer.extractCard(cardIndex); // Sacamos la carta de la mano del jugador
            table.addToWell(playedCard); // Ponemos la carta en el pozo (boca abajo)
            System.out.println(currentPlayer.getName() + " says: I played a " + currentCardNumber); // Declaración pública

            // LÓGICA DE DESCONFÍO
            boolean distrust; // Variable para saber si el siguiente jugador desconfía
            if (nextPlayer.isComputer()) { // Si el siguiente es la máquina...
                distrust = nextPlayer.decideDistrust(table.getTotalWell()); // Decide según el tamaño del pozo
            } else { // Si el siguiente es humano...
                System.out.println(nextPlayer.getName() + ", do you believe them? (y/n)");
                distrust = (view.askDistrust() == 'y'); // Lee 'y' para sí, cualquier otra cosa para no
            }

            // DECISIÓN DE DESCONFIAR
            if (distrust) { // Si el jugador siguiente decidió desconfiar
                // Miramos la carta real que se acaba de jugar en el pozo
                Card lastCard = table.peekLastCard(); 
                System.out.println("The card was actually: " + lastCard.toString());
                
                // Comparamos el número real con el número que se decía estar jugando
                if (lastCard.getNum() == currentCardNumber) { 
                    // El jugador decía la verdad: El que desconfió pierde y se lleva el pozo
                    System.out.println(">>> " + nextPlayer.getName() + " was WRONG! They take the whole well.");
                    table.deliverLoser(nextPlayer); // El método de Table entrega todas las cartas al perdedor
                } else {
                    // El jugador mentía: El jugador actual pierde y se lleva el pozo
                    System.out.println(">>> " + currentPlayer.getName() + " was CAUGHT LYING! They take the whole well.");
                    table.deliverLoser(currentPlayer); // El mentiroso recibe el castigo
                }
                
                // Tras un desconfío, el pozo queda vacío y se reinicia el tema de la ronda
                currentCardNumber = -1; 

                // Cambiamos el turno de escoger carta al siguiente jugador:
                turn = (turn + 1) % 3; 
                System.out.println("\n--- NEW ROUND! It's " + players[turn].getName() + "'s turn to choose a number ---");
                
                // Saltamos el resto del bucle para volver al inicio con el nuevo turno
                continue;
            }

            // COMPROBACIÓN DE VICTORIA Y CAMBIO DE TURNO
            // Si el jugador que acaba de tirar se ha quedado sin cartas, gana la partida
            if (currentPlayer.getHand().isEmpty()) { 
                System.out.println("¡¡¡ CONGRATULATIONS " + currentPlayer.getName() + " !!! You won the game!");
                gameOver = true; // Rompemos el bucle while
            } else {
                // Si nadie ha ganado, pasamos el turno al siguiente jugador (0 -> 1 -> 2 -> 0)
                turn = (turn + 1) % 3;
            }
        }
        System.out.println("Thanks for playing Distrust!");
    }
}
