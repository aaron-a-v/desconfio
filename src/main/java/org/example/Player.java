package org.example;
import java.util.ArrayList;
import java.util.Random;

public class Player {
    private String name;
    private ArrayList<Card> hand;

    public Player(String name){
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public String getName() { return name; }
    public ArrayList<Card> getHand(){ return hand;}
    public void receiveCard(Card cardr){ hand.add(cardr); } // Añadimos cartas a nuestra mano

    // Métodos abstractos que cada tipo de jugador implementará a su manera
    public abstract int[] decidePlay(int currentRank);
    public abstract boolean decideDistrust(int totalWell){}

    public ArrayList<Card> extractMultipleCards(int[] indices) { // Método que acepte el array de índices
        ArrayList<Card> chosenCards = new ArrayList<>();
        // Ordenamos de mayor a menor para no desordenar el ArrayList al borrar
        java.util.Arrays.sort(indices);
        for (int i = indices.length - 1; i >= 0; i--) {
            chosenCards.add(hand.remove(indices[i])); // Eliminamos de la mano y lo guardamos en la lista de cartas jugadas
        }
        return chosenCards;
    }

    public void checkAndRemoveQuartets() {
        // Creamos un array de 13 posiciones que serva de contador: la posición 1 contará cuántos "Ases" hay, etc.
        int[] counts = new int[13]; 
        
        // Recorremos cada carta que el jugador tiene en su mano
        for (Card c : hand) {
            // Obtenemos el número de la carta y sumamos 1 en su posición correspondiente del contador
            counts[c.getNum()]++;
        }

        // Ahora revisamos nuestro contador del 2 al 12 (saltamos el 1 porque es comodín)
        for (int num = 2; num <= 12; num++) {
            // Si el contador para un número específico ha llegado a 4...
            if (counts[num] == 4) {
                // Informamos por consola que se ha encontrado un cuarteto
                System.out.println("\n" + name + " has the four cards of " + num + " and removes them from the game!");
                
                // Guardamos el número en una variable final para poder usarla
                int numToRemove = num;
                
                // Borramos de la lista 'hand' todas las cartas cuyo número coincida con el cuarteto
                // removeIf recorre la lista y elimina automáticamente las que cumplan la condición
                hand.removeIf(card -> card.getNum() == numToRemove);
            }
        }
    }

}
