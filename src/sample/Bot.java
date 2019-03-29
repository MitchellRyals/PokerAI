package sample;

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


        return (int) Math.ceil((currentCash / 10) * betMultiplier) * 10;
    }

    private static double calculateChanceOfWinning() {
        
    }
}
