package demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormulaOperation {

	public static void main(String[] args) {
		
		String formula = "(((SUM(this.C117491513)+SUM(this.C117491515))+120)/"
				+ "(SUM(this.C117491512)+SUM(this.C117491514)))*100";	
		
		String str = "(1-(SUM(this.C117498214)+SUM(this.C117498216)))/SUM(this.C117498176)*100";
		
		FormulaOperation t1 = new FormulaOperation();
		System.out.println(t1.fromlaLogicForAggregation(str));

	}

	public String fromlaLogicForAggregation(String formula) {

		formula = formula.replace("(this.C", "_");
		
		String[] token = formula.split("\\)");		
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < token.length; i++) {
			//System.out.println("token ::" + token[i]);
			if (token[i].isEmpty()) {
				sb.append(")");
			} else {
				if (!token[i].isEmpty() && !token[i].contains("SUM_") && !(i==(token.length-1))) {
					sb.append(token[i]+")");
				} else
					sb.append(token[i]);
			}
		}
		
		StringBuilder finalString = new StringBuilder(formula);
		finalString = finalString.reverse();
		char[] charlist = String.valueOf(finalString).toCharArray();		
		for(int j=0 ;j<charlist.length;j++){		
			if((""+charlist[j]).equals(")")){
				sb.append(")");
			}else{
				break;
			}
		}		
		System.out.println("Last formula : " + sb);
		return sb.toString();
	}
	

}
