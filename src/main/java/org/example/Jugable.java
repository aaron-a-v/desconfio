package org.example;

public interface Jugable {

    // Define que acciones obligatorias debe ser capaz de realizar cualquier entidad que participe en la partida

    // Obliga a decidir qué cartas poner en la mesa
    int[] decidePlay(int currentRank);

    // Obliga a decidir si desconfía o no del jugador anterior
    boolean decideDistrust(int totalWell);

}
