package demo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Demo extends DemoParent{
	int id;
	String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Demo [id=" + id + ", name=" + name + "]";
	}	
	
}

class DemoParent{
	int parent ;

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "DemoParent [parent=" + parent + "]";
	}
}


public class Test {

	public void dataPrint(DemoParent dp){
		
		System.out.println(dp);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*List l1 = new ArrayList<Object>();
		l1.add(1);
		l1.add(2);
		List l2 = new ArrayList<Object>();
		l2.add(3);
		l2.add(4);
		
		List l3 = new ArrayList<>();
		l3.addAll(l1);
		l3.addAll(l2);
		
		System.out.println(l3.toString());
		*/
		
		//dataPrint()
		
		Test test = new Test();
		//test.regularEXpressionForonlyDigis();
		
		//test.sortListBasisOnDatetimestring();
		//test.divide();
		//test.looptesting();
		//test.listBehaviour();
		test.hashmapBehavr();
		//GzipFilter
	}
	
	void hashmapBehavr(){
		Map<String, Object> eventMap = new HashMap<>();
		eventMap.put("55", null);
		eventMap.forEach((k,v)->{  
			System.out.println(k + " : "+v);
		});
		eventMap.put("55", "abcg");
		
		eventMap.forEach((k,v)->{  
			System.out.println(k + " : "+v);
		});
	}
	
	void listBehaviour(){
		Demo demo1 = new Demo();
		demo1.setId(1);
		Demo demo2 = new Demo();
		demo1.setId(2);
		Demo demo3 = new Demo();
		demo1.setId(3);
		
		List<Demo> list = new ArrayList<>();
		
		list.add(demo1);
		list.add(demo2);
		
		System.out.println(list.size());
		
		demo1.setName("demo1");
		list.set(0, demo1);
		
		System.out.println(list.size());
		
		
	}
	
	void looptesting(){
		System.out.println();
		
		
		long iterations = 0;
        // Count iterations from 100 to 200 decrementing.
        for (int u = 100; u <= 200; u--) {
        	
            iterations++;
        }
        System.out.println("Iterations from 100 to 200: " + iterations);
        
        long iterations1 = 0;
        // Count iterations from 100 to 200 decrementing.
        for (int u = 0; u <= 100; u++) {
            iterations1++;
        }
        System.out.println("Iterations from 100 to 200: " + iterations1);
	}
	
	void regularEXpressionForonlyDigis(){
	
		String str = "117498163";
		String s2= "117498162";
		String s3 = "timestamp";
		String s4 = "SubElementID";
		String s5 = "count_3765476584543";
		
		String regex = "[0-9]+";
		

		String regex1 = "\\d+";
		
		System.out.println(str.matches(regex));
		System.out.println(str.matches(regex1));
		
		System.out.println(s3.matches(regex));
		System.out.println(s3.matches(regex1));
		
		System.out.println(s5.matches(regex));
		System.out.println(s5.matches(regex1));
		
		System.out.println();
		
	}
	
	void divide(){
		Double avrg = new Double(0); 
		Object obj1 = "1234.0";
		Object obj2 = "12";
		avrg = ((Double.parseDouble((String) obj1)) / ((Double.parseDouble((String) obj2))));
		
		System.out.println(avrg);
	}
	
	void sortListBasisOnDatetimestring(){
		
		List<String> str = new ArrayList<>();
		str.add("2017-07-20T09:41:00.000Z");
		str.add("2017-07-19T05:56:00.000Z");
		str.add("2017-07-18T05:21:00.000Z");
		str.add("2017-07-21T05:21:00.000Z");
		str.add("2017-07-18T05:00:33.000Z");
		
		System.out.println(str);
		
		
		Collections.sort(str, new Comparator<String>() {
			DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");

			@Override
			public int compare(String o1, String o2) {
				try {
					return f.parse(o1).compareTo(f.parse(o2));
				} catch (ParseException e) {
					throw new IllegalArgumentException(e);
				}
			}
		});

		
		System.out.println("after sort :: "+str.toString());
		
	}

}
