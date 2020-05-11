/* Copyright 2009-2016 David Hadka
 *
 * This file is part of the MOEA Framework.
 *
 * The MOEA Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The MOEA Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the MOEA Framework.  If not, see <http://www.gnu.org/licenses/>.
 */
package NSGAS;

//import NSGAV.utilsPopulation;
//import NSGAS.NSGASNondominatedSortingPopulation;
//import org.apache.commons.math3.util.MathArrays;

import org.apache.commons.math3.util.MathArrays;
import org.moeaframework.core.FrameworkException;
import org.moeaframework.core.Population;
import org.moeaframework.core.Solution;
import org.moeaframework.core.comparator.RankComparator;

import java.util.*;

import static org.moeaframework.core.FastNondominatedSorting.RANK_ATTRIBUTE;

//import org.moeaframework.core.comparator.CrowdingComparator;
//import org.moeaframework.core.comparator.DominanceComparator;
//import org.moeaframework.util.Vector;
//import org.moeaframework.util.weights.NormalBoundaryIntersectionGenerator;
////////////////////Using this file

/**
 * Implementation of the reference-point-based nondominated sorting method
 * for NSGA-III.  NSGA-III includes an additional parameter, the number of
 * divisions, that controls the spacing of reference points.  For large
 * objective counts, an alternate two-layered approach was also proposed
 * allowing the user to specify the divisions on the outer and inner layer.
 * When using the two-layered approach, the number of outer divisions should
 * less than the number of objectives, otherwise it will generate reference
 * points overlapping with the inner layer.  If there are M objectives and
 * p divisions, then {@code binomialCoefficient(M+p-1, p)} reference points are
 * generated.
 * <p>
 * Unfortunately, since no official implementation has been released by the
 * original authors, we have made our best effort to implement the algorithm as
 * described in the journal article.  We would like to thank Tsung-Che Chiang
 * for developing the first publicly available implementation of NSGA-III in
 * C++.
 * <p>
 * References:
 * <ol>
 *   <li>Deb, K. and Jain, H.  "An Evolutionary Many-Objective Optimization
 *       Algorithm Using Reference-Point-Based Nondominated Sorting Approach,
 *       Part I: Solving Problems With Box Constraints."  IEEE Transactions on
 *       Evolutionary Computation, 18(4):577-601, 2014.
 *   <li>Deb, K. and Jain, H.  "Handling Many-Objective Problems Using an
 *       Improved NSGA-II Procedure.  WCCI 2012 IEEE World Contress on
 *       Computational Intelligence, Brisbane, Australia, June 10-15, 2012.
 *   <li><a href="http://web.ntnu.edu.tw/~tcchiang/publications/nsga3cpp/nsga3cpp.htm">C++ Implementation by Tsung-Che Chiang</a>
 * </ol>
 * NSGA-GS remove the max distance solution in a group.
 */
public class NSGASReferencePointNondominatedSortingPopulation extends NSGASNondominatedSortingPopulation {

	/**
	 * The name of the attribute for storing the normalized objectives.
	 */
	static final String NORMALIZED_OBJECTIVES = "Normalized Objectives";
	static final String Min_OBJECTIVES = "Min Objectives";
	static final String Max_OBJECTIVES = "Max Objectives";
	static final String Distance_OBJECTIVES = "Distance to Min Objectives";
	static final String Group_OBJECTIVES = "Group Objectives";
	/**
	 * The number of objectives.
	 */
	private final int numberOfObjectives;

	/**
	 * The number of outer divisions.
	 */
	int divisionsOuter;

	/**
	 * The number of inner divisions, or {@code 0} if no inner divisions should
	 * be used.
	 */
	int divisionsInner;

	/**
	 * The ideal point, updated each iteration.
	 */
	double[] idealPoint;
	double[] idealMaxPoint;
	//static double[] averagePreviousPoint;
	//static double[] averageCurrentPoint;

	/**
	 * The list of reference points, or weights.
	 */
	//private List<double[]> weights;

	//private Solution average;

	//private static DominanceComparator comparator;

	//private static DominanceComparator comparator2;

	//private static Population resultFilter;

	//private static DominanceComparator comparator3;

