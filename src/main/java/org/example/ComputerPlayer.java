package org.example;
import java.util.Random;

public class ComputerPlayer extends Player {
    private Random random = new Random();

    public ComputerPlayer(String name){ super(name);}


    @Override
    public int[] decidePlay(int numplay) { // La máquina selecciona su jugada
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
                return new int[]{find}; // Juega la carta real
            }
        }
        // Si no la tiene o decide mentir por el 20%, juega una carta aleatoria de su mano
        int randomIndex = random.nextInt(hand.size());
        return new int[]{randomIndex};
    }


    @Override
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

}
