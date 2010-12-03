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

    private static boolean readerOpened=false;
    private static BufferedReader eia_br;

    /*reynard newly added*/
    static String curFile;

    public static void main(String[] args){

        String human_annotation = "D:/m2w cs/evaluation-m2w/src/input_files/allsession_anno";
        String auto_annotation = "D:/m2w cs/evaluation-m2w/src/input_files/allsession_auto";

        String evaluation_file = "D:/m2w cs/evaluation-m2w/src/output_files/all_session_result";

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
                   save_human_rank();
                   save_auto_rank();

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
    
    public static void writeToEvaluation(String filePath, String human_annotation, String auto_annotation, String curtopic, boolean isnew){

        try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));
               	
                	bw.write("\n---------------- NIST Evaluation --------------------- \n");
                	
                	bw.write("Calculating File... " + curFile +"\n");
                	
                	//bw.write("--------------------------------------------------------------- \n");


                for(int i = 0; i < categories.length; i++){


                    if(!categories[i].equals(curtopic)){

                        continue;

                    }

                    String category = categories[i];
                    
                    bw.write("-----------------------------" + category.toUpperCase() + "---------------------------- \n");
	   			
		   			 int[] auto_qscore = auto_quintile_scores.get(category);
		   			 int[] human_qscore = human_quintile_scores.get(category);
		   		
//		   			 int counter = 0;
   			
                    /*new compare evaluation method*/
                    CompareEval CpEval = new CompareEval();
                    CpEval.compareEval(bw, speakers, auto_qscore, human_qscore);

                    
                }  

                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


    }

}



