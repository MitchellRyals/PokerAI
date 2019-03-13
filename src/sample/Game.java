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

        //deals 5 cards to both the bot and player
        dealHuman(deck);
        dealBot(deck);

    }

    public static void dealHuman(List<Card> deck) {
        while (humanHand.size() < 5) {
            humanHand.add(deck.get(0));
            deck.remove(0);
            Card.setDeck(deck);
            Main.updateHumanHand(humanHand);
        }
    }

    public static void dealBot(List<Card> deck) {
        while (botHand.size() < 5) {
            botHand.add(deck.get(0));
            deck.remove(0);
            Main.updateBotHand(botHand);
        }
    }

    public static void discard(List<Integer> toBeDiscarded) {
        //this sort and reverse is so that I can remove elements without giving an out of bounds error due to
        //list resizing.
        Collections.sort(toBeDiscarded);
        Collections.reverse(toBeDiscarded);

        for (Integer i: toBeDiscarded) {
            humanHand.remove((int)i);
        }

        List<Card> deck = Card.getDeck();
        dealHuman(deck);
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
