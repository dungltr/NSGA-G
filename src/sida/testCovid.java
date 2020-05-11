package sida;

import java.util.ArrayList;
import java.util.List;

import static NSGAV.TestNSGA.*;

public class testCovid {
    static int population = 100;// = Ask("Population",100);
    static int  maxEvaluate = population*100;// = Ask("maxEvaluate",population*10);
    static int  runSeed = 50;// = Ask("RunSeed", 20);
    static int  sbx = 30;// = Ask("sbx",30);
    static int pm = 20;// = Ask("pm", 20);
    public static void main(String[] args){
        testCovid();
    }
    public static void testCovid(){
        List<String> Algorithms = new ArrayList<>();
        int periodDefault = Ask("Using periods",0);
        int timesDefault = Ask("How many times to run",1);
        Algorithms = AskAlgorithms();
        System.out.println("All algorithms is: ");
        printList(Algorithms);
        population = Ask("Population",100);
        maxEvaluate = Ask("maxEvaluate",population*10);
        runSeed = Ask("RunSeed", 50);
        sbx = Ask("sbx",30);
        pm = Ask("pm", 20);
        for (int i = 0; i < timesDefault; i++){
            for (int j = 0; j < Algorithms.size(); j++){
                if (periodDefault == 0) {
                    ExperimentPeriod.experimentPeriod(population,maxEvaluate,Algorithms.get(j));
                }
                else {
                    Experiment.experiment1(population,maxEvaluate,Algorithms.get(i));
                }
            }
        }
    }
}
