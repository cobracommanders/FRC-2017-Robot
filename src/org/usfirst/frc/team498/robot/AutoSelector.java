package org.usfirst.frc.team498.robot;


//depreciated forms of previous auto selectors
public enum AutoSelector {
	DRIVEFORWARD, MIDPEG, TOPPEG, BOTPEG //In relation to the BOILER, where the BOILER is the bottom. From RED ALLIANCE
	}
/*
public class Shoes{
	public Shoes(){

		Integer[] m=new Integer[10];
		m[0]=1;
		m[1]=2;
		m[2]=3;
		m[3]=5;
		m[4]=6;
		m[5]=6;
		m[6]=7;
		m[7]=8;
		m[8]=90;
		m[9]=10;
				
		//This is totally the most serious work I could be doing right now.
		//Mostly because I don't know what I am doing.
		//Nobody is paying enough attention to me to worry about what I'm doing, though
		//I could literally be making a program designed to delete all the files on everything ever.
		//They would never know, because they aren't paying enough attention.
		//I have no idea what to say now.
		//Plz help.
		//Randy, I need a job.
		//Plz
		//Right now.
 		
		ArrayList<Integer> goodItems=QueryEngine.Where(m, new QueryWhere<Integer>() {

			@Override
			public boolean WhereLambda(Integer item) {
				if(item>5){
					return true;
				}
				else{
					return false;
				}
			}
		});
 		

	
	
		
		ArrayList<Drive2016> goodItems=QueryEngine.Where(m, new QueryWhere<Drive2016>() {

			@Override
			public boolean WhereLambda(Drive2016 item) {
				if(item.speedCap%2==0){
					return true;
				}
				else{
					return false;
				}
			}
		});
 		
}
	 
	}


public class QueryEngine{
	public  static  <T> ArrayList<T> Where(T[] items, QueryWhere<T> whereQuery){
		ArrayList<T> goodItems=new ArrayList<T>();
		for(int j=0;j<items.length;j++){
			if(whereQuery.WhereLambda(items[j])){
				goodItems.add(items[j]);
			}
		}
		return goodItems;
	}
	public  static  <T> T First(T[] items, QueryWhere<T> whereQuery){
		ArrayList<T> goodItems=new ArrayList<T>();
		for(int j=0;j<items.length;j++){
			if(whereQuery.WhereLambda(items[j])){
				return items[j];
			}
		}
		return n;
	}
}
 public interface QueryWhere<T>{
	boolean WhereLambda(T item);
	}
 */