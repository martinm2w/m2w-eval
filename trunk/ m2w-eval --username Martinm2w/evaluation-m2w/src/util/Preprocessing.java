package util;

import java.io.BufferedReader;
//import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
//import java.io.FileWriter;
import java.io.IOException;
//import java.io.OutputStreamWriter;
import java.io.PrintWriter;
//import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * m2w: things to do before preprocessing: replace all the " of " string in file with " ";
 * @author m2w
 * @date 2011.02.17
 */
public class Preprocessing {
    
    private String input;
    private String output;
    private String pre_input;
//    private String pre_input2;

	public static void main(String[] args) throws IOException{
		
		Preprocessing p = new Preprocessing();
                p.setInput("/home/ruobo/NetBeansProjects/evaluation-m2w/src/input_files/scil_annotated_withHalfDGR_jessamyn_leadership_4_ngt.txt");
                p.setOutput("/home/ruobo/NetBeansProjects/evaluation-m2w/src/preprocessed/MANUAL_inv_scil_annotated_withHalfDGR_jessamyn_leadership_4_ngt");
                p.setPre_input("/home/ruobo/NetBeansProjects/evaluation-m2w/src/preprocessed/pre_input");

                p.parseInvQt();
		


}

//        ==============================================new methods===============================================

        /**
         * m2w: this method parses the qt of involvment. make it ready for manual eval
         */
        public void parseInvQt(){

            ArrayList<String> theList = this.parseUsePart("processing Involvement...");
//            String pre_input = this.getPre_input();
            String output = this.getOutput();
            String tempStr = null;
            String tempFileName = null;
            
                for(int i = 0; i < theList.size(); i++){
                    tempStr = theList.get(i);

                    if(tempStr.startsWith("processing:")){// if starts with "processing:" then get the file name and make it looks like Feb19B
                        
                    }

                    if(tempStr.length() < 6){// if length < 6 , get the cat name , and make the line, add % , so we will only print lines ends with $ or % to the file.
                        
                    }

                    if(tempStr.startsWith("The")){// if starts with "The", then split " " and get [3] and [5], and add $
                        theList.set(i, (tempStr.split(" ", -1)[3] + "\t" + tempStr.split(" ", -1)[5]) + "\t$")  ;
                    }

                    
                    // parse the other stuff, and then , make the empty lines when writing to a file.
                    // keep the arraylist simple.
                }
            System.out.println(theList);
            
            
        }

        public void parseTskQt(String input, String output){
            
        }




//         ===============================================util methods============================================

        /**
         * m2w : this method is for parsing the useful lines for certain category preprocessing. cuz different category has different formats.
         * @param CatgString : this string is the line that indicates the beginning of a category.
         */
        public ArrayList<String> parseUsePart(String CatgString){

            String input = this.getInput();
            String pre_input = this.getPre_input();
            String cat = CatgString;
            
		ArrayList<String> theList = new ArrayList<String>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(input));
			PrintWriter pr = new PrintWriter(pre_input);

			String tempStr = null;
			while((tempStr = br.readLine()) != null ) {//1st. whole file

                            if(tempStr.contains("processing:")){// if temp string contains processing:, then save the file name to list;
                                theList.add(tempStr);
                            }

                            if(tempStr.equals(cat)){//if tempStr equals CatgString, start saving to list
                                theList.add(tempStr);
                                String thisLine = null;

                                while(!(thisLine = br.readLine()).equals(null) && !thisLine.contains("processing") ){//while temp string not reaches next processing, mark, save to list
                                    br.mark(1000);
//                                    thisLine
                                    if(!thisLine.equals("")//blank lines
                                    && !thisLine.contains("=")//inv
                                    && !thisLine.contains("+")//inv
                                    && !thisLine.contains("set")//tpc
                                    && !thisLine.contains("%")//tsk
                                    && !thisLine.contains("total")){// tsk
                                    theList.add(thisLine);
                                    }
                                }
                                
                                br.reset();
                            }
			}//1st
			br.close();

