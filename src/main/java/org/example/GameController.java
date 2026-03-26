package org.example;

public class GameController {
    private Table table;
    private GameView view;
    private Player p1;
    private Player p2;
    private Player p3;

    public GameController() {
        this.view = new GameView();
    }

    public void play(){
        view.showWelcome();
        String name1 = view.askPlayerName1();
        String name2 = view.askPlayerName2();
        p1 = new Player(name1, false);
        p2 = new Player(name2, false);
        p3 = new Player("Machine", true);
        this.table = new Table();
        view.showGreeting(name1);
        view.showGreeting(name2);
        table.shuffleDeck();
        table.distribute(p1, p2, p3);
        view.showTable(p2.getHand().size(),p3.getHand().size(), table.getTotalWell());
    }
}
