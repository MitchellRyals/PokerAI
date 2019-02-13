package sample;

public class Card {
    String imagePath;
    Integer cardValue;

    public Card(String imagePath, Integer cardValue) {
        this.imagePath = imagePath;
        this.cardValue = cardValue;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Integer getCardValue() {
        return cardValue;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setGetCardValue(Integer cardValue) {
        this.cardValue = cardValue;
    }
}
