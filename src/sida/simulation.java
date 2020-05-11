package sida;

import java.util.ArrayList;
import java.util.List;

public class simulation {
    // Transmission rate due to contacts with UNDETECTED asymptomatic infected
    private static final double alfa = 0.57;
    private static final int alfaIndex = 0;
    // Transmission rate due to contacts with DETECTED asymptomatic infected
    private static final double beta=0.0114;
    private static final int betaIndex = 1;
    // Transmission rate due to contacts with UNDETECTED symptomatic infected
    private static final double gamma=0.456;
    private static final int gammaIndex = 2;
    // Transmission rate due to contacts with DETECTED symptomatic infected
    private static final double delta=0.0114;
    private static final int deltaIndex = 3;
    // Detection rate for ASYMPTOMATIC
    private static final double epsilon=0.171;
    private static final int epsilonIndex = 4;
    // Detection rate for SYMPTOMATIC
    private static final double theta=0.3705;
    private static final int thetaIndex = 5;
    // Worsening rate: UNDETECTED asymptomatic infected becomes symptomatic
    private static final double zeta=0.1254;
    private static final int zetaIndex = 6;
    // Worsening rate: DETECTED asymptomatic infected becomes symptomatic
    private static final double eta=0.1254;
    private static final int etaIndex = 7;
    // Worsening rate: UNDETECTED symptomatic infected develop life-threatening symptoms
    private static final double mu=0.0171;
    private static final int muIndex = 8;
    // Worsening rate: DETECTED symptomatic infected develop life-threatening symptoms
    private static final double nu=0.0274;
    private static final int nuIndex = 9;
    // Mortality rate for infected with life-threatening symptoms
    private static final double tau=0.01;
    private static final int tauIndex = 10;
    // Recovery rate for undetected asymptomatic infected
    private static final double lambda=0.0342;
    private static final int lambdaIndex = 11;
    // Recovery rate for detected asymptomatic infected
    private static final double rho=0.0342;
    private static final int rhoIndex = 12;
    // Recovery rate for undetected symptomatic infected
    private static final double kappa=0.0171;
    private static final int kappaIndex = 13;
    // Recovery rate for detected symptomatic infected
    private static final double xi=0.0171;
    private static final int xiIndex = 14;
    // Recovery rate for life-threatened symptomatic infected
    private static final double sigma=0.0171;
    private static final int sigmaIndex = 15;
    private static final int MIndex = 10;
    private static final int PIndex = 11;

