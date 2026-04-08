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
        }
    }
    }
}
