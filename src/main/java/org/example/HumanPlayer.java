package org.example;
import java.util.ArrayList;
import java.util.Random;

public class HumanPlayer extends Player {
    private GameView view;

    public HumanPlayer(String name, GameView view){
        super(name);
        this.view = view;
    }

    public String getName() {
        return name;
    }

    @Override
    public int[] decidePlay(int currentRank) {
        return view.askPlayerChoices(this);
    }

    @Override
    public boolean decideDistrust(int totalWell) {
        return view.askDistrust() == 'y';
    }

}