	/**
	 * Constructs an empty population that maintains the {@code rank} attribute
	 * for its solutions.
	 *
	 * @param numberOfObjectives the number of objectives
	 * @param divisionsOuter the number of outer divisions
	 * @param divisionsInner the number of inner divisions
	 */
	public NSGASReferencePointNondominatedSortingPopulation(int numberOfObjectives,
															int divisionsOuter, int divisionsInner) {
		super();
		this.numberOfObjectives = numberOfObjectives;
		this.divisionsOuter = divisionsOuter;
		this.divisionsInner = divisionsInner;

		initialize();
	}



	/**
	 * Initializes the ideal point and reference points (weights).
	 */
	private void initialize() {
		idealPoint = new double[numberOfObjectives];
		idealMaxPoint = new double[numberOfObjectives];
		Arrays.fill(idealPoint, Double.POSITIVE_INFINITY);
		Arrays.fill(idealMaxPoint, Double.NEGATIVE_INFINITY);
		//System.out.println("Say hello from initialize() in NSGAS");
		//
		//weights = new NormalBoundaryIntersectionGenerator(numberOfObjectives,
		//		divisionsOuter, divisionsInner).generate();
		//for (int j=0;j<weights.size();j++)
		//System.out.println("divisionsOuter:="+divisionsOuter);
		//System.out.println("divisionsInner:="+divisionsInner);
		//System.out.println("numberOfObjectives:="+numberOfObjectives);
		//System.out.println("divisionsInner:="+divisionsInner);
		//NSGAIV.matrixPrint.printArray(idealPoint);
	}
	/**
	 * Updates the ideal point given the solutions currently in this population.
	 * Determine new coordinates
	 */
	protected void updateIdealPoint() {
		for (Solution solution : this) {
			if (solution.getNumberOfObjectives() != numberOfObjectives) {
				throw new FrameworkException("incorrect number of objectives");
			}

			for (int i = 0; i < numberOfObjectives; i++) {
				idealPoint[i] = Math.min(idealPoint[i], solution.getObjective(i));
			}
			/*
			System.out.println("\nidealPoint:=");
			NSGAIV.matrixPrint.printArray(idealPoint);
			*/
		}
	}
	/**
	 * Updates the ideal max point given the solutions currently in this population.
	 * Determine new coordinates
	 */
	protected void updateIdealMaxPoint() {
		for (Solution solution : this) {
			if (solution.getNumberOfObjectives() != numberOfObjectives) {
				throw new FrameworkException("incorrect number of objectives");
			}

			for (int i = 0; i < numberOfObjectives; i++) {
				idealMaxPoint[i] = Math.max(idealMaxPoint[i], solution.getObjective(i));
			}
			/*
			System.out.println("\nidealPoint:=");
			NSGAIV.matrixPrint.printArray(idealPoint);
			*/
		}
	}
	/**
	 * Offsets the solutions in this population by the ideal point.  This
	 * method does not modify the objective values, it creates a new attribute
	 * with the name {@value NORMALIZED_OBJECTIVES}.
	 * Convert any point to new points with new coordinates
	 */
	protected void translateByIdealPoint() {
		for (Solution solution : this) {
			double[] objectives = solution.getObjectives();

			for (int i = 0; i < numberOfObjectives; i++) {
				objectives[i] -= idealPoint[i];
			}

			solution.setAttribute(NORMALIZED_OBJECTIVES, objectives);
		}
	}


