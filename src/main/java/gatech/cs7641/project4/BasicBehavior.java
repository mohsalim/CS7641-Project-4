package gatech.cs7641.project4;

import burlap.behavior.policy.GreedyQPolicy;
import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;
import burlap.behavior.singleagent.auxiliary.StateReachability;
import burlap.behavior.singleagent.auxiliary.performance.LearningAlgorithmExperimenter;
import burlap.behavior.singleagent.auxiliary.performance.PerformanceMetric;
import burlap.behavior.singleagent.auxiliary.performance.TrialMode;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.ValueFunctionVisualizerGUI;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.ArrowActionGlyph;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.LandmarkColorBlendInterpolation;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.PolicyGlyphPainter2D;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.StateValuePainter2D;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.LearningAgentFactory;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.behavior.singleagent.learning.tdmethods.SarsaLam;
import burlap.behavior.singleagent.planning.Planner;
import burlap.behavior.singleagent.planning.deterministic.DeterministicPlanner;
import burlap.behavior.singleagent.planning.deterministic.informed.Heuristic;
import burlap.behavior.singleagent.planning.deterministic.informed.astar.AStar;
import burlap.behavior.singleagent.planning.deterministic.uninformed.bfs.BFS;
import burlap.behavior.singleagent.planning.deterministic.uninformed.dfs.DFS;
import burlap.behavior.singleagent.planning.stochastic.policyiteration.PolicyIteration;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.behavior.valuefunction.QProvider;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.domain.singleagent.gridworld.GridWorldDomain;
import burlap.domain.singleagent.gridworld.GridWorldTerminalFunction;
import burlap.domain.singleagent.gridworld.GridWorldVisualizer;
import burlap.domain.singleagent.gridworld.SalimGridWorldDomain;
import burlap.domain.singleagent.gridworld.state.GridAgent;
import burlap.domain.singleagent.gridworld.state.GridLocation;
import burlap.domain.singleagent.gridworld.state.GridWorldState;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.auxiliary.stateconditiontest.TFGoalCondition;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.vardomain.VariableDomain;
import burlap.mdp.singleagent.common.GoalBasedRF;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.HashableStateFactory;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import burlap.visualizer.Visualizer;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author James MacGlashan.
 */
public class BasicBehavior {
	GridWorldDomain gwdg;
	OOSADomain domain;
	TerminalFunction tf;
	StateConditionTest goalCondition;
	State initialState;
	HashableStateFactory hashingFactory;
	SimulatedEnvironment env;
	
	// Default to 4x4.
	public static int WIDTH = 4;
	public static int HEIGHT = 4;

	public BasicBehavior(boolean isSmallCase){
		int dimension = SalimGridWorldDomain.getDimension(isSmallCase);
		WIDTH = dimension;
		HEIGHT = dimension;
		gwdg = new SalimGridWorldDomain(WIDTH, HEIGHT);
		
		if(isSmallCase) {
			((SalimGridWorldDomain)gwdg).setSmallCase();
		} else {
			((SalimGridWorldDomain)gwdg).setLargeCase();			
		}
		
		prettyPrintMap(gwdg.getMap());

		
		tf = new GridWorldTerminalFunction(WIDTH - 1, HEIGHT - 1);
		gwdg.setTf(tf);
		goalCondition = new TFGoalCondition(tf);
		domain = gwdg.generateDomain();

		initialState = new GridWorldState(new GridAgent(0, 0), new GridLocation(WIDTH - 1, HEIGHT - 1, "loc0"));
		hashingFactory = new SimpleHashableStateFactory();

		env = new SimulatedEnvironment(domain, initialState);
		
		//VisualActionObserver observer = new VisualActionObserver(domain, GridWorldVisualizer.getVisualizer(gwdg.getMap()));
		//observer.initGUI();
		//env.addObservers(observer);
	}
	
	private void prettyPrintMap(int[][] map) {		
		System.out.println("Map: ");
		System.out.println(Arrays.deepToString(map).replace('[', '{').replace(']', '}'));
		
//		String str = "";
//		
//		// Iterate map backwards format correctly.
//		for(int i = 0; i < map.length ; i++) {
//			String numbers = "";
//			for(int j = map[i].length - 1; j >= 0 ; j--) {
//				numbers += map[i][j] + ",";
//			}
//			// Trim off last comma.
//			numbers = numbers.substring(0, numbers.length() - 1);
//			// Close numbers array.
//			numbers =  "{ " + numbers + " } ,";
//			// Add array to 2D array array.
//			str += numbers;
//		}
//		
//		// Close 2D array.
//		str = "{ " + str + " }";
//		// Print map string.
//		System.out.println(str);
	}


	public void visualize(String outputpath){
		Visualizer v = GridWorldVisualizer.getVisualizer(gwdg.getMap());
		new EpisodeSequenceVisualizer(v, domain, outputpath);
	}

