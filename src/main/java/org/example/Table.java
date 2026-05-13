package org.example;
import java.util.ArrayList;
import java.util.Collections;

public class Table {
    private ArrayList<Card> deck;
    private ArrayList<Card> well;

    public Table(){
        deck = new ArrayList<>(); // Establecemos el ArrayList de la baraja
        well = new ArrayList<>(); // Establecemos el ArrayList del pozo
        String[] sticks = {"coins", "cups", "swords", "sticks"}; // Establecemos los nombres de los palos
        for (String s : sticks) { // Ejecutamos el bucle para los cuatro palos
            for (int i = 1; i <= 12; i++){ // Ejecutamos un segundo bucle para las diez cartas de cada palo, teniendo así 40
                if (i == 8 || i == 9) {continue;} // Saltamos el 8 y el 9
                deck.add(new Card(i, s)); // Añadimos a cada número un palo y lo metemos en el ArrayList de la baraja
            }
        }
    }

    public void shuffleDeck() {
        Collections.shuffle(deck);
    } // Barajamos las cartas

    public void distribute(ArrayList<Player> players) {
    int playerIndex = 0;
    while (!deck.isEmpty()) {
        // Sacamos la última carta del mazo
        Card c = deck.remove(deck.size() - 1);
        // Se la damos al jugador que toque en la ronda
        players.get(playerIndex).receiveCard(c);
        
        // Pasamos al siguiente jugador, y si llegamos al final, volvemos al primero
        playerIndex = (playerIndex + 1) % players.size();
    }
}

    public void addToWell(Card cardr){ well.add(cardr);} // Se añaden cartas al pozo al final de la lista

    public void deliverLoser(Player loser){
        for (Card cardr : well){
            loser.receiveCard(cardr); // Entregamos todas las cartas de la mesa al perdedor
        }
        well.clear(); // Al entregar las cartas vaciamos del pozo
    }

    public int getTotalWell(){ // Obtenemos el total de cartas que hay en el pozo
        return well.size();
    }

    public Card peekLastCard(){
        if (well.isEmpty()) {return null;} // Levantar la última carta para Desconfiar
        return well.get(well.size()-1);
    }
}