	/**
	 * Normalizes the solutions in this population by the given intercepts
	 * (or scaling factors).  This method does not modify the objective values,
	 * it modifies the {@value NORMALIZED_OBJECTIVES} attribute.
	 *
	 * Translate to new coordinates with scaling
	 */
	protected void normalizeByMinMax() {
		for (Solution solution : this) {
			double[] objectives = (double[])solution.getAttribute(NORMALIZED_OBJECTIVES);

			for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
				objectives[i] /= idealMaxPoint[i]-idealPoint[i];
			}
		}
	}


	public static double[] average (List<Solution> solutions,int numberOfObjectives) {
		double [] averagePoint = new double[numberOfObjectives];
		for (int i = 0; i < numberOfObjectives; i++) {
			averagePoint[i] = 0;
			for (Solution solution : solutions) {
				utilsPopulation.printSolution(solution);
				averagePoint[i] = averagePoint[i] - solution.getObjective(i)/solutions.size();
				System.out.println("\n The solution.getObjective("+i+")"+solution.getObjective(i));
				System.out.println("\n The solution.solutions.size()"+solutions.size());
			}
		}
		matrixPrint.printArray(averagePoint);
		return averagePoint;
	}


	protected static Solution findSumMetricSolution (Population resultFilter){
		double[] Distance = new double[resultFilter.size()];
		for (int i=0; i<resultFilter.size();i++) {
			Distance[i] = (double)resultFilter.get(i).getAttribute(Distance_OBJECTIVES);
		}
		double max = 0;
		int index = 0;
		for (int i=0; i<Distance.length;i++){
			max = Math.max(max, Distance[i]);
			if (max==Distance[i]) {
				index = i;
			}
		}
		return resultFilter.get(index);
	}


	/*
	protected static double distance(Solution solution){
		double distance = 0;
		for (int i = 0; i< solution.getNumberOfObjectives();i++){
			distance = distance + solution.getObjective(i)*solution.getObjective(i);
		}
		return distance;
	}
	*/

	public static boolean compareVector(double[] v1, double[] v2){
		if (v1.length!=v2.length){
			System.out.println("Cannot compare two vector with difference length");
			return false;
		}
		else{
			int temp = 0;
			for (int i = 0; i < v1.length; i++){
				if (v1[i] != v2[i])
					temp ++;
			}
			if (temp == 0) return true;
			else return false;

		}
	}

	public static Population removeMaxMetricInGroup (Population resultFront, int newSize){
		// reset all solutions in lastFront to group -1
		for (int i = 0; i< resultFront.size();i++){
			resultFront.get(i).setAttribute(Group_OBJECTIVES,-1);
		}

		Population BigGroup = resultFront;//
		// devide all the solution in multiple groups ((Group_OBJECTIVES,Grp))
		int Grp = -1;
		for(int i = 0; i< BigGroup.size(); i++){
			if ((int)BigGroup.get(i).getAttribute(Group_OBJECTIVES)==-1){
				Grp++;
				BigGroup.get(i).setAttribute(Group_OBJECTIVES,Grp);
				double [] mini = (double[]) BigGroup.get(i).getAttribute(Min_OBJECTIVES);
				double [] maxi = (double[]) BigGroup.get(i).getAttribute(Max_OBJECTIVES);
				for (int j = i+1; j < BigGroup.size(); j++){
					if ((i!=j)&&((int)BigGroup.get(j).getAttribute(Group_OBJECTIVES)==-1)){
						double [] minj = (double[]) BigGroup.get(j).getAttribute(Min_OBJECTIVES);
						double [] maxj = (double[]) BigGroup.get(j).getAttribute(Max_OBJECTIVES);
						if (compareVector(mini, minj)&&compareVector(maxi, maxj)){
							BigGroup.get(j).setAttribute(Group_OBJECTIVES,Grp);
						}
					}
				}
			}

		}
		//
		int Grp_Max = Grp+1;
		//System.out.println("big group :="+ Grp_Max);
		// Create a list of groups
		List<Population> Groups = new ArrayList<>();// List of group
		for (int i=0; i < Grp_Max; i++) {
			Population Group = new Population();
			for (Solution solution: BigGroup){
				if((int)solution.getAttribute(Group_OBJECTIVES)==i){
					Group.add(solution);
				}
			}
			Groups.add(Group);
		}
		// Remove worst solution in groups
		while(resultFront.size()>newSize){
			Random rand = new Random();
			// nextInt is normally exclusive of the top value,
			// so add 1 to make it inclusive
			int randomNum = rand.nextInt(Grp_Max);
			if (Groups.get(randomNum).size()>0){
				Solution solution = findSumMetricSolution (Groups.get(randomNum));
				Groups.get(randomNum).remove(solution);
				resultFront.remove(solution);
			}
		}
		//System.out.println("After truncated with removeMaxDistance and Size:="+resultFront.size()+"and newSize is:="+newSize);
		return resultFront;
	}


	public void updateMinMaxPoint(int sizeScale, Population population){
		double[] pointScale = new double[sizeScale+1];
		pointScale[0] = 0;
		double delta = (double) (1)/sizeScale;
		for (int i = 1; i<sizeScale+1; i++){
			pointScale[i] = pointScale[i-1] + delta;
		}
		for (Solution solution : population) {
			double[] objectives = (double[])solution.getAttribute(NORMALIZED_OBJECTIVES);
			double[] objectivesBackup = new double[objectives.length];
			double[] objectivesLow =  new double[objectives.length];
			double[] objectivesUp = new double[objectives.length];

			for (int i = 0; i < objectivesLow.length; i++){
				objectivesBackup[i] = objectives[i];
				objectivesLow[i] = objectives[i];
				objectivesUp[i] = objectives[i];
			}

			//double[] objectivesUp = (double[])solution.getAttribute(NORMALIZED_OBJECTIVES);
			for (int i = 0; i < numberOfObjectives; i++) {
				int j = 0;
				while ((objectivesLow[i]>pointScale[j])&&(j < sizeScale)){
					j++;
				}
				if (j>0)
					objectivesLow[i] = pointScale[j-1];
				else objectivesLow[i] = pointScale[0];
			}
			for (int i = 0; i < numberOfObjectives; i++) {
				int j = sizeScale;
				while ((objectivesUp[i]<pointScale[j])&&(j > 0)){
					j--;
				}
				if (j<sizeScale)
					objectivesUp[i] = pointScale[j+1];
				else objectivesUp[i] = pointScale[sizeScale];
			}

			double distance = Sum(objectivesLow, objectives);
			solution.setAttribute(Min_OBJECTIVES, objectivesLow);
			solution.setAttribute(Max_OBJECTIVES, objectivesUp);
			solution.setAttribute(Distance_OBJECTIVES, distance);
		}
	}
	public double Sum(double[] objectivesLow, double[] objectives){
		double sum = 0;
		for(int i = 0; i < objectivesLow.length; i++){
			sum = sum + (objectives[i] - objectivesLow[i]);
		}
		return sum;
	}


	/**
	 * Truncates the population to the specified size using the reference-point
	 * based nondominated sorting method.
	 */
	@Override
	public void truncate(int size, Comparator<? super Solution> comparator) {
		//System.out.println("Hello from truncate function in NSGASReferencePoint.java in NSGAS package");
		if (size() > size) {
			// remove all solutions past the last front
			sort(new RankComparator());
			int maxRank = (Integer)get(size-1).getAttribute(RANK_ATTRIBUTE);
			Population front = new Population();
			//Population previousFront = new Population();// Dung edit
			//Population currentFront = new Population();// Dung edit

			for (int i = 0; i < size(); i++) {
				int rank = (Integer)get(i).getAttribute(RANK_ATTRIBUTE);

				if (rank > maxRank) {
					front.add(get(i));
				}
			}

			removeAll(front);

			updateIdealPoint();
			updateIdealMaxPoint();
			// translate objectives so the ideal point is at the origin
			translateByIdealPoint();

			// calculate the extreme points, calculate the hyperplane defined
			// by the extreme points, and compute the intercepts
			// normalizeByIntercepts(calculateIntercepts());
			normalizeByMinMax();


			// get the solutions in the last front
			front = new Population();

			for (int i = 0; i < size(); i++) {
				int rank = (Integer)get(i).getAttribute(RANK_ATTRIBUTE);

				if (rank == maxRank) {
					front.add(get(i));
					//currentFront.add(get(i));
				}//else {
				//if (rank==maxRank-1) previousFront.add(get(i));
				//}
			}
			removeAll(front);
			int tempGrid = front.size() + size() - size;

			int gridPoint = (int)Math.ceil(Math.pow(tempGrid, 1.0 / (front.get(0).getNumberOfObjectives()-1)));

			updateMinMaxPoint(gridPoint, front);

			int N = size - size();// size = 100; size() is the members of previous front

			Population temp = removeMaxMetricInGroup(front,N);
			//Population temp = removeMaxDistanceInGrid(currentFront,N);
			if(temp.size()+size()==size){
				for (Solution solution : temp){
					add(solution);
				}
				//System.out.println("no need to go to while size():"+size()+" = size"+size);
			}


		}
		//System.out.println("outside size():"+size()+" == size"+size);
	}

	/**
	 * Truncates the population to the specified size using the reference-point
	 * based nondominated sorting method.
	 */
	@Override
	public void truncate(int size) {
		truncate(size, new RankComparator());
	}
	public static int compare(Solution solution1, Solution solution2) {
		boolean dominate1 = false;
		boolean dominate2 = false;

		for (int i = 0; i < solution1.getNumberOfObjectives(); i++) {
			if (solution1.getObjective(i) < solution2.getObjective(i)) {
				dominate1 = true;

				if (dominate2) {
					return 0;
				}
			} else if (solution1.getObjective(i) > solution2.getObjective(i)) {
				dominate2 = true;

				if (dominate1) {
					return 0;
				}
			}
		}

		if (dominate1 == dominate2) {
			return 0;
		} else if (dominate1) {
			return -1;
		} else {
			return 1;
		}
	}

}
