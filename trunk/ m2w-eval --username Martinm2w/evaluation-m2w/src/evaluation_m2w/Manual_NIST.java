package evaluation_m2w;


import java.io.*;
import java.util.*;
import java.util.logging.*;

import util.*;

/**
 *
 * @author m2w
 */

public class Manual_NIST {



    static String[] categories;

    static String[] speakers;
    
    static HashMap<String, String> categories_map = new HashMap<String, String>();
    static HashMap<String, String> speakers_map = new HashMap<String, String>();

    static HashMap<String, String[]> human_scores = new HashMap<String, String[]>(); // key: category; value: score, order by speakers
    static HashMap<String, String[]> auto_scores = new HashMap<String, String[]>(); // key: category; value: score, order by speakers
    static HashMap<String, int[]> human_quintile_scores = new HashMap<String, int[]>(); // key: category; value: quintile scores, order by speakers
    static HashMap<String, int[]> auto_quintile_scores = new HashMap<String, int[]>(); // key: category; value: quintile scores, order by speakers
    static HashMap<String, String[]> human_actual_scores = new HashMap<String, String[]>(); // key: category; value: actual scores, order by speakers
    static HashMap<String, String[]> auto_actual_scores = new HashMap<String, String[]>(); // key: category; value: actual scores, order by speakers

    static boolean ifDouble = false;



    private static boolean readerOpened=false;
    private static BufferedReader eia_br;

    /*reynard newly added*/
    static String curFile;

