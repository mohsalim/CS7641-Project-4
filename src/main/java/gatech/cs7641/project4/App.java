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
    	BasicBehavior.runExample(false, 4, 4);
    	
    	// Small case with policy iteration.
    	BasicBehavior.runExample(true, 4, 4);
    	
    	// Large case with value iteration.
       	BasicBehavior.runExample(false, 11, 11);
       	
       	// Large case with policy iteration.
    	BasicBehavior.runExample(true, 11, 11);

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
