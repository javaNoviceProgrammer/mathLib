package tests;

import java.util.HashMap;
import java.util.Map;

import mathLib.util.MathUtils;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class TestExpressionBuilder {
	public static void main(String[] args) {

		// step 1: define some parameters and their values
		Map<String, Double> vars = new HashMap<>() ;
		vars.put("x", 3.0) ;
		vars.put("y", 1.2) ;
		vars.put("z", 2.2) ;

		// step 2: define a math expression
		String expression = "x^2-3y-1.2z" ;

		// step 3: create an expression builder
		ExpressionBuilder eb = new ExpressionBuilder(expression) ;

		// step 4: assign the variable names to the expression builder
		eb.variables(vars.keySet()) ;

		// step 5: make the expression
		Expression e = eb.build() ;

		// step 6: set the variables in the expression
		e.setVariables(vars) ;

		// step 7: print the evaluation of the expression
		System.out.println(e.evaluate());

		// using math utils
		System.out.println(MathUtils.evaluate(expression, vars));
	}
}
