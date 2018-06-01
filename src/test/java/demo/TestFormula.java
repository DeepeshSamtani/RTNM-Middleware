package demo;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestFormula {

	public static void main(String[] args) {
		System.out.println("in main method -------- ");

		TestFormula formula = new TestFormula();
		String formulastr = "(SUM(this.C117499812)+(MIN(this.C117499812)+SUM(this.C117499813)+SUM(this.C117499816)+SUM(this.C117499817))*100";
		System.out.println("formula : " + formula);
		formula.formulaWithCounters(formulastr);

		System.out.println("test isAlpha :: " + formula.isAlpha("dhsdh"));
	}

	public Map<String, Set<String>> formulaWithCounters(String formula) {

		List<String> parsedCounters = Stream.of(formula.replace("this.C", "").trim().split("\\W"))
				.filter(c -> c.length() > 2).collect(Collectors.toList());
		Map<String, Set<String>> countersAggtype = new HashMap<>();
		for (int i = 0; i < parsedCounters.size(); i++) {
			if (isAlpha(parsedCounters.get(i))) {				
				Set<String> aggSet = null;
				if (null == countersAggtype.get(parsedCounters.get(i + 1))) {
					aggSet = new HashSet<>();
				} else {
					aggSet = countersAggtype.get(parsedCounters.get(i + 1));
				}
				aggSet.add(parsedCounters.get(i));
				countersAggtype.put(parsedCounters.get(i + 1), aggSet);			
				
			}
		}

		countersAggtype.forEach((c, val) -> {
			System.out.println(c + " : " + val.toString());
		});

		return countersAggtype;

	}

	public boolean isAlpha(String name) {
		return name.matches("[a-zA-Z]+");
	}

}