    private static final int dayState0 = 4;
    private static final int dayState1 = 12;
    private static final int dayState2 = 22;
    private static final int dayState3 = 28;
    private static final int dayState4 = 38;
    private static final int dayStateFinal = 350;
    public static void main (String[] args){
        sidatheSimulation ();
        System.out.println("Stop");
    }
    public static List<Double> day38(List<Double> parameter){
        List<Double> Parameter = parameter;
        Parameter.set(epsilonIndex,0.2);
        Parameter.set(rhoIndex,0.02);
        Parameter.set(kappaIndex,0.02);
        Parameter.set(xiIndex,0.02);
        Parameter.set(sigmaIndex,0.01);

        Parameter.set(zetaIndex,0.025);
        Parameter.set(etaIndex,0.025);
        return Parameter;
    }
    public static List<Double> day28(List<Double> parameter){
        List<Double> Parameter = parameter;
        Parameter.set(alfaIndex,0.21);
        Parameter.set(gammaIndex,0.11);
        return Parameter;
    }
    public static List<Double> day22(List<Double> parameter){
        List<Double> Parameter = parameter;
        Parameter.set(alfaIndex,0.36);
        Parameter.set(betaIndex,0.005);
        Parameter.set(gammaIndex,0.2);
        Parameter.set(deltaIndex,0.005);

        Parameter.set(muIndex,0.008);
        Parameter.set(nuIndex,0.015);

        Parameter.set(zetaIndex,0.034);
        Parameter.set(etaIndex,0.034);

        Parameter.set(lambdaIndex,0.08);
        Parameter.set(rhoIndex,0.0171);
        Parameter.set(kappaIndex,0.0171);
        Parameter.set(xiIndex,0.0171);
        Parameter.set(sigmaIndex,0.0171);
        return Parameter;
    }
    public static List<Double> day12(List<Double> parameter){
        List<Double> Parameter = parameter;
        Parameter.set(epsilonIndex,0.1425);
        return Parameter;
    }
    public static List<Double> day4(List<Double> parameter){
        List<Double> Parameter = parameter;
        Parameter.set(alfaIndex,0.4218);
        Parameter.set(gammaIndex,0.285);
        Parameter.set(betaIndex,0.0057);
        Parameter.set(deltaIndex,0.0057);
        return Parameter;
    }
    public static List<List<Double>> sidatheSimulation (){
        long population = 60000000;
        double Step = 0.01;
        int Days = 350;
        int days = model.initDays(Days);
        List<Double> parameter = model.initParameter();
        //List<Double> r = model.calculateParameter(parameter);
        //List<Double> parameter = new ArrayList<>();
        List<Double> variablesInit = model.initVariables(population);
        List<Double> vectorsInit = model.initVectors(population);
        List<List<Double>> variables = new ArrayList<>();
        List<List<Double>> vectors = new ArrayList<>();
        variables.add(variablesInit);
        vectors.add(vectorsInit);
        double step = model.initStep(Step);
        first6common(vectors,parameter,step);
        int state = 5;
        Days = dayStateFinal - dayState4;
        periodCommom(vectors,parameter,step,Days,state);
        ////////////////////////////////////////////////
        List<Double> x = vectors.get(vectors.size()-2);
        return vectors;
    }
    public static List<List<Double>> first6common (List<List<Double>> vectors, List<Double> parameter, double step){
        // Normal //////////////////////////////////
        int state = 0;
        int Days = dayState0;
        periodCommom(vectors,parameter,step,Days,state);
        ////////////////////////////////////////////
        // Basic social distancing (awareness, schools closed)
        state ++;//1
        Days = dayState1 - dayState0;
        periodCommom(vectors,parameter,step,Days,state);
        ////////////////////////////////////////////
        // Screening limited to / focused on symptomatic subjects
        state ++;//2
        Days = dayState2 - dayState1;
        periodCommom(vectors,parameter,step,Days,state);
        ///////////////////////////////////////////
        // % Social distancing: lockdown, mild effect
        state ++;//3
        Days = dayState3 - dayState2;
        periodCommom(vectors,parameter,step,Days,state);
        //  % Social distancing: lockdown, strong effect
        state ++;//4
        Days = dayState4 - dayState3;
        periodCommom(vectors,parameter,step,Days,state);
        //
        return vectors;
    }
    public static List<List<Double>> periodCommom(List<List<Double>> vectors, List<Double> parameter, double step, int days, int state){
        List<Double> Parameter = parameter;
        switch (state) {
            case 0: System.out.println("Case 0!");
                break;
            case 1: Parameter = day4(parameter);
                break;
            case 2: Parameter = day12(parameter);
                break;
            case 3: Parameter = day22(parameter);
                break;
            case 4: Parameter = day28(parameter);
                break;
            case 5: Parameter = day38(parameter);
                break;
            default : break;
        }
        int steps = (int) (days/step);
        if (state==0) steps = steps - 1;
        List<List<Double>> variables = getVariable (vectors);
        for (int i = 0; i < steps; i++){
            List<List<Double>> arrayVariables = model.computerVariables (variables, Parameter, step);
            //variables.add(arrayVariables.get(arrayVariables.size()-1));
            List<List<Double>> arrayVectors = model.updateVectors(vectors, arrayVariables, Parameter);
            //vectors.add(arrayVectors.get(arrayVectors.size()-1));
        }
        return vectors;
    }
    public static List<List<Double>> getVariable (List<List<Double>> vectors){
        List<List<Double>> variables = new ArrayList<>();
        for (int i = 0; i < vectors.size(); i++){
            List<Double> temp = new ArrayList<>();
            for (int j = 0; j < MIndex; j++){
                temp.add(vectors.get(i).get(j));
            }
            variables.add(temp);
        }
        return variables;
    }
}
