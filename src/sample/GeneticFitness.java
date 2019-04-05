package sample;

import java.util.ArrayList;

public class GeneticFitness {
    ArrayList<Integer> discardList;
    int fitnessScore;

    public GeneticFitness(ArrayList<Integer> discardList, int fitnessScore) {
        this.fitnessScore = fitnessScore;
        this.discardList = discardList;
    }

    public GeneticFitness(){
    }

    public void setDiscardList(ArrayList<Integer> discardList) { this.discardList = discardList; }

    public void setFitnessScore(int fitnessScore) { this.fitnessScore = fitnessScore; }

    public ArrayList<Integer> getDiscardList() { return discardList; }

    public int getFitnessScore() { return fitnessScore; }

}
