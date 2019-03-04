package sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private static List<Card> humanHand = new ArrayList<Card>();
    private static List<Card> botHand = new ArrayList<Card>();

    public static void beginGame() {
        List<Card> deck = new ArrayList<Card>();
        deck = Card.createDeck();
        Collections.shuffle(deck);

        for (int i = 0; i < 5; i++) {
            deal(deck);
        }
    }

    private static void deal(List<Card> deck) {
        humanHand.add(deck.get(0));
        deck.remove(0);
        Main.updateHumanHand(humanHand);
        botHand.add(deck.get(0));
        deck.remove(0);
        Main.updateBotHand(botHand);
    }

    private static int getHandValue(List<Card> hand) {
        return 1;
    }

    private static boolean compareHands() {
        return true;
    }
}
