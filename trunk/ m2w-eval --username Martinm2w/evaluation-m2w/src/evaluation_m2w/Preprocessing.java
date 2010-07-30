package evaluation_m2w;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Preprocessing {

	public static void main(String[] args) throws IOException{
		
		Preprocessing p = new Preprocessing();
		
		//p.parseTskCtrl();
		p.parseExpDis(args[0], args[1], args[2], args[3]);
		
	}
	
	public void parseExpDis (String input1, String input2, String input3, String input4) {
		
		String human_file = input1; // "D:/m2w cs/evaldata/input_log/older/expressive_disagreement_lauren_3"
		String auto_file = input2; // "D:/m2w cs/evaldata/input_log/older/expressive_disagreement_auto_3"
		
		String human_preprocessed = input3; //"D:/m2w cs/evaluation-m2w/src/preprocessed/expressive_disagreement_lauren_3.p"
		String auto_preprocessed = input4; //"D:/m2w cs/evaluation-m2w/src/preprocessed/expressive_disagreement_auto_3.p"
		
		ArrayList<ArrayList<String>> HList = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> AList = new ArrayList<ArrayList<String>>();
		
		ArrayList<Integer> delHList = new ArrayList<Integer>();
		ArrayList<Integer> delAList = new ArrayList<Integer>();
		
		
			
		
		
		try {
			BufferedReader hbr = new BufferedReader(new FileReader(human_file));
			BufferedReader abr = new BufferedReader(new FileReader(auto_file));
			
			PrintWriter hpw = new PrintWriter(human_preprocessed);
			PrintWriter apw = new PrintWriter(auto_preprocessed);
			
			String tempStrh = null; 
			String tempStra = null;
			
			
			/*1.1save human file into HList*/  // ok
			while((tempStrh = hbr.readLine()) != null ) {
				//System.out.println("tempStrh : " + tempStrh);
				
				if(tempStrh.contains("$$$$")){
					String tempsh = null;
					ArrayList<String> templist = new ArrayList<String>();
					templist.add(tempStrh);
					
					do{ 

                        hbr.mark(1000);
                        tempsh=hbr.readLine();
                        templist.add(tempsh);

                   }while(tempsh!=null && !tempsh.contains("%%%%%"));

                   hbr.reset();
                   
                   HList.add(templist);
					
				}
				
				if(tempStrh.contains("%%%")){
					String tempsh = null;
					ArrayList<String> templist = new ArrayList<String>();
					//templist.add(tempStrh);
					
					do{ 

                        hbr.mark(1000);
                        tempsh=hbr.readLine();
                        templist.add(tempsh);

                   }while(tempsh!=null && !tempsh.contains("$$$$$$"));
					//}while(tempsh!=null && ( tempsh.contains("The") || tempsh.contains("+++++") || tempsh.contains("calculate") || tempsh.contains("qt_thrs")));
                   hbr.reset();
                   
                   HList.add(templist);
					
				}
				
			} //while
				
			/*1.2save auto file into AList*/ //ok
			while((tempStra = abr.readLine()) != null ) {
				
				if(tempStra.contains("$$$$")){
					String tempsa = null;
					ArrayList<String> templist = new ArrayList<String>();
					//templist.add(tempStra);
					
					do{ 

                        abr.mark(1000);
                        tempsa=abr.readLine();
                        templist.add(tempsa);

                   }while(tempsa!=null && !tempsa.contains("$$$$$$$$"));

                   abr.reset();
                   
                   AList.add(templist);
					
				}
				
			} //while
				
			
			System.out.println("Hlist size is :"+ HList.size());
			System.out.println("Alist size is :"+ AList.size());
			
			/*2.1parse human, delete -1 */ //ok
			for (int i = 0; i < HList.size(); i ++){
				for(int j = 0 ; j < HList.get(i).size(); j++){
					String tempStr = HList.get(i).get(j).toString(); //nullpointer
					if (tempStr.contains("-1")){
						delHList.add(i);
						delAList.add(i);
						break;
					}
				}
			}
			
			for(int i = 0; i < delHList.size(); i ++){
				
				System.out.println("delHList :"+ delHList.get(i));
				System.out.println("delAList :"+ delAList.get(i));
				
				int a = delHList.get(i) - i; // calculate  index after size shrink
				int b = delAList.get(i) - i;
				
				HList.remove(a);
				AList.remove(b);
				
			}
			
			delHList.clear();
			delAList.clear();
			
			
			/*2.1parse auto, delete -1*/ //ok
			for (int i = 0; i < AList.size(); i ++){
				for(int j = 0 ; j < AList.get(i).size(); j++){
					
					String tempStr = AList.get(i).get(j).toString();
					
					if (tempStr.contains("-1")){
						delAList.add(i);
						delHList.add(i);
						break;
					}
				}
			}
			
			for(int i = 0; i < delAList.size(); i ++){
				
				System.out.println("delHList 2:"+ delHList.get(i));
				System.out.println("delAList 2:"+ delAList.get(i));
				
				int a = delHList.get(i) - i; 
				int b = delAList.get(i) - i;
				
				HList.remove(a);
				AList.remove(b);
			}
			
			delHList.clear();
			delAList.clear();	
				
				
			/*3.write to files*/  //ok
			for (int i = 0; i < HList.size(); i ++) {
				for (int j = 0 ; j < HList.get(i).size(); j++){
					hpw.println(HList.get(i).get(j));
					//System.out.println(HList.get(i).get(j).toString());
				}
			}
			hpw.close();
			
			for (int i = 0; i < AList.size(); i ++) {
				for (int j = 0 ; j < AList.get(i).size(); j++){
					apw.println(AList.get(i).get(j));
					//System.out.println(AList.get(i).get(j).toString());
				}
			}
			apw.close();
			
			
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
				
				
				
		}

			
	public void parseTskCtrl () { // ok
		String human_file = "D:/m2w cs/evaluation/src/input_files/task_control_5_Lauren_annotated_7";
		String auto_file = "D:/m2w cs/evaluation/src/input_files/task_control_5_automated_7";
		
		String human_preprocessed = "D:/m2w cs/evaluation/src/preprocessed/task_control_5_Lauren_annotated_7.p";
		String auto_preprocessed = "D:/m2w cs/evaluation/src/preprocessed/task_control_5_automated_7.p";
		
		ArrayList<ArrayList<String>> HList = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> AList = new ArrayList<ArrayList<String>>();
		
		ArrayList<Integer> delHList = new ArrayList<Integer>();
		ArrayList<Integer> delAList = new ArrayList<Integer>();
		
				
		try {
			
			BufferedReader hbr = new BufferedReader(new FileReader(human_file));
			BufferedReader abr = new BufferedReader(new FileReader(auto_file));
			
			PrintWriter hpw = new PrintWriter(human_preprocessed);
			PrintWriter apw = new PrintWriter(auto_preprocessed);
			
			String tempStrh = null; 
			String tempStra = null;
			
			/*1.1save human file into HList*/  // ok
			while((tempStrh = hbr.readLine()) != null ) {
			//	System.out.println("tempStrh : " + tempStrh);
				
				if(tempStrh.contains("$$$$")){
					String tempsh = null;
					ArrayList<String> templist = new ArrayList<String>();
					templist.add(tempStrh);
					
					do{ 

                        hbr.mark(1000);
                        tempsh=hbr.readLine();
                        templist.add(tempsh);

                   }while(tempsh!=null && !tempsh.contains("%%%%%"));

                   hbr.reset();
                   
                   HList.add(templist);
					
				}
				
				if(tempStrh.contains("%%%")){
					String tempsh = null;
					ArrayList<String> templist = new ArrayList<String>();
					templist.add(tempStrh);
					
					do{ 

                        hbr.mark(1000);
                        tempsh=hbr.readLine();
                        templist.add(tempsh);

                   }while(tempsh!=null && ( tempsh.contains("The") || tempsh.contains("+++++") || tempsh.contains("calculate") || tempsh.contains("qt_thrs")));

                   hbr.reset();
                   
                   HList.add(templist);
					
				}
				
			} //while
			
			/*1.2save auto file into AList*/ //ok
			while((tempStra = abr.readLine()) != null ) {
				
				if(tempStra.contains("$$$$")){
					
					String tempsa = null;
					ArrayList<String> templist = new ArrayList<String>();
					templist.add(tempStra);
					
					do{ 

                        abr.mark(1000);
                        tempsa=abr.readLine();
                        templist.add(tempsa);

                   }while(tempsa!=null && !tempsa.contains("%%%%%%%%"));

                   abr.reset();
                   
                   AList.add(templist);
					
				}
				
				
				
				if(tempStra.contains("%%%")){
					
					String tempsa = null;
					ArrayList<String> templist = new ArrayList<String>();
					templist.add(tempStra);
					
					do{ 

                        abr.mark(1000);
                        tempsa=abr.readLine();
                        templist.add(tempsa);

                   }while(tempsa!=null && ( tempsa.contains("The") || tempsa.contains("++++") || tempsa.contains("calculate") || tempsa.contains("qt_thrs") ) );

                   abr.reset();
                   
                   AList.add(templist);
					
				}
				
			} //while
			
			System.out.println("Hlist size is :"+ HList.size());
			System.out.println("Alist size is :"+ AList.size());
			
			
			/*2.1parse human, delete -1 */ //ok
			for (int i = 0; i < HList.size(); i ++){
				for(int j = 0 ; j < HList.get(i).size(); j++){
					String tempStr = HList.get(i).get(j).toString();
					if (tempStr.contains("-1")){
						delHList.add(i);
						delAList.add(i);
						break;
					}
				}
			}
			
			for(int i = 0; i < delHList.size(); i ++){
				
				System.out.println("delHList :"+ delHList.get(i));
				System.out.println("delAList :"+ delAList.get(i));
				
				int a = delHList.get(i) - i; // calculate  index after size shrink
				int b = delAList.get(i) - i;
				
				HList.remove(a);
				AList.remove(b);
				
			}
			
			delHList.clear();
			delAList.clear();
			
			
			/*2.1parse auto, delete -1*/ //ok
			for (int i = 0; i < AList.size(); i ++){
				for(int j = 0 ; j < AList.get(i).size(); j++){
					
					String tempStr = AList.get(i).get(j).toString();
					
					if (tempStr.contains("-1")){
						delAList.add(i);
						delHList.add(i);
						break;
					}
				}
			}
			
			for(int i = 0; i < delAList.size(); i ++){
				
				System.out.println("delHList 2:"+ delHList.get(i));
				System.out.println("delAList 2:"+ delAList.get(i));
				
				int a = delHList.get(i) - i; 
				int b = delAList.get(i) - i;
				
				HList.remove(a);
				AList.remove(b);
			}
			
			delHList.clear();
			delAList.clear();
			
			/*3.write to files*/  //ok
			for (int i = 0; i < HList.size(); i ++) {
				for (int j = 0 ; j < HList.get(i).size(); j++){
					hpw.println(HList.get(i).get(j));
					//System.out.println(HList.get(i).get(j).toString());
				}
			}
			hpw.close();
			
			for (int i = 0; i < AList.size(); i ++) {
				for (int j = 0 ; j < AList.get(i).size(); j++){
					apw.println(AList.get(i).get(j));
					//System.out.println(AList.get(i).get(j).toString());
				}
			}
			apw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
	}

	}




/*	
						humBr.mark(1000);
						String checkLine = humBr.readLine();
						humBr.reset();
						
						if checkline is empty
						if (checkLine.equals("")){
							
							System.out.println("============human has not qt_thrs==============");
							
							read out extra lines from human
							String lines = "";
							String linesA = "";
							do{ //read out extra lines
								humBr.mark(1000);
                                lines = humBr.readLine();
                                System.err.println("human deleted:" + lines);
                                
                           }while(lines !=null && !lines.contains("$"));
							humBr.reset();
							
							
							read out extra lines from auto
						
								do{ //read out extra lines
									autoBr.mark(1000);
	                                linesA = autoBr.readLine();
	                                System.err.println("auto deleted:"+ linesA);
	                                
	                           }while(linesA !=null && !linesA.contains("$"));
								autoBr.reset();
							
							
							continue;  outter while
							}*/
						
						
					/*	if check line has qt_thrs
						if (checkLine.contains("qt_thrs")){
							
							System.out.println("=================human has qt_thrs===========================");
							
							check the auto line
							while((tempStrAt = autoBr.readLine()) != null && !tempStrAt.equals("$$$$$") ){
								autoList.add(tempStrAt);
								
								if (tempStrAt.contains("calculate Expressive Disagreement -")){
								
								autoBr.mark(1000);
								String checkLineA = autoBr.readLine();
								autoBr.reset();
								
								if (checkLineA.equals("")){
									
									System.out.println("--------------------auto has not qt_thrs-----------------------");
									
									String lines = "";
									String linesA = "";
									
									read out extra data from human
									do{ //read out extra lines
										humBr.mark(1000);
		                                lines = humBr.readLine();
		                                
		                           }while(lines !=null && !lines.contains("$$$$$$"));
									humBr.reset();
									
									
									read out extra lines from auto
									do{ //read out extra lines
										autoBr.mark(1000);
		                                linesA = autoBr.readLine();
		                                
		                           }while(linesA !=null && !linesA.contains("$$$$$$"));
									autoBr.reset();
									
									break;
								} if (checkLineA.equals(""))
								
								
								if (checkLineA.contains("qt_thrs")){
									
									System.out.println("---------------auto line has qt_thrs too-------------------  ");
									
									
									save the lines of human
									String lines = "";
									do{ //read out extra lines
										humBr.mark(1000);
		                                lines = humBr.readLine();
		                                humanList.add(lines);
		                                
		                           }while(lines !=null && !lines.contains("$$$$$$$$"));
									humBr.reset();
									
									
									
									save the lines of auto
									String linesA = "";
									
									do{ //read out extra lines
										autoBr.mark(1000);
		                                linesA = autoBr.readLine();
		                                autoList.add(lines);
		                                
		                           }while(linesA !=null && !linesA.contains("$$$$$$$$"));
										autoBr.reset();
									
								} if (checkLineA.contains("qt_thrs"))
								
							  } if (tempStrAt.contains("calculate Expressive Disagreement -")){
							
							} while loop
							
							
							
							
							continue;  outter while
						}
						
					}
					
					
					
					
					
					
				}*/
		
	/*			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		try {
			BufferedWriter bwh = new BufferedWriter(new FileWriter(human_preprocessed));
			BufferedWriter bwa = new BufferedWriter(new FileWriter(auto_preprocessed));
			
			write to human file
			for(int i = 0; i < humanList.size(); i ++){
				bwh.write(humanList.get(i).toString() + "\n");
			}
			bwh.close();
			
			write to auto file
			for(int i = 0; i < autoList.size(); i ++){
				bwa.write(autoList.get(i).toString()+ "\n");
			}
			bwa.close();
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}*/
