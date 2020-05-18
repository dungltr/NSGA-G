package sida;

import java.util.ArrayList;
import java.util.List;

import static sida.Experiment.*;
import static sida.Experiment.estimateStateFirst;
import static sida.ExperimentTotal.*;
import static sida.model.*;

public class ExperimentFinal {
    public static String NewLine = "\n";
    public static String Comma = ",";

    private static String dateForm = "yyyy_MM_dd_HH_mm_ss";
    public static int newCaseIndex;
    public static int totalDeathsIndex = 5;
    public static int newDeathsIndex = 6;


    private static int totalCaseIndex = 0;
    private static int deathsIndex = 1;
    private static int recoveredIndex = 2;
    private static int positiveIndex = 3;

    private static String totalCase = "totalCase";
    private static String totalDeath = "totalDeath";
    private static String totalRecovered = "totalRecovered";


    private static int populationSize = 100;
    private static int maxEvolution = 100000;

    private static int dataIndex = 55;

    private static int dayFromFirstState = 0;
    private static int daysFirstState = 8;

    private static int dayFromSecondState = dayFromFirstState + daysFirstState;
    private static int daysSecondState;

    private static int daysFromFinalState;
    private static int daysFinalState = 11;

    //private static int firstStateIndex = 8;
    //
    //private static int daysFinalState = 4;

    private static String linkData = "/home/ubuntu/sidarthe/country/";

    public static void main(String arg[]){

        List<String> countries = new ArrayList<>();
        List<String> algorithms = new ArrayList<>();
        countries.add("kaz");
        algorithms.add("NSGAS");
        for(int i = 0; i < 10; i++) {
            runExperiment(countries, algorithms, populationSize, maxEvolution);
        }
    }
    public static void runExperiment(List<String> countries, List<String> algorithms, int populationSize, int maxEvolution){
        for (int i = 0; i < countries.size(); i++){
            for (int j = 0; j < algorithms.size(); j++){
                ExperimentCountryFinal(countries.get(i),algorithms.get(j), populationSize, maxEvolution);
            }
        }
    }
    public static void ExperimentCountryFinal(String country, String algorithm, int populationSize, int maxEvolution){
        ///////// Read data from file
        //List<List<String>> data = readData("kaz", "data/owid-covid-data.csv");
        List<String[]> data = readData("kaz");
        //List<String> variables = new ArrayList<>();
        //variables.add("total");
        List<List<Double>> infoData = getData2(data);
        storeInfo(infoData,country);
        //double[] totalCasePerMillionn = getInfo(data.get(0).get(newDeathsIndex+1),data);
        //int population = 1000000 * (int) (infoData.get(0)[infoData.get(0).length-1]/totalCasePerMillionn[totalCasePerMillionn.length-1]);
        int population = 18000000;
        System.out.println("The population of "+ country + " is " + population);
        printListDouble(infoData.get(totalCaseIndex));//"data/time_series_covid19_confirmed_global.csv";
        printListDouble(infoData.get(deathsIndex));//"data/time_series_covid19_deaths_global.csv";
        printListDouble(infoData.get(recoveredIndex));//"data/time_series_covid19_recovered_global.csv";
        System.out.println("The size of data is "+ infoData.get(0).size());

        /////////////////////// Setup data from the first step
        double stepUnit = 0.01;
        List<Double> RLimit = new ArrayList<>();
        RLimit.add(3.0);
        RLimit.add(3.0);
        List<List<Double>> realValues = initRealValues(population,infoData);
        List<Double> parameterInit = model.initParameter();
        List<Double> variablesInit = model.initVariables(population);

        ///////////////////////
        ////////////////// State 1 ///////////////////////////////////////////////////// 1
        List<Double> bestParameters = stateFirst4Objectives(populationSize, maxEvolution, algorithm,
                stepUnit, daysFirstState, dayFromFirstState,
                variablesInit, realValues,
                parameterInit, RLimit,
                country);
        //////// Estimate state 1
        int steps = (int) (daysFirstState/stepUnit);
        List<List<Double>> Variables = estimateStateFirst(variablesInit, steps, bestParameters, stepUnit);
        ////// Display state 1
        displayStateFirst(Variables, steps, dayFromFirstState, daysFirstState, realValues);
        /////////////////// State 2 ///////////////////////////////////////////////////// 1 + 53
        int dayFrom = dayFromSecondState;
        daysFromFinalState = realValues.size() - daysFinalState;
        int days = realValues.size() - dayFromSecondState - 1; // number of days is 62 - 8  - 1 = 53
        System.out.println("The number of parameters is "+ days);
        List<List<Double>> listBestParameter = createListParemeter(bestParameters);
        stateTwo4Objects(populationSize, maxEvolution, algorithm,
                stepUnit, days, dayFrom, Variables, realValues,
                listBestParameter, RLimit);
        ///////////////////////////////////////////////////////////////////////////////// 1 + 53 + 1
        ///State Final
        dayFrom = daysFromFinalState; // From dayFrom = 62 - 11 = 51
        days = daysFinalState;//infoData.get(0).size() - daysFromFinalState; // 55 - 50 = 5
        System.out.println("The size of data is "+ infoData.get(0).size() + "the day in final state is " + days);
        List<List<Double>> finalParameter = createFinalListParemeter(listBestParameter);
        List<Double> init = indexVariable(Variables,(int)((double)daysFromFinalState/stepUnit));
        //List<Double> initP = finalVariable(listBestParameter);
        List<Double> initP = finalParameter();
        List<Double> parameter = stateFinal4Objects(populationSize, maxEvolution, algorithm,
                stepUnit, days, dayFrom,
                init, realValues, initP, RLimit);
        finalParameter.add(parameter);
        listBestParameter.add(parameter);
        /////////Estimate state final////////////////////////////////////////////////////
        steps = (int) (days/stepUnit);
        steps = steps + 1;
        List<Double> variablesInitFinal = indexVariable(Variables,(int)((double)daysFromFinalState/stepUnit));
        List<List<Double>> VariablesFinal = estimateStateFirst(variablesInitFinal, steps, parameter, stepUnit);
        VariablesFinal.remove(0);

        //////////////////
        listBestParameter.add(finalParameter());///////////////////////////////////////// 1 + 42 + 1 + 1 = 45
        //updateParameterAll(listBestParameter);
        //////////////////////////////////////////////////////
        storeAll(listBestParameter, populationSize, maxEvolution, country, algorithm);
        System.out.println("Exits");

    }
}
