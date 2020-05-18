package sida;

import draft.utils.writeFile;
import org.moeaframework.core.Solution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.math3.util.Precision.round;
import static sida.ExperimentPeriod.storeAll;
import static sida.NSGA.*;
import static sida.model.*;
import static sida.simulation.sidatheSimulation;

public class Experiment {
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

    public static void main(String[] args){
        int populationSize = 100;
        int maxEvolution = 2000000;
        List<String> algorithms = new ArrayList<>();
        algorithms.add("NSGAII");
        algorithms.add("NSGAIII");
        algorithms.add("NSGAS");
        algorithms.add("MOEAD");
        algorithms.add("eMOEA");
        for (int i = 0; i < 10; i++) {
            experiment1(populationSize, maxEvolution, algorithms.get(2));
        }
    }
    public static List<Double> stateFirst (int populationSize, int maxEvolution, String algorithm,
                                           double stepUnit, int days, int dayFrom,
                                           List<Double> variablesInit, List<List<Double>> realValues,
                                           List<Double> parameterInit, List<Double> RLimit){
        Solution solution = NSGACovid(populationSize,
                maxEvolution,
                algorithm,
                stepUnit, days,
                dayFrom,
                variablesInit,
                realValues,
                parameterInit,
                RLimit);
        List<Double> bestParameters = getVariableSolution(solution);
        System.out.println("The best parameter values of "+algorithm+" algorithm are ");
        printListDouble(bestParameters);
        if (dayFrom == 0) updateParameterFile(bestParameters);
        updateParameterFile(dayFrom,bestParameters);
        return bestParameters;
    }
    public static List<Double> stateFirst4Objects (int populationSize, int maxEvolution, String algorithm,
                                           double stepUnit, int days, int dayFrom,
                                           List<Double> variablesInit, List<List<Double>> realValues,
                                           List<Double> parameterInit, List<Double> RLimit){
        Solution solution = NSGACovid4Object(populationSize,
                maxEvolution,
                algorithm,
                stepUnit, days,
                dayFrom,
                variablesInit,
                realValues,
                parameterInit,
                RLimit);
        List<Double> bestParameters = getVariableSolution(solution);
        System.out.println("The best parameter values of "+algorithm+" algorithm are ");
        printListDouble(bestParameters);
        if (dayFrom == 0) updateParameterFile(bestParameters);
        updateParameterFile(dayFrom,bestParameters);
        return bestParameters;
    }
    public static List<List<Double>> stateTwo (int populationSize, int maxEvolution, String algorithm,
                                               double stepUnit, int days, int dayFrom,
                                               List<List<Double>> variables, List<List<Double>> realValues,
                                               List<List<Double>> listBestParameter, List<Double> RLimit){
        //int finalExperiment = realValues.size() - 1;
        int finalExperiment = dayFrom + days;
        int deltaDay = 1;
        int steps = (int) (1/stepUnit);
        //dayFrom = dayFrom + days;
        for (int i = dayFrom; i < finalExperiment; i++){
            List<Double> variablesInit = finalVariable(variables);
            List<Double> parameterInit = finalVariable(listBestParameter);
            List<Double> Rlimit = R0LimitCalculate(i, listBestParameter, RLimit);
            RLimit.clear();
            RLimit.add(Rlimit.get(0));
            RLimit.add(Rlimit.get(1));
            System.out.println("The indicator R0 at day " + i + " is " + calculate1(parameterInit) + " or " + calculate2(parameterInit));
            System.out.println("Experiment from day "+ i);
            List<Double> nextParameter = stateFirst(populationSize, maxEvolution, algorithm,
                    stepUnit, deltaDay, i,
                    variablesInit, realValues, parameterInit, RLimit);

            List<List<Double>> Variables = estimateStateTwo(variablesInit, steps, nextParameter, stepUnit);
            displayStateFirst(variables,steps,i,1,realValues);
            for (int j = 0; j < Variables.size(); j ++){
                variables.add(Variables.get(j));
            }
            listBestParameter.add(nextParameter);
        }
        return listBestParameter;
    }
    public static List<List<Double>> stateTwo4Objects (int populationSize, int maxEvolution, String algorithm,
                                               double stepUnit, int days, int dayFrom,
                                               List<List<Double>> variables, List<List<Double>> realValues,
                                               List<List<Double>> listBestParameter, List<Double> RLimit){
        //int finalExperiment = realValues.size() - 1;
        int finalExperiment = dayFrom + days;
        int deltaDay = 1;
        int steps = (int) (1/stepUnit);
        //dayFrom = dayFrom + days;
        for (int i = dayFrom; i < finalExperiment; i++){
            List<Double> variablesInit = finalVariable(variables);
            List<Double> parameterInit = finalVariable(listBestParameter);
            List<Double> Rlimit = R0LimitCalculate(i, listBestParameter, RLimit);
            //RLimit.clear();
            //RLimit.add(Rlimit.get(0));
            //RLimit.add(Rlimit.get(1));
            System.out.println("The indicator R0 at day " + i + " is " + calculate1(parameterInit) + " or " + calculate2(parameterInit));
            System.out.println("Experiment from day "+ i);
            List<Double> nextParameter = stateFirst4Objects(populationSize, maxEvolution, algorithm,
                    stepUnit, deltaDay, i,
                    variablesInit, realValues, parameterInit, RLimit);

            List<List<Double>> Variables = estimateStateTwo(variablesInit, steps, nextParameter, stepUnit);
            displayStateFirst(variables,steps,i,1,realValues);
            for (int j = 0; j < Variables.size(); j ++){
                variables.add(Variables.get(j));
            }
            listBestParameter.add(nextParameter);
        }
        return listBestParameter;
    }
    public static List<Double> stateFinal (int populationSize, int maxEvolution, String algorithm,
                                           double stepUnit, int days, int dayFrom,
                                           List<Double> variablesInit, List<List<Double>> realValues,
                                           List<Double> parameterInit, List<Double> RLimit){
        Solution solution = NSGACovidPeriod(populationSize,
                maxEvolution,
                algorithm,
                stepUnit, days,
                dayFrom,
                variablesInit,
                realValues,
                parameterInit,
                RLimit);
        List<Double> bestParameters = getVariableSolution(solution);
        System.out.println("The best parameter values of "+algorithm+" algorithm are ");
        printListDouble(bestParameters);
        if (dayFrom == 0) updateParameterFile(bestParameters);
        updateParameterFile(dayFrom,bestParameters);
        return bestParameters;
    }
    public static List<Double> stateFinal4Objects (int populationSize, int maxEvolution, String algorithm,
                                           double stepUnit, int days, int dayFrom,
                                           List<Double> variablesInit, List<List<Double>> realValues,
                                           List<Double> parameterInit, List<Double> RLimit){
        Solution solution = NSGACovidPeriod4Objects(populationSize,
                maxEvolution,
                algorithm,
                stepUnit, days,
                dayFrom,
                variablesInit,
                realValues,
                parameterInit,
                RLimit);
        List<Double> bestParameters = getVariableSolution(solution);
        System.out.println("The best parameter values of "+algorithm+" algorithm are ");
        printListDouble(bestParameters);
        if (dayFrom == 0) updateParameterFile(bestParameters);
        updateParameterFile(dayFrom,bestParameters);
        return bestParameters;
    }
    public static void displayStateFirst(List<List<Double>> Variables, int steps,
                                         int dayFrom, int days,
                                         List<List<Double>> realValues){
        List<Double> finalVariables = finalVariable(Variables);
        List<Double> estimateValues = realValues(finalVariables);

        System.out.println("The estimate values are at the fourth day when using parameter of NSGA");
        printListDouble (estimateValues);

        System.out.println("The simulation values of SidarTheSimulation at the fourth day and the number of steps:" + (steps - 1));
        List<List<Double>> sida = sidatheSimulation();
        printListDouble (realValues(sida.get(steps-1)));

        System.out.println("The real values are from Data at the fourth day:");
        int dayIndex = dayFrom + days;
        printListDouble (realValues.get(dayIndex));
    }
    public static List<List<Double>> estimateStateFirst(List<Double> variablesInit, int steps,
                                                        List<Double> bestParameters, double stepUnit){
        List<List<Double>> Variables = new ArrayList<>();
        Variables.add(variablesInit);
        for (int i = 0; i < steps - 1; i++) {
            List<List<Double>> listVariables = computerVariables(Variables, bestParameters, stepUnit);
        }
        return Variables;
    }
    public static List<List<Double>> estimateStateTwo(List<Double> variablesInit, int steps,
                                                        List<Double> bestParameters, double stepUnit){
        List<List<Double>> Variables = new ArrayList<>();
        Variables.add(variablesInit);
        for (int i = 0; i < steps; i++) {
            List<List<Double>> listVariables = computerVariables(Variables, bestParameters, stepUnit);
        }
        Variables.remove(0);
        return Variables;
    }
    public static List<List<Double>> estimateStateTwoTotal(List<Double> variablesInit, int steps,
                                                      List<Double> bestParameters, double stepUnit){
        List<List<Double>> Variables = new ArrayList<>();
        Variables.add(variablesInit);
        for (int i = 0; i < steps; i++) {
            List<List<Double>> listVariables = computerVariables(Variables, bestParameters, stepUnit);
        }
        Variables.remove(0);
        return Variables;
    }
    public static List<List<Double>> createListParemeter(List<Double> bestParameters){
        List<List<Double>> listBestParameter = new ArrayList<>();
        List<Double> tempParameter = new ArrayList<>();
        for (int i = 0; i < bestParameters.size();i++){
            tempParameter.add(bestParameters.get(i));
        }
        listBestParameter.add(tempParameter);
        return listBestParameter;
    }
    public static List<List<Double>> createFinalListParemeter(List<List<Double>> listBestParameters){
        List<List<Double>> finalBestParameter = new ArrayList<>();
        for (int i = 0; i < listBestParameters.size(); i ++){
            List<Double> tempParameter = new ArrayList<>();
            for (int j = 0; j < listBestParameters.get(listBestParameters.size()-1).size();j++){
                tempParameter.add(listBestParameters.get(i).get(j));
            }
            finalBestParameter.add(tempParameter);
        }
        return finalBestParameter;
    }
    public static void experiment1 (int populationSize, int maxEvolution, String algorithm){
        int population = 60000000;
        double stepUnit = 0.01;
        int days = 3;
        int dayFrom = 0;
        List<Double> RLimit = new ArrayList<>();
        RLimit.add(3.0);
        RLimit.add(3.0);
        List<List<Double>> realValues = initRealValues(population);
        List<Double> parameterInit = model.initParameter();
        List<Double> variablesInit = model.initVariables(population);
        ////////////////// State 1 /////////////////////////////////////////////////////
        List<Double> bestParameters = stateFirst(populationSize, maxEvolution, algorithm,
                stepUnit, days, dayFrom,
                variablesInit, realValues, parameterInit, RLimit);
        //////// Estimate state 1
        int steps = (int) (days/stepUnit);
        List<List<Double>> Variables = estimateStateFirst(variablesInit, steps, bestParameters, stepUnit);
        ////// Display state 1
        displayStateFirst(Variables, steps, dayFrom, days, realValues);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // State 2
        /*
          dayFrom = dayFrom + days; // From dayFrom = 3
        finalStateIndex = 38;
        days = finalStateIndex - days; // number of days is 35
         */
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        dayFrom = dayFrom + days; // From dayFrom = 3
        days = CasiTotali.length - days - 1;// 46 - 3 - 1 = 42
        List<List<Double>> listBestParameter = createListParemeter(bestParameters);
        stateTwo(populationSize, maxEvolution, algorithm,
                stepUnit, days, dayFrom, Variables, realValues,
                listBestParameter, RLimit);

        listBestParameter.add(finalParameter());
        updateParameterAll(listBestParameter);
        storeAll(listBestParameter, populationSize, maxEvolution, algorithm);
        System.out.println("Exits");

    }

