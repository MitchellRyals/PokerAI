# Poker Against an Intelligent Agent
This was a project I finished during my final semester before getting my Bachelor's degree. This is a full stack application played by the player and uses recursion and heuristic values to judge what the bot should discard.

This is a standard game of 5-card draw poker made with Java and JavaFX. Below you'll find a layout of what to expect in each file. If you would like to see a flowchart of how the game plays, take a look at GameFlow.png

# How to play
Navigate to and download out/artifacts/JavaFXApp and download and run JavaFXApp.jar

# Code
## Main.java
When I started the project, I figured it was going to be pretty small and put all the front end into the Main file. This and Game.java completely control the flow of the game. Any buttons that the player has to press to take their turn are located in Main.java.

The only thing backend here that is of interest is the way the cards are programmed. Each one of the player's cards are buttons that get selected or deselected during the discarding turn depending on whether they are clicked or not.

## Game.java
Half of this class is the actual discarding, dealing hands, and game flow. The other half is a getHandValue() function that returns an integer score based on several factors. See this chart for the scores possible:

---
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
---

It first creates a list of suits and a list of values from whatever hand is passed to it. It then sorts the suit array and checks the first and last value to see if the hand is a flush or not. If it is, it'll check if it is a royal flush or straight flush. If not then it will fill an array with the amount of times each card appears and use that to check for full house or any amount of pairs. If none of the above are found, it'll just return the highest card value in the hand.

## Bot.java
This class contains a recursive call to discard the missing card out of a flush (5 of the same suit) because I figured the odds are decently likely that he will get a flush if 4 of them are already matching suits. The recursive call uses the same concept as quick sort minus the pivot in that it works from both ends and if an index is found to be in common, it means that the card in question is the only one missing out of being a flush and he will discard it (with a couple of edge cases in if statements).
You'll also find a linear function to decide what he bets using a percentage multiplier based on his current cash.

## GeneticAlgorithm.java
This is the main heuristic deciding what the bot discards. This is a variant of the genetic algorithm without elitism (because poker is random and there is no clear cut solution we are looking for). It was important to me that he doesn't cheat so he creates a brand new deck and subtracts only his hand from it so that he has no knowledge of what is in the human player's hand. The algorithm discards at random from hundreds of copies of his hand and picks the best upper half of scores from these. It'll then combine elements of the discard lists at random from these parents with a 10% (at the time of writing) chance of mutating a random discard element to avoid getting stuck at a local maximum.

After 10 generations of 300 states each, it returns a random list from the upper 15% of the final generation to use as what the bot will discard.

Newly added to the application, all the scores of the discards are written to a text file called GeneticAlgorithmData.txt after each time he runs this algorithm.
