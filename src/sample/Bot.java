package sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bot {
    private static int botHandValue;

    public static void setHandValue(int value) {
        botHandValue = value;
    }

    /********************************************
     * CARD VALUES
     * 2-14 = 2, 3, 4, ... , Jack (11), Queen (12), King (13), Ace (14)
     * 16-28 = pair (return 14 + card value)
     * 30-42 = 2x pairs (return 28 + card value)
     * 44-56 = 3 of a kind (return 42 + card value)
     * 58-70 = straight (return 56 + card value)
     * 72-84 = flush (return 70 + card value)
     * 86-98 = full house (return 84 + card value)
     * 100-112 = 4 of a kind (return 98 + card value)
     * 114-126 = straight flush (return 112 + card value)
     * 141 = royal flush
     **********************************************/

    public static List<Integer> getDiscardChoice() {
        List<Integer> toBeDiscarded = new ArrayList<Integer>();
        //List<Card> hand = Game.getBotHand();
        List<Card> hand = Card.generateDebugHand();
        List<Card> deck = Card.getDeck();

        String[] suitList = new String[5];
        int i = 0;

        for (Card c: hand) {
            suitList[i] = c.getSuit().substring(0, 1);
            i++;
        }

        //we're going to have the bot go for high probability wins here by going for flushes if he has 4 matching suits
        int resultOfFlushCheck = isFlush(suitList);
        if (resultOfFlushCheck > -1)
            toBeDiscarded.add(resultOfFlushCheck);

        return toBeDiscarded;
    }

    public static int getBetAmount() {
        double betMultiplier = 0;
        int currentCash = Main.getBotCash();
        //Possible values have a max of 141
        //so let's get this out of the way first. If he gets a royal flush, you can bet he's going all in.
        if (botHandValue == 141) {
            betMultiplier = 1.0;
        }
        else
            betMultiplier = calculateChanceOfWinning();

        int botBetAmount = (int) Math.ceil((currentCash / 10) * betMultiplier) * 10;
        Main.changeBotActionMessage("Opponent bets $" + botBetAmount);
        return botBetAmount;
    }

    private static double calculateChanceOfWinning() {
        List<Card> hand = Game.getBotHand();
        List<Card> deck = Card.getDeck();
        int playerBet = Main.getPlayerBet();

        for (int i = 0; i < hand.size(); i++) {

        }

        return 0.5;
    }

    //A heavily modified variant of the function to check for flushes in the Game.java file. The difference is
    //that it returns the index of the card to discard if the bot is close to getting a flush. Otherwise, -1
    private static int isFlush(String[] suitList) {
        int forwardSearch = 0;
        int backwardSearch = suitList.length - 1;

        // Two recursion calls, using the same idea as quick sort. They start from both ends and
        // recursively call themselves towards the other side. If they end in the same index value, it gets discarded
        // as it means the bot is 1 off from a flush.
        forwardSearch = findFlushForward(suitList, forwardSearch);
        backwardSearch = findFlushBackward(suitList, backwardSearch);
        //todo: add start and end cases, have him discard for a straight possibly. Otherwise have him discard things not in a value range
        if (forwardSearch == backwardSearch)
            return forwardSearch;
        else
            return -1;
    }

    //recursively calls itself to find if one of the suits is missing
    private static int findFlushForward(String[] suitList, int i) {
        if (!suitList[i].equals(suitList[i+1]) && i < suitList.length - 1)
            return i + 1;
        else
            return findFlushForward(suitList, i + 1);
    }

    //recursively calls itself to find if one of the suits is missing.
    private static int findFlushBackward(String[] suitList, int i) {
        if (!suitList[i].equals(suitList[i-1]) && i >= 1)
            return i - 1;
        else
            return findFlushBackward(suitList, i - 1);
    }
}
