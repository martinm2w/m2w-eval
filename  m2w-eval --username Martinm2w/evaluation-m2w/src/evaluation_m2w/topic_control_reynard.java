package evaluation_m2w;


import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import util.CompareEval;
import util.Filenames;
import util.MatchEval;

/**
 *
 * @author jgh
 */

public class topic_control_reynard {


   // static String[] categories = {"LTI", "SMT", "CS", "TL"};
   // static String[] speakers = {"jennifer", "ken", "kerri", "kara", "lance", "chris", "jenny"};

    static String[] categories;

    static String[] speakers;
    
    static HashMap<String, String> categories_map = new HashMap<String, String>();
    static HashMap<String, String> speakers_map = new HashMap<String, String>();


//    static HashMap<String, String[]> human_qt_thrs = new HashMap<String, String[]>(); // key: category; value: qt_thrs array
//    static HashMap<String, String[]> auto_qt_thrs = new HashMap<String, String[]>(); // key: category; value: qt_thrs array
    static HashMap<String, String[]> human_scores = new HashMap<String, String[]>(); // key: category; value: score, order by speakers
    static HashMap<String, String[]> auto_scores = new HashMap<String, String[]>(); // key: category; value: score, order by speakers
    static HashMap<String, int[]> human_quintile_scores = new HashMap<String, int[]>(); // key: category; value: quintile scores, order by speakers
    static HashMap<String, int[]> auto_quintile_scores = new HashMap<String, int[]>(); // key: category; value: quintile scores, order by speakers
//    static HashMap<String, String[]> human_actual_scores = new HashMap<String, String[]>(); // key: category; value: actual scores, order by speakers
//    static HashMap<String, String[]> auto_actual_scores = new HashMap<String, String[]>(); // key: category; value: actual scores, order by speakers

    /*new_file_names*/
//    static Filenames filename = new Filenames();
    
    
    private static boolean readerOpened=false;
    private static BufferedReader eia_br;

    /*reynard newly added*/
    static String seNumber;

    public static void main(String[] args){

//        String[] annotators = {"brian", "kerri", "lauren"};

        //String[] annotators = {"one"};

       // for(int i = 0; i < annotators.length; i++){

        String human_annotation = "D:/m2w cs/evaluation-m2w/src/input_files/reynard_human_rank_merged";
        String auto_annotation = "D:/m2w cs/evaluation-m2w/src/input_files/reynard_merged_rank";

        String evaluation_file = "D:/m2w cs/evaluation-m2w/src/output_files/reynard_merged_rank_result_SCIL";

            try { //extract names/topics
                BufferedReader br = new BufferedReader(new FileReader(human_annotation));
                
                /*read in file name*/
//                filename.extractFileNames(human_annotation, auto_annotation);


                String tempstr;

                while((tempstr=br.readLine())!=null){
                    if(tempstr.endsWith("score")){//get speaker names

                        String name;

//                        if(tempstr.endsWith("score")){

                            name= tempstr.split("\t")[0];

//                        }


                        name=name.toLowerCase();

                        if( (speakers_map.get(name)==null)){ //if speaker not yet in list

                            speakers_map.put(name, name);

                        }

                   }
                    /*merged*/
                    else if(tempstr.contains("new session merged")){
                    	
                    	if(categories_map.get("merged") == null){
                    		
                    		categories_map.put("merged", "merged");
                    		
                    	}
                    }
                    /*lti*/
                    else if(tempstr.contains("new session lti")){//get topics

                        	if(categories_map.get("lti") == null){
                        		
                        		categories_map.put("lti", "lti");
                        		
                        	}

                    }

                }

                System.err.println(Arrays.toString(speakers_map.keySet().toArray()));

                                System.err.println(Arrays.toString(categories_map.keySet().toArray()));


                Object[] speakerarray=speakers_map.keySet().toArray();


                speakers=new String[speakerarray.length];

                for(int ei=0;ei<speakerarray.length;ei++){

                    speakers[ei]=(String)speakerarray[ei];

                    System.err.println(speakers[ei]);

                }


                Object[] categoriesarray=categories_map.keySet().toArray();

                categories=new String[categoriesarray.length];

                for(int ei=0;ei<categoriesarray.length;ei++){

                    categories[ei]=(String)categoriesarray[ei];

                    System.out.println(categories[ei]);

                }



                br.close();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(topic_disagreement.class.getName()).log(Level.SEVERE, null, ex);
            }
              catch(IOException e){

                  e.printStackTrace();

            }


        init();

        extract_info_human(evaluation_file, human_annotation,auto_annotation);


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
//        human_qt_thrs.clear();
//        auto_qt_thrs.clear();
        human_quintile_scores.clear();
        auto_quintile_scores.clear();
//        human_actual_scores.clear();
//        auto_actual_scores.clear();
        seNumber = null;
    }

