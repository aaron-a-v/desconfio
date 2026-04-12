package org.example;
import java.util.ArrayList;
import java.util.Random;

public class Player {
    private String name;
    private ArrayList<Card> hand;
    private Random random = new Random();
    private boolean isComputer;

    public Player(String name, boolean b){
        this.name = name;
        this.isComputer = b; // Guardamos si es máquina o no
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Card> getHand(){ return hand;}

    public void receiveCard(Card cardr){
        hand.add(cardr);
    } // Añadimos cartas a nuestra mano

    public Card extractCard(int index){ // Tiramos una carta
        return hand.remove(index);
    }


    public int selectCardComputer(int numplay) { // La máquina selecciona su jugada
        // La máquina comprueba si tiene cartas del número que se está jugando
        int find = -1;
        for (int i = 0; i < hand.size(); i++) { // La máquina recorre su mano
            if (hand.get(i).getNum() == numplay) { // Si obtiene de su mano el número que se está jugando piensa
                find = i;
                break;
            }
        }
        if (find!=-1) {
            if (random.nextInt(10) < 8) { // Hay un 80% de probabilidad de que juegue diciendo la verdad
                return find; // Juega la carta real
            }
        }
        // Si no la tiene o decide mentir por el 20%, juega una carta aleatoria de su mano
        return random.nextInt(hand.size());
    }


    public boolean decideDistrust(int totalWell){ // La máquina decide si desconfiar
        // Si el pozo es de 5 cartas o más, la máquina tiene un 75% de probabilidades de desconfiar
        if (totalWell > 5) {
            return random.nextInt(100) < 75;
        } 
        // Si el pozo es de 5 o menos, mantiene una probabilidad del 50%
        else {
            return random.nextBoolean(); // nextBoolean() es equivalente al 50%
        }
    }

    public boolean isComputer() {
        return isComputer;
    }

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