    public static void main(String[] args){

        String human_annotation = "/home/ruobo/NetBeansProjects/evaluation-m2w/src/preprocessed/_MANUAL_tpc_scil_annotated_withHalfDGR_jessamyn_leadership_4_ngt";
        String auto_annotation = "/home/ruobo/NetBeansProjects/evaluation-m2w/src/preprocessed/_MANUAL_tpc_scil_automated_withHalfDGR_jessamyn_leadership_4_ngt";

        String evaluation_file = "/home/ruobo/NetBeansProjects/evaluation-m2w/src/output_files/_MANUAL_tpc_scil_withHalfDGR_jessamyn_leadership_4_ngt.txt";

            try { //extract names/topics
                BufferedReader br = new BufferedReader(new FileReader(human_annotation));
                
                String tempstr;

                while((tempstr=br.readLine())!=null){
                    if(tempstr.endsWith("$")){//get speaker names

                        String name;

                        name= tempstr.split("\t")[0].toLowerCase();

//                        name=name.toLowerCase();

                        if( (speakers_map.get(name)==null)){ //if speaker not yet in list

                            speakers_map.put(name, name);

                        }

                   }

                    /*tpc tkc inv*/
                    else if(tempstr.contains("%")){//get topics

                        	if(categories_map.get(tempstr.split("\t")[1]) == null){
                        		
                        		categories_map.put(tempstr.split("\t")[1], tempstr.split("\t")[1]);
                        		
                        	}

                    }

                }

                System.err.println(Arrays.toString(speakers_map.keySet().toArray()));

                System.err.println(Arrays.toString(categories_map.keySet().toArray()));


                Object[] speakerarray=speakers_map.keySet().toArray();


                speakers=new String[speakerarray.length];

                for(int ei=0;ei<speakerarray.length;ei++){

                    speakers[ei]=(String)speakerarray[ei];

//                    System.err.println(speakers[ei]);

                }


                Object[] categoriesarray=categories_map.keySet().toArray();

                categories=new String[categoriesarray.length];

                for(int ei=0;ei<categoriesarray.length;ei++){

                    categories[ei]=(String)categoriesarray[ei];

//                    System.out.println(categories[ei]);

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
        human_quintile_scores.clear();
        auto_quintile_scores.clear();
        curFile = null;
    }

    public static void extract_info_human(String evaluation_file, String file,String auto_annotation){
        BufferedReader br;
        String tempStr = "";

         String curtopic="";

         boolean isnew=false;

        try {
           br = new BufferedReader(new FileReader(file));
           while ((tempStr = br.readLine()) != null){

               if(tempStr.contains("%")){
            	   
            	   /*get curFile name*/
            	   curFile = tempStr.split("\t")[0];
            	   System.out.println("current file is:" + curFile);
            	   
                   for(int i = 0; i < categories.length; i++){
                       if(tempStr.toLowerCase().contains(categories[i])){

                           curtopic=categories[i];

                           System.err.println(curtopic);

                           /* save scores */
                           String speaker = "";
                           String[] score_array = new String[speakers.length];
                           while((speaker = br.readLine()) != null &&
                               speaker.endsWith("$")) {

                               for(int k = 0; k < speakers.length; k++){
                                   if(speaker.toLowerCase().contains(speakers[k] + "\t")){
                                        score_array[k] = speaker.toLowerCase();
                                        //System.err.println("score_array[" + k + "]: " + score_array[k]);
                                   }
                               }
                           }
                       human_scores.put(categories[i], score_array);
                       }
                   }

                   extract_info_auto(evaluation_file, file, auto_annotation, curtopic, isnew);
                   
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

    /*extract merged*/
    public static void extract_info_auto(String evaluation_file, String file,String auto_annotation, String curtopic, boolean isnew){
        //BufferedReader br;
        String tempStr = "";

        try {
           if(!readerOpened){

                   eia_br = new BufferedReader(new FileReader(auto_annotation));
                   readerOpened=true;

                }

           while ((tempStr = eia_br.readLine()) != null){
        	   
               if(tempStr.contains("%")){
                   for(int i = 0; i < categories.length; i++){
                	   if(tempStr.toLowerCase().contains(categories[i])){
                           /* save scores */
                           String speaker = "";
                           String[] score_array = new String[speakers.length];
                           while((speaker = eia_br.readLine()) != null &&
                                   speaker.endsWith("$")) {
                               if(speaker.contains(".")){
                                   ifDouble = true;
                               }
                               for(int k = 0; k < speakers.length; k++){
                                   if(speaker.toLowerCase().contains(speakers[k] + "\t")){
                                        score_array[k] = speaker.toLowerCase();

                                        //System.err.println("score_array[" + k + "]: " + score_array[k]);
                                   }
                               }
                           }
                       auto_scores.put(categories[i], score_array);
                       }
                   }
//                   System.out.println(auto_scores.get(categories[0])..contains("."));
                   if(ifDouble){// check if has double entry type
                       save_human_actual_scores();
                       save_auto_actual_scores();
                   }else{
                       save_human_rank();
                       save_auto_rank();
                   }

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

                    if(scores[j] != null && scores[j].contains(speakers[k] + "\t")){
                        String speaker_scores = scores[j];
                        String quintile_score = speaker_scores.split("\t")[1];
                        quintile_score_array[k] = Integer.parseInt(quintile_score);
//                        System.out.println("human score of" + speakers[k] + " : " + quintile_score_array[k]);

                    }
                }
            }
            
            System.out.println("human array : " + Arrays.toString(quintile_score_array));
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
                          String quintile_score = speaker_scores.split("\t")[1];
                          quintile_score_array[k] = Integer.parseInt(quintile_score);
//                          System.out.println("auto score of" + speakers[k] + " : " + quintile_score_array[k]);
                      }
                  }
              }
              
              System.out.println("auto array : " + Arrays.toString(quintile_score_array));
              auto_quintile_scores.put(category[i].toString(), quintile_score_array);
          }
    }

    public static void save_human_actual_scores(){
        Object[] category = human_scores.keySet().toArray();
        for(int i = 0; i < category.length; i++){
            String[] actual_score_array = new String[speakers.length];
            String[] scores = human_scores.get(category[i].toString());
            for(int j = 0; j < scores.length; j++){
                if(scores[j] == null || scores[j].equals("")){
                    continue;
                }
                for(int k = 0; k < speakers.length; k++){
                    if(scores[j].contains(speakers[k] + "\t")){
                        String speaker_scores = scores[j];
                        String actual_score = speaker_scores.split("\t")[1];
                        actual_score_array[k] = actual_score;
//                         System.out.println("human score of " + speakers[k] + " : " + actual_score_array[k]);
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
                if(scores[j] == null || scores[j].equals("")){
                    continue;
                }
                for(int k = 0; k < speakers.length; k++){
                   if(scores[j].contains(speakers[k] + "\t")){
                        String speaker_scores = scores[j];
                        String actual_score = speaker_scores.split("\t")[1];
                        actual_score_array[k] = actual_score;
//                         System.out.println("auto score of " + speakers[k] + " : " + actual_score_array[k]);
                    }
                }
            }
            auto_actual_scores.put(category[i].toString(), actual_score_array);
        }
    }
    
    public static void writeToEvaluation(String filePath, String human_annotation, String auto_annotation, String curtopic, boolean isnew){

        try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));
               	
//                	bw.write("\n---------------- NIST --------------------- \n");
                	
//                	bw.write("File: " + curFile +"\n");
                	
                	//bw.write("--------------------------------------------------------------- \n");
                
                //m2w: edited, for simple output, 2011-02-22 

                for(int i = 0; i < categories.length; i++){


                    if(!categories[i].equals(curtopic)){

                        continue;

                    }

                    String category = categories[i];
                    bw.write("\n");
                    bw.write(curFile +"\t" + category + "\n");
	   			
                    int[] auto_qscore = auto_quintile_scores.get(category);
                    int[] human_qscore = human_quintile_scores.get(category);
                    String[] auto_ascore = auto_actual_scores.get(category);
                    String[] human_ascore = human_actual_scores.get(category);
//                    System.out.println(Arrays.asList(auto_ascore));
//                    System.out.println(Arrays.asList(human_ascore));

                    /*new compare evaluation method*/
                    CompareEval CpEval = new CompareEval();
//                  CpEval.compareEval(bw, speakers, auto_qscore, human_qscore);                    
                    /*simple NIST evaluation method*/



                    if(ifDouble){//if entries in auto score contains a dot, then use the nist simple double method
//                                 doesn't work right now, just preprocess the double into int, using the ranking in
                        CpEval.nistEval_simple(bw, speakers, auto_ascore, human_ascore);
                    }else{
                        CpEval.nistEval_simple(bw, speakers, auto_qscore, human_qscore);
                    }
                    

                    
                    
                }  

                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


    }

}



