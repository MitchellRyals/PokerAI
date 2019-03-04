package sample;

public class Card {
    String imagePath;
    Integer value;
    String suit;

    /*****************CARD LEGEND***********
     *
     * 2 = 2
     * 3 = 3
     * 4 = 4
     * ...
     * Jack = 11
     * Queen = 12
     * King = 13
     * Ace = 14
     **************************************/

    public Card(String imagePath, Integer value, String suit) {
        this.imagePath = imagePath;
        this.value = value;
        this.suit = suit;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Integer getValue() { return value; }

    public String getSuit() { return suit; }

    public static Card[] createDeck() {
        Card[] deck = new Card[52];
        //clubs diamonds hearts spades
        String[] suitTypes = {"c", "d", "h", "s"};
        int i = 0;

        for (int suitCounter = 0; suitCounter < suitTypes.length; suitCounter++) {
            for (int cardValue = 2; cardValue < 15; cardValue++) {
                String path = Integer.toString(cardValue) + suitTypes[suitCounter];
                deck[i] = new Card(path, cardValue, suitTypes[suitCounter]);
                i++;
            }
        }

        return deck;
    }
}
