package sida;

import static sida.ExperimentPeriod.runScript;

public class test {
    private static String linkData = "/home/ubuntu/sidarthe/country/";
    private static String countryCode = "kaz";
    public static void main(String[] args){
        System.out.println("Hello");
        String sidarFolder = linkData+countryCode;
        String directory = sidarFolder +"/" + "2020_05_15_16_18_48_NSGAS_P_100_Evolution_100";
        runScript(sidarFolder,directory);
    }
}
