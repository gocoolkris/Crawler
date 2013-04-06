package test.edu.upenn.cis455;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RunAllTests extends TestCase 
{
  public static Test suite() 
  {
    try {
      Class[]  testClasses = {
        /* Add the names of your unit test classes here */
        // Class.forName("your.class.name.here") 
    		  Class.forName("test.edu.upenn.cis455.ChannelContentServiceTest"),
    		  Class.forName("test.edu.upenn.cis455.ChannelServiceTest"),
    		  Class.forName("test.edu.upenn.cis455.DocumentServiceTest"),
    		  Class.forName("test.edu.upenn.cis455.SubscriberServiceTest"),
    		  Class.forName("test.edu.upenn.cis455.UserServiceTest"),
   
    		  Class.forName("test.edu.upenn.cis455.DoMTreeBuilderTest"),
    		  Class.forName("test.edu.upenn.cis455.HttpClientTest"),
    		  Class.forName("test.edu.upenn.cis455.GrammarValidatorTest"),
    		  Class.forName("test.edu.upenn.cis455.XPathValidatorTest"),
    		  Class.forName("test.edu.upenn.cis455.XPathEngineImplTest")
    		  
      };   
      
      return new TestSuite(testClasses);
    } catch(Exception e){
      e.printStackTrace();
    } 
    
    return null;
  }
}