    public static List<Double> R0LimitCalculate (int i, List<List<Double>> listBestParameter, List<Double> Rlimit){
        List<Double> RLimit = new ArrayList<>();
        switch (i){
            case (3): { // look back to the first -> parameter(0)
                RLimit.clear();
                RLimit.add(calculate1(listBestParameter.get(0)));
                RLimit.add(calculate2(listBestParameter.get(0)));
            }
                break;
            //case (4): { // look back to date 3 -> store in parameter(2)
            //    RLimit.clear();
            //    RLimit.add(calculate1(listBestParameter.get(1)));
            //    RLimit.add(calculate2(listBestParameter.get(1)));
            //    break;
            //}
            case (11): { // look back to 4 -> 2
                RLimit.clear();
                RLimit.add(calculate1(listBestParameter.get(2)));
                RLimit.add(calculate2(listBestParameter.get(2)));
            }
                break;
            //case (21): { // look back to 11 -> 9
            //    RLimit.clear();
            //    RLimit.add(calculate1(listBestParameter.get(9)));
            //    RLimit.add(calculate2(listBestParameter.get(9)));
            //}
            //    break;
            case (27): {// look back to 21 -> 19
                RLimit.clear();
                RLimit.add(calculate1(listBestParameter.get(9)));
                RLimit.add(calculate2(listBestParameter.get(9)));
            }
                break;
            case (37): { // look back to 27 -> 25
                RLimit.clear();
                RLimit.add(calculate1(listBestParameter.get(25)));
                RLimit.add(calculate2(listBestParameter.get(25)));
            }
                break;
            default: {
                RLimit.clear();
                RLimit.add(Rlimit.get(0));
                RLimit.add(Rlimit.get(1));
            }
                break;
        }
        System.out.println("The indicator R0 limit is:");
        printListDouble(RLimit);
        return RLimit;
    }
    public static List<Double> experimentAday (List<List<Double>> Variables, List<Double> initParameter,
                                               List<List<Double>> realValues, int start, int populationSize,
                                               int maxEvolution, String algorithm, List<Double> Rlimit){
        double step = 0.01;
        int days = 1;
        List<Double> parameterInit = new ArrayList<>();
        for (int i = 0; i < initParameter.size(); i++){
            parameterInit.add(initParameter.get(i));
        }
        List<Double> variablesInit = new ArrayList<>();
        for (int i = 0; i < Variables.get(Variables.size()-1).size(); i++){
            variablesInit.add(Variables.get(Variables.size()-1).get(i));
        }

        Solution solution = NSGACovid(populationSize,
                maxEvolution,
                algorithm,
                step, days,
                start,
                variablesInit,
                realValues,
                parameterInit,
                Rlimit);
        List<Double> bestParameters = getVariableSolution(solution);
        System.out.println("The best parameter values of "+algorithm+" algorithm are ");
        printListDouble(bestParameters);
        updateParameterFile(start,bestParameters);


        int steps = (int) (days/step);

        double stepUnit = step;
        for (int i = 0; i < steps; i++) {
            List<List<Double>> listVariables = computerVariables(Variables, bestParameters, stepUnit);
        }
        List<Double> finalVariables = finalVariable(Variables);
        List<Double> estimateValues = realValues(finalVariables);
        int finalDay = start + days;
        System.out.println("The final variables at day "+ finalDay +" are\n");
        printListDouble (finalVariables);
        System.out.println("The final estimates "+ finalDay +" are\n");
        printListDouble (estimateValues);

        int dayIndex = start + days;
        System.out.println("The real values are from Data at:"+dayIndex);
        printListDouble (realValues.get(dayIndex));
        return bestParameters;
    }

