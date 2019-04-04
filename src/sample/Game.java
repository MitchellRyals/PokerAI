package sample;

import java.util.*;

public class Game {
    private static List<Card> humanHand = new ArrayList<Card>();
    private static List<Card> botHand = new ArrayList<Card>();

    public static void beginGame() {
        List<Card> deck = Card.createDeck();
        Collections.shuffle(deck);

        //deals 5 cards to both the bot and player
        dealHuman(deck);
        dealBot(deck);

        Bot.setHandValue(getHandValue(botHand));
    }

    public static void dealHuman(List<Card> deck) {
        while (humanHand.size() < 5) {
            humanHand.add(deck.get(0));
            deck.remove(0);
            Card.setDeck(deck);
        }

        //this comparator is used to sort enums using a property. Essentially sorts my player hand
        Comparator comparator1 = new Comparator<Card>() {
            public int compare(Card e1, Card e2) {
                return e1.getRank() - e2.getRank();
            }
        };

        Collections.sort(humanHand, comparator1);
        Main.updateHumanHand(humanHand);
    }

    public static void dealBot(List<Card> deck) {
        while (botHand.size() < 5) {
            botHand.add(deck.get(0));
            deck.remove(0);
        }

        //this comparator is used to sort enums using a property. Essentially sorts my bot hand
        Comparator comparator1 = new Comparator<Card>() {
            public int compare(Card e1, Card e2) {
                return e1.getRank() - e2.getRank();
            }
        };

        Collections.sort(botHand, comparator1);
        Main.updateBotHand(botHand);
    }

    public static void discard(List<Integer> toBeDiscarded, boolean isPlayer) {
        //this sort and reverse is so that I can remove elements without giving an out of bounds error due to
        //list resizing.
        Collections.sort(toBeDiscarded);
        Collections.reverse(toBeDiscarded);

        if (isPlayer) {
            for (int i : toBeDiscarded) {
                humanHand.remove(i);
            }
        }
        else {
            for (int i : toBeDiscarded) {
                botHand.remove(i);
            }
        }

        List<Card> deck = Card.getDeck();
        if (isPlayer) {
            dealHuman(deck);
        }
        else
            dealBot(deck);
    }

    public static void postDiscardRound() {
        int botBetAmount = Bot.getBetAmount();
        List<Integer> botDiscardList = Bot.getDiscardChoice();
        discard(botDiscardList, false);

        String botDiscard = "";
        for (Integer i: botDiscardList)
            botDiscard += i + " ";

        Main.changeBotActionMessage("Bot discarded: " + botDiscard);
        Main.changeCenterMessage("Your opponent bet $" + botBetAmount + "\nChoose to call, fold, \nor choose a bet amount and press raise.");
        //finalizeRound(); //todo: replace this line with a fold/bet/call round
    }

    public static void finalizeRound() {
        int playerCardValue = getHandValue(humanHand);
        int botCardValue = getHandValue(botHand);

        String winOrLose = compareHands(playerCardValue, botCardValue);
        Main.changeCenterMessage(winOrLose, playerCardValue, botCardValue);
        Main.enableButton(Main.getNextRoundButton());
    }

    public static boolean checkGameOver(int playerCash, int botCash) {
        String message;

        if (playerCash <= 0) {
            message = "You ran out of money.\nGame over.";
            Main.changeCenterMessage(message);
            Main.enableButton(Main.getNewGameButton());
            return true;
        }
        else if (botCash <= 0) {
            message = "The AI ran out of money.\nYou win.";
            Main.changeCenterMessage(message);
            Main.enableButton(Main.getNewGameButton());
            return true;
        }

        return false;
    }

