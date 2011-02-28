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

public class Tpc_m2w {


   // static String[] categories = {"LTI", "SMT", "CS", "TL"};
   // static String[] speakers = {"jennifer", "ken", "kerri", "kara", "lance", "chris", "jenny"};

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

    /*new_file_names*/
    static Filenames filename = new Filenames();
    
    
    private static boolean readerOpened=false;
    private static BufferedReader eia_br;
    

    public static void main(String[] args){

//        String[] annotators = {"brian", "kerri", "lauren"};

        //String[] annotators = {"one"};

       // for(int i = 0; i < annotators.length; i++){

        String human_annotation = "/home/ruobo/NetBeansProjects/evaluation-m2w/src/input_files/scil_annotated_withHalfDGR_jessamyn_leadership_4_ngt.txt";
        String auto_annotation = "/home/ruobo/NetBeansProjects/evaluation-m2w/src/input_files/scil_automated_withHalfDGR_jessamyn_leadership_4_ngt.txt";

        String evaluation_file = "/home/ruobo/NetBeansProjects/evaluation-m2w/src/output_files/nist_simple_tpc_scil_withHalfDGR_jessamyn_leadership_4_ngt.txt";

            try { //extract names/topics
                BufferedReader br = new BufferedReader(new FileReader(human_annotation));
                
                /*read in file name*/
                filename.extractFileNames(human_annotation, auto_annotation);


                String tempstr;

                while((tempstr=br.readLine())!=null){
                    if(tempstr.startsWith("The quintile score of ")|| tempstr.startsWith("The quintile score ")){//get speaker names

                        String name;

                        if(tempstr.startsWith("The quintile score of ")){

                            name=tempstr.split(" ")[4];

                        }
                        else{

                           name=tempstr.split(" ")[3];

                        }


                        name=name.toLowerCase();

                        if( (speakers_map.get(name)==null)){ //if speaker not yet in list

                            speakers_map.put(name, name);

                        }


      //                  System.err.println(tempstr);
    //                    System.err.println(firsthalf);
  //                      System.err.println(secondhalf);
//                        System.err.println(tempstr.substring("The quintile score of ".length(), firstsemicolon));


                    }
//                    /*merged files*/
//                    else if(tempstr.contains("calculate Merged quintile")){
//
//                    	if(categories_map.get("merged quintile") == null){
//
//                    		categories_map.put("merged quintile", "merged quintile");
//
//                    	}
//                    }
                    /*normal files*/
                    else if(tempstr.contains("calculate Topic Control - ")){//get topics

                       // System.err.println(tempstr.split("\"")[1]);

                        if(!tempstr.split(" ")[4].equals("")){

                            if( categories_map.get(tempstr.split(" ")[4].toLowerCase())==null){ //if category not yet in list

                                categories_map.put(tempstr.split(" ")[4].toLowerCase(), tempstr.split(" ")[4].toLowerCase());

                            }

                        }
                        else{

                            if( categories_map.get(tempstr.split(" ")[5].toLowerCase())==null){ //deal with inconsistant input file formats

                                categories_map.put(tempstr.split(" ")[5].toLowerCase(), tempstr.split(" ")[5].toLowerCase());

                            }

                        }

                    }

                }

                System.err.println("speakers: " + Arrays.toString(speakers_map.keySet().toArray()));

                System.err.println("cate: " + Arrays.toString(categories_map.keySet().toArray()));


                Object[] speakerarray=speakers_map.keySet().toArray();


                speakers=new String[speakerarray.length];

                for(int ei=0;ei<speakerarray.length;ei++){
                    speakers[ei]=(String)speakerarray[ei];
                }


                Object[] categoriesarray=categories_map.keySet().toArray();

                categories=new String[categoriesarray.length];

                for(int ei=0;ei<categoriesarray.length;ei++){
                    categories[ei]=(String)categoriesarray[ei];
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
        human_qt_thrs.clear();
        auto_qt_thrs.clear();
        human_quintile_scores.clear();
        auto_quintile_scores.clear();
        human_actual_scores.clear();
        auto_actual_scores.clear();
    }

    public static void extract_info_human(String evaluation_file, String file,String auto_annotation){
        BufferedReader br;
        String tempStr = "";

         String curtopic="";

         boolean isnew=false;

        try {
           br = new BufferedReader(new FileReader(file));
           while ((tempStr = br.readLine()) != null){

               if(tempStr.contains("processing:")){

                   isnew=true;

               }

               if(tempStr.contains("calculate Topic Control -")){

                 

                   for(int i = 0; i < categories.length; i++){
                       if(tempStr.toLowerCase().contains(categories[i])){

                           curtopic=categories[i];

                           System.err.println(curtopic);

                          /* save scores */
                           String speaker = "";
                           String[] score_array = new String[speakers.length];

                           do{ //read out extra lines

                                br.mark(1000);
                                speaker=br.readLine();


                           }while(speaker!=null && !speaker.contains("The quintile score"));
                           br.reset();

                           while((speaker = br.readLine()) != null &&
                               speaker.contains("The quintile score") || speaker.contains("set")){

//                               if(speaker.contains("set") ){
//                                   continue;
//                               }
                               for(int k = 0; k < speakers.length; k++){
                                   if(speaker.toLowerCase().contains(speakers[k]) && !speaker.contains("set")){
                                        score_array[k] = speaker.toLowerCase();
//                                        System.out.println("score_array[" + k + "]: " + score_array[k]);
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

    public static void extract_info_auto(String evaluation_file, String file,String auto_annotation, String curtopic, boolean isnew){
        //BufferedReader br;
        String tempStr = "";

        try {
           if(!readerOpened){

                   eia_br = new BufferedReader(new FileReader(auto_annotation));
                   readerOpened=true;

                }

           while ((tempStr = eia_br.readLine()) != null){
        	   
               if(tempStr.contains("calculate Topic Control - ")){
                   for(int i = 0; i < categories.length; i++){
                       if(tempStr.toLowerCase().contains(categories[i])){
                           
                           /* save scores */
                           String speaker = "";
                           String[] score_array = new String[speakers.length];


                           do{ //read out extra lines

                                eia_br.mark(1000);
                                speaker=eia_br.readLine();


                           }while(speaker!=null && !speaker.contains("The quintile score"));

                           eia_br.reset();
                           
                           while((speaker = eia_br.readLine()) != null &&
                                   speaker.contains("The quintile score") || speaker.contains("set")){
                               
                               for(int k = 0; k < speakers.length; k++){
                                   if(speaker.toLowerCase().contains(speakers[k]) && !speaker.contains("set")){
                                        score_array[k] = speaker.toLowerCase();
//                                        System.out.println("score_array[" + k + "]: " + score_array[k]);
                                   }
                               }
                           }
                       auto_scores.put(categories[i], score_array);
                       }
                   }

                   save_human_quintile_scores();
                    save_auto_quintile_scores();
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

    public static void save_human_quintile_scores(){
        Object[] category = human_scores.keySet().toArray();
        for(int i = 0; i < category.length; i++){
            int[] quintile_score_array = new int[speakers.length];
            String[] scores = human_scores.get(category[i].toString());
            for(int j = 0; j < scores.length; j++){
                for(int k = 0; k < speakers.length; k++){
//                    System.err.println(scores[j] + "       " + speakers[k]);
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
                        System.out.println("quintile score for speaker " + speakers[k] + " is " + quintile_score_array[k]);

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

//    public static void save_human_actual_scores(){
//        Object[] category = human_scores.keySet().toArray();
//        for(int i = 0; i < category.length; i++){
//            String[] actual_score_array = new String[speakers.length];
//            String[] scores = human_scores.get(category[i].toString());
//            for(int j = 0; j < scores.length; j++){
//                for(int k = 0; k < speakers.length; k++){
//                    if(scores[j] != null && scores[j].contains(speakers[k] + " ")){
//                        String speaker_scores = scores[j];
//                        String[] tmp = speaker_scores.split("---");
//                        String tmp2 = tmp[1].trim();
//                        String[] tmp3 = tmp2.split(":");
//                        String actual_score = tmp3[1].trim();
//                        actual_score_array[k] = actual_score;
//                    }
//                }
//            }
//            human_actual_scores.put(category[i].toString(), actual_score_array);
//        }
//    }

//    public static void save_auto_actual_scores(){
//        Object[] category = auto_scores.keySet().toArray();
//        for(int i = 0; i < category.length; i++){
//            String[] actual_score_array = new String[speakers.length];
//            String[] scores = auto_scores.get(category[i].toString());
//            for(int j = 0; j < scores.length; j++){
//                for(int k = 0; k < speakers.length; k++){
//                    if(scores[j] != null && scores[j].contains(speakers[k] + " ")){
//                        String speaker_scores = scores[j];
//                        String[] tmp = speaker_scores.split("---");
//                        String tmp2 = tmp[1].trim();
//                        String[] tmp3 = tmp2.split(":");
//                        String actual_score = tmp3[1].trim();
//                        actual_score_array[k] = actual_score;
//                    }
//                }
//            }
//            auto_actual_scores.put(category[i].toString(), actual_score_array);
//        }
//    }

    public static void writeToEvaluation(String filePath, String human_annotation, String auto_annotation, String curtopic, boolean isnew){

//        double[] HighestRestMismatch = new double[speakers.length];
//        double[] HighLowMismatch = new double[speakers.length];
//        double[] ExactMatch = new double[speakers.length];
//        double[] PartialMatch = new double[speakers.length];

        try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));

           
                if(isnew){ //only print for new section
                	
//                	bw.write("---------------- Topic Control Evaluation --------------------- \n");
                	bw.write("\n");
		        bw.write("------------ TPC ------------- \n");
                        filename.printSimpleFileNames(bw);
//                	filename.printFileNames(bw);
//                	filename.printFileNamesAutoSurvey(bw);
//                	filename.printFileNamesSurveyHuman(bw);
                	
//                	bw.write("--------------------------------------------------------------- \n");
                }

                for(int i = 0; i < categories.length; i++){

                    //System.err.println(curtopic);

                    if(!categories[i].equals(curtopic)){

                        continue;

                    }

                    String category = categories[i];
                    
//                    bw.write("-----------------------------" + category + "---------------------------- \n");
                    bw.write("-------------" + category + "------------- \n");
//                    bw.write("Speaker \t Auto_annotated \t Human_annotated \t Highest/Rest/Mismatch \t " +
//		   				 "High/Low/Mismatch \t Exact-match \t Partial-match \n");
		   			
		   			 int[] auto_qscore = auto_quintile_scores.get(category);
		   			 int[] human_qscore = human_quintile_scores.get(category);


//                                         
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
//                    CpEval.compareEval(bw, speakers, auto_qscore, human_qscore);
                    CpEval.nistEval_simple(bw, speakers, auto_qscore, human_qscore);
//                    
                    
                }  //for each category

                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


    }

}



