package org.example;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    private String name;
    private ArrayList<Card> hand;
    private Random random = new Random();

    public Player(String name, boolean b){
        this.name = name;
        this.hand = new ArrayList<>(); // ArrayList de las cartas de la mano
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
        // La máquina solo desconfía si el pozo tiene más de 5 cartas y la probabilidad del 50% del boolean sale True
        return totalWell > 5 && random.nextBoolean();
    }

}
