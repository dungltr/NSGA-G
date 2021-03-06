package NSGAS;

import org.moeaframework.core.*;
import org.moeaframework.core.comparator.AggregateConstraintComparator;
import org.moeaframework.core.comparator.ChainedComparator;
import org.moeaframework.core.comparator.DominanceComparator;
import org.moeaframework.core.operator.RandomInitialization;
import org.moeaframework.core.operator.TournamentSelection;
import org.moeaframework.core.spi.AlgorithmProvider;
import org.moeaframework.core.spi.OperatorFactory;
import org.moeaframework.core.spi.ProviderNotFoundException;
import org.moeaframework.util.TypedProperties;

import java.util.Properties;

//import NSGAIVNondominatedSortingPopulation;
//Using this file
public class NSGASProvider extends AlgorithmProvider {

	@Override
/*	public Algorithm getAlgorithm(String name, Properties properties, Problem problem) {
		if (name.equalsIgnoreCase("NSGAS")) {
			// if the user requested the RandomWalker algorithm
			TypedProperties typedProperties = new TypedProperties(properties);
			
			// allow the user to customize the population size (default to 100)
			int populationSize = typedProperties.getInt("populationSize", 100);
			
			// initialize the algorithm with randomly-generated solutions
			Initialization initialization = new RandomInitialization(problem, populationSize);
			
			// use the operator factory to create a polynomial mutation operator
			Variation variation = OperatorFactory.getInstance().getVariation("pm", properties, problem);
			
			// construct and return the RandomWalker algorithm
			return newNSGAIV(typedProperties, problem);
		} else {
			// return null if the user requested a different algorithm
			return null;
		}
	}
*/	public Algorithm getAlgorithm(String name, Properties properties, Problem problem) {
		TypedProperties typedProperties = new TypedProperties(properties);
		//System.out.println("Hello from getAlgorithm in NSGASProvider");
		try {
			if (name.equalsIgnoreCase("NSGAS") ||
					name.equalsIgnoreCase("NSGA-S")) {
				return newNSGAS(typedProperties, problem);
			} else {
				return null;
			}
		} catch (FrameworkException e) {
			throw new ProviderNotFoundException(name, e);
		}
	}
	private Algorithm newNSGAS(TypedProperties properties, Problem problem) {
		//System.out.println("Hello from newNSGASfrom NSGAProvider");
		int divisionsOuter = 4;
		int divisionsInner = 0;
		/*
		//System.out.println("Say hello from NSGAS provider");
		if (properties.contains("divisionsOuter") && properties.contains("divisionsInner")) {
			divisionsOuter = (int)properties.getDouble("divisionsOuter", 4);
			divisionsInner = (int)properties.getDouble("divisionsInner", 0);
		} else if (properties.contains("divisions")){
			divisionsOuter = (int)properties.getDouble("divisions", 4);
		} else if (problem.getNumberOfObjectives() == 1) {
			divisionsOuter = 100;
		} else if (problem.getNumberOfObjectives() == 2) {
			divisionsOuter = 99;
		} else if (problem.getNumberOfObjectives() == 3) {
			divisionsOuter = 12;
		} else if (problem.getNumberOfObjectives() == 4) {
			divisionsOuter = 8;
		} else if (problem.getNumberOfObjectives() == 5) {
			divisionsOuter = 6;
		} else if (problem.getNumberOfObjectives() == 6) {
			divisionsOuter = 4;
			divisionsInner = 1;
		} else if (problem.getNumberOfObjectives() == 7) {
			divisionsOuter = 3;
			divisionsInner = 2;
		} else if (problem.getNumberOfObjectives() == 8) {
			divisionsOuter = 3;
			divisionsInner = 2;
		} else if (problem.getNumberOfObjectives() == 9) {
			divisionsOuter = 3;
			divisionsInner = 2;
		} else if (problem.getNumberOfObjectives() == 10) {
			divisionsOuter = 3;
			divisionsInner = 2;
		} else {
			divisionsOuter = 2;
			divisionsInner = 1;
		}
		
		int populationSize;
		
		if (properties.contains("populationSize")) {
			populationSize = (int)properties.getDouble("populationSize", 100);
		} else {
			// compute number of reference points
			populationSize = (int)(CombinatoricsUtils.binomialCoefficient(problem.getNumberOfObjectives() + divisionsOuter - 1, divisionsOuter) +
					(divisionsInner == 0 ? 0 : CombinatoricsUtils.binomialCoefficient(problem.getNumberOfObjectives() + divisionsInner - 1, divisionsInner)));

			// round up to a multiple of 4
			populationSize = (int)Math.ceil(populationSize / 4d) * 4;
		}
		*/
		int populationSize = 100;
		if (properties.contains("populationSize")) {
			populationSize = (int)properties.getDouble("populationSize", 100);
		}
		//System.out.println("Population is:="+populationSize);
		Initialization initialization = new RandomInitialization(problem,
				populationSize);// Create initialization

		NSGASReferencePointNondominatedSortingPopulation population = new NSGASReferencePointNondominatedSortingPopulation(
				problem.getNumberOfObjectives(), divisionsOuter, divisionsInner);
		Selection selection = null;
		if (problem.getNumberOfConstraints() == 0) {
			selection = new Selection() {

				@Override
				public Solution[] select(int arity, Population population) {
					Solution[] result = new Solution[arity];

					for (int i = 0; i < arity; i++) {
						result[i] = population.get(PRNG.nextInt(population.size()));
					}

					return result;
				}

			};
		} else {
			selection = new TournamentSelection(2, new ChainedComparator(
					new AggregateConstraintComparator(),
					new DominanceComparator() {

						@Override
						public int compare(Solution solution1, Solution solution2) {
							return PRNG.nextBoolean() ? -1 : 1;
						}

					}));
		}

		/////////////////////NSGASReferencePointNondominatedSortingPopulation
		/*NSGASNondominatedSortingPopulation population =
				new NSGASNondominatedSortingPopulation();// Create population
		TournamentSelection selection = null;		
		if (properties.getBoolean("withReplacement", true)) {
			selection = new TournamentSelection(2, new ChainedComparator(
					new ParetoDominanceComparator(),
					new CrowdingComparator()));//Create selection
		}
		 */
		////////////////////
		// disable swapping variables in SBX operator to remain consistent with
		// Deb's implementation (thanks to Haitham Seada for identifying this
		// discrepancy)

		if (!properties.contains("sbx.swap")) {
			properties.setBoolean("sbx.swap", false);
		}

		if (!properties.contains("sbx.distributionIndex")) {
			properties.setDouble("sbx.distributionIndex", 30.0);
		}

		if (!properties.contains("pm.distributionIndex")) {
			properties.setDouble("pm.distributionIndex", 20.0);
		}

		Variation variation = OperatorFactory.getInstance().getVariation(null,
				properties, problem);// Create variation

		return new NSGAS(problem, population, null, selection, variation,
				initialization);
	}
}