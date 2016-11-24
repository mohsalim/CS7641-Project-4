package gatech.cs7641.project4;

import burlap.behavior.singleagent.auxiliary.performance.LearningAlgorithmExperimenter;
import burlap.behavior.singleagent.auxiliary.performance.PerformanceMetric;
import burlap.behavior.singleagent.auxiliary.performance.TrialMode;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.LearningAgentFactory;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.domain.singleagent.gridworld.GridWorldDomain;
import burlap.domain.singleagent.gridworld.state.GridAgent;
import burlap.domain.singleagent.gridworld.state.GridLocation;
import burlap.domain.singleagent.gridworld.state.GridWorldState;
import burlap.mdp.auxiliary.common.ConstantStateGenerator;
import burlap.mdp.auxiliary.common.SinglePFTF;
import burlap.mdp.auxiliary.stateconditiontest.TFGoalCondition;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.singleagent.common.GoalBasedRF;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.simple.SimpleHashableStateFactory;


/**
 * @author James MacGlashan.
 */
public class PlotTest {
	public static void run(){
		System.out.println("PlotTest.java starting...");

		// 11x11 grid world.
		GridWorldDomain gw = new GridWorldDomain(11,11);
		
		// Four rooms layout.
		gw.setMapToFourRooms();
		
		// Stochastic transitions with 0.8 success rate.
		gw.setProbSucceedTransitionDynamics(0.8);
		
		// Ends when the agent reaches a location.
		final TerminalFunction tf = new SinglePFTF(
				PropositionalFunction.findPF(gw.generatePfs(), GridWorldDomain.PF_AT_LOCATION));

		// Reward function definition.
		final RewardFunction rf = new GoalBasedRF(new TFGoalCondition(tf), 5., -0.1);

		// Update grid world with terminal function. 
		gw.setTf(tf);
		
		// Update grid world with reward function.
		gw.setRf(rf);

		// Generate the grid world domain.
		final OOSADomain domain = gw.generateDomain();

		// Setup initial state.
		GridWorldState s = new GridWorldState(new GridAgent(0, 0), new GridLocation(10, 10, "loc0"));

		// Initial state generator.
		final ConstantStateGenerator sg = new ConstantStateGenerator(s);

		// Set up the state hashing system for looking up states.
		final SimpleHashableStateFactory hashingFactory = new SimpleHashableStateFactory();

		/**
		 * Create factory for Q-learning agent
		 */
		LearningAgentFactory qLearningFactory = new LearningAgentFactory() {
			public String getAgentName() {
				return "Q-learning";
			}

			public LearningAgent generateAgent() {
				return new QLearning(domain, 0.99, hashingFactory, 0.3, 0.1);
			}
		};

		// Define learning environment.
		SimulatedEnvironment env = new SimulatedEnvironment(domain, sg);

		// Define experiment.
		LearningAlgorithmExperimenter exp = new LearningAlgorithmExperimenter(env,
				10, 100, qLearningFactory);

		exp.setUpPlottingConfiguration(500, 250, 2, 1000, TrialMode.MOST_RECENT_AND_AVERAGE,
				PerformanceMetric.CUMULATIVE_STEPS_PER_EPISODE, PerformanceMetric.AVERAGE_EPISODE_REWARD);

		// Start experiment.
		exp.startExperiment();
		
		System.out.println("PlotTest.java finished.");
	}
}