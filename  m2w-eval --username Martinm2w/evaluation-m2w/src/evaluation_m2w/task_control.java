/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evaluation_m2w;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jgh
 */
public class task_control {


    //static String[] categories = {"DI"};
    //static String[] speakers = {"kerri", "ken", "kara", "chris", "jennifer", "jenny", "lance"};

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

        /*String[] annotators = {"brian", "kerri", "lauren"};*/

        /*for(int i = 0; i < annotators.length; i++){*/

        String human_annotation = args[0]; //"D:/m2w cs/evaluation/src/preprocessed/task_control_5_Lauren_annotated_7_new";
        String auto_annotation = args[1]; // "D:/m2w cs/evaluation/src/preprocessed/task_control_5_automated_7_new";


        String evaluation_file = args[2]; //"D:/m2w cs/evaldata/output_log/2010.07.30/task_control_5_Lauren_annotated_7.result1";



         try { //extract names/topics
                BufferedReader br = new BufferedReader(new FileReader(human_annotation));



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
                    else if(tempstr.contains("calculate Task Control - ")){//get topics

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

        extract_info_human(evaluation_file, human_annotation,auto_annotation);

      //  extract_info_human(human_annotation);
        //extract_info_auto(auto_annotation);
        //save_human_quintile_scores();
        //save_auto_quintile_scores();
        //save_human_actual_scores();
        //save_auto_actual_scores();

        //writeToEvaluation(evaluation_file, human_annotation, auto_annotation);
        //System.out.println(human_actual_scores.size() + " " + auto_actual_scores.size());
        //} /*for loop*/

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
        	   
        	   if(tempStr.contains("$$$$$$$$$$$$")){// change isnew value

                   isnew=true;

               }

               if(tempStr.contains("calculate Task Control - ")){
            	   
                   for(int i = 0; i < categories.length; i++){
                	   
                       if(tempStr.toLowerCase().contains(categories[i])){
                    	   
                    	   curtopic=categories[i];
                    	   
                           String qt_thrs = br.readLine();
                           
                           while(!qt_thrs.contains("qt_thrs: ")){
                               qt_thrs = br.readLine();
                           }
                           String[] qt_thrs1 = qt_thrs.split("\\s+");
                           String[] qt_thrs_array = new String[qt_thrs1.length-1];
                           for(int j = 0; j < qt_thrs_array.length; j++){
                               qt_thrs_array[j] = qt_thrs1[j+1];
                               //System.out.println(qt_thrs_array[j] + " " + categories[i]);
                           }
                           /* save qt_thrs */
                           human_qt_thrs.put(categories[i], qt_thrs_array);

                           /* save scores */
                           String speaker = "";
                           String[] score_array = new String[speakers.length];
                           while((speaker = br.readLine()) != null &&
                               speaker.contains("The quintile score")) {

                               for(int k = 0; k < speakers.length; k++){
                                   if(speaker.toLowerCase().contains(speakers[k] + " ")){
                                        score_array[k] = speaker.toLowerCase();
                                        //System.out.println("score_array[" + k + "]: " + score_array[k]);
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
        BufferedReader br;
        String tempStr = "";

        try {

        if(!readerOpened){

                   eia_br = new BufferedReader(new FileReader(auto_annotation));
                   readerOpened=true;

               }


           
           while ((tempStr = eia_br.readLine()) != null){
               if(tempStr.contains("calculate Task Control - ")){
                   for(int i = 0; i < categories.length; i++){
                       if(tempStr.toLowerCase().contains(categories[i])){
                           String qt_thrs = eia_br.readLine();
                           while(!qt_thrs.contains("qt_thrs: ")){
                               qt_thrs = eia_br.readLine();
                           }
                           String[] qt_thrs1 = qt_thrs.split("\\s+");
                           String[] qt_thrs_array = new String[qt_thrs1.length-1];
                           for(int j = 0; j < qt_thrs_array.length; j++){
                               qt_thrs_array[j] = qt_thrs1[j+1];
                               //System.out.println(qt_thrs_array[j] + " " + categories[i]);
                           }
                           /* save qt_thrs */
                           auto_qt_thrs.put(categories[i], qt_thrs_array);

                           /* save scores */
                           String speaker = "";
                           String[] score_array = new String[speakers.length];
                           while((speaker = eia_br.readLine()) != null &&
                                   speaker.contains("The quintile score")) {
                               for(int k = 0; k < speakers.length; k++){
                                   if(speaker.toLowerCase().contains(speakers[k] + " ")){
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
                if(scores[j] == null || scores[j].equals("")){
                    continue;
                }
                for(int k = 0; k < speakers.length; k++){
                    if(scores[j].contains(speakers[k] + " ")){
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
                if(scores[j] == null || scores[j].equals("")){
                    continue;
                }
                for(int k = 0; k < speakers.length; k++){
                    if(scores[j].contains(speakers[k] + " ")){
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
                if(scores[j] == null || scores[j].equals("")){
                    continue;
                }
                for(int k = 0; k < speakers.length; k++){
                    if(scores[j].contains(speakers[k] + " ")){
                        String speaker_scores = scores[j];
                        String[] tmp = speaker_scores.split("---");
                        String tmp2 = tmp[1].trim();
                        String[] tmp3 = tmp2.split(":");
                        String actual_score = tmp3[1].trim();
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
                if(scores[j] == null || scores[j].equals("")){
                    continue;
                }
                for(int k = 0; k < speakers.length; k++){
                    if(scores[j].contains(speakers[k] + " ")){
                        String speaker_scores = scores[j];
                        String[] tmp = speaker_scores.split("---");
                        String tmp2 = tmp[1].trim();
                        String[] tmp3 = tmp2.split(":");
                        String actual_score = tmp3[1].trim();
                        actual_score_array[k] = actual_score;
                    }
                }
            }
            auto_actual_scores.put(category[i].toString(), actual_score_array);
        }
    }

    public static void writeToEvaluation(String filePath, String human_annotation, String auto_annotation, String curtopic, boolean isnew){

        double[] HighestRestMismatch = new double[speakers.length];
        double[] HighLowMismatch = new double[speakers.length];
        double[] ExactMatch = new double[speakers.length];
        double[] PartialMatch = new double[speakers.length];

        try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));
                
                if(isnew){
                	
	                bw.write("---------------- Task Control Evaluation --------------------- \n");
	                bw.write("Human annotated file: " + human_annotation + "\n");
	                bw.write("Auto annotated file: " + auto_annotation + "\n");
	                bw.write("--------------------------------------------------------------- \n");

                }
                
                for(int i = 0; i < categories.length; i++){

                    if(!categories[i].equals(curtopic)){

                        continue;

                    }
                    
/*                for(int i = 0; i < categories.length; i++){

                    if(!categories[i].toLowerCase().equals(curtopic.toLowerCase())){

                        continue;

                    }*/

                    String category = categories[i];
                    bw.write("-----------------------------" + category + "---------------------------- \n");
                    bw.write("Speaker \t Auto_annotated \t Human_annotated \t Highest/Rest/Mismatch \t " +
                            "High/Low/Mismatch \t Exact-match \t Partial-match \n");
                    int[] auto_qscore = auto_quintile_scores.get(category);
                    int[] human_qscore = human_quintile_scores.get(category);

                    String[] auto_ascore = auto_actual_scores.get(category);
                    String[] human_ascore = human_actual_scores.get(category);

                    String[] auto_qt = auto_qt_thrs.get(category);
                    String[] human_qt = human_qt_thrs.get(category);

                    int counter = 0;

                    for(int j = 0; j < speakers.length; j++){

                        if(auto_qscore == null || human_qscore == null){
                            continue;
                        }
                        if(auto_qscore[j] == 0 && human_qscore[j] == 0){
                            continue;
                        }

                        counter++;

                        String speaker = speakers[j];
                        bw.write(speaker + "\t");
                        bw.write(auto_qscore[j] + "\t");
                        bw.write(human_qscore[j] + "\t");
                        /* Highest/Rest/Mismatch */
                        if(auto_qscore[j] == 5 && human_qscore[j] == 5){
                            bw.write("Highest\t");
                            HighestRestMismatch[j] = 1;
                        }
                        else if(auto_qscore[j] == 5 || human_qscore[j] == 5){
                            bw.write("Mismatch\t");
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
                        else if(auto_qscore[j] < 4 && human_qscore[j] < 4){
                            bw.write("Low\t");
                            HighLowMismatch[j] = 0.5;
                        }
                        else{
                            bw.write("Mismatch\t");
                            HighLowMismatch[j] = 0;
                        }

                        /* Exact-match */
                        if(auto_qscore[j] == human_qscore[j]){
                            bw.write("yes\t");
                            ExactMatch[j] = 1;
                        }
                        else{
                            bw.write("no\t");
                            ExactMatch[j] = 0;
                        }

                        /* Partial-match */
                        if(auto_qscore[j] == human_qscore[j]){
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

                            System.err.println(Arrays.toString(auto_actual_scores_array));
                            System.err.println(Arrays.toString(human_actual_scores_array));

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
                                    if (mid_qt_thrs_index != 4){
                                    	
                                    
                                    if(mid_qt_thrs_index != 3){
                                        p = auto_mid_qt_thrs - auto_actual_score;
                                        np = auto_actual_score - Double.parseDouble(auto_qt[mid_qt_thrs_index+1]);
                                    }
                                    else{
                                        p = human_actual_score - human_mid_qt_thrs;
                                        np = Double.parseDouble(human_qt[mid_qt_thrs_index-1]) - human_actual_score;
                                    }
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

                                System.err.println(auto_qscore[j]);

                                int mid_qt_thrs_index = 5 - auto_qscore[j];

                                System.err.println(mid_qt_thrs_index);

                                if(auto_qt.length > mid_qt_thrs_index && human_qt.length > mid_qt_thrs_index){
                                    double auto_mid_qt_thrs = Double.parseDouble(auto_qt[mid_qt_thrs_index]);
                                    double human_mid_qt_thrs = Double.parseDouble(human_qt[mid_qt_thrs_index]);
                                    double p = 0.0;
                                    double np = 0.0;
                                    if (mid_qt_thrs_index != 4){
                                    
                                    if(mid_qt_thrs_index != 3){

                                        //if(human_qt[mid_qt_thrs_index+1]

                                        p = human_mid_qt_thrs - human_actual_score;

                                        System.err.println(Arrays.toString(human_qt));

                                        np = human_actual_score - Double.parseDouble(human_qt[mid_qt_thrs_index+1]);
                                    }
                                    else{
                                        System.err.println("%%%%%%%%%%%%%%%%%");
                                        p = auto_actual_score - auto_mid_qt_thrs;
                                        np = Double.parseDouble(auto_qt[mid_qt_thrs_index-1]) - auto_actual_score;
                                    }
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



