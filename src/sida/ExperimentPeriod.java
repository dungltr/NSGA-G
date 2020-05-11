package sida;

import sida.utils.*;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static sida.Experiment.*;
import static sida.NSGA.initRealValues;
import static sida.model.finalParameter;
import static sida.model.finalVariable;

public class ExperimentPeriod {
    private static String dateForm = "yyyy_MM_dd_HH_mm_ss";
    private static int populationSize = 100;
    private static int maxEvolution = 1000000;
    private static int finalStateIndex = 38;
    public static void main(String[] args){
        List<String> algorithms = new ArrayList<>();
        algorithms.add("NSGAS");
        algorithms.add("NSGAII");
        //algorithms.add("NSGAG");
        //algorithms.add("NSGAR");

        //algorithms.add("NSGAIV");
        //algorithms.add("NSGAIII");
        System.out.println("Hello");
        for (int k = 0; k < 5; k++){
            for (int i = 100; i < 10000; i= i*10){
                for (int j = 0; j < algorithms.size(); j++) {
                    System.out.println("Experiment with population"+ i + " and maxEvolution " + maxEvolution);
                    experimentPeriod(i, maxEvolution, algorithms.get(j));
                    //experiment1(i, maxEvolution, algorithms.get(j));
                }
            }
        }

        //storeAll();
    }
    public static void experimentPeriod (int populationSize, int maxEvolution, String algorithm){
        int population = 60000000;
        double stepUnit = 0.01;
        int days = 3;
        int dayFrom = 0;
        List<Double> RLimit = new ArrayList<>();
        RLimit.add(5.0);
        RLimit.add(5.0);
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
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        dayFrom = dayFrom + days; // From dayFrom = 3
        finalStateIndex = 38;
        days = finalStateIndex - days; // number of days is 35
        List<List<Double>> listBestParameter = createListParemeter(bestParameters);
        stateTwo(populationSize, maxEvolution, algorithm,
                stepUnit, days, dayFrom, Variables, realValues,
                listBestParameter, RLimit);
        //
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // State final
        ///////////////////////////////////////////////////////
        dayFrom = dayFrom + days; // From dayFrom = 3 + 35 = 38
        days = CasiTotali.length - finalStateIndex; // 46 - 38 = 8
        //days = 2;
        List<List<Double>> finalParameter = createFinalListParemeter(listBestParameter);
        List<Double> init = finalVariable(Variables);
        //List<Double> initP = finalVariable(listBestParameter);
        List<Double> initP = finalParameter();
                List<Double> parameter = stateFinal(populationSize, maxEvolution, algorithm,
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
        //////////////////////////////////////////////////////
        listBestParameter.add(finalParameter());
        updateParameterAll(listBestParameter);
        //////////////////////////////////////////////////////
        storeAll(listBestParameter, populationSize, maxEvolution, algorithm);
        System.out.println("Exits");
    }
    public static void storeAll(List<List<Double>> listBestParameter, int populationSize, int maxEvolution, String algorithm){
        DateFormat dateFormat = new SimpleDateFormat(dateForm);
        Date date = new Date();
        System.out.println(dateFormat.format(date));

        Path path = FileSystems.getDefault().getPath("").toAbsolutePath();

        String sidarFolder = "/home/ubuntu/sidarthe";
        String nameFolder = dateFormat.format(date);
        //String currentPath = path.toString();
        String directory = sidarFolder +"/" + nameFolder + "_"+algorithm+"_P_"+populationSize+"_Evolution_"+maxEvolution;
        String name = "setParameterDayAll.m";
        utils.updateParameterAll(directory, name, listBestParameter);
        ////////////////// Main simulation
        if (listBestParameter.size()<42){
            storeMFile39(sidarFolder,directory);
        }
        else storeMFile45(sidarFolder,directory);
        //storeMFile39(sidarFolder,directory);
        ///////////////////////////////////////////////////////////////
        String MOEAfoler = path.toString();
        directory = MOEAfoler;
        utils.updateParameterAll(directory, name, listBestParameter);
        storeMFile39(sidarFolder,directory);
        runScript(sidarFolder,directory);
        ///////////////////////////////////////////////////////////////
    }
    public static void storeMFile39 (String sidarFolder, String directory){
        List<String> listFile = new ArrayList<>();
        listFile.add("Sidarthe_Simulation_Dung39.m");
        listFile.add("calculate1.m");
        listFile.add("calculate2.m");
        listFile.add("calParameter.m");
        listFile.add("run.m");
        listFile.add("run.sh");
        listFile.add("initParameter.m");
        for (int i = 0; i < listFile.size(); i++){
            String sidarSimulationName = listFile.get(i);
            String sidarSimulationLink = sidarFolder + "/" +sidarSimulationName;
            String sidarSimulationLinkStore = directory + "/" +sidarSimulationName;
            String sidarSimulationContent = utils.readFile(sidarSimulationLink);
            sida.utils.writeFile.writeString(sidarSimulationLinkStore,sidarSimulationContent);
        }
    }
    public static void storeMFile45 (String sidarFolder, String directory){
        List<String> listFile = new ArrayList<>();
        listFile.add("Sidarthe_Simulation_Dung45.m");
        listFile.add("calculate1.m");
        listFile.add("calculate2.m");
        listFile.add("calParameter.m");
        listFile.add("run.m");
        listFile.add("run.sh");
        listFile.add("initParameter.m");
        for (int i = 0; i < listFile.size(); i++){
            String sidarSimulationName = listFile.get(i);
            String sidarSimulationLink = sidarFolder + "/" +sidarSimulationName;
            String sidarSimulationLinkStore = directory + "/" +sidarSimulationName;
            String sidarSimulationContent = utils.readFile(sidarSimulationLink);
            sida.utils.writeFile.writeString(sidarSimulationLinkStore,sidarSimulationContent);
        }
    }
    public static void runScript(String sidarFolder, String directory){
        String sidarSimulationName = "run.sh";
        String sidarSimulationMFile = "run.m";
        String sidarSimulationLinkStore = directory + "/" +sidarSimulationName;
        String command = utils.readFile(sidarSimulationLinkStore);
        System.out.println("Call script in "+ directory);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", "cd " + directory);
        processBuilder.command("bash", "-c", "chmod +x "+ directory+"/run.sh");
        command = command.replace(sidarSimulationMFile, directory+"/"+sidarSimulationMFile);
        System.out.println("The command is "+ command);
        processBuilder.command("bash", "-c", command);
    }
}
