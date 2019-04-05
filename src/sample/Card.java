package sample;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

//Card images courtesy of the ACBL
public class Card {
    private final Rank rank;
    private final Suit suit;
    private final int id;
    private static List<Card> deck;

    public enum Rank { TWO, THREE, FOUR, FIVE, SIX,
        SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE }

    public enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES }

    public Card(Rank rank, Suit suit, int id) {
        this.rank = rank;
        this.suit = suit;
        this.id = id;
    }

    public static void setDeck(List<Card> newDeck) {
        deck = newDeck;
    }

    public String getImagePath() { return "sample/cards/" + suit + rank + ".png"; }

    public int getRank() { return rank.ordinal() + 2; }

    public String getSuit() { return suit.toString(); }

    public int getId() { return id; }

    public static List<Card> getDeck() { return deck; }

    public static List<Card> createDeck() {
        deck = new ArrayList<Card>();
        int i = 0;

        for (Suit suit: Suit.values()) {
            for (Rank rank: Rank.values()) {
                deck.add(new Card(rank, suit, i));
                i++;
            }
        }

        return deck;
    }

    public static List<Card> generateDebugHand() {
        List<Card> debugHand = new ArrayList<Card>();

        debugHand.add(new Card(Rank.ACE, Suit.HEARTS, 38));
        debugHand.add(new Card(Rank.FIVE, Suit.HEARTS, 29));
        debugHand.add(new Card(Rank.TEN, Suit.HEARTS, 34));
        debugHand.add(new Card(Rank.JACK, Suit.HEARTS, 35));
        debugHand.add(new Card(Rank.NINE, Suit.CLUBS, 7));
        Main.updateBotHand(debugHand);

        return debugHand;
    }
}
