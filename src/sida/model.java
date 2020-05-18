package sida;

import java.util.ArrayList;
import java.util.List;

public class model {
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
    // number of parameter
    private static final int parMax = 16;

    private static final int Orizzonte = 350;
    private static final double Step = 0.01;
    private static final int RowSize = 10;
    private static final int ColumnSize = 10;

    private static final long population = 60000000;

    private static final int SIndex = 0;
    private static final int IIndex = 1;
    private static final int DIndex = 2;
    private static final int AIndex = 3;
    private static final int RIndex = 4;
    private static final int TIndex = 5;
    private static final int HIndex = 6;
    private static final int EIndex = 7;

    private static final int HDiagnosticatiIndex = 8;
    private static final int InfetitiRealiIndex = 9;
    private static final int MIndex = 10;
    private static final int PIndex = 11;

    public static void main(String[] args){
        test();
    }
    public static void test(){
        List<Double> parameter = initParameter();
        List<Double> r = calculateParameter(parameter);
        List<Double> variablesInit = initVariables(population);
        List<Double> vectorsInit = initVectors(population);
        List<List<Double>> variables = new ArrayList<>();
        List<List<Double>> vectors = new ArrayList<>();
        variables.add(variablesInit);
        vectors.add(vectorsInit);
        int days = initDays(Orizzonte);
        days = 3;
        double stepUnit = initStep(Step);
        int steps = (int) (days/stepUnit);
        for (int i = 0; i < steps; i++){
            List<List<Double>> arrayVariables = computerVariables (variables, parameter, stepUnit);
            //variables.add(arrayVariables.get(arrayVariables.size()-1));

            List<List<Double>> arrayVectors = updateVectors(vectors, arrayVariables, parameter);
            //vectors.add(arrayVectors.get(arrayVectors.size()-1));
        }
        System.out.println("Stop");
    }
    public static List<Double> initVariables (long population){
        List<Double> Vectors = new ArrayList<>();
        double I = 200.00/population;
        double D = 20.00/population;
        double A = 1.00/population;
        double R = 2.00/population;
        double T = 0.00;
        double H = 0.00;
        double E = 0.00;
        double HDiagnosticati = 0.00;
        double InfettiReali = I+D+A+R+T;
        Vectors.add(1-I-D-A-R-T-H-E);//     S
        Vectors.add(I);//                   I
        Vectors.add(D);//                   D
        Vectors.add(A);//                   A
        Vectors.add(R);//                   R
        Vectors.add(T);//                   T
        Vectors.add(H);//                   H
        Vectors.add(E);//                   E
        Vectors.add(HDiagnosticati);//      H_diagnosticati
        Vectors.add(InfettiReali);//        Infetti_reali
        return Vectors;
    }
    public static List<Double> initVectors (long population){
        List<Double> Vectors = new ArrayList<>();
        double I = 200.00/population;
        double D = 20.00/population;
        double A = 1.00/population;
        double R = 2.00/population;
        double T = 0.00;
        double H = 0.00;
        double E = 0.00;
        double HDiagnosticati = 0.00;
        double InfettiReali = I+D+A+R+T;
        Vectors.add(1-I-D-A-R-T-H-E);//     S
        Vectors.add(I);//                   I
        Vectors.add(D);//                   D
        Vectors.add(A);//                   A
        Vectors.add(R);//                   R
        Vectors.add(T);//                   T
        Vectors.add(H);//                   H
        Vectors.add(E);//                   E
        Vectors.add(HDiagnosticati);//      H_diagnosticati
        Vectors.add(InfettiReali);//        Infetti_reali
        Vectors.add(0.0);//                 M
        Vectors.add(0.0);//                 P
        return Vectors;
    }
    public static List<Double> initParameter(){
        List<Double> parameter = new ArrayList<>();
        parameter.add(alfa);
        parameter.add(beta);
        parameter.add(gamma);
        parameter.add(delta);
        parameter.add(epsilon);
        parameter.add(theta);
        parameter.add(zeta);
        parameter.add(eta);
        parameter.add(mu);
        parameter.add(nu);
        parameter.add(tau);
        parameter.add(lambda);
        parameter.add(rho);
        parameter.add(kappa);
        parameter.add(xi);
        parameter.add(sigma);
        return parameter;
    }
    public static List<Double> finalParameter(){
        List<Double> parameter = new ArrayList<>();
        parameter.add(0.21);
        parameter.add(0.005);
        parameter.add(0.11);
        parameter.add(0.005);
        parameter.add(0.2);
        parameter.add(0.3705);
        parameter.add(0.025);
        parameter.add(0.025);
        parameter.add(0.008);
        parameter.add(0.015);
        parameter.add(0.01);
        parameter.add(0.08);
        parameter.add(0.02);
        parameter.add(0.02);
        parameter.add(0.02);
        parameter.add(0.01);
        return parameter;
    }
    public static int initDays(int Orizzonte){
        return Orizzonte;
    }
    public static double initStep(double Step){
        return Step;
    }
    public static List<Double> calculateParameter(List<Double> parameter){
        List<Double> R = new ArrayList<>();
        R.add(parameter.get(epsilonIndex)
                +parameter.get(zetaIndex)
                +parameter.get(lambdaIndex));
        R.add(parameter.get(etaIndex)
                +parameter.get(rhoIndex));
        R.add(parameter.get(thetaIndex)
                +parameter.get(muIndex)
                +parameter.get(kappaIndex));
        R.add(parameter.get(nuIndex)
                +parameter.get(xiIndex));
        R.add(parameter.get(sigmaIndex)
                +parameter.get(tauIndex));
        return R;
    }
    public static double calculate1(List<Double> parameter){
        List<Double> r = calculateParameter(parameter);
        double r1 = r.get(0);
        double r2 = r.get(1);
        double r3 = r.get(2);
        double r4 = r.get(3);
        double r5 = r.get(4);
        double temp = parameter.get(alfaIndex)/r1
                +parameter.get(betaIndex)*parameter.get(epsilonIndex)/(r1*r2)
                +parameter.get(gammaIndex)*parameter.get(zetaIndex)/(r1*r3)
                +parameter.get(deltaIndex)*parameter.get(etaIndex)*parameter.get(epsilonIndex)/(r1*r2*r4)
                +parameter.get(deltaIndex)*parameter.get(zetaIndex)*parameter.get(thetaIndex)/(r1*r3*r4);
        return temp;
    }
    public static double calculate2(List<Double> parameter){
        List<Double> r = calculateParameter(parameter);
        double r1 = r.get(0);
        double r2 = r.get(1);
        double r3 = r.get(2);
        double r4 = r.get(3);
        double r5 = r.get(4);
        double temp = (parameter.get(alfaIndex)*r2*r3*r4+parameter.get(epsilonIndex)*parameter.get(betaIndex)*r3*r4
                +parameter.get(gammaIndex)*parameter.get(zetaIndex)*r2*r4
                +parameter.get(deltaIndex)*parameter.get(etaIndex)*parameter.get(epsilonIndex)*r3
                +parameter.get(deltaIndex)*parameter.get(zetaIndex)*parameter.get(thetaIndex)*r2)/(r1*r2*r3*r4);
        return temp;
    }
    public static double[][] updateMatrix(List<Double> parameter, List<Double> finalValue){
        double[][] B = new double[RowSize][ColumnSize];
        double x2 = finalValue.get(1);
        double x3 = finalValue.get(2);
        double x4 = finalValue.get(3);
        double x5 = finalValue.get(4);

        B[0][0] = 0-parameter.get(alfaIndex)*x2
                -parameter.get(betaIndex)*x3
                -parameter.get(gammaIndex)*x4
                -parameter.get(deltaIndex)*x5;
        B[1][0] = parameter.get(alfaIndex)*x2
                +parameter.get(betaIndex)*x3
                +parameter.get(gammaIndex)*x4
                +parameter.get(deltaIndex)*x5;

        B[1][1] = 0 - (parameter.get(epsilonIndex)+parameter.get(zetaIndex)+parameter.get(lambdaIndex));
        B[2][1] = parameter.get(epsilonIndex);
        B[2][2] = 0 - (parameter.get(etaIndex)+parameter.get(rhoIndex));
        B[3][1] = parameter.get(zetaIndex);
        B[3][3] = 0 - (parameter.get(thetaIndex) + parameter.get(muIndex) + parameter.get(kappaIndex));
        B[4][2] = parameter.get(etaIndex);
        B[4][3] = parameter.get(thetaIndex);
        B[4][4] = 0 - (parameter.get(nuIndex) + parameter.get(xiIndex));
        B[5][3] = parameter.get(muIndex);
        B[5][4] = parameter.get(nuIndex);
        B[5][5] = 0- (parameter.get(sigmaIndex) + parameter.get(tauIndex));
        B[6][1] = parameter.get(lambdaIndex);
        B[6][2] = parameter.get(rhoIndex);
        B[6][3] = parameter.get(kappaIndex);
        B[6][4] = parameter.get(xiIndex);
        B[6][5] = parameter.get(sigmaIndex);
        B[7][5] = parameter.get(tauIndex);
        B[8][2] = parameter.get(rhoIndex);
        B[8][4] = parameter.get(xiIndex);
        B[8][5] = parameter.get(sigmaIndex);
        B[9][0] = parameter.get(alfaIndex)*x2
                +parameter.get(betaIndex)*x3
                +parameter.get(gammaIndex)*x4
                +parameter.get(deltaIndex)*x5;
        return B;
    }
    public static List<Double> finalVariable (List<List<Double>> variables) {
        List<Double> values = new ArrayList<>();
        for (int i = 0; i < variables.get(0).size(); i++){
            values.add(variables.get(variables.size()-1).get(i));
        }
        return values;
    }
    public static List<Double> indexVariable (List<List<Double>> variables, int index) {
        List<Double> values = new ArrayList<>();
        for (int i = 0; i < variables.get(0).size(); i++){
            values.add(variables.get(index).get(i));
        }
        return values;
    }
    public static List<Double> deltaVariables (double[][] B, List<Double> lastVariables){
        List<Double> nextValues = new ArrayList<>();
        if (B[0].length == lastVariables.size()){
            for (int i = 0; i < B.length; i ++){
                double next = 0;
                for (int j = 0; j < lastVariables.size(); j ++){
                    next = next + B[i][j]*lastVariables.get(j);
                }
                nextValues.add(next);
            }
        }
        return nextValues;
    }

