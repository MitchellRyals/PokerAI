package sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private static List<Card> humanHand = new ArrayList<Card>();
    private static List<Card> botHand = new ArrayList<Card>();

    public static void beginGame() {
        //THIS IS THE MAIN GAME FUNCTION
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

    private static void getHandValue(List<Card> hand) {
        Integer returnValue;
        String[] suitList = new String[5];
        int i = 0;

        for (Card c: hand) {
            suitList[i] += c.getSuit().toString().substring(0, 1);
            i++;
        }
        System.out.println(suitList);

        //return 1;
    }

    private static String compareHands(Integer humanHandValue, Integer botHandValue) {
        if (humanHandValue > botHandValue) {
            return "You win!";
        }
        else if (humanHandValue == botHandValue) {
            return "Draw!";
        }
        else return "You lost!";
    }
}
