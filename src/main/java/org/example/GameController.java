package org.example;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;

public class GameController {
    private Table table;
    private GameView view;
    private Scanner scan;
    private ArrayList<Player> playerList;
    private int currentCardNumber; // El número que se está jugando (ej. todos juegan "Ases")
    private java.util.HashMap<String, Integer> lieCounter; // Hashmap para contar las veces que pillaron a cada jugador

    public GameController() {
        this.view = new GameView();
        this.scan = new Scanner(System.in);
        playerList = new ArrayList<>();
        this.lieCounter = new HashMap<>();
    }

    public void play(){
        view.showWelcome();
        
        int numTotal = 0;
        while (numTotal < 3 || numTotal > 6) {
            System.out.println("¿How many players will there be in total? (3-6):"); // Cuántos jugadores habrá en total?
            numTotal = scan.nextInt();
            scan.nextLine();
        }

        System.out.println("¿How many of them will be human?"); // Cuántos de ellos serán humanos?
        int numHumans = scan.nextInt();
        scan.nextLine();


        // Añadir humanos
        for (int i = 1; i <= numHumans; i++) {
            System.out.println("Nombre del jugador " + i + ":");
            playerList.add(new HumanPlayer(scan.nextLine(), view));
        }

        // Añadir máquinas hasta completar el total
        for (int i = playerList.size() + 1; i <= numTotal; i++) {
            playerList.add(new ComputerPlayer("Machine " + (i - numHumans)));
        }

        for (Player p : playerList) {
            lieCounter.put(p.getName(), 0);
        }

        this.table = new Table();

        table.shuffleDeck(); // Barajamos la baraja
        table.distribute(playerList); // Repartimos las cartas a cada jugador

        // LÓGICA DE JUEGO
        int turn = 0; // Iniciamos con el jugador 1
        boolean gameOver = false; // Establecemos una variable que nos indicará cuando acaba el bucle
        int currentCardNumber = -1; // -1 indica que la ronda aún no tiene un número asignado (el pozo está vacío)
        
        while (!gameOver) {
            Player currentPlayer = playerList.get(turn); // Identificamos quién juega ahora
            // El siguiente jugador es siempre el que está a la derecha (índice + 1)
            Player nextPlayer = playerList.get((turn + 1) % playerList.size());
            
            currentPlayer.checkAndRemoveQuartets(); // Comprobamos que no tenga 4 cartas iguales

            // Mostramos el estado de la mesa (pasamos la lista para ver a todos los rivales)
            view.showTable(playerList, currentPlayer, table.getTotalWell());

            // LÓGICA DE INICIO DE RONDA
            if (table.getTotalWell() == 0) { // Si no hay cartas en el pozo, el jugador actual elige el número
                boolean validNumber = false;
                while (!validNumber) {
                    try {
                        if (currentPlayer instanceof ComputerPlayer) { // Comprobamos si es el turno de la máquina
                            currentCardNumber = currentPlayer.getHand().get(0).getNum(); // La máquina elige el número de su primera carta
                        } else { // Comprobamos si es el turno de un jugador
                            
                            // Mostramos las cartas al jugador humano para que sepa qué tiene antes de decidir
                            view.startRound(currentPlayer);
                            System.out.println("\n" + "Choose the rank for this round (1-7, 10-12):");
                            currentCardNumber = scan.nextInt(); // Leemos el número que el jugador humano elige para la ronda
                            scan.nextLine();
                            
                            // Lanzamos nuestra excepción personalizada si el número es inválido
                            if (currentCardNumber == 8 || currentCardNumber == 9 || currentCardNumber < 1 || currentCardNumber > 12) {
                                throw new InvalidRankException("The rank " + currentCardNumber + " is not valid in the Spanish deck (1-7, 10-12).");
                            }
                        }
                        validNumber = true; // Si llegamos aquí, el número es válido y salimos del bucle
                    } catch (InvalidRankException e) { // Capturamos la excepción personalizada y mostramos su mensaje
                        System.out.println("Error: " + e.getMessage());
                    } catch (Exception e) { // Capturamos cualquier otra excepción para evitar que el programa se caiga
                        System.out.println("Invalid input. Please enter a number.");
                        scan.nextLine(); // Limpiar el buffer
                    }
                }
                System.out.println(">>> ROUND THEME: " + currentCardNumber + " <<<"); // Informamos el número de la ronda
            }

            // LÓGICA DE TIRAR CARTA
            int[] indices = currentPlayer.decidePlay(currentCardNumber); // El jugador decide qué cartas jugar (recibe un array de índices)

            // Extraemos las cartas y las ponemos en el pozo
            java.util.ArrayList<Card> cardsPlayed = currentPlayer.extractMultipleCards(indices); // Extraemos las cartas de la mano del jugador según los índices elegidos
            for (Card c : cardsPlayed) { // Añadimos cada carta al pozo
                table.addToWell(c);
            }
            System.out.println(currentPlayer.getName() + " played " + cardsPlayed.size() + " cards as " + currentCardNumber); // Informamos por consola cuántas cartas se han jugado y qué número representaban

            // LÓGICA DE DESCONFÍO
            System.out.println("\n"+"**************************************"+"\n");
            System.out.println(nextPlayer.getName()+", it's your turn");
            boolean distrust = nextPlayer.decideDistrust(table.getTotalWell()); // El siguiente jugador decide si desconfía (recibe el total de cartas en el pozo para tomar su decisión)
    
            if (distrust) { // Si decide desconfiar, comprobamos si el último jugador ha mentido o no
                System.out.println("!!! " + nextPlayer.getName() + " decided to DISTRUST!");
                Card lastCard = table.peekLastCard();
                System.out.println("The last card played was: " + lastCard.toString());
    
                // El 1 actúa como comodín
                if (lastCard.getNum() == currentCardNumber || lastCard.getNum() == 1) { // Si el número de la última carta coincide con el número de la ronda, o es un comodín, el siguiente jugador se ha equivocado al desconfiar
                    System.out.println(">>> " + nextPlayer.getName() + " was WRONG! They take the whole well.");
                    table.deliverLoser(nextPlayer);
                } else {
                    System.out.println(">>> " + currentPlayer.getName() + " was CAUGHT LYING! They take the whole well."); // Si no coincide, el jugador actual ha mentido y ha sido pillado
                    lieCounter.put(currentPlayer.getName(), lieCounter.get(currentPlayer.getName()) + 1);
                    table.deliverLoser(currentPlayer);
                }
    
                currentCardNumber = -1; // Reseteamos el número de la ronda porque el pozo se ha vaciado tras el desconfío
                // Tras un desconfío, el turno pasa al siguiente del que desconfió para iniciar nueva ronda
                int indexNext = (playerList.indexOf(nextPlayer) + 1) % playerList.size(); // Calculamos el índice del siguiente jugador al que desconfiaba
                turn = indexNext; // Establecemos el turno al siguiente jugador para que empiece la nueva ronda
                continue; // Saltamos el resto del bucle para iniciar la siguiente ronda directamente
            }

            // COMPROBACIÓN DE VICTORIA
            if (currentPlayer.getHand().isEmpty()) {
                System.out.println("\n¡¡¡ CONGRATULATIONS " + currentPlayer.getName() + " !!! You won the game!");
                gameOver = true;
            } else {
                turn = (turn + 1) % playerList.size();
            }
        }
    
        // Estadísticas finales
        System.out.println("\n--- FINAL STATISTICS (Lies caught) ---");
        lieCounter.forEach((name, count) -> System.out.println(name + ": " + count + " times."));
    }
}
