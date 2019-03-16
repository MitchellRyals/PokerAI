package sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

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
        //todo: add bot turn here
        getHandValue(humanHand);
    }

    private static Integer getHandValue(List<Card> hand) {
        Integer returnValue;
        String[] suitList = new String[5];
        Integer[] valueList = new Integer[5];
        int i = 0;

        for (Card c: hand) {
            suitList[i] = c.getSuit().substring(0, 1);
            valueList[i] = c.getRank();
            i++;
        }

        Arrays.sort(suitList);
        Arrays.sort(valueList);

        if (isSameSuit(suitList)) {
            if (isRoyalFlush(valueList)) {
                return 20;
            }
            else if (isFlush(valueList)) {
                return 19;
            }
        }
        

        return 1;
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

    //Down here is a series of functions that will check for flush, straight, pair, etc.
    private static boolean isSameSuit(String[] suitList) {
        //compare the last and first suit of the already sorted array
        return (suitList[suitList.length - 1] == suitList[0]);
    }

    private static boolean isRoyalFlush(Integer[] valueList) {
        //a decrementing counter starting at 14 because that is the value of an ace
        int cardCounter = 10;

        //checks for ace>king>queen>jack>10
        for (int i = 0; i < valueList.length; i++) {
            if (valueList[i] != cardCounter) {
                return false;
            }
            cardCounter++;
        }

        return true;
    }

    private static boolean isFlush(Integer[] valueList) {
        int smallest = valueList[0];

        //checks for 5 cards in a row from a sorted array
        for (int i = 0; i < valueList.length; i++) {
            if (valueList[i] != smallest) {
                return false;
            }
            smallest++;
        }

        return true;
    }
}
