package sample;

import java.util.List;
import java.util.ArrayList;

//Card images courtesy of the ACBL
public class Card {
    private final Rank rank;
    private final Suit suit;
    private static List<Card> deck;

    public enum Rank { TWO, THREE, FOUR, FIVE, SIX,
        SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE }

    public enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES }

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public static void setDeck(List<Card> newDeck) {
        deck = newDeck;
    }

    public String getImagePath() { return "sample/cards/" + suit + rank + ".png"; }

    public int getRank() { return rank.ordinal() + 2; }

    public String getSuit() { return suit.toString(); }

    public static List<Card> getDeck() { return deck; }

    public static List<Card> createDeck() {
        deck = new ArrayList<Card>();
        int i = 0;

        for (Suit suit: Suit.values()) {
            for (Rank rank: Rank.values()) {
                deck.add(new Card(rank, suit));
                i++;
            }
        }

        return deck;
    }

    public static List<Card> generateDebugHand() {
        List<Card> debugHand = new ArrayList<Card>();

        debugHand.add(new Card(Rank.ACE, Suit.HEARTS));
        debugHand.add(new Card(Rank.FIVE, Suit.CLUBS));
        debugHand.add(new Card(Rank.TEN, Suit.CLUBS));
        debugHand.add(new Card(Rank.JACK, Suit.CLUBS));
        debugHand.add(new Card(Rank.NINE, Suit.CLUBS));
        Main.updateBotHand(debugHand);

        return debugHand;
    }
}
