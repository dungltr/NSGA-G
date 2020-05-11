package sida;

import draft.utils.writeFile;
import org.moeaframework.core.Solution;
import org.moeaframework.problem.misc.Lis;
import sida.utils.utils;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static sida.Experiment.*;
import static sida.ExperimentPeriod.storeMFile39;
import static sida.ExperimentPeriod.storeMFile45;
import static sida.NSGA.*;
import static sida.model.finalParameter;
import static sida.model.finalVariable;
import static sida.utils.utils.printListListString;

public class ExperimentTotal {
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
    private static int maxEvolution = 1000000;
    private static int finalStateIndex;
    private static int dataIndex = 55;

    private static String linkData = "/home/ubuntu/sidarthe/country/";

    public static void main(String arg[]){

       List<String> countries = new ArrayList<>();
       List<String> algorithms = new ArrayList<>();
       countries.add("kaz");
       algorithms.add("NSGAS");
       for(int i = 0; i < 2; i++) {
           runExperiment(countries, algorithms, populationSize, maxEvolution);
       }
    }
    public static void runExperiment(List<String> countries, List<String> algorithms, int populationSize, int maxEvolution){
        for (int i = 0; i < countries.size(); i++){
            for (int j = 0; j < algorithms.size(); j++){
                ExperimentCountry(countries.get(i),algorithms.get(j), populationSize, maxEvolution);
            }
        }
    }
    public static void storeInfo(List<List<Double>> infoData, String country){
        List<String> listName = new ArrayList<>();
        listName.add(totalCase);
        listName.add(totalDeath);
        listName.add(totalRecovered);
        for (int i = 0; i < listName.size(); i++){
            String content = "function temp = "+listName.get(i)+"()\n";
            content = content + "temp = "+infoData.get(i).toString()+";\n";
            content = content + "end";
            String link = linkData+country+"/"+listName.get(i)+".m";
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
            writeFile.writeString(link,content);
        }
    }
    public static void ExperimentCountry(String country, String algorithm, int populationSize, int maxEvolution){
        ///////// Read data from file
        //List<List<String>> data = readData("kaz", "data/owid-covid-data.csv");
        List<String[]> data = readData("kaz");
        List<String> variables = new ArrayList<>();
        variables.add("total");
        List<List<Double>> infoData = getData2(data);
        storeInfo(infoData,country);
        //double[] totalCasePerMillionn = getInfo(data.get(0).get(newDeathsIndex+1),data);
        //int population = 1000000 * (int) (infoData.get(0)[infoData.get(0).length-1]/totalCasePerMillionn[totalCasePerMillionn.length-1]);
        int population = 18000000;
        System.out.println("The population of "+ country + " is " + population);
        printListDouble(infoData.get(totalCaseIndex));//"data/time_series_covid19_confirmed_global.csv";
        printListDouble(infoData.get(deathsIndex));//"data/time_series_covid19_deaths_global.csv";
        printListDouble(infoData.get(recoveredIndex));//"data/time_series_covid19_recovered_global.csv";
        //printListDouble(infoData.get(positiveIndex));

        /////////////////////// Setup data from the first step
        double stepUnit = 0.01;
        int days = 8;
        int dayFrom = 0;
        List<Double> RLimit = new ArrayList<>();
        RLimit.add(3.0);
        RLimit.add(3.0);
        List<List<Double>> realValues = initRealValues(population,infoData);
        List<Double> parameterInit = model.initParameter();
        List<Double> variablesInit = model.initVariables(population);

        ///////////////////////
        ////////////////// State 1 /////////////////////////////////////////////////////
        List<Double> bestParameters = stateFirst4Objectives(populationSize, maxEvolution, algorithm,
                stepUnit, days, dayFrom,
                variablesInit, realValues,
                parameterInit, RLimit,
                country);
        //////// Estimate state 1
        int steps = (int) (days/stepUnit);
        List<List<Double>> Variables = estimateStateFirst(variablesInit, steps, bestParameters, stepUnit);
        ////// Display state 1
        displayStateFirst(Variables, steps, dayFrom, days, realValues);
        /////////////////// State 2 /////////////////////////////////////////////////////
        dayFrom = dayFrom + days; // From dayFrom = 8
        finalStateIndex = 52;
        days = finalStateIndex - days; // number of days is 45
        List<List<Double>> listBestParameter = createListParemeter(bestParameters);
        stateTwo4Objects(populationSize, maxEvolution, algorithm,
                stepUnit, days, dayFrom, Variables, realValues,
                listBestParameter, RLimit);
        /////////////////////////////////////////////////////////////////////////////////
        ///State Final

        dayFrom = dayFrom + days; // From dayFrom = 8 + 45 = 53
        days = infoData.get(0).size() - finalStateIndex; // 55 - 52 = 3
        System.out.println("The size of data is "+ infoData.get(0).size() + "the day in final state is " + days);
        List<List<Double>> finalParameter = createFinalListParemeter(listBestParameter);
        List<Double> init = finalVariable(Variables);
        //List<Double> initP = finalVariable(listBestParameter);
        List<Double> initP = finalParameter();
        List<Double> parameter = stateFinal4Objects(populationSize, maxEvolution, algorithm,
                stepUnit, days, dayFrom,
                init, realValues, initP, RLimit);
        finalParameter.add(parameter);
        listBestParameter.add(parameter);
        /////////Estimate state final
        steps = (int) (days/stepUnit);
        steps = steps + 1;
        List<Double> variablesInitFinal = finalVariable(Variables);
        List<List<Double>> VariablesFinal = estimateStateFirst(variablesInitFinal, steps, parameter, stepUnit);
        VariablesFinal.remove(0);

        //////////////////
        listBestParameter.add(finalParameter());
        updateParameterAll(listBestParameter);
        //////////////////////////////////////////////////////
        storeAll(listBestParameter, populationSize, maxEvolution, country, algorithm);
        System.out.println("Exits");

    }
    public static void storeAll(List<List<Double>> listBestParameter, int populationSize, int maxEvolution,
                                String countryCode, String algorithm){
        DateFormat dateFormat = new SimpleDateFormat(dateForm);
        Date date = new Date();
        System.out.println(dateFormat.format(date));

        Path path = FileSystems.getDefault().getPath("").toAbsolutePath();

        String sidarFolder = linkData+countryCode;
        String nameFolder = dateFormat.format(date);
        //String currentPath = path.toString();
        String directory = sidarFolder +"/" + nameFolder + "_"+algorithm+"_P_"+populationSize+"_Evolution_"+maxEvolution;
        String name = "setParameterDayAll.m";
        utils.updateParameterAll(directory, name, listBestParameter);
        ////////////////// Main simulation
        if (listBestParameter.size()<49){
            storeMFile49(sidarFolder,directory,49);
        }
        else storeMFile54(sidarFolder,directory,54);
        //storeMFile39(sidarFolder,directory);
        ///////////////////////////////////////////////////////////////
        String MOEAfoler = path.toString();
        directory = MOEAfoler;
        utils.updateParameterAll(directory, name, listBestParameter);
        //runScript(sidarFolder,directory);
        ///////////////////////////////////////////////////////////////
    }
    public static List<Double> stateFirst4Objectives (int populationSize, int maxEvolution, String algorithm,
                                           double stepUnit, int days, int dayFrom,
                                           List<Double> variablesInit, List<List<Double>> realValues,
                                           List<Double> parameterInit, List<Double> RLimit,
                                                      String country){
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
        if (dayFrom == 0) updateParameterFile(bestParameters,country);
        updateParameterFile(dayFrom,bestParameters,country);
        return bestParameters;
    }
    public static void updateParameterFile(int start, List<Double> bestParameters, String country){
        String folder = linkData+country;
        Path folderPath = Paths.get(folder);
        if(!Files.exists(folderPath)){
            try {
                Files.createDirectory(folderPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String link = folder+"/setParameterDay"+start+".m";
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
    public static void updateParameterFile(List<Double> bestParameters, String country){
        String folder = linkData+country;
        Path folderPath = Paths.get(folder);
        if(!Files.exists(folderPath)){
            try {
                Files.createDirectory(folderPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String link = folder+"/initParameter.m";
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
    /*
    public static List<double[]> getData(List<List<String>> data){
        List<double[]> infoData = new ArrayList<>();
        double[] totalCases = getInfo(data.get(0).get(totalCaseIndex),data); //% Total Cases
        double[] newCases = getInfo(data.get(0).get(newCaseIndex),data); //% Deaths
        double[] totalDeaths = getInfo(data.get(0).get(totalDeathsIndex),data);
        double[] newDeaths = getInfo(data.get(0).get(newDeathsIndex),data);
        infoData.add(totalCases); //% Total Cases -> //% Deaths -> //% Recovered -> //% Currently Positive
        infoData.add(totalDeaths); //% Deaths

        return infoData;
    }
     */
    public static List<List<Double>> getData2(List<String[]> data){
        List<List<Double>> infoData = new ArrayList<>();
        for(int i = 0; i < data.size(); i++){
            String[] temp = data.get(i);
            List<Double> number = new ArrayList<>();
            for (int j = 0; j < temp.length-dataIndex; j ++){
                number.add(Double.parseDouble(data.get(i)[j+dataIndex]));
            }
            infoData.add(number);
        }
        return infoData;
    }
    /*
    public static double[] getInfo2(String name, List<String[]> data){
        int sizeInfor = data.size() - 1;
        double[] info = new double[sizeInfor];// ignore the header line
        int index = data.get(0).indexOf(name);
        data.remove(0); // remove header;
        for (int i = 0; i < sizeInfor; i++){
            String number = data.get(i).get(index);
            info[i] = Double.parseDouble(number);
        }
        return info;
    }
    */
    public static List<String[]> readData(String codeCountry){
        List<String[]> result = new ArrayList<>();
        String totalCaseNameFile = "data/time_series_covid19_confirmed_global.csv";
        String totalDeathNameFile = "data/time_series_covid19_deaths_global.csv";
        String totalRecoverdNameFile = "data/time_series_covid19_recovered_global.csv";
        List<String> variableNameFile = new ArrayList<>();
        variableNameFile.add(totalCaseNameFile);//% Total Cases -> //% Deaths -> //% Recovered -> //% Currently Positive
        variableNameFile.add(totalDeathNameFile);
        variableNameFile.add(totalRecoverdNameFile);
        for (int i = 0; i < variableNameFile.size(); i++){
            String[] temp = readAFile(codeCountry,variableNameFile.get(i));
            result.add(temp);
        }
        return result;
    }
    public static String[] readAFile(String codeCountry, String fileName){
        String[] content = utils.readFile(fileName).split(NewLine);
        String resultTemp = "";
        for (int i = 0; i < content.length; i++){
            if (content[i].toLowerCase().contains(codeCountry.toLowerCase())){
                resultTemp = content[i];
            }
        }
        String[] result = resultTemp.split(Comma);
        return result;
    }
    public static List<List<String>> readData(String codeCountry, String fileName){
        List<List<String>> totalData = new ArrayList<>();
        //List<String> data = new ArrayList<>();
        /////////////////////
        String[] content = utils.readFile(fileName).split(NewLine);
        for (int i = 0; i < content.length; i++){
            content[i] = content[i].toLowerCase();
        }
        /////////////////////
        String[] headerContent = content[0].split(Comma);
        List<String> header = new ArrayList<>();
        for (int i = 0; i < headerContent.length; i++){
            header.add(headerContent[i]);
        }
        totalData.add(header);
        //////////////////////
        for (int i = 0; i < content.length; i++){
            List<String> tempLine = new ArrayList<>();
            if (content[i].contains(codeCountry.toLowerCase())){
                String[] line = content[i].split(Comma);
                for (int j = 0; j< line.length; j++){
                    tempLine.add(line[j]);
                }
                totalData.add(tempLine);
            }
        }
        //////////////////////
        printListListString(totalData);
        return totalData;
    }
    public static double[] getInfo(String name, List<List<String>> data){
        int sizeInfor = data.size() - 1;
        double[] info = new double[sizeInfor];// ignore the header line
        int index = data.get(0).indexOf(name);
        data.remove(0); // remove header;
        for (int i = 0; i < sizeInfor; i++){
            String number = data.get(i).get(index);
            info[i] = Double.parseDouble(number);
        }
        return info;
    }

    public static List<List<Double>> initRealValues (int population, List<List<Double>> infoData){
        List<List<Double>> initValues = new ArrayList<>();
        for(int i = 0; i < infoData.get(0).size(); i ++){
            List<Double> values = new ArrayList<>();
            values.add(infoData.get(totalCaseIndex).get(i)/population);
                    //+infoData.get(deathsIndex).get(i)
                    //+infoData.get(recoveredIndex).get(i))/population);
            // % Total Case is CasiTotali = Deceduti + Guariti + Positivi
            values.add(infoData.get(recoveredIndex).get(i)/population);// % Recovered is Guariti
            values.add(infoData.get(deathsIndex).get(i)/population);// % Deaths is Deceduti
            values.add((infoData.get(totalCaseIndex).get(i)
                    -infoData.get(deathsIndex).get(i)
                    -infoData.get(recoveredIndex).get(i))/population); ;// % Currently Positive is Positivi
            initValues.add(values);
        }
        return initValues;
    }
    public static void updateParameterAll(List<List<Double>> bestParameters){
        String link = linkData+"setParameterDayAll.m";
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
        String link = linkData+"setParameterDay"+start+".m";
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
    public static List<String> prepareListFile(int code){
        List<String> listFile = new ArrayList<>();
        listFile.add("Sidarthe_Simulation_Dung"+code+".m");
        listFile.add("calculate1.m");
        listFile.add("calculate2.m");
        listFile.add("calParameter.m");
        listFile.add("run.m");
        listFile.add("run.sh");
        listFile.add("initParameter.m");
        listFile.add("totalCase.m");
        listFile.add("totalDeath.m");
        listFile.add("totalRecovered.m");
        return listFile;
    }
    public static void storeMFile49 (String sidarFolder, String directory, int code){
        List<String> listFile = prepareListFile(code);
        for (int i = 0; i < listFile.size(); i++){
            String sidarSimulationName = listFile.get(i);
            String sidarSimulationLink = sidarFolder + "/" +sidarSimulationName;
            String sidarSimulationLinkStore = directory + "/" +sidarSimulationName;
            String sidarSimulationContent = utils.readFile(sidarSimulationLink);
            sida.utils.writeFile.writeString(sidarSimulationLinkStore,sidarSimulationContent);
        }
    }
    public static void storeMFile54 (String sidarFolder, String directory, int code){
        List<String> listFile = prepareListFile(code);
        for (int i = 0; i < listFile.size(); i++){
            String sidarSimulationName = listFile.get(i);
            String sidarSimulationLink = sidarFolder + "/" +sidarSimulationName;
            String sidarSimulationLinkStore = directory + "/" +sidarSimulationName;
            String sidarSimulationContent = utils.readFile(sidarSimulationLink);
            sida.utils.writeFile.writeString(sidarSimulationLinkStore,sidarSimulationContent);
        }
    }
}
