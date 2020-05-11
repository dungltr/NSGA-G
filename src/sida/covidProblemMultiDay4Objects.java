package sida;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.problem.AbstractProblem;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.math3.util.FastMath.abs;
import static sida.model.*;

public class covidProblemMultiDay4Objects extends AbstractProblem {
    ////// Parameter
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
    /////////////////////////////
    /**
     * The number of variables defined by this problem.
     *         int numberOfVariables = initVariables.size();
     *         int numberOfObjectives = realFinalValues.size();
     *         int numberOfConstraints = 1;
     */
    public static int numberOfVariables;
    public static int numberOfObjectives;
    public static int numberOfConstraints;
    public static double step;
    public static int days;
    public static int start;
    public static List<Double> Rlimit = new ArrayList<>();

    public static List<Double> initVariables = new ArrayList<>();
    public static List<Double> parameters = new ArrayList<>();
    public static List<Double> realFinalValues = new ArrayList<>();



    public covidProblemMultiDay4Objects(List<Double> initVariables, List<List<Double>> realFinalValues, List<Double> parameters,
                                        double step, int days, List<Double> R_limit, int start) {
        /**
         * The number of variables defined by this problem.
         *         int numberOfVariables = initVariables.size();
         *         int numberOfObjectives = realFinalValues.size();
         *         int numberOfConstraints = 1;
         */
        super(parameters.size(), realFinalValues.get(realFinalValues.size()-1).size()*days, 1);
        //super(4, 4, 1);
        this.numberOfConstraints = 1;
        this.numberOfVariables = parameters.size(); // consider parameters are variable in the problem
        this.numberOfObjectives = realFinalValues.get(realFinalValues.size()-1).size()*days; // consider the error of final values are the objectives

        this.Rlimit.clear();
        this.Rlimit.add(R_limit.get(0));
        this.Rlimit.add(R_limit.get(1));

        this.step = step;
        this.days = days;
        this.start = start;
        this.initVariables.clear();// InitVariables of the estimate problem
        for (int i = 0; i < initVariables.size(); i++){
            this.initVariables.add(initVariables.get(i));
        }
        this.parameters.clear();// The variables of evolution problem and the parameters of estimate problem
        for (int i = 0; i < parameters.size(); i++){
            this.parameters.add(parameters.get(i));
        }
        this.realFinalValues.clear();// The final variables of the estimate problem
        for (int i = start; i < realFinalValues.size(); i++){
            for (int j = 0; j <realFinalValues.get(realFinalValues.size()-1).size(); j++){
                this.realFinalValues.add(realFinalValues.get(i).get(j));
            }

        }

    }

    @Override
    public void evaluate(Solution solution) {
        List<Double> parameter = new ArrayList<>();
        for (int i = 0; i < numberOfVariables; i++){
            parameter.add(((RealVariable)solution.getVariable(i)).getValue());
        }

        int steps = (int) (this.days/this.step);
        double stepUnit = this.step;
        int start = this.start;
        List<List<Double>> Variables = new ArrayList<>();
        List<Double> temp = new ArrayList<>();
        for(int j = 0; j < this.initVariables.size(); j++){
            temp.add(this.initVariables.get(j));
        }
        Variables.add(temp);

        for (int i = 0; i < steps; i++){
            List<List<Double>> listVariables = computerVariables (Variables, parameter, stepUnit);
        }

        Variables.remove(0);
        List<Double> estimateValues = new ArrayList<>();
        for (int i = 0; i < Variables.size(); i = i + (int) (1/this.step)){
            List<Double> tempEstimate = realValues4Objectives(Variables.get(i));
                    //realValues(Variables.get(i));
            for (int j = 0; j < tempEstimate.size(); j++){
                estimateValues.add(tempEstimate.get(j));
            }
        }

        double[] functions = new double[realFinalValues.size()];
        double[] constraints = new double[realFinalValues.size()];

        boolean check = true;
        for (int i = 0; i < realFinalValues.size(); i++){
            if (estimateValues.get(i) < 0) {
                check = false;
            }
            for (int j = betaIndex; j < epsilonIndex; j++){
                if (((RealVariable)(solution.getVariable(j))).getValue() >
                        ((RealVariable)(solution.getVariable(alfaIndex))).getValue()){
                    check = false;
                }
            }

            if (((RealVariable)(solution.getVariable(epsilonIndex))).getValue() >
                    ((RealVariable)(solution.getVariable(thetaIndex))).getValue()){
                check = false;
            }
        }
        double R0_1 = calculate1(parameter);
        double R0_2 = calculate2(parameter);
        double underLimit = 0.0;
        if(start == 0){
            underLimit = 2.0;
        }
        if ((check)&&(0.8 < R0_1)&&(R0_1 < this.Rlimit.get(0))&&(R0_1 > underLimit) &&(R0_2 < this.Rlimit.get(1))){
            for (int i = 0; i < realFinalValues.size(); i++){
                functions[i] = abs(estimateValues.get(i) - realFinalValues.get(i));
                solution.setObjective(i, functions[i]);
                constraints[i] = functions[i];
                //solution.setConstraint(i, constraints[i] <= 0.0 ? 0.0 : constraints[i]);// if constraint <= 0 then = 0 else = constrain;
            }
        }
        else{
            for (int i = 0; i < realFinalValues.size(); i++){
                solution.setObjective(i, Double.POSITIVE_INFINITY);
            }
        }

    }
    @Override
    public Solution newSolution() {
        Solution solution = new Solution(numberOfVariables, numberOfObjectives, numberOfConstraints);
        //Solution solution = new Solution(4, 4, 2);
        for (int i = 0; i < numberOfVariables; i++){
            solution.setVariable(i, new RealVariable(0, 1));
        }

        //solution.setVariable(3, new BinaryIntegerVariable(0,100));
        return solution;
    }

}