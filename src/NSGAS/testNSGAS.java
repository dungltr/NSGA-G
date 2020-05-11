package NSGAS;

import NSGAV.GeneratorLatexTable;
import NSGAV.ReadFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static NSGAV.TestNSGA.*;

public class testNSGAS {
    static int population = 100;// = Ask("Population",100);
    static int  maxEvaluate = population*100;// = Ask("maxEvaluate",population*10);
    static int  runSeed = 50;// = Ask("RunSeed", 20);
    static int  sbx = 30;// = Ask("sbx",30);
    static int pm = 20;// = Ask("pm", 20);

    public static void testNSGAS() throws IOException {
        int useDefault = Ask("Do you want to use a default setting",0);
        List<String> Problems = new ArrayList<>();
        List<String> Algorithms = new ArrayList<>();
        if (useDefault == 0){
            Problems = AskProblem();
            printList(Problems);
            Algorithms = AskAlgorithms();
            System.out.println("All algorithms is: ");
            printList(Algorithms);
            population = Ask("Population",100);
            maxEvaluate = Ask("maxEvaluate",population*10);
            runSeed = Ask("RunSeed", 50);
            sbx = Ask("sbx",30);
            pm = Ask("pm", 20);
        }
        else {
            for(int i = 15; i <= 20; i++){
                for(int j = 1; j <= 2; j++){
                    Problems.add("DTLZ"+ (2*j-1) +"_"+i);
                }
                for(int j = 1; j <=2; j++){
                    Problems.add("WFG"+ (2*j-1) +"_"+i);
                }
                //for(int j = 1; j <= 3; j++){
                //    Problems.add("LZ"+ j +"_"+i);
                //}
            }
            System.out.println("All problems are: ");
            printList(Problems);
            Algorithms.add("NSGAII");
            Algorithms.add("NSGAIII");
            Algorithms.add("NSGAG");
            Algorithms.add("MOEAD");
            //Algorithms.add("eMOEA");
            //Algorithms.add("PESA2");
            //Algorithms.add("NSGAR");
            Algorithms.add("NSGAS");
            System.out.println("All algorithms are: ");
            printList(Algorithms);
        }

        String[] algorithms = new String [Algorithms.size()];// Algorithms.toArray();
        for (int i = 0; i<algorithms.length;i++){
            algorithms[i] = Algorithms.get(i);
        }
        List<String> Standards = new ArrayList<>();
        Standards.add("Generational Distance");
        //Standards.add("Hyper volume");
        Standards.add("Inverted Generational Distance");
        Standards.add("Maximum Pareto Front Error");
        String Caption = "";
        GregorianCalendar gcalendar = new GregorianCalendar();
        int year =  gcalendar.get(Calendar.YEAR);
        int month = gcalendar.get(Calendar.MONTH);
        int date = gcalendar.get(Calendar.DATE);
        int hour = gcalendar.get(Calendar.HOUR);
        int min = gcalendar.get(Calendar.MINUTE);

        String dateOfYear = year + "_" + month + "_" + date + "-" + hour + "_" + min;

        String homeFile = ReadFile.readhome("HOME_jMetalData")
                +"/"+ dateOfYear + "MOEA_"+Problems.get(0)+"_"+Problems.get(Problems.size()-1)
                +population+"P"+maxEvaluate+"M"+runSeed+"N"
                +algorithms.length+"A";
        System.out.println(homeFile);
        for (int i=0; i<Standards.size(); i++){
            Caption = Standards.get(i);
            StandardDistance(homeFile, Problems, algorithms, Caption);
            GeneratorLatexTable.GeneratorComputeTimeToLatex(homeFile, Caption, Problems, algorithms);
        }
    }
    public static void main(String args[]) throws IOException {
        testNSGAS();
    }
}
