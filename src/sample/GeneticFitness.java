package sample;

import java.util.ArrayList;

public class GeneticFitness {
    ArrayList<GeneticFitness> parents;
    GeneticFitness child;
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

    public void addParent(GeneticFitness parent) { this.parents.add(parent); }

    public void addChild(GeneticFitness child) { this.child = child; }

    public ArrayList<Integer> getDiscardList() { return discardList; }

    public int getFitnessScore() { return fitnessScore; }

    public ArrayList<GeneticFitness> getAllParents() { return parents; }

    public GeneticFitness getParent(int index) { return parents.get(index); }

    public GeneticFitness getChild(int index) { return child; }


}
