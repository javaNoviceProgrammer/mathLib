package tests;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TestScriptEngine {

	public static void main(String[] args) {



		try {
			StringBuilder sb = new StringBuilder() ;
//			sb.append("import numpy as np\n") ;
//			sb.append("x = np.linspace(0, 10, 100)\n") ;
			sb.append("print('x')") ;

			ScriptEngineManager scriptEngineManager = new ScriptEngineManager() ;
			ScriptEngine javaEngine = scriptEngineManager.getEngineByName("python") ;
			javaEngine.eval(sb.toString()) ;
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



//	    ScriptEngineManager mgr = new ScriptEngineManager();
//	    List<ScriptEngineFactory> factories = mgr.getEngineFactories();
//	    for (ScriptEngineFactory factory : factories)
//	    {
//	        System.out.println("ScriptEngineFactory Info");
//	        String engName = factory.getEngineName();
//	        String engVersion = factory.getEngineVersion();
//	        String langName = factory.getLanguageName();
//	        String langVersion = factory.getLanguageVersion();
//	        System.out.printf("\tScript Engine: %s (%s)\n", engName, engVersion);
//	        List<String> engNames = factory.getNames();
//	        for (String name : engNames)
//	        {
//	            System.out.printf("\tEngine Alias: %s\n", name);
//	        }
//	        System.out.printf("\tLanguage: %s (%s)\n", langName, langVersion);
//	        }



	    }


}