	public void BFSExample(String outputPath){
		DeterministicPlanner planner = new BFS(domain, goalCondition, hashingFactory);
		Policy p = planner.planFromState(initialState);
		PolicyUtils.rollout(p, initialState, domain.getModel()).write(outputPath + "bfs");
	}

	public void DFSExample(String outputPath){
		DeterministicPlanner planner = new DFS(domain, goalCondition, hashingFactory);
		Policy p = planner.planFromState(initialState);
		PolicyUtils.rollout(p, initialState, domain.getModel()).write(outputPath + "dfs");
	}

	public void AStarExample(String outputPath){
		Heuristic mdistHeuristic = new Heuristic() {

			public double h(State s) {
				GridAgent a = ((GridWorldState)s).agent;
				double mdist = Math.abs(a.x-10) + Math.abs(a.y-10);

				return -mdist;
			}
		};

		DeterministicPlanner planner = new AStar(domain, goalCondition, hashingFactory, mdistHeuristic);
		Policy p = planner.planFromState(initialState);

		PolicyUtils.rollout(p, initialState, domain.getModel()).write(outputPath + "astar");
	}

	public void valueIterationExample(String outputPath){
		System.out.println("Value iteration example starting...");
		Planner planner = new ValueIteration(domain, 0.99, hashingFactory, 0.001, 100);
		Policy p = planner.planFromState(initialState);

		System.out.println("Value iteration example rollout starting...");
		PolicyUtils.rollout(p, initialState, domain.getModel()).write(outputPath + "vi");
		System.out.println("Value iteration example rollout complete.");

		simpleValueFunctionVis((ValueFunction)planner, p);
		//manualValueFunctionVis((ValueFunction)planner, p);
	}

	public void policyIterationExample(String outputPath){
		System.out.println("Policy iteration example starting...");
		// TODO are these parameters good?
		Planner planner = new PolicyIteration(domain, 0.99, hashingFactory, 0.001, 0.001, 100, 100);
		Policy p = planner.planFromState(initialState);

		System.out.println("Policy iteration example rollout starting...");
		PolicyUtils.rollout(p, initialState, domain.getModel()).write(outputPath + "pi");
		System.out.println("Policy iteration example rollout complete.");

		simpleValueFunctionVis((ValueFunction)planner, p);
		//manualValueFunctionVis((ValueFunction)planner, p);
	}

	public void qLearningExample(String outputPath){
		LearningAgent agent = new QLearning(domain, 0.99, hashingFactory, 0., 1.);

		//run learning for 50 episodes
		for(int i = 0; i < 50; i++){
			Episode e = agent.runLearningEpisode(env);

			e.write(outputPath + "ql_" + i);
			System.out.println(i + ": " + e.maxTimeStep());

			//reset environment for next learning episode
			env.resetEnvironment();
		}

		simpleValueFunctionVis((ValueFunction)agent, new GreedyQPolicy((QProvider) agent));
	}


	public void sarsaLearningExample(String outputPath){
		LearningAgent agent = new SarsaLam(domain, 0.99, hashingFactory, 0., 0.5, 0.3);

		//run learning for 50 episodes
		for(int i = 0; i < 50; i++){
			Episode e = agent.runLearningEpisode(env);

			e.write(outputPath + "sarsa_" + i);
			System.out.println(i + ": " + e.maxTimeStep());

			//reset environment for next learning episode
			env.resetEnvironment();
		}
	}

	public void simpleValueFunctionVis(ValueFunction valueFunction, Policy p){
		System.out.println("Getting reachable states...");
		List<State> allStates = StateReachability.getReachableStates(initialState, domain, hashingFactory);
		
		System.out.println("Getting grid world value function visual...");
		ValueFunctionVisualizerGUI gui = GridWorldDomain.getGridWorldValueFunctionVisualization(allStates, WIDTH, HEIGHT, valueFunction, p);
		
		System.out.println("Initializing GUI...");
		gui.initGUI();
	}

