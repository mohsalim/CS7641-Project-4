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
    	//BasicBehavior.runExample(false, true);
    	
    	// Small case with policy iteration.
    	//BasicBehavior.runExample(true, true);
    	
    	// Large case with value iteration.
       	BasicBehavior.runExample(false, false);
       	
       	// Large case with policy iteration.
    	//BasicBehavior.runExample(true, false);
    	
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
