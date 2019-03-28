package sample;

public class Bot {
    private static int botHandValue;
    private static int currentCash;

    public static void setHandValue(int value) {
        botHandValue = value;
    }

    public static int getBetAmount() {
        currentCash = Main.getBotCash();
        //Possible values have a max of 141
        //so let's get this out of the way first. If he gets a royal flush or a straight he's betting a huge amount
        if (botHandValue == 141) {
            return currentCash;
        }

    }
}
