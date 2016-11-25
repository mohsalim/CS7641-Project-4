package gatech.cs7641.project4;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	// Small case with value iteration.
    	BasicBehavior.runExample(ReinforcementLearnerType.ValueIteration, true);
    	
    	// Small case with policy iteration.
    	BasicBehavior.runExample(ReinforcementLearnerType.PolicyIteration, true);

    	// Small case with q-learner.
    	BasicBehavior.runExample(ReinforcementLearnerType.QLearner, true);
    	
    	// Large case with value iteration.
    	BasicBehavior.runExample(ReinforcementLearnerType.ValueIteration, false);
       	
       	// Large case with policy iteration.
    	BasicBehavior.runExample(ReinforcementLearnerType.PolicyIteration, false);
    	
    	// Large case with q-learner.
    	BasicBehavior.runExample(ReinforcementLearnerType.QLearner, false);
    	
    	//HelloGridWorld.run();
    	//PlotTest.run();
    	
    	//MCVideo.run();
    	
    	// Grid game examples. (Choose one).
    	//GridGameExample.VICoCoTest();
		//GridGameExample.VICorrelatedTest();
		//GridGameExample.QLCoCoTest();
		//GridGameExample.saInterface();
    }
}
