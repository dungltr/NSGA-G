package sida;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.RealVariable;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.math3.util.Precision.round;
import static sida.testCovid.sbx;
import static sida.testCovid.pm;

public class NSGA {
    public static int[] CasiTotali = {3, 20, 79, 132, 219, 322, 400, 650, // 46 days
            888, 1128, 1694, 2036, 2502, 3089, 3858, 4636, 5883, 7375,
            9172, 10149, 12462, 15113, 17660, 21157,
            24747, 27980, 31506, 35713, 41035, 47021, 53578, 59138, 63927, 69176,
            74386, 80539, 86498, 92472, 97689, 101739, 105792, 110574, 115242, 119827, 124632, 128948};//popolazione; % D+R+T+E+H_diagnosticati
    public static int[] Deceduti = {0, 1, 2, 2, 5, 10, 12, 17, // 46 days
            21, 29, 34, 52, 79, 107, 148, 197, 233, 366,
            463, 631, 827, 1016, 1266, 1441,
            1809, 2158, 2503, 2978, 3405, 4032, 4825, 5476, 6077, 6820,
            7503, 8165, 9134, 10023, 10779, 11591, 12428, 13155, 13915, 14681, 15362, 15887};//popolazione; % E
    public static int[] Guariti = {0, 0, 0, 1, 1, 1, 3, 45, // 46 days
            46, 50, 83, 149, 160, 276, 414, 523, 589, 622,
            724, 1004, 1045, 1258, 1439, 1966,
            2335, 2749, 2941, 4025, 4440, 5129, 6072, 7024, 7432, 8326,
            9362, 10361, 10950, 12384, 13030, 14620, 15729, 16847, 18278, 19758, 20996, 21815};//popolazione; % H_diagnosticati
    public static int[] Positivi = {3, 19, 77, 129, 213, 311, 385, 588, // 46 days
            821, 1049, 1577, 1835, 2263, 2706, 3296, 3916, 5061, 6387,
            7985, 8514, 10590, 12839, 14955, 17750,
            20603, 23073, 26062, 28710, 33190, 37860, 42681, 46638, 50418, 54030,
            57521, 62013, 66414, 70065, 73880, 75528, 77635, 80572, 83049, 85388, 88274, 91246};//popolazione; % D+R+T
    public static int[] Isolamento_domiciliare = {49, 91, 162, 221, 284, // 43 days
            412, 543, 798, 927, 1000, 1065, 1155, 1060, 1843, 2180,
            2936, 2599, 3724, 5036, 6201, 7860,
            9268, 10197, 11108, 12090, 14935, 19185, 22116, 23783, 26522, 28697,
            30920, 33648, 36653, 39533, 42588, 43752, 45420, 48134, 50456, 52579, 55270, 58320};//popolazione;, %D
    public static int[] Ricoverati_sintomi = {54, 99, 114, 128, 248, // 43 days
            345, 401, 639, 742, 1034, 1346, 1790, 2394, 2651, 3557,
            4316, 5038, 5838, 6650, 7426, 8372,
            9663, 11025, 12894, 14363, 15757, 16020, 17708, 19846, 20692, 21937,
            23112, 24753, 26029, 26676, 27386, 27795, 28192, 28403, 28540, 28741, 29010, 28949};//popolazione; % R
    public static int[] Terapia_intensiva = {26, 23, 35, 36, 56, // 43 days
            64, 105, 140, 166, 229, 295, 351, 462, 567, 650,
            733, 877, 1028, 1153, 1328, 1518,
            1672, 1851, 2060, 2257, 2498, 2655, 2857, 3009, 3204, 3396,
            3489, 3612, 3732, 3856, 3906, 3981, 4023, 4035, 4053, 4068, 3994, 3977};//popolazione; %T
    public static int day4 = 0;
    public static int day7 = day4 + CasiTotali.length - Isolamento_domiciliare.length;
    public static int day12 = day7 + 5;
    public static int day22 = day12 + 10;
    public static int day28 = day22 + 6;
    public static int day38 = day28 + 10;
    public static int day50 = day38 + 12;
    public static void main (String args[]){
        List<String> algorithms = new ArrayList<>();
        algorithms.add("NSGAII");
        algorithms.add("NSGAIII");
        algorithms.add("NSGAV");
        algorithms.add("MOEAD");
        algorithms.add("eMOEA");
        int populationSize = 1000;
        int maxEvolution = 1000;
        testNSGACovid(populationSize,maxEvolution,algorithms.get(1));
    }
    public static List<List<Double>> initRealValues (int population){
        List<List<Double>> initValues = new ArrayList<>();
        for (int i = 0; i < CasiTotali.length; i++){
            List<Double> values = new ArrayList<>();
            values.add((double)(CasiTotali[i])/population);// % Total Case is CasiTotali = Deceduti + Guariti + Positivi
            values.add((double)(Deceduti[i])/population); // % Deaths is Deceduti
            values.add((double)(Guariti[i])/population); // % Recovered is Guariti
            values.add((double)(Positivi[i])/population); // % Currently Positive is Positivi
            int delayDay = CasiTotali.length - Isolamento_domiciliare.length;// 46 - 43 = 3
            if(i > (delayDay - 1)){ // i = 3
                values.add((double)(Isolamento_domiciliare[i-delayDay])/population);
                values.add((double)(Ricoverati_sintomi[i-delayDay])/population);
                values.add((double)(Terapia_intensiva[i-delayDay])/population);
            }
            initValues.add(values);
        }
        return initValues;
    }
    public static Solution NSGACovid(int populationSize,
                                     int maxEvolution,
                                     String algorithm,
                                     double step,
                                     int days,
                                     int start,
                                     List<Double> variablesInit,
                                     List<List<Double>> realValues,
                                     List<Double> parameter,
                                     List<Double> Rlimit){
        int dayTo = start + days;
        covidProblem problem = new covidProblem(variablesInit, realValues.get(dayTo), parameter, step, days, Rlimit, start);
        NondominatedPopulation result = new Executor()
                .withProperty("populationSize", populationSize)
                .withAlgorithm(algorithm)
                .withProblem(problem)
                .withMaxEvaluations(maxEvolution)
                .withProperty("sbx.distributionIndex", sbx)
                .withProperty("pm.distributionIndex", pm)
                .run();
        Solution solution = bestResult(result);
        return solution;
    }
    public static Solution NSGACovid4Object(int populationSize,
                                     int maxEvolution,
                                     String algorithm,
                                     double step,
                                     int days,
                                     int start,
                                     List<Double> variablesInit,
                                     List<List<Double>> realValues,
                                     List<Double> parameter,
                                     List<Double> Rlimit){
        int dayTo = start + days;
        covidProblem4Objectives problem = new covidProblem4Objectives(variablesInit, realValues.get(dayTo), parameter,
        step, days, Rlimit, start);
        //(variablesInit, realValues.get(dayTo), parameter, step, days, Rlimit, start);
        NondominatedPopulation result = new Executor()
                .withProperty("populationSize", populationSize)
                .withAlgorithm(algorithm)
                .withProblem(problem)
                .withMaxEvaluations(maxEvolution)
                .withProperty("sbx.distributionIndex", sbx)
                .withProperty("pm.distributionIndex", pm)
                .run();
        Solution solution = bestResult(result);
        return solution;
    }
    public static Solution NSGACovidPeriod(int populationSize,
                                     int maxEvolution,
                                     String algorithm,
                                     double step,
                                     int days,
                                     int start,
                                     List<Double> variablesInit,
                                     List<List<Double>> realValues,
                                     List<Double> parameter,
                                     List<Double> Rlimit){
        int dayTo = start + days;
        covidProblemMultiDay problem = new covidProblemMultiDay(variablesInit, realValues, parameter, step, days, Rlimit, start);
        NondominatedPopulation result = new Executor()
                .withProperty("populationSize", populationSize)
                .withAlgorithm(algorithm)
                .withProblem(problem)
                .withMaxEvaluations(maxEvolution)
                .withProperty("sbx.distributionIndex", sbx)
                .withProperty("pm.distributionIndex", pm)
                .run();
        Solution solution = bestResult(result);
        return solution;
    }
    public static Solution NSGACovidPeriod4Objects(int populationSize,
                                           int maxEvolution,
                                           String algorithm,
                                           double step,
                                           int days,
                                           int start,
                                           List<Double> variablesInit,
                                           List<List<Double>> realValues,
                                           List<Double> parameter,
                                           List<Double> Rlimit){
        int dayTo = start + days;
        covidProblemMultiDay4Objects problem = new covidProblemMultiDay4Objects(variablesInit, realValues, parameter, step, days, Rlimit, start);
        NondominatedPopulation result = new Executor()
                .withProperty("populationSize", populationSize)
                .withAlgorithm(algorithm)
                .withProblem(problem)
                .withMaxEvaluations(maxEvolution)
                .withProperty("sbx.distributionIndex", sbx)
                .withProperty("pm.distributionIndex", pm)
                .run();
        Solution solution = bestResult(result);
        return solution;
    }
    public static List<Double> getVariableSolution (Solution solution){
        List<Double> variables = new ArrayList<>();
        for (int i =  0; i < solution.getNumberOfVariables(); i++){
            double temp = ((RealVariable)solution.getVariable(i)).getValue();
            variables.add(temp);
        }
        return variables;
    }
    public static void testNSGACovid(int populationSize, int maxEvolution, String algorithm){
        int population = 60000000;
        double step = 0.01;
        int days = 3;
        int start = 0;
        List<Double> Rlimit = new ArrayList<>();
        Rlimit.add(3.0);
        Rlimit.add(3.0);
        int dayIndex = start + days;

        List<List<Double>> realValues = initRealValues(population);
        List<Double> parameter = model.initParameter();
        //List<Double> r = model.calculateParameter(parameter);
        //List<Double> parameter = new ArrayList<>();
        List<Double> variablesInit = model.initVariables(population);
        List<Double> vectorsInit = model.initVectors(population);
        List<List<Double>> variables = new ArrayList<>();
        List<List<Double>> vectors = new ArrayList<>();

        long startTimer = System.currentTimeMillis();
        covidProblem problem = new covidProblem(variablesInit, realValues.get(dayIndex), parameter, step, days, Rlimit, start);
        NondominatedPopulation result = new Executor()
                .withProperty("populationSize", populationSize)
                .withAlgorithm(algorithm)
                .withProblem(problem)
                .withMaxEvaluations(maxEvolution)
                .run();
        long stopTimer = System.currentTimeMillis();
        System.out.println("The system algorithm "+ algorithm + " runs in "+ (stopTimer-startTimer)/1000);

        System.out.println("There is "+ result.size()+ " results");
        Solution solution = bestResult(result);
        System.out.println("The best solution is " + solution);
        String title = "";
        for (int i = 0; i < result.get(0).getNumberOfVariables(); i++){
            title = title + "V" + i + "\t"+ "\t";
        }
        for (int i = 0; i < result.get(0).getNumberOfObjectives(); i++){
            title = title + "Obj" + i + "\t"+ "\t";
        }
        System.out.println(title);
        String value = "";
        for (int i = 0; i < solution.getNumberOfVariables(); i++){
            double temp = ((RealVariable)solution.getVariable(i)).getValue();
            value = value + round(temp,4)  + "\t"+ "\t";
        }
        for (int i = 0; i < solution.getNumberOfObjectives(); i++){
            //double temp = ((RealVariable)solution.getVariable(i)).getValue();
            value = value + round((double)solution.getObjective(i),4) + "\t"+ "\t";
        }
        System.out.println(value);

    }
    public static Solution bestResult (NondominatedPopulation result){
        double min = Double.POSITIVE_INFINITY;
        int minIndex = 0;
        for (int i = 0; i < result.size(); i++){
            double sumSquare = 0;
            for (int j = 0; j < result.get(i).getNumberOfObjectives(); j++){
                sumSquare = sumSquare + result.get(i).getObjective(j)*result.get(i).getObjective(j);
            }
            if (sumSquare < min){
                min = sumSquare;
                minIndex = i;
            }
        }
        return result.get(minIndex);
    }
}
