package com.enewschamp;

import java.util.HashMap;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

public class TestUtil {

	public static void main(String[] args) {

		Evaluator eval = new Evaluator();
		try {
			String expression="'#{name}' == 'Deepak'";
			HashMap<String,String> varmap = new HashMap<String,String>();
			varmap.put("name", "Deepak");
			eval.setVariables(varmap);
			String result = eval.evaluate(expression);
			
			System.out.println(result);
		} catch (EvaluationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
