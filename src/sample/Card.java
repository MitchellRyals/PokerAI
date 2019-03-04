package sample;

import java.util.List;
import java.util.ArrayList;

public class Card {
    private final Rank rank;
    private final Suit suit;
    private int imagePath;

    public enum Rank { TWO, THREE, FOUR, FIVE, SIX,
        SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE }

    public enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES }

    public Card(Rank rank, Suit suit, int imagePath) {
        this.rank = rank;
        this.suit = suit;
        this.imagePath = imagePath;
    }

    public String getImagePath() { return "sample/cards/" + imagePath + ".png"; }

    public Rank getRank() { return rank; }

    public Suit getSuit() { return suit; }

    public static List<Card> createDeck() {
        List<Card> deck = new ArrayList<Card>();
        int i = 0;

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(rank, suit, i));
                i++;
            }
        }


        return deck;
    }
}
