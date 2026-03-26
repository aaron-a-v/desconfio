package org.example;

public class Card {
    private int num; // Número de la carta
    private String stick; // Palo de la carta

    public Card(int num, String stick) {
        this.num = num;

        if (stick.equals("coins") || stick.equals("cups") || stick.equals("swords") || stick.equals("sticks")) {
            this.stick = stick;
        }
    }

    public int getNum() {
        return num;
    }

    public String toString(){
        String nameCard = String.valueOf(num);
        if (num == 10) nameCard = "jack";
        if (num == 11) nameCard = "horse";
        if (num == 12) nameCard = "king";
        if (num == 1) nameCard = "as";
        return nameCard + "of" + stick;
    }
}