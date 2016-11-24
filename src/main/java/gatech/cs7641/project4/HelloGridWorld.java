package gatech.cs7641.project4;

import burlap.domain.singleagent.gridworld.*;
import burlap.domain.singleagent.gridworld.state.*;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.shell.visual.VisualExplorer;
import burlap.visualizer.Visualizer;

public class HelloGridWorld {
	public static void run() {
		// 11x11 grid world.
		GridWorldDomain gw = new GridWorldDomain(11,11);
		
		// Four rooms layout.
		gw.setMapToFourRooms();
		
		// Stochastic transitions with 0.8 success rate.
		gw.setProbSucceedTransitionDynamics(0.8);
		
		// Generate the grid world domain.
		SADomain domain = gw.generateDomain();

		// Setup initial state.
		State s = new GridWorldState(new GridAgent(0, 0), new GridLocation(10, 10, "loc0"));

		// Create visualizer and explorer.
		Visualizer v = GridWorldVisualizer.getVisualizer(gw.getMap());
		VisualExplorer exp = new VisualExplorer(domain, v, s);

		// Set control keys to use w-s-a-d.
		exp.addKeyAction("w", GridWorldDomain.ACTION_NORTH, "");
		exp.addKeyAction("s", GridWorldDomain.ACTION_SOUTH, "");
		exp.addKeyAction("a", GridWorldDomain.ACTION_WEST, "");
		exp.addKeyAction("d", GridWorldDomain.ACTION_EAST, "");

		exp.initGUI();
	}
}
