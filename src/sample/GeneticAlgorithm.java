package sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class GeneticAlgorithm {
    private final int populationSize = 300;
    private final int generations = 10;
    private final int numParents = populationSize/2;
    private int[] fitnessMode = new int[142]; //highest possible score

    public ArrayList<Integer> geneticAlgorithmDiscard(List<Card> actualBotHand) {
        Card geneticCardDeck = new Card();
        List<Card> untouchedDeck = geneticCardDeck.createDeck();
        List<GeneticFitness> parentsList;

        //subtract the cards in his hand from the deck. this works because the deck isn't shuffled yet
        for (Card c: actualBotHand)
            untouchedDeck.remove(new Integer(c.getId()));

        List<GeneticFitness> populationList = createFirstGeneration(untouchedDeck, actualBotHand);

        Comparator comparator = new Comparator<GeneticFitness>() {
            public int compare(GeneticFitness e1, GeneticFitness e2) {
                return Math.max(e1.getFitnessScore(), e2.getFitnessScore());
            }
        };

        Collections.sort(populationList, comparator);
        Collections.reverse(populationList);

        parentsList = createParentsFromCurrentGeneration(populationList);

        for (int i = 0; i < generations; i++) {
            int choice1 = (int)(Math.random() * parentsList.size()/2 - 1);
            int choice2 = (int)(Math.random() * parentsList.size()/2 - 1);
            crossover(parentsList.get(choice1), parentsList.get(choice2));
        }

        Collections.sort(populationList, comparator);
        Collections.reverse(populationList);

        int topPercentile = (int)(Math.random() * (populationList.size() * 0.1));
        ArrayList<Integer> discardList = populationList.get(topPercentile).discardList;

        /*for (int index = 0; index < fitnessMode.length; index++)
            if (fitnessMode[index] > 0)
                System.out.println(index + " fitness " + fitnessMode[index]);
                */

        return discardList;
    }

    private List<Card> geneticAlgorithmFillHand(List<Card> deck, List<Card> hand) {
        while (hand.size() < 5) {
            hand.add(deck.get(0));
            deck.remove(0);
        }

        Comparator comparator = Game.getComparator();

        Collections.sort(hand, comparator);

        return hand;
    }

    private GeneticFitness crossover(GeneticFitness choice1, GeneticFitness choice2) {
        GeneticFitness crossoverResult = new GeneticFitness();
        List<Integer> choice1Discard = choice1.getDiscardList();
        List<Integer> choice2Discard = choice2.getDiscardList();
        List<Integer> crossoverDiscard = new ArrayList<>();

        if (choice1Discard.size() > 1) {
            for (int i = 0; i < choice1Discard.size(); i++)
                if (Math.random() * choice1Discard.size() - 1 >= 0.5)
                    crossoverDiscard.add(choice1Discard.get(i));

            //ensure at least SOMETHING makes it into the discard array
            if (choice1Discard.isEmpty())
                crossoverDiscard.add(choice1Discard.get(0));
        }
        else
            crossoverDiscard.add(choice1Discard.get(0));

        for (int i = 0; i < choice2Discard.size(); i++)
            if (Math.random() * choice2Discard.size() - 1 >= 0.5)
                if (!crossoverDiscard.contains(choice2Discard.get(i)))
                    crossoverDiscard.add(choice2Discard.get(i));

        //slight mutation chance
        if (Math.random() > 0.9) {
            int mutator = (int)(Math.random() * 4);
            if (!crossoverDiscard.contains(mutator))
                crossoverDiscard.add(mutator);
        }

        return crossoverResult;
    }

    private List<GeneticFitness> createFirstGeneration(List<Card> untouchedDeck, List<Card> actualBotHand) {
        List<GeneticFitness> populationList = new ArrayList<>();

        //so this for loop goes through the population, generating copies of hands with random discards
        //and gets their score, storing it in the fitness list. I then use the high scores later
        for (int i = 0; i < populationSize; i++) {
            GeneticFitness currentSample = new GeneticFitness();
            ArrayList copyDeck = new ArrayList(untouchedDeck);
            List<Card> geneticHandCopy = new ArrayList<Card>(actualBotHand);
            ArrayList<Integer> indexToDiscard = new ArrayList<>();

            int amountToDiscard = (int) (1 + (Math.random() * 4));

            for (int j = 0; j < amountToDiscard; j++) {
                //this while loop ensures a unique discard value
                while (true) {
                    int randomIndex = (int) (Math.random() * (geneticHandCopy.size() - 1));
                    if (!indexToDiscard.contains(randomIndex)) {
                        indexToDiscard.add(randomIndex);
                        break;
                    }
                }
            }

            Collections.sort(indexToDiscard);
            Collections.reverse(indexToDiscard);
            for (int x: indexToDiscard)
                geneticHandCopy.remove(x);

            Collections.shuffle(copyDeck);
            geneticHandCopy = geneticAlgorithmFillHand(copyDeck, geneticHandCopy);

            //add everything to the data structure
            currentSample.setDiscardList(indexToDiscard);
            currentSample.setFitnessScore(Game.getHandValue(geneticHandCopy));
            //fitnessMode[currentSample.getFitnessScore()]++;
            populationList.add(currentSample);
        }

        return populationList;
    }

    private List<GeneticFitness> createParentsFromCurrentGeneration(List<GeneticFitness> populationList) {
        List<GeneticFitness> parentsList = new ArrayList<>();

        for (int i = 0; i < numParents; i++) {
            double chanceFitParent = Math.random();
            //this if statement gives a high chance of selecting fit individuals as parents
            if (chanceFitParent > 0.4)
                parentsList.add(populationList.get((int)(Math.random() * numParents/2)));
            else
                parentsList.add(populationList.get((int)(Math.random() * (numParents/2 + numParents/2))));
        }

        return parentsList;
    }
}
