package sida.utils;

import draft.utils.writeFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.apache.commons.math3.util.Precision.round;

public class utils {
    public static void updateParameterAll(String directory, String name, List<List<Double>> bestParameters){
        String link = directory + "/" + name;
        System.out.println("The file is store at: "+ link);
        Path directoryPath = Paths.get(directory);
        if(!Files.exists(directoryPath)) {
            try {
                Files.createDirectory(directoryPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    public static String readFile(String fileName) {
        String sCurrentLine = "nothing";
        String content = "";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((sCurrentLine = br.readLine()) != null) {
                content = content + sCurrentLine + "\n" ;
//	                             return sCurrentLine;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;//sCurrentLine;
    }
    public static void printListString (List<String> list){
        String value = "";
        for (int i = 0; i < list.size(); i++){
            value = value + list.get(i) + "\t"+ "\t";
        }
        System.out.println(value);
    }
    public static void printListListString (List<List<String>> list){
        String value = "";
        for (int i = 0; i < list.size(); i++){
            printListString(list.get(i));
        }
    }
}