	public void manualValueFunctionVis(ValueFunction valueFunction, Policy p){
		List<State> allStates = StateReachability.getReachableStates(initialState, domain, hashingFactory);

		//define color function
		LandmarkColorBlendInterpolation rb = new LandmarkColorBlendInterpolation();
		rb.addNextLandMark(0., Color.RED);
		rb.addNextLandMark(1., Color.BLUE);

		//define a 2D painter of state values, specifying which attributes correspond to the x and y coordinates of the canvas
		StateValuePainter2D svp = new StateValuePainter2D(rb);
		svp.setXYKeys("agent:x", "agent:y", new VariableDomain(0, WIDTH), new VariableDomain(0, HEIGHT), 1, 1);

		//create our ValueFunctionVisualizer that paints for all states
		//using the ValueFunction source and the state value painter we defined
		ValueFunctionVisualizerGUI gui = new ValueFunctionVisualizerGUI(allStates, svp, valueFunction);

		//define a policy painter that uses arrow glyphs for each of the grid world actions
		PolicyGlyphPainter2D spp = new PolicyGlyphPainter2D();
		spp.setXYKeys("agent:x", "agent:y", new VariableDomain(0, WIDTH), new VariableDomain(0, HEIGHT), 1, 1);

		spp.setActionNameGlyphPainter(GridWorldDomain.ACTION_NORTH, new ArrowActionGlyph(0));
		spp.setActionNameGlyphPainter(GridWorldDomain.ACTION_SOUTH, new ArrowActionGlyph(1));
		spp.setActionNameGlyphPainter(GridWorldDomain.ACTION_EAST, new ArrowActionGlyph(2));
		spp.setActionNameGlyphPainter(GridWorldDomain.ACTION_WEST, new ArrowActionGlyph(3));
		spp.setRenderStyle(PolicyGlyphPainter2D.PolicyGlyphRenderStyle.DISTSCALED);

		//add our policy renderer to it
		gui.setSpp(spp);
		gui.setPolicy(p);

		//set the background color for places where states are not rendered to grey
		gui.setBgColor(Color.GRAY);

		//start it
		gui.initGUI();
	}

	public void experimentAndPlotter(){
		// Different reward function for more structured performance plots.
		((FactoredModel)domain.getModel()).setRf(new GoalBasedRF(this.goalCondition, 5.0, -0.1));

		/**
		 * Create factories for Q-learning agent and SARSA agent to compare
		 */
		LearningAgentFactory qLearningFactory = new LearningAgentFactory() {
			public String getAgentName() {
				return "Q-Learning";
			}

			public LearningAgent generateAgent() {
				return new QLearning(domain, 0.99, hashingFactory, 0.3, 0.1);
			}
		};

		// Sarsa-Leaner is way too slow. I only need one reinforcement learner for this project, and Q-Learner does the job.
		/*
		LearningAgentFactory sarsaLearningFactory = new LearningAgentFactory() {
			public String getAgentName() {
				return "SARSA";
			}

			public LearningAgent generateAgent() {
				return new SarsaLam(domain, 0.99, hashingFactory, 0.0, 0.1, 1.);
			}
		};
		
		LearningAlgorithmExperimenter exp = new LearningAlgorithmExperimenter(env, 10, 100, qLearningFactory, sarsaLearningFactory);
		*/
		
		LearningAlgorithmExperimenter exp = new LearningAlgorithmExperimenter(env, 10, 100, qLearningFactory);
		exp.setUpPlottingConfiguration(500, 250, 2, 1000,
				TrialMode.MOST_RECENT_AND_AVERAGE,
				PerformanceMetric.CUMULATIVE_STEPS_PER_EPISODE,
				PerformanceMetric.AVERAGE_EPISODE_REWARD);

		exp.startExperiment();
		exp.writeStepAndEpisodeDataToCSV("expData");
	}

	public static void runExample(ReinforcementLearnerType riType, boolean isSmallCase) {
		// Create grid world.
		String smallCaseStr = isSmallCase ? "small case" : "large case";
		System.out.println("BasicBehavior.runExample " + smallCaseStr + " starting...");
		BasicBehavior example = new BasicBehavior(isSmallCase);
		String outputPath = "output/";

		// Run iteration.		
		switch(riType) {
		case PolicyIteration:
			System.out.println("Running policy iteration example...");
			example.policyIterationExample(outputPath);
			break;
		case ValueIteration:
			System.out.println("Running value iteration example...");
			example.valueIterationExample(outputPath);			
			break;
		case QLearner:
			System.out.println("Running q-learner example...");
			example.qLearningExample(outputPath);
			break;
		default:
			System.out.print("Invalid ReinforcementLearnerType: " + riType);
			return;
		}
		
		// Plot data.
		System.out.println("Starting plotter...");
		example.experimentAndPlotter();
		System.out.println("Finished plotter.");

		// Visualize world.
		System.out.println("Visualizing example...");
		example.visualize(outputPath);
		System.out.println("BasicBehavior.runExample finished.");
	}
	
	
	public static void main(String[] args) {
		System.out.println("BasicBehavior.java starting...");
		BasicBehavior example = new BasicBehavior(false);
		String outputPath = "output/";

		System.out.println("Running example...");
		//example.BFSExample(outputPath);
		//example.DFSExample(outputPath);
		//example.AStarExample(outputPath);
		//example.valueIterationExample(outputPath);
		//example.policyIterationExample(outputPath);
		example.qLearningExample(outputPath);
		//example.sarsaLearningExample(outputPath);

		System.out.println("Starting plotter...");
		example.experimentAndPlotter();
		System.out.println("Finished plotter.");

		System.out.println("Visualizing example...");
		example.visualize(outputPath);
		System.out.println("BasicBehavior.java finished.");
	}
	
}