    public static List<List<Double>> computerVariables (List<List<Double>> oldVariables,
                                                        List<Double> parameter,
                                                        double step){
        List<List<Double>> values = oldVariables;
        List<Double> lastVariables = finalVariable(values);
        double[][] B = updateMatrix(parameter, lastVariables);
        List<Double> deltaVariables = deltaVariables(B,lastVariables);
        List<Double> nextVariables = new ArrayList<>();
        for (int i = 0; i < lastVariables.size(); i++){
            nextVariables.add(lastVariables.get(i)+deltaVariables.get(i)*step);
        }
        values.add(nextVariables);
        return values;
    }
    public static List<List<Double>> updateVectors(List<List<Double>> vectors,
                                                   List<List<Double>> variables,
                                                   List<Double> parameter){
        List<List<Double>> Listvectors = vectors;
        //for(i = 0; i < variables.get(0).size(); i++){
        //    Listvectors.get(Listvectors.size()-1).add(variables.get(variables.size()-1).get(i));
            //vectors.get(i).add(variables.get(i).get(variables.get(0).size()-1));
        //}
        List<Double> tempList = new ArrayList<>();
        for(int i = 0; i < variables.get(variables.size()-1).size(); i ++){
            double temp = variables.get(variables.size()-1).get(i);
            tempList.add(temp);
            //Listvectors.get(Listvectors.size()-1).add(temp);
        }
        Listvectors.add(tempList);
        double E = variables.get(variables.size()-1).get(EIndex);
        double M =  E /
                (vectors.get(0).get(SIndex)
                -vectors.get(vectors.size()-1).get(SIndex));
        //P(i)=E(i)/((epsilon*r3+(theta+mu)*zeta)*(I(1)+S(1)-I(i)-S(i))/(r1*r3)+(theta+mu)*(A(1)-A(i))/r3);
        List<Double> r = calculateParameter(parameter);
        double r3 = r.get(2);
        double r1 = r.get(0);
        double deltaTemp = vectors.get(0).get(IIndex)
                + vectors.get(0).get(SIndex)
                - vectors.get(vectors.size()-1).get(SIndex)
                - vectors.get(vectors.size()-1).get(IIndex);
        double deltaTempA = vectors.get(0).get(AIndex) - vectors.get(vectors.size()-1).get(AIndex);
        double T2 = parameter.get(epsilonIndex)*r3;
        double T3 = (parameter.get(thetaIndex)
                + parameter.get(muIndex))*parameter.get(zetaIndex);
        double T4 = T2 + T3;
        double T5 = T4 * deltaTemp;
        double T1 = T5 / (r1 * r3);
        double P = E / (T1
                + (parameter.get(thetaIndex) + parameter.get(muIndex))
                * (deltaTempA)
                / r3);
        Listvectors.get(Listvectors.size()-1).add(M);
        Listvectors.get(Listvectors.size()-1).add(P);
        return Listvectors;
    }
    public static List<Double> realValues (List<Double> finalVectors){
        List<Double> estimateValues = new ArrayList<>();
        // CasiTotali = D+R+T+E+H_diagnosticati
        double estimateCasiTotali = finalVectors.get(DIndex)
                + finalVectors.get(RIndex)
                + finalVectors.get(TIndex)
                + finalVectors.get(EIndex)
                + finalVectors.get(HDiagnosticatiIndex);
        // Guariti = H_diagnosticati
        double estimateGuariti = finalVectors.get(HDiagnosticatiIndex);
        // Deceduti = E
        double estimateDeceduti = finalVectors.get(EIndex);
        // Positivi = D+R+T
        double estimatePositivi = finalVectors.get(DIndex)
                + finalVectors.get(RIndex)
                + finalVectors.get(TIndex);
        // Isolamento_domiciliare = D
        double estimateIsolamentoDomiciliare = finalVectors.get(DIndex);
        // Ricoverati_sintomi = R
        double estimateRicoveratiSintomi = finalVectors.get(RIndex);
        // Terapia_intensiva = T
        double estimateTerapiaIntensiva = finalVectors.get(TIndex);
        estimateValues.add(estimateCasiTotali);
        estimateValues.add(estimateGuariti);
        estimateValues.add(estimateDeceduti);
        estimateValues.add(estimatePositivi);
        estimateValues.add(estimateIsolamentoDomiciliare);
        estimateValues.add(estimateRicoveratiSintomi);
        estimateValues.add(estimateTerapiaIntensiva);
        return estimateValues;
    }
    public static List<Double> realValues4Objectives (List<Double> finalVectors){
        List<Double> estimateValues = new ArrayList<>();
        // CasiTotali = D+R+T+E+H_diagnosticati
        double estimateCasiTotali = finalVectors.get(DIndex)
                + finalVectors.get(RIndex)
                + finalVectors.get(TIndex)
                + finalVectors.get(EIndex)
                + finalVectors.get(HDiagnosticatiIndex);
        // Guariti = H_diagnosticati
        double estimateGuariti = finalVectors.get(HDiagnosticatiIndex);
        // Deceduti = E
        double estimateDeceduti = finalVectors.get(EIndex);
        // Positivi = D+R+T
        double estimatePositivi = finalVectors.get(DIndex)
                + finalVectors.get(RIndex)
                + finalVectors.get(TIndex);
        // Isolamento_domiciliare = D
        //double estimateIsolamentoDomiciliare = finalVectors.get(DIndex);
        // Ricoverati_sintomi = R
        //double estimateRicoveratiSintomi = finalVectors.get(RIndex);
        // Terapia_intensiva = T
        //double estimateTerapiaIntensiva = finalVectors.get(TIndex);
        estimateValues.add(estimateCasiTotali);
        estimateValues.add(estimateGuariti);
        estimateValues.add(estimateDeceduti);
        estimateValues.add(estimatePositivi);
        //estimateValues.add(estimateIsolamentoDomiciliare);
        //estimateValues.add(estimateRicoveratiSintomi);
        //estimateValues.add(estimateTerapiaIntensiva);
        return estimateValues;
    }
}