			for(int i = 0; i < theList.size(); i ++){// print to pre_in file
                                pr.println(theList.get(i));
			}

			pr.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

                return theList;
            
        }
















	//	========================================old methods===============================================
	/**
	 * m2w :this method is for parsing the data out of the file to do NIST MANUAL eval, it's topic control, actual data
	 *
	 * @param inputFile
	 * @param outputFile
	 */
	public void parseManualEvalActTpc(String inputFile, String outputFile){
		
		String input = inputFile;
		String output = outputFile;
		ArrayList<String> theList = new ArrayList<String>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			PrintWriter pr = new PrintWriter(outputFile);
			
			
			String tempStr = null;
			while((tempStr = br.readLine()) != null ) {//1st. whole file
				theList.add(tempStr);
				
				
				
				
				
				/*if (tempStr.contains("processing:")){
					if (tempStr.contains("processing: daLog_2009_05_05.xml")){// this file is not in the post session
						break;
					}
					String fileName = (tempStr.split(" ")[1].split("_")[0] + tempStr.split(" ")[1].split("_",-2)[1].split("p")[1]); //get file name like March13B
					String tempSubStr = null;//for substring in title
					
					while((tempSubStr = br.readLine()) != null){//2nd. in each file
						br.mark(1000);
						if(tempSubStr.toLowerCase().contains("processing ")){
							String tempCatName = tempSubStr.toLowerCase().split(" ")[1].split("...")[0];
							String catName = null;
							if (tempCatName.equals("task control")){
								catName = "tsk";
							}
							if (tempCatName.equals("topic control")){
								catName = "tpc";
							}
							if (tempCatName.equals("involvement")){
								catName = "inv";
							}
							else catName = "bad";
							
							if (tempCatName.equals("task control") || tempCatName.equals("topic control") || tempCatName.equals("involvement")){
								while((tempSubStr = br.readLine()) != null){// in each category
									br.mark(1000);
									if (tempSubStr.contains(s)){
										
									}
									
									
									
								}//3rd 
								br.reset();
							}
						}
						
						fileName = null;
					}//2nd
					br.reset();
					
				}//if*/
			}//1st
			br.close();
			
			for(int i = 0; i < theList.size(); i ++){
				
				if (theList.get(i).contains("_")){
					String tempListStr = theList.get(i);
					tempListStr = tempListStr.split("_")[0] + "\t" + tempListStr.split("_")[1];
					theList.set(i, tempListStr);
				}
			}
			
			for(int i = 0; i < theList.size(); i ++){
				pr.println(theList.get(i));
			}
			
			pr.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	

	
	
	public void parseExpDisForMatch (String input1, String input2, String input3, String input4) {
		
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
			
			/*1.1 save human file into HList*/  // ok
			while((tempStrh = hbr.readLine()) != null ) {
				//System.out.println("tempStrh : " + tempStrh);
				
				if(tempStrh.contains("processing:")){  ///if it's a new file

					ArrayList<String> templist = new ArrayList<String>();
					templist.add(tempStrh);
					
					do{ 

                        hbr.mark(1000);
                        tempStrh=hbr.readLine();
                        templist.add(tempStrh);

                   }while(tempStrh!=null && !tempStrh.contains("%%%%"));
					
                   hbr.reset();
                   
                   HList.add(templist);
					//System.err.println("tempsh is :" + tempStrh);
				}
				
				
				
				
				if(tempStrh.contains("+++++")){  /// if new calculate block
					
					//String tempsh = null;
					ArrayList<String> templist = new ArrayList<String>();
					templist.add(tempStrh);
					
					do{ 

                        hbr.mark(1000);
                        tempStrh=hbr.readLine();
                        templist.add(tempStrh);

                   }while(tempStrh!=null && !tempStrh.contains("$$$"));
					
                  
					

	                     hbr.reset();
	                     HList.add(templist);
				}
			} //while
			
//			/*1.1save human file into HList*/  // ok
//			while((tempStrh = hbr.readLine()) != null ) {
//				//System.out.println("tempStrh : " + tempStrh);
//				
//				if(tempStrh.contains("processing:")){  ///if it's a new file
//
//					ArrayList<String> templist = new ArrayList<String>();
//					templist.add(tempStrh);
//					
//					do{ 
//
//                        hbr.mark(1000);
//                        tempStrh=hbr.readLine();
//                        templist.add(tempStrh);
//
//                   }while(tempStrh!=null && !tempStrh.contains("%%%%"));
//					
//                   hbr.reset();
//                   
//                   HList.add(templist);
//					//System.err.println("tempsh is :" + tempStrh);
//				}
//				
//				
//				
//				
//				if(tempStrh.contains("+++++")){  /// if new calculate block
//					
//					//String tempsh = null;
//					ArrayList<String> templist = new ArrayList<String>();
//					templist.add(tempStrh);
//					
//					while((tempStrh = hbr.readLine()) != null && !tempStrh.contains("$$$")){
//						
//						 hbr.mark(1000);
//	                     templist.add(tempStrh);
//					}
//	                     hbr.reset();
//	                     HList.add(templist);
//				}	
//			} //while
				
			/*1.2save auto file into AList*/ //
			while((tempStra = abr.readLine()) != null ) {
				
				if(tempStra.contains("processing:")){ /// if is new file
					
					//String tempStra = null;
					ArrayList<String> templist = new ArrayList<String>();
					templist.add(tempStra);
					
					do{ 

                        abr.mark(1000);
                        tempStra=abr.readLine();
                        templist.add(tempStra);

                   }while(tempStra!=null && !tempStra.contains("%%%%%%%%"));

                   abr.reset();
                   
                   AList.add(templist);
					
				}
				
				
				
				if(tempStra.contains("+++")){ /// if is a category in a file
					
					//String tempsa = null;
					ArrayList<String> templist = new ArrayList<String>();
					templist.add(tempStra);
					
//					while((tempStra = abr.readLine()) != null && !tempStra.contains("$$$$")){
//						
//						abr.mark(1000);
//						templist.add(tempStra);
//					}
					do{ 

                        abr.mark(1000);
                        tempStra=abr.readLine();
                        templist.add(tempStra);

                   }while(tempStra!=null && ( !tempStra.contains("$$$")));
				

                   abr.reset();
                   
                   AList.add(templist);
					
				}
				
			} //while
				
			
			System.out.println("Hlist size is :"+ HList.size());
			System.out.println("Alist size is :"+ AList.size());
			
			hbr.close();
			abr.close();
			
			/*2.1parse human, delete -1 */ //ok
			for (int i = 0; i < HList.size(); i ++){
				for(int j = 0 ; j < HList.get(i).size() - 1; j++){
					String tempStr = HList.get(i).get(j).toString(); //nullpointer
					
					System.out.println(tempStr);
					
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
			
			
			
			/*2.2parse auto, delete -1*/ //ok
			for (int i = 0; i < AList.size(); i ++){
				for(int j = 0 ; j < AList.get(i).size() - 1; j++){
					
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
				

			System.out.println("Hlist final size : " + HList.size());
			System.out.println("Alist final size : " + AList.size());
			
			
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

			
	public void parseTskCtrl (String input1, String input2, String input3, String input4) { // ok
		String human_file = input1; //"D:/m2w cs/evaluation/src/input_files/task_control_5_Lauren_annotated_7";
		String auto_file = input2; //"D:/m2w cs/evaluation/src/input_files/task_control_5_automated_7";
		
		String human_preprocessed = input3; //"D:/m2w cs/evaluation/src/preprocessed/task_control_5_Lauren_annotated_7.p";
		String auto_preprocessed = input4; //"D:/m2w cs/evaluation/src/preprocessed/task_control_5_automated_7.p";
		
		ArrayList<ArrayList<String>> HList = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> AList = new ArrayList<ArrayList<String>>();
		
		ArrayList<Integer> delHList = new ArrayList<Integer>();
		ArrayList<Integer> delAList = new ArrayList<Integer>();
		
		ArrayList<Integer> addList = new ArrayList<Integer>();
		
				
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

                   }while(tempsh!=null && !tempsh.contains("total_ac_oc_:"));

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

                   }while(tempsa!=null && !tempsa.contains("total_ac_oc_:"));//header sensor changed to total_ac_oc_:

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
			System.out.println("Hlist 1 1  is :"+ HList.get(0).get(0));
			
			
			/*2.1parse human, delete -1 */ //ok
			for (int i = 0; i < HList.size(); i ++){
				for(int j = 0 ; j < HList.get(i).size(); j++){
					String tempStr = HList.get(i).get(j);
//					System.out.println(tempStr);
					if ((tempStr != null) && tempStr.contains("-1")){
						
						System.out.println(i);
						
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
			
			
			/*2.1parse auto, add actual score = 0*/ //ok
			for (int i = 0; i < AList.size(); i ++){
				for(int j = 0 ; j < AList.get(i).size(); j++){
					
					String tempStr = AList.get(i).get(j);
					
					if ((tempStr != null) && tempStr.contains("-1")){
						
						AList.get(i).set(j, tempStr + " --- actual score: 0");
						
						continue;	
					}
									
				}
				
			}
			
//			for(int i = 0; i < delAList.size(); i ++){
//				
//				System.out.println("delHList 2:"+ delHList.get(i));
//				System.out.println("delAList 2:"+ delAList.get(i));
//				
//				int a = delHList.get(i) - i; 
//				int b = delAList.get(i) - i;
//				
//				HList.remove(a);
//				AList.remove(b);
//			}
//			
//			delHList.clear();
//			elAList.clear();
			
			
			System.out.println("Hlist final size : " + HList.size());
			System.out.println("Alist final size : " + AList.size());
					
			/*2.3.1 new, add qt_thrs: 0.0 to -1 categories*/
			for (int i = 0; i < AList.size(); i ++){//get category number needs to be added.
				for(int j = 0 ; j < AList.get(i).size(); j++){
			
					String tempStr = AList.get(i).get(j).toString();
					
					if (tempStr.contains("-1")){
					
						addList.add(i);
						break;
					}
					
				}
				
			}
			
			/*2.3.2 adding*/
			for (int i = 0; i < addList.size(); i ++){
				
				int catNum = addList.get(i);//get category number from add list
				
				for(int j = 0 ; j < AList.get(catNum).size(); j++){
					
					String tempStr = AList.get(catNum).get(j).toString(); // get line from category
					
					if (tempStr.contains("-1")){//add qt_thrs
						
						AList.get(catNum).add(j, "qt_thrs:  0.0 0.0 0.0 0.0 0.0"); // ���� j - 1 , �� j, ���뵽��-1 ����һ��
						break;
						
					}
										
				}
			}
					
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
			
			hbr.close();
			abr.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void parseExpDisForCompare (String input1, String input2, String input3, String input4) {
			
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
					
					if(tempStrh.contains("processing: ")){  ///if it's a new file

						ArrayList<String> templist = new ArrayList<String>();
						templist.add(tempStrh);
						
						do{ 

	                        hbr.mark(1000);
	                        tempStrh=hbr.readLine();
	                        templist.add(tempStrh);

	                   }while(tempStrh!=null && !tempStrh.contains("$$$$$$")); //�ĳ������block��ȡ
						
	                   hbr.reset();
	                   
	                   HList.add(templist);
						//System.err.println("tempsh is :" + tempStrh);
					}
					
					
					
					
//					if(tempStrh.contains("+++++")){  /// if new calculate block
//						
//						//String tempsh = null;
//						ArrayList<String> templist = new ArrayList<String>();
//						templist.add(tempStrh);
//						
//						do{ 
//
//	                        hbr.mark(1000);
//	                        tempStrh=hbr.readLine();
//	                        templist.add(tempStrh);
//
//	                   }while(tempStrh!=null && !tempStrh.contains("$$$"));
//						
//	                  
//						
//
//		                     hbr.reset();
//		                     HList.add(templist);
//					}
				} //while
					
				
				
				
				
				
				/*1.2save auto file into AList*/ //
				while((tempStra = abr.readLine()) != null ) {
					
					if(tempStra.contains("processing: ")){ /// if is new file
						
						//String tempStra = null;
						ArrayList<String> templist = new ArrayList<String>();
						templist.add(tempStra);
						
						do{ 

	                        abr.mark(1000);
	                        tempStra=abr.readLine();
	                        templist.add(tempStra);

	                   }while(tempStra!=null && !tempStra.contains("$$$$$$"));

	                   abr.reset();
	                   
	                   AList.add(templist);
						
					}
					
					
					
//					if(tempStra.contains("+++")){ /// if is a category in a file
//						
//						//String tempsa = null;
//						ArrayList<String> templist = new ArrayList<String>();
//						templist.add(tempStra);
//						
////						while((tempStra = abr.readLine()) != null && !tempStra.contains("$$$$")){
////							
////							abr.mark(1000);
////							templist.add(tempStra);
////						}
//						do{ 
//
//	                        abr.mark(1000);
//	                        tempStra=abr.readLine();
//	                        templist.add(tempStra);
//
//	                   }while(tempStra!=null && ( !tempStra.contains("$$$")));
//					
//
//	                   abr.reset();
//	                   
//	                   AList.add(templist);
//						
//					}
					
				} //while
					
				
				System.out.println("Hlist size is :"+ HList.size());
				System.out.println("Alist size is :"+ AList.size());
				System.out.println("Alist is :"+ AList);
				System.out.println("Hlist is :"+ HList);
				
				hbr.close();
				abr.close();
				
				/*2.1parse human, delete -1 */ //ok
				for (int i = 0; i < HList.size(); i ++){
					
					System.out.println("H:"+ HList.get(i).size());
					System.out.println("A:"+ AList.get(i).size());
					
					for(int j = 0 ; j < HList.get(i).size() -1; j++){
						
						String tempStr = HList.get(i).get(j); //nullpointer
						
						
						
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
				
				
				
				/*2.2parse auto, add actual score: 0 to -1 */ //ok
				for (int i = 0; i < AList.size(); i ++){
					for(int j = 0 ; j < AList.get(i).size() - 1; j++){
						
						String tempStr = AList.get(i).get(j).toString();
						
						if (tempStr.contains("-1")){
							
							AList.get(i).set(j, tempStr + " --- actual score: 0");
							continue;	
						}
					}
				}
//						if (tempStr.contains("-1")){
//							delAList.add(i);
//							delHList.add(i);
//							break;
//						}
//					}
//				}
//				
//				for(int i = 0; i < delAList.size(); i ++){
//					
//					System.out.println("delHList 2:"+ delHList.get(i));
//					System.out.println("delAList 2:"+ delAList.get(i));
//					
//					int a = delHList.get(i) - i; 
//					int b = delAList.get(i) - i;
//					
//					HList.remove(a);
//					AList.remove(b);
//				}
//				
//				delHList.clear();
//				delAList.clear();	
					

				System.out.println("Hlist final size : " + HList.size());
				System.out.println("Alist final size : " + AList.size());
				
				
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

    /**
     * @return the input
     */
    public String getInput() {
        return input;
    }

    /**
     * @param input the input to set
     */
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * @return the output
     */
    public String getOutput() {
        return output;
    }

    /**
     * @param output the output to set
     */
    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * @return the pre_input
     */
    public String getPre_input() {
        return pre_input;
    }

    /**
     * @param pre_input the pre_input to set
     */
    public void setPre_input(String pre_input) {
        this.pre_input = pre_input;
    }
		
		
		
	

	}