    public static void printListDouble (List<Double> list){
        String value = "";
        for (int i = 0; i < list.size(); i++){
            value = value + round(list.get(i),10)  + "\t"+ "\t";
        }
        System.out.println(value);
    }
    public static void updateParameterAll(List<List<Double>> bestParameters){
        String link = "/home/ubuntu/sidarthe/setParameterDayAll.m";
        String outPut = "function [alfa, beta, gamma, delta, epsilon, theta, zeta, eta, mu, nu, tau, lambda, rho, kappa, xi, sigma] = setParameterDayAll(i)";
        outPut = outPut + "\n";
        outPut = outPut + "MatrixParameter=[";
        for(int i = 0; i < bestParameters.size(); i ++){
            for(int j = 0; j < bestParameters.get(0).size(); j ++){
                if (j < bestParameters.get(i).size()-1){
                    outPut = outPut + bestParameters.get(i).get(j) + ",";
                }
                else {
                    if (i < bestParameters.size() - 1) {
                        outPut = outPut + bestParameters.get(i).get(j) + ";";
                    }
                    else outPut = outPut + bestParameters.get(i).get(j) + "];";
                }
            }
        }
        outPut = outPut + "\n";
        outPut = outPut + "alfa=MatrixParameter(i,1);"+"\n";
        outPut = outPut + "beta=MatrixParameter(i,2);"+"\n";
        outPut = outPut + "gamma=MatrixParameter(i,3);"+"\n";
        outPut = outPut + "delta=MatrixParameter(i,4);"+"\n";
        outPut = outPut + "epsilon=MatrixParameter(i,5);"+"\n";
        outPut = outPut + "theta=MatrixParameter(i,6);"+"\n";
        outPut = outPut + "zeta=MatrixParameter(i,7);"+"\n";
        outPut = outPut + "eta=MatrixParameter(i,8);"+"\n";
        outPut = outPut + "mu=MatrixParameter(i,9);"+"\n";
        outPut = outPut + "nu=MatrixParameter(i,10);"+"\n";
        outPut = outPut + "tau=MatrixParameter(i,11);"+"\n";
        outPut = outPut + "lambda=MatrixParameter(i,12);"+"\n";
        outPut = outPut + "rho=MatrixParameter(i,13);"+"\n";
        outPut = outPut + "kappa=MatrixParameter(i,14);"+"\n";
        outPut = outPut + "xi=MatrixParameter(i,15);"+"\n";
        outPut = outPut + "sigma=MatrixParameter(i,16);"+"\n";
        outPut = outPut + "end";
        Path filePath = Paths.get(link);
        if(Files.exists(filePath)){
            System.out.println("The file is already exist");
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeFile.writeString(link,outPut);
    }
    public static void updateParameterFile(int start, List<Double> bestParameters){
        String link = "/home/ubuntu/sidarthe/setParameterDay"+start+".m";
        String outPut = "function [alfa, beta, gamma, delta, epsilon, theta, zeta, eta, mu, nu, tau, lambda, rho, kappa, xi, sigma] = setParameterDay"+start+"()";
        outPut = outPut + "\n";
        outPut = outPut + "alfa="+bestParameters.get(0)+";\n";
        outPut = outPut + "beta="+bestParameters.get(1)+";\n";
        outPut = outPut + "gamma="+bestParameters.get(2)+";\n";
        outPut = outPut + "delta="+bestParameters.get(3)+";\n";
        outPut = outPut + "epsilon="+bestParameters.get(4)+";\n";
        outPut = outPut + "theta="+bestParameters.get(5)+";\n";
        outPut = outPut + "zeta="+bestParameters.get(6)+";\n";
        outPut = outPut + "eta="+bestParameters.get(7)+";\n";
        outPut = outPut + "mu="+bestParameters.get(8)+";\n";
        outPut = outPut + "nu="+bestParameters.get(9)+";\n";
        outPut = outPut + "tau="+bestParameters.get(10)+";\n";
        outPut = outPut + "lambda="+bestParameters.get(11)+";\n";
        outPut = outPut + "rho="+bestParameters.get(12)+";\n";
        outPut = outPut + "kappa="+bestParameters.get(13)+";\n";
        outPut = outPut + "xi="+bestParameters.get(14)+";\n";
        outPut = outPut + "sigma="+bestParameters.get(15)+";\n";
        outPut = outPut + "end";
        Path filePath = Paths.get(link);
        if(Files.exists(filePath)){
            System.out.println("The file is already exist");
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeFile.writeString(link,outPut);
    }
    public static void updateParameterFile(List<Double> bestParameters){
        String link = "/home/ubuntu/sidarthe/initParameter.m";
        String outPut = "function [alfa, beta, gamma, delta, epsilon, theta, zeta, eta, mu, nu, tau, lambda, rho, kappa, xi, sigma] = initParameter()";
        outPut = outPut + "\n";
        outPut = outPut + "alfa="+bestParameters.get(0)+";\n";
        outPut = outPut + "beta="+bestParameters.get(1)+";\n";
        outPut = outPut + "gamma="+bestParameters.get(2)+";\n";
        outPut = outPut + "delta="+bestParameters.get(3)+";\n";
        outPut = outPut + "epsilon="+bestParameters.get(4)+";\n";
        outPut = outPut + "theta="+bestParameters.get(5)+";\n";
        outPut = outPut + "zeta="+bestParameters.get(6)+";\n";
        outPut = outPut + "eta="+bestParameters.get(7)+";\n";
        outPut = outPut + "mu="+bestParameters.get(8)+";\n";
        outPut = outPut + "nu="+bestParameters.get(9)+";\n";
        outPut = outPut + "tau="+bestParameters.get(10)+";\n";
        outPut = outPut + "lambda="+bestParameters.get(11)+";\n";
        outPut = outPut + "rho="+bestParameters.get(12)+";\n";
        outPut = outPut + "kappa="+bestParameters.get(13)+";\n";
        outPut = outPut + "xi="+bestParameters.get(14)+";\n";
        outPut = outPut + "sigma="+bestParameters.get(15)+";\n";
        outPut = outPut + "end";
        Path filePath = Paths.get(link);
        if(Files.exists(filePath)){
            System.out.println("The file is already exist");
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeFile.writeString(link,outPut);
    }
}