    public static void extract_info_human(String evaluation_file, String file,String auto_annotation){
        BufferedReader br;
        String tempStr = "";

         String curtopic="";

         boolean isnew=false;

        try {
           br = new BufferedReader(new FileReader(file));
           while ((tempStr = br.readLine()) != null){

//        	   /*added session static no. here*/
//        	   if(tempStr.contains("new")){
//        		   seNumber = tempStr.split("\t")[1];
//        		   System.out.println("session number is :" + seNumber);
//        	   }

               if(tempStr.contains("new")){
            	   
            	   /*added session static no. here*/
            	   seNumber = tempStr.split("\t")[1];
        		   System.out.println("session number is :" + seNumber);

                   for(int i = 0; i < categories.length; i++){
                       if(tempStr.toLowerCase().contains(categories[i])){

                           curtopic=categories[i];

                           System.err.println(curtopic);

//                           String qt_thrs = br.readLine();
//                           String[] qt_thrs1 = qt_thrs.split("\\s+");
//                           String[] qt_thrs_array = new String[qt_thrs1.length-1];
//                           for(int j = 0; j < qt_thrs_array.length; j++){
//                               qt_thrs_array[j] = qt_thrs1[j+1];
                               //System.out.println(qt_thrs_array[j] + " " + categories[i]);
//                           }
                           /* save qt_thrs */
//                           human_qt_thrs.put(categories[i], qt_thrs_array);

                           /* save scores */
                           String speaker = "";
                           String[] score_array = new String[speakers.length];
                           while((speaker = br.readLine()) != null &&
                               speaker.endsWith("score")) {

                               for(int k = 0; k < speakers.length; k++){
                                   if(speaker.toLowerCase().contains(speakers[k] + "\t")){
                                        score_array[k] = speaker.toLowerCase();
                                        //System.out.println("score_array[" + k + "]: " + score_array[k]);
                                   }
                               }
                           }
                       human_scores.put(categories[i], score_array);
                       }
                   }

                   extract_info_auto_merged(evaluation_file, file, auto_annotation, curtopic, isnew);
//                   extract_info_auto_lti(evaluation_file, file, auto_annotation, curtopic, isnew);
                   
                   isnew=false;

               }

           }

           readerOpened=false; //next human annotator
           eia_br.close();

           br.close();
        } catch (FileNotFoundException e) {
           e.printStackTrace();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    
    /*lti or merged*/
    
    public static void extract_info_auto_lti(String evaluation_file, String file,String auto_annotation, String curtopic, boolean isnew){
        //BufferedReader br;
        String tempStr = "";

        try {
           if(!readerOpened){

                   eia_br = new BufferedReader(new FileReader(auto_annotation));
                   readerOpened=true;

                }

           while ((tempStr = eia_br.readLine()) != null){
        	   
               if(tempStr.contains("new session lti")){
                   for(int i = 0; i < categories.length; i++){
//                       if(tempStr.toLowerCase().contains("processing "+categories[i])){
//                           String qt_thrs = eia_br.readLine();
//                           String[] qt_thrs1 = qt_thrs.split("\\s+");
//                           String[] qt_thrs_array = new String[qt_thrs1.length-1];
//                           for(int j = 0; j < qt_thrs_array.length; j++){
//                               qt_thrs_array[j] = qt_thrs1[j+1];
//                               //System.out.println(qt_thrs_array[j] + " " + categories[i]);
//                           }
                           /* save qt_thrs */
//                           auto_qt_thrs.put(categories[i], qt_thrs_array);

                           /* save scores */
                           String speaker = "";
                           String[] score_array = new String[speakers.length];
                           while((speaker = eia_br.readLine()) != null &&
                                   speaker.endsWith("score")) {
                               for(int k = 0; k < speakers.length; k++){
                                   if(speaker.toLowerCase().contains(speakers[k] + "\t")){
                                        score_array[k] = speaker.toLowerCase();
                                        //System.out.println("score_array[" + k + "]: " + score_array[k]);
                                   }
                               }
                           }
                       auto_scores.put(categories[i], score_array);
//                       }
                   }
                   save_human_rank();
                   save_auto_rank();
//                   save_human_quintile_scores();
//                    save_auto_quintile_scores();
//                    save_human_actual_scores();
//                    save_auto_actual_scores();

                     writeToEvaluation(evaluation_file, file, auto_annotation, curtopic, isnew);
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
    
    
    
    /*extract merged*/
    public static void extract_info_auto_merged(String evaluation_file, String file,String auto_annotation, String curtopic, boolean isnew){
        //BufferedReader br;
        String tempStr = "";

        try {
           if(!readerOpened){

                   eia_br = new BufferedReader(new FileReader(auto_annotation));
                   readerOpened=true;

                }

           while ((tempStr = eia_br.readLine()) != null){
        	   
               if(tempStr.contains("new session merged")){
                   for(int i = 0; i < categories.length; i++){
//                       if(tempStr.toLowerCase().contains("processing "+categories[i])){
//                           String qt_thrs = eia_br.readLine();
//                           String[] qt_thrs1 = qt_thrs.split("\\s+");
//                           String[] qt_thrs_array = new String[qt_thrs1.length-1];
//                           for(int j = 0; j < qt_thrs_array.length; j++){
//                               qt_thrs_array[j] = qt_thrs1[j+1];
//                               //System.out.println(qt_thrs_array[j] + " " + categories[i]);
//                           }
                           /* save qt_thrs */
//                           auto_qt_thrs.put(categories[i], qt_thrs_array);

                           /* save scores */
                           String speaker = "";
                           String[] score_array = new String[speakers.length];
                           while((speaker = eia_br.readLine()) != null &&
                                   speaker.endsWith("score")) {
                               for(int k = 0; k < speakers.length; k++){
                                   if(speaker.toLowerCase().contains(speakers[k] + "\t")){
                                        score_array[k] = speaker.toLowerCase();
                                        //System.out.println("score_array[" + k + "]: " + score_array[k]);
                                   }
                               }
                           }
                       auto_scores.put(categories[i], score_array);
//                       }
                   }
                   save_human_rank();
                   save_auto_rank();
//                   save_human_quintile_scores();
//                    save_auto_quintile_scores();
//                    save_human_actual_scores();
//                    save_auto_actual_scores();

                     writeToEvaluation(evaluation_file, file, auto_annotation, curtopic, isnew);
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
    
    public static void save_human_rank(){
    	Object[] category = human_scores.keySet().toArray();
        for(int i = 0; i < category.length; i++){
            int[] quintile_score_array = new int[speakers.length];
            String[] scores = human_scores.get(category[i].toString());
            for(int j = 0; j < scores.length; j++){
                for(int k = 0; k < speakers.length; k++){
//                    System.err.println(scores[j] + "       " + speakers[k]);
                    if(scores[j] != null && scores[j].contains(speakers[k] + "\t")){
                        String speaker_scores = scores[j];
                        String quintile_score = speaker_scores.split("\t")[3].split(" ")[0];
                        System.out.println(quintile_score);
                        quintile_score_array[k] = Integer.parseInt(quintile_score);
                        System.out.println("quintile score for speaker " + speakers[k] + " is " + quintile_score_array[k]);

                    }
                }
            }
            human_quintile_scores.put(category[i].toString(), quintile_score_array);
        }
    }

    public static void save_auto_rank(){
    	  Object[] category = auto_scores.keySet().toArray();
          for(int i = 0; i < category.length; i++){
              int[] quintile_score_array = new int[speakers.length];
              String[] scores = auto_scores.get(category[i].toString());
              for(int j = 0; j < scores.length; j++){
                  for(int k = 0; k < speakers.length; k++){
                      if(scores[j] != null && scores[j].contains(speakers[k] + "\t")){
                          String speaker_scores = scores[j];
                          String quintile_score = speaker_scores.split("\t")[3].split(" ")[0];
                          quintile_score_array[k] = Integer.parseInt(quintile_score);
                      }
                  }
              }
              auto_quintile_scores.put(category[i].toString(), quintile_score_array);
          }
    }
    public static void save_human_quintile_scores(){
        Object[] category = human_scores.keySet().toArray();
        for(int i = 0; i < category.length; i++){
            int[] quintile_score_array = new int[speakers.length];
            String[] scores = human_scores.get(category[i].toString());
            for(int j = 0; j < scores.length; j++){
                for(int k = 0; k < speakers.length; k++){
//                    System.err.println(scores[j] + "       " + speakers[k]);
                    if(scores[j] != null && scores[j].contains(speakers[k] + " ")){
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
                    if(scores[j] != null && scores[j].contains(speakers[k] + " ")){
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
                    if(scores[j] != null && scores[j].contains(speakers[k] + " ")){
                        String speaker_scores = scores[j];
                        String[] tmp = speaker_scores.split("---");
                        String tmp2 = tmp[1].trim();
                        String[] tmp3 = tmp2.split(":");
                        String actual_score = tmp3[1].trim();
                        actual_score_array[k] = actual_score;
                    }
                }
            }
//            human_actual_scores.put(category[i].toString(), actual_score_array);
        }
    }

    public static void save_auto_actual_scores(){
        Object[] category = auto_scores.keySet().toArray();
        for(int i = 0; i < category.length; i++){
            String[] actual_score_array = new String[speakers.length];
            String[] scores = auto_scores.get(category[i].toString());
            for(int j = 0; j < scores.length; j++){
                for(int k = 0; k < speakers.length; k++){
                    if(scores[j] != null && scores[j].contains(speakers[k] + " ")){
                        String speaker_scores = scores[j];
                        String[] tmp = speaker_scores.split("---");
                        String tmp2 = tmp[1].trim();
                        String[] tmp3 = tmp2.split(":");
                        String actual_score = tmp3[1].trim();
                        actual_score_array[k] = actual_score;
                    }
                }
            }
//            auto_actual_scores.put(category[i].toString(), actual_score_array);
        }
    }

    public static void writeToEvaluation(String filePath, String human_annotation, String auto_annotation, String curtopic, boolean isnew){

//        double[] HighestRestMismatch = new double[speakers.length];
//        double[] HighLowMismatch = new double[speakers.length];
//        double[] ExactMatch = new double[speakers.length];
//        double[] PartialMatch = new double[speakers.length];

        try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));

           
//                if(isnew){ //only print for new section
                	
                	bw.write("---------------- Topic Control Evaluation --------------------- \n");
                	
                	bw.write("session: " + seNumber +"\n");
                	
                	bw.write("--------------------------------------------------------------- \n");
//                }

                for(int i = 0; i < categories.length; i++){

                    //System.err.println(curtopic);

                    if(!categories[i].equals(curtopic)){

                        continue;

                    }

                    String category = categories[i];
                    
                    bw.write("-----------------------------" + category + "---------------------------- \n");
//                    bw.write("Speaker \t Auto_annotated \t Human_annotated \t Highest/Rest/Mismatch \t " +
//		   				 "High/Low/Mismatch \t Exact-match \t Partial-match \n");
		   			
		   			 int[] auto_qscore = auto_quintile_scores.get(category);
		   			 int[] human_qscore = human_quintile_scores.get(category);
		   			
		   			 //String[] auto_ascore = auto_actual_scores.get(category);
		   			 //String[] human_ascore = human_actual_scores.get(category);
		   			
//		   			 String[] auto_qt = auto_qt_thrs.get(category);
//		   			 String[] human_qt = human_qt_thrs.get(category);
		   			
		   			 int counter = 0;
   			
                    /*old match evaluation method*/
//                    MatchEval me = new MatchEval();
//                    me.matchEval(bw, auto_qscore, human_qscore, auto_qt, human_qt, speakers, category, human_actual_scores, auto_actual_scores, counter);
                    
                    /*new compare evaluation method*/
                    CompareEval CpEval = new CompareEval();
                    CpEval.compareEval(bw, speakers, auto_qscore, human_qscore);
//                    
                    
                }  //for each category

                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


    }

}



