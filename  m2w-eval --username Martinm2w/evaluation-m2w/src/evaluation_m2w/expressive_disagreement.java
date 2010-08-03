/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evaluation_m2w;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author m2w
 */
public class expressive_disagreement {


    //static String[] categories = {"DRX"};
    //static String[] speakers = {"kara_kerri", "ken_jennifer", "chris_jennifer", "ken_kerri", "jennifer_jenny",
    //  "jennifer_lance", "kara_jennifer", "jennifer_kerri", "chris_ken", "chris_kerri", "kara_chris"};


    static String[] categories;

    static String[] speakers;

    static HashMap<String, String> categories_map = new HashMap<String, String>();
    static HashMap<String, String> speakers_map = new HashMap<String, String>();

    static HashMap<String, String[]> human_qt_thrs = new HashMap<String, String[]>(); // key: category; value: qt_thrs array
    static HashMap<String, String[]> auto_qt_thrs = new HashMap<String, String[]>(); // key: category; value: qt_thrs array
    static HashMap<String, String[]> human_scores = new HashMap<String, String[]>(); // key: category; value: score, order by speakers
    static HashMap<String, String[]> auto_scores = new HashMap<String, String[]>(); // key: category; value: score, order by speakers
    static HashMap<String, int[]> human_quintile_scores = new HashMap<String, int[]>(); // key: category; value: quintile scores, order by speakers
    static HashMap<String, int[]> auto_quintile_scores = new HashMap<String, int[]>(); // key: category; value: quintile scores, order by speakers
    static HashMap<String, String[]> human_actual_scores = new HashMap<String, String[]>(); // key: category; value: actual scores, order by speakers
    static HashMap<String, String[]> auto_actual_scores = new HashMap<String, String[]>(); // key: category; value: actual scores, order by speakers


	private static boolean readerOpened=false;
	private static BufferedReader eia_br;


    public static void main(String[] args){

        String[] annotators = {"brian", "kerri", "lauren"};
        	
        String human_annotation = "D:/m2w cs/evaluation/src/input_files/expressive_disagreement_"+annotators[2]+"_3";
        String auto_annotation = "D:/m2w cs/evaluation/src/input_files/expressive_disagreement_auto_3";
        String evaluation_file = "D:/work/mine top/evaluation data/result/expressive_disagreement/expressive_disagreement_"+annotators[2]+"_3.result1";
        
            try { //extract names/topics
                BufferedReader br = new BufferedReader(new FileReader(human_annotation));

                // get names ===
                String tempstr;

                while((tempstr=br.readLine())!=null){
                    if(tempstr.startsWith("The quintile score of ")){//get speaker names /* && tempstr.matches(".*[A-Za-z]_[A-Za-z].*")*/s
                    		String curname = tempstr.split(" ")[4].toLowerCase(); //get current name
                                              
                        if( (speakers_map.get(curname)==null)){ //if speaker not yet in list
                            speakers_map.put(curname, curname); //put the name in corresponding index
                        }
                    }else if(tempstr.contains("calculate Expressive Disagreement")){//get topics
                    		String curCtg = tempstr.split(" ")[4].toLowerCase(); // get current topic
                    		
                        if( categories_map.get(curCtg)==null){ //if category not yet in list
                            categories_map.put(curCtg, curCtg); //then put in the map
                        }
                    }
                }
                
                
                // build speakers array
                Object[] speakerarray=speakers_map.keySet().toArray();
                speakers=new String[speakerarray.length];

                for(int ei=0;ei<speakerarray.length;ei++){
                    speakers[ei]=(String)speakerarray[ei];
                    System.out.println(speakers[ei]);
                }

                // build categories array
                Object[] categoriesarray=categories_map.keySet().toArray();// put keys into array
                categories=new String[categoriesarray.length];
                
                for(int ei=0;ei<categoriesarray.length;ei++){ // put all curCtg into categories
                    categories[ei]=(String)categoriesarray[ei];
                    System.err.println(categories[ei]);
                }

                br.close();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(topic_disagreement.class.getName()).log(Level.SEVERE, null, ex);
            }
              catch(IOException e){
                  e.printStackTrace();
            }
              
        init();

        extract_info_human(evaluation_file, human_annotation, auto_annotation);

		try {
			eia_br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
}



    public static void init(){

        speakers_map.clear();
        categories_map.clear();
        human_scores.clear();
        auto_scores.clear();
        human_qt_thrs.clear();
        auto_qt_thrs.clear();
        human_quintile_scores.clear();
        auto_quintile_scores.clear();
        human_actual_scores.clear();
        auto_actual_scores.clear();
    }
    
    
   
    public static void extract_info_human(String evaluation_file, String file, String auto_annotation){
        BufferedReader br;
        String tempStr = "";

        try {
           br = new BufferedReader(new FileReader(file));
           while ((tempStr = br.readLine()) != null){
        	   System.out.println(tempStr);
        	   
        	  if(tempStr.contains("calculate Expressive Disagreement -")){
        		  
        		 // String cat = tempStr;
        		  
        		 // if((tempStr = br.readLine()).contains("qt_thrs: ")){
                	  
                	  for(int i = 0; i < categories.length; i++){
                           if(tempStr.toLowerCase().contains(categories[i])){
                    	     
                    		
                    	 
                           String[] qt_thrs1 = tempStr.split("\\s+"); // \S+	Several non-whitespace characters
                           String[] qt_thrs_array = new String[qt_thrs1.length-1];
                           		for(int j = 0; j < qt_thrs_array.length; j++){
                               qt_thrs_array[j] = qt_thrs1[j+1];
                               //System.out.println(qt_thrs_array[j] + " " + categories[i]);
                           }
                           
                           
                           /*save qt_thrs*/ 
                           human_qt_thrs.put(categories[i], qt_thrs_array);

                          /*  save scores*/ 
                           String speaker = "";
                           String[] score_array = new String[speakers.length];

                           		do{ //read out extra lines
                                br.mark(1000);
                                speaker=br.readLine();
                                
                           		}while(speaker!=null && !speaker.contains("The quintile score") && !speaker.contains("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"));

                           		br.reset();

                           		while((speaker = br.readLine()) != null &&
                               speaker.contains("The quintile score")) {
                               
                           			for(int k = 0; k < speakers.length; k++){
                                  // String[] speakers_order = speakers;
                           				if(speaker.toLowerCase().contains(speakers[k])){
                                        score_array[k] = speaker.toLowerCase();
                                        System.out.println("score_array[" + k + "]: " + score_array[k]);
                                   }
                               }
                               
                               System.out.println(speaker);
                               System.out.println(score_array.toString());
                               
                           }
                           human_scores.put(categories[i], score_array);
                       
                    	   }
                    	   
                    	   
                    	   
                    	   
                      }	   
                       
                   }

                   System.err.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");

			extract_info_auto(evaluation_file, file, auto_annotation);

               }

           
          

           readerOpened=false; 
           eia_br.close(); 

           br.close();

           
           
        } catch (FileNotFoundException e) {
           e.printStackTrace();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }


 public static void extract_info_auto(String evaluation_file, String file, String auto_annotation){// (output, human, auto)
       // BufferedReader br;
        String tempStr = "";


        try {


        if(!readerOpened){

				eia_br = new BufferedReader(new FileReader(auto_annotation));
				readerOpened=true;

		}


           while ((tempStr = eia_br.readLine()) != null){
               if(tempStr.contains("calculate Expressive Disagreement -")){
                   for(int i = 0; i < categories.length; i++){
                       if(tempStr.toLowerCase().contains(categories[i])){
                           String qt_thrs = eia_br.readLine();
                           String[] qt_thrs1 = qt_thrs.split("\\s+");
                           String[] qt_thrs_array = new String[qt_thrs1.length-1];// put thrs in array
                           for(int j = 0; j < qt_thrs_array.length; j++){
                               qt_thrs_array[j] = qt_thrs1[j+1]; // scores starts from qt_thrs1 j+1 
                               //System.out.println(qt_thrs_array[j] + " " + categories[i]);
                           }
                           /* save qt_thrs */
                           auto_qt_thrs.put(categories[i], qt_thrs_array);

                           /* save scores */
                           String speaker = "";
                           String[] score_array = new String[speakers.length];


					//eia_br.readLine();eia_br.readLine();eia_br.readLine();


                           do{ //read out extra lines

                                eia_br.mark(1000);
                                speaker=eia_br.readLine();


                           }while(speaker!=null && !speaker.contains("The quintile score") && !speaker.contains("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"));

                           eia_br.reset();

                           while((speaker = eia_br.readLine())!=null && speaker.contains("The quintile score")) {
                               for(int k = 0; k < speakers.length; k++){
                            	   
                                   //String[] speakers_order = speakers[k].split("_");
                                   if(speaker.toLowerCase().contains(speakers[k])){
                                        score_array[k] = speaker.toLowerCase();
                                        //System.out.println("score_array[" + k + "]: " + score_array[k]);
                                   }
                               }
                           }
                       auto_scores.put(categories[i], score_array);
                       }
                   }

			save_human_quintile_scores();
            	save_auto_quintile_scores();
            	save_human_actual_scores();
            	save_auto_actual_scores();

 	            writeToEvaluation(evaluation_file, file, auto_annotation);
			init();
			break;


               }
           }

        } catch (FileNotFoundException e) {
           e.printStackTrace();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    public static void save_human_quintile_scores(){
        Object[] category = human_scores.keySet().toArray();
        for(int i = 0; i < category.length; i++){
            int[] quintile_score_array = new int[speakers.length];
            String[] scores = human_scores.get(category[i].toString());
            //System.out.println(scores.length + " length");
            for(int j = 0; j < scores.length; j++){
                //System.out.println(scores[j]);
                for(int k = 0; k < speakers.length; k++){
                    //String[] speakers_order = speakers[k].split("_");
                    if(scores[j] == null || scores[j].equals("")){
                        continue;
                    }
                    if(scores[j].contains(speakers[k])){
                        String speaker_scores = scores[j];
                        String[] tmp = speaker_scores.split("---");
                        String tmp2 = tmp[0].trim();
                        String[] tmp3 = tmp2.split(":");
                        String quintile_score = tmp3[1].trim();
                        quintile_score_array[k] = Integer.parseInt(quintile_score);
                        //System.out.println("quintile score for speaker " + speakers[k] + " is " + quintile_score_array[k]);

                    }
                }
            }
            human_quintile_scores.put(category[i].toString(), quintile_score_array);
        }
    }

    public static void save_auto_quintile_scores(){
        Object[] category = auto_scores.keySet().toArray();
        for(int i = 0; i < category.length; i++){
            int[] quintile_score_array = new int[speakers.length];
            String[] scores = auto_scores.get(category[i].toString());
            for(int j = 0; j < scores.length; j++){
                for(int k = 0; k < speakers.length; k++){
                    //String[] speakers_order = speakers[k].split("_");
                    if(scores[j] == null || scores[j].equals("")){
                        continue;
                    }
                    if(scores[j].contains(speakers[k])){
                        String speaker_scores = scores[j];
                        String[] tmp = speaker_scores.split("---");
                        String tmp2 = tmp[0].trim();
                        String[] tmp3 = tmp2.split(":");
                        String quintile_score = tmp3[1].trim();
                        quintile_score_array[k] = Integer.parseInt(quintile_score);
                    }
                }
            }
            auto_quintile_scores.put(category[i].toString(), quintile_score_array);
        }
    }

    public static void save_human_actual_scores(){
        Object[] category = human_scores.keySet().toArray();
        for(int i = 0; i < category.length; i++){
            String[] actual_score_array = new String[speakers.length];
            String[] scores = human_scores.get(category[i].toString());
            for(int j = 0; j < scores.length; j++){
                for(int k = 0; k < speakers.length; k++){
                   // String[] speakers_order = speakers[k].split("_");
                    if(scores[j] == null || scores[j].equals("")){
                        continue;
                    }
                    if(scores[j].contains(speakers[k])){

                        /*String speaker_scores = scores[j];
                        String[] tmp = speaker_scores.split("---");
                        String tmp2 = tmp[1].trim();
                        String[] tmp3 = tmp2.split(":");
                        String actual_score = tmp3[1].trim();*/
                    	String actual_score = scores[j].split(" ")[10];
                        actual_score_array[k] = actual_score;
                    }
                }
            }
            human_actual_scores.put(category[i].toString(), actual_score_array);
        }
    }

    public static void save_auto_actual_scores(){
        Object[] category = auto_scores.keySet().toArray();
        for(int i = 0; i < category.length; i++){
            String[] actual_score_array = new String[speakers.length];
            String[] scores = auto_scores.get(category[i].toString());
            for(int j = 0; j < scores.length; j++){
                for(int k = 0; k < speakers.length; k++){
                    //String[] speakers_order = speakers[k].split("_");
                    if(scores[j] == null || scores[j].equals("")){
                        continue;
                    }
                    if(scores[j].contains(speakers[k])){
                        /*String speaker_scores = scores[j];
                        String[] tmp = speaker_scores.split("---");
                        String tmp2 = tmp[1].trim();
                        String[] tmp3 = tmp2.split(":");*/
                        String actual_score = scores[j].split(" ")[10];
                        actual_score_array[k] = actual_score;
                    }
                }
            }
            auto_actual_scores.put(category[i].toString(), actual_score_array);
        }
    }

    public static void writeToEvaluation(String filePath, String human_annotation, String auto_annotation){

        double[] HighestRestMismatch = new double[speakers.length];
        double[] HighLowMismatch = new double[speakers.length];
        double[] ExactMatch = new double[speakers.length];
        double[] PartialMatch = new double[speakers.length];

        try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));

                bw.write("---------------- Expressive Disagreement Evaluation --------------------- \n");
                bw.write("Human annotated file: " + human_annotation + "\n");
                bw.write("Auto annotated file: " + auto_annotation + "\n");
                bw.write("--------------------------------------------------------------- \n");

                for(int i = 0; i < categories.length; i++){
                    String category = categories[i];
                    bw.write("-----------------------------" + category + "---------------------------- \n");
                    bw.write("Speaker \t Auto_annotated \t Human_annotated \t Highest/Rest/Mismatch \t " +
                            "High/Low/Mismatch \t Exact-match \t Partial-match \n");
                    int[] auto_qscore = auto_quintile_scores.get(category);
                    int[] human_qscore = human_quintile_scores.get(category);

                   // String[] auto_ascore = auto_actual_scores.get(category);
                   // String[] human_ascore = human_actual_scores.get(category);

                    String[] auto_qt = auto_qt_thrs.get(category);
                    String[] human_qt = human_qt_thrs.get(category);

                    int counter = 0;

                    for(int j = 0; j < speakers.length; j++){
                        if(auto_qscore[j] == 0 && human_qscore[j] == 0){
                            continue;
                        }

                        counter++;

                        String speaker = speakers[j];
                        bw.write(speaker + "\t");
                        if(human_qscore == null){
                            human_qscore = new int[speakers.length];
                        }
                        if(auto_qscore == null){
                            auto_qscore = new int[speakers.length];
                        }
                        if(auto_qscore[j] != 0){
                            bw.write(auto_qscore[j] + "\t");
                        }
                        else{
                            bw.write("NA\t");
                        }
                        if(human_qscore[j] != 0){
                            bw.write(human_qscore[j] + "\t");
                        }
                        else{
                            bw.write("NA\t");
                        }
                        /* Highest/Rest/Mismatch */
                        if(auto_qscore[j] == 5 && human_qscore[j] == 5){
                            bw.write("Highest\t");
                            HighestRestMismatch[j] = 1;
                        }
                        else if(auto_qscore[j] == 5 || human_qscore[j] == 5){
                            bw.write("Mismatch\t");
                            HighestRestMismatch[j] = 0;
                        }
                        else if(auto_qscore[j] == 0 || human_qscore[j] == 0){
                            bw.write("NA\t");
                            HighestRestMismatch[j] = 0;
                        }
                        else{
                            bw.write("Rest\t");
                            HighestRestMismatch[j] = 0.5;
                        }

                        /* High/Low/Mismatch */
                        if(auto_qscore[j] >= 4 && human_qscore[j] >=4){
                            bw.write("High\t");
                            HighLowMismatch[j] = 1;
                        }
                        else if(auto_qscore[j] == 0 || human_qscore[j] == 0){
                            bw.write("NA\t");
                            HighLowMismatch[j] = 0;
                        }
                        else if(auto_qscore[j] < 4 && human_qscore[j] < 4){
                            bw.write("Low\t");
                            HighLowMismatch[j] = 0.5;
                        }
                        else{
                            bw.write("Mismatch\t");
                            HighLowMismatch[j] = 0;
                        }

                        /* Exact-match */
                        if(auto_qscore[j] != 0 && auto_qscore[j] == human_qscore[j]){
                            bw.write("yes\t");
                            ExactMatch[j] = 1;
                        }
                        else if(auto_qscore[j] == 0 || human_qscore[j] == 0){
                            bw.write("NA\t");
                            ExactMatch[j] = 0;
                        }
                        else{
                            bw.write("no\t");
                            ExactMatch[j] = 0;
                        }

                        /* Partial-match */
                        if(auto_qscore[j] == 0 || human_qscore[j] == 0){
                            bw.write("NA\t");
                            PartialMatch[j] = 0;
                        }
                        else if(auto_qscore[j] == human_qscore[j]){
                            bw.write("1\t");
                            PartialMatch[j] = 1;
                        }
                        else if(auto_qscore[j]-human_qscore[j] > 1 || auto_qscore[j]-human_qscore[j] < -1){
                            bw.write("0\t");
                            PartialMatch[j] = 0;
                        }
                        else{ // calculate partial score
                            String[] auto_actual_scores_array = auto_actual_scores.get(category);
                            String[] human_actual_scores_array = human_actual_scores.get(category);

                            //check if human_actual_scores_array is all null

                            boolean notallnull;
                            notallnull=false;

                            for(int gh=0; gh<human_actual_scores_array.length;gh++){

                                if(human_actual_scores_array[gh]!=null){

                                    notallnull=true;
                                    break;

                                }

                            }

                           if(!notallnull){ // human_actual_scores_array is all null
                                bw.write("0\t");
                                PartialMatch[j] = 0;

                           }
                           else{



                            if(auto_actual_scores_array == null){
                                auto_actual_scores_array = new String[speakers.length];
                            }
                            else if(human_actual_scores_array == null){
                                human_actual_scores_array = new String[speakers.length];
                            }
                            double auto_actual_score = 0.0;
                            double human_actual_score = 0.0;
                            if(auto_actual_scores_array[j] != null && !auto_actual_scores_array[j].equals("")
                               && human_actual_scores_array[j] != null && !human_actual_scores_array[j].equals(""))
                            {
                                auto_actual_score = Double.parseDouble(auto_actual_scores_array[j]);
                                human_actual_score = Double.parseDouble(human_actual_scores_array[j]);
                            }

                            if(human_qscore[j] - auto_qscore[j]  == 1){

                                int mid_qt_thrs_index = 5 - human_qscore[j];
                                if(auto_qt.length > mid_qt_thrs_index && human_qt.length > mid_qt_thrs_index){
                                double auto_mid_qt_thrs = Double.parseDouble(auto_qt[mid_qt_thrs_index]);
                                double human_mid_qt_thrs = Double.parseDouble(human_qt[mid_qt_thrs_index]);
                                double p = 0.0;
                                double np = 0.0;
                                if(mid_qt_thrs_index != 3){
                                    p = auto_mid_qt_thrs - auto_actual_score;
                                    np = auto_actual_score - Double.parseDouble(auto_qt[mid_qt_thrs_index-1]);
                                }
                                else{
                                    p = human_actual_score - human_mid_qt_thrs;
                                    np = Double.parseDouble(human_qt[mid_qt_thrs_index-1]) - human_actual_score;
                                }
                                System.out.println("p: " + p + "np: "+ np);
                                if(p <= np){
                                    bw.write("0.5\t");
                                    PartialMatch[j] = 0.5;
                                }
                                else{
                                    bw.write("0\t");
                                    PartialMatch[j] = 0;
                                }
                                }
                            }
                            else if(human_qscore[j] - auto_qscore[j]  == -1){
                                int mid_qt_thrs_index = 5 - auto_qscore[j];
                                if(auto_qt.length > mid_qt_thrs_index && human_qt.length > mid_qt_thrs_index){
                                double auto_mid_qt_thrs = Double.parseDouble(auto_qt[mid_qt_thrs_index]);
                                double human_mid_qt_thrs = Double.parseDouble(human_qt[mid_qt_thrs_index]);
                                double p = 0.0;
                                double np = 0.0;
                                if(mid_qt_thrs_index != 3){
                                    p = human_mid_qt_thrs - human_actual_score;
                                    np = human_actual_score - Double.parseDouble(human_qt[mid_qt_thrs_index-1]);
                                }
                                else{
                                    p = auto_actual_score - auto_mid_qt_thrs;
                                    np = Double.parseDouble(auto_qt[mid_qt_thrs_index-1]) - auto_actual_score;
                                }
                                System.out.println("p: " + p + "np: "+ np);
                                if(p <= np){
                                    bw.write("0.5\t");
                                    PartialMatch[j] = 0.5;
                                }
                                else{
                                    bw.write("0\t");
                                    PartialMatch[j] = 0;
                                }

                            }
                            }
                        }
                        }



                        bw.write("\n");
                    }
                    /* Precision */
                    bw.write("Precision: \t");

                    int HRM = 0, HLM = 0, EXM = 0;
                    double PM = 0;
                    for(int k = 0; k < speakers.length; k++){
                        if(HighestRestMismatch[k] != 0){
                            HRM++;
                        }
                        if(HighLowMismatch[k] != 0){
                            HLM++;
                        }
                        if(ExactMatch[k] != 0){
                            EXM++;
                        }
                        if(PartialMatch[k] != 0){
                            PM += PartialMatch[k];
                        }
                    }
                    System.err.println("counter = " + counter);
//                    System.err.println("HRM = " + HRM);
                    double HRM_precision = (double)HRM/(double)counter;
                    bw.write(HRM_precision + "(Highest/Rest/Mismatch)\t");
                    double HLM_precision = (double)HLM/(double)counter;
                    bw.write(HLM_precision + "(High/Low/Mismatch)\t");
                    double EXM_precision = (double)EXM/(double)counter;
                    bw.write(EXM_precision + "(Exact-match)\t");
                    double PM_precision = (double)PM/(double)counter;
                    bw.write(PM_precision + "(Partial-match)\t");

                    bw.write("\n");

                }

                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


    }

}



