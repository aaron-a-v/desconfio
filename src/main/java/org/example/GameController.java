package org.example;

public class GameController {
    private Table table;
    private GameView view;
    private Player p1, p2, p3;
    private int currentCardNumber; // El número que se está jugando (ej. todos juegan "Ases")

    public GameController() {
        this.view = new GameView();
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
                if (currentPlayer.isComputer()) { // Si es la máquina
                    currentCardNumber = currentPlayer.getHand().get(0).getNum(); // Elige el número de su primera carta
                } else { // Si es humano
                    boolean validNumber = false; // Variable para validar la carta elegida
                    while (!validNumber) { // Permanecemos en el bucle hasta que se elija un número válido de la baraja
                        System.out.println(currentPlayer.getName() + ", choose the card for this round (1-7, 10-12):");
                        currentCardNumber = scan.nextInt(); // Leemos el número
                        scan.nextLine(); // Limpiamos el buffer del Scanner
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
        }
    }
    }
}