    private static int getHandValue(List<Card> hand) {
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

        //checks for a straight or royal flush before anything else is done
        if (isFlush(suitList)) {
            if (isRoyalFlush(valueList)) {
                //the highest possible value
                return 141;
            }
            else if (isStraight(valueList)) {
                //return 14 lower than a royal flush plus the value of the highest card value
                return 126 + valueList[valueList.length -1];
            }

            return 70 + valueList[valueList.length - 1];
        }

        //I am skipping the 0 and 1 spot of this array for simplicity's sake even though it may not be the most efficient.
        //this array will hold the number of each card in order to avoid redundant checking for pairs
        int[] numEachCard = new int[15];

        for (i = 0; i < valueList.length; i++) {
            int value = valueList[i];
            numEachCard[value]++;
        }

        //checks for full house and/or pairs. Returns highest index'd card if it can't find any.
        return checkForPairs(numEachCard);
    }

    private static String compareHands(int humanHandValue, int botHandValue) {
        //I also decided to do the betting setting/getting in this function
        int humanMoney = Main.getPlayerCash();
        int humanBet = Main.getPlayerBet();
        int botMoney = Main.getBotCash();
        int botBet = Main.getBotBet();
        int betAmount = humanBet + botBet;

        int newHumanMoney = humanMoney - humanBet;
        int newBotMoney = botMoney - botBet;

        if (humanHandValue > botHandValue) {
            Main.setPlayerCash(newHumanMoney + betAmount);
            Main.setBotCash(newBotMoney);
            return "You win!";
        }
        else if (humanHandValue == botHandValue) {
            return "Draw!";
        }
        else {
            Main.setPlayerCash(newHumanMoney);
            Main.setBotCash(newBotMoney + betAmount);
            return "You lost!";
        }
    }

    //From here on is just functions that check for various card hands a function that empties the hands
    private static boolean isFlush(String[] suitList) {
        //compare the last and first suit of the already sorted array to check for a flush
        return (suitList[suitList.length - 1].equals(suitList[0]));
    }

    private static boolean isRoyalFlush(Integer[] valueList) {
        //a decrementing counter starting at 14 because that is the value of an ace
        int cardCounter = 10;

        //checks for 10>jack>queen>king>ace
        for (int i = 0; i < valueList.length; i++) {
            if (valueList[i] != cardCounter) {
                return false;
            }
            cardCounter++;
        }

        return true;
    }

    private static boolean isStraight(Integer[] valueList) {
        int smallestCard = valueList[0];

        //checks for 5 cards in a row from a sorted array
        for (int i = 0; i < valueList.length; i++) {
            if (valueList[i] != smallestCard) {
                return false;
            }
            smallestCard++;
        }

        return true;
    }

    private static boolean isFullHouse(int[] numEachCard) {
        //since this function is called if a 3-pair is detected, all we have to do is look for a 2 pair
        for (int i = 2; i < numEachCard.length; i++) {
            if (numEachCard[i] == 2) {
                return true;
            }
        }

        //if no 2-pairs are found
        return false;
    }

    private static int checkForPairs(int[] numEachCard) {
        int highest = -1;
        int index = -1;
        boolean doubleTwoPair = false;

        for (int i = 2; i < numEachCard.length; i++) {
            if (numEachCard[i] >= highest) {
                //this inner if statement will check for 2 two-pairs.
                if (numEachCard[i] == 2 && highest == 2) {
                    doubleTwoPair = true;
                }

                highest = numEachCard[i];
                index = i;
            }
        }

        //see card values up in the starred comment for why this return value is what it is.
        switch (highest) {
            case 2:
                if (doubleTwoPair) {
                    return 28 + index;
                }
                else
                    return 14 + index;
            case 3:
                //if there is a 3-pair, check for a 2-pair to see if there is a full house
                if (isFullHouse(numEachCard)) {
                    return 84 + index;
                }
                else
                    return 42 + index;
            case 4:
                return 98 + index;
            default:
                return index;
                //the default SHOULD return the last (literal highest card value) found card due to how the loop iterates.
        }
    }

    public static void emptyHands() {
        humanHand.clear();
        botHand.clear();
    }

    public static List<Card> getBotHand() { return botHand; }
}