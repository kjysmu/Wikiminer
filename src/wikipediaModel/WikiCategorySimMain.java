package wikipediaModel;

public class WikiCategorySimMain {
	
	public static void main(String args[]) throws Exception{
		
		
		WikiCategorySim categorySim = new WikiCategorySim();
		
		categorySim.setSubWeight(0.0);
		categorySim.run();
		categorySim.setSubWeight(0.1);
		categorySim.run();
		categorySim.setSubWeight(0.2);
		categorySim.run();
		categorySim.setSubWeight(0.3);
		categorySim.run();
		categorySim.setSubWeight(0.4);
		categorySim.run();
		categorySim.setSubWeight(0.5);
		categorySim.run();
		categorySim.setSubWeight(0.6);
		categorySim.run();
		categorySim.setSubWeight(0.7);
		categorySim.run();
		categorySim.setSubWeight(0.8);
		categorySim.run();
		categorySim.setSubWeight(0.9);
		categorySim.run();
		categorySim.setSubWeight(1.0);
		categorySim.run();
		
		System.out.println("complete");
		
	}

}
