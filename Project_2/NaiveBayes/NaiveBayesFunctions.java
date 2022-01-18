
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NaiveBayesFunctions {

    private ArrayList<String> spam_emails = new ArrayList<String>();
    private ArrayList<String> ham_emails = new ArrayList<String>();
    //get ham emails

    public ArrayList<String> getHam_emails() {
        return ham_emails;
    }
    //get spam emails

    public ArrayList<String> getSpam_emails() {
        return spam_emails;
    }

    //calculate probabilities
    public double getVectorProb(HashMap<String, Integer> email, HashMap<String, Integer> voc) {
        double laplace_num = 1;
        double laplace_den = voc.size();
        double prob = 1;

        for (Map.Entry<String, Integer> voc_word : voc.entrySet()) {
            String word = voc_word.getKey();
            if (email.containsKey(word)) {
                prob += Math.log((voc_word.getValue() + laplace_num) / (voc.size() + laplace_den));
            } else {
                prob += Math.log((0 + laplace_num) / (voc.size() + laplace_den));
            }
        }
        return prob;
    }

    // get prediction spam or ham
    public int getPrediction(HashMap<String, Integer> email, HashMap<String, Integer> spam_voc, HashMap<String, Integer> ham_voc, double spamSize, double HamSize, double totalSize) {
        double probIsSpam = spamSize / totalSize;
        double probIsHam = HamSize / totalSize;
        double isSpam = Math.log(probIsSpam) + getVectorProb(email, spam_voc);
        double isHam = Math.log(probIsHam) + getVectorProb(email, ham_voc);

        if (isSpam > isHam) {
            return 1;
        }

        return 0;
    }

    //read all Test txt files/emails and create an ArrayList of String, every email is a String
    public ArrayList<String> getTestEmails(File file) throws IOException {
        ArrayList<String> emails = new ArrayList<String>();
        BufferedReader breader = null;

        for (File email_dir : file.listFiles()) {
            breader = new BufferedReader(new FileReader(email_dir));
            int i;
            String data = "";
            while ((i = breader.read()) != -1) {
                data += (char) i;
            }
            emails.add(data);
            breader.close();
        }

        return emails;
    }

    //read all Train txt files/emails , separate spam from ham emails 	
    public void getTrainEmails(File file) throws IOException {
        String regex = "\\Bspmsg|spmsg\\B";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher;
        boolean isSpam;
        BufferedReader breader = null;

        for (File emails : file.listFiles()) {
            breader = new BufferedReader(new FileReader(emails));
            matcher = pattern.matcher(emails.getName());
            isSpam = matcher.find();

            int i;
            String data = "";
            while ((i = breader.read()) != -1) {
                data += (char) i;
            }
            if (isSpam) {
                spam_emails.add(data);
            } else {
                ham_emails.add(data);
            }
            breader.close();
        }
    }

    public int[] labelArray(int size, File file) {
        int[] array = new int[size];
        String regex = "\\Bspmsg|spmsg\\B";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher;
        boolean isSpam;
        String email_dir;
        int index = 0;

        for (File emails : file.listFiles()) {
            email_dir = emails.getName();
            matcher = pattern.matcher(email_dir);
            isSpam = matcher.find();
            if (isSpam) {
                array[index] = 1;
            }
            index++;
        }
        return array;
    }

    //create an arralist with all the words
    public ArrayList<String> getWordList(ArrayList<String> emails) {
        ArrayList<String> words = new ArrayList<String>();
        for (String email : emails) {
            for (String word : getTokens(email)) {
                words.add(word);
            }
        }
        return words;
    }

    //create an arralist of emails, every email is an ArrayList of words 
    public ArrayList<ArrayList<String>> getTestWordList(ArrayList<String> emails) {
        ArrayList<ArrayList<String>> emailList = new ArrayList<ArrayList<String>>();
        ArrayList<String> words;

        for (String email : emails) {
            words = new ArrayList<String>();
            for (String word : getTokens(email)) {
                words.add(word);
            }
            emailList.add(words);
        }
        return emailList;
    }

    //put words in hashMap, with key the word/value the frequency
    public HashMap<String, Integer> countWords(ArrayList<String> list) {
        HashMap<String, Integer> dict = new HashMap<String, Integer>();
        for (String word : list) {
            if (dict.containsKey(word)) {
                dict.put(word, dict.get(word) + 1);
            } else {
                dict.put(word, 1);
            }
        }
        return dict;
    }
    //put emails in ArrayList, every email is a hashMap, with key the word/value the frequency

    public ArrayList<HashMap<String, Integer>> countTestWords(ArrayList<ArrayList<String>> list) {
        ArrayList<HashMap<String, Integer>> email_dict = new ArrayList<HashMap<String, Integer>>();
        HashMap<String, Integer> dict;

        for (ArrayList<String> email : list) {
            dict = new HashMap<String, Integer>();
            for (String word : email) {
                if (dict.containsKey(word)) {
                    dict.put(word, dict.get(word) + 1);
                } else {
                    dict.put(word, 1);
                }
            }
            email_dict.add(dict);
        }
        return email_dict;
    }

    //sort the hashmap by value
    public HashMap<String, Integer> sortHashMap(HashMap<String, Integer> hashmap) {
        // Create a list from elements of HashMap 
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(hashmap.entrySet());
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        // put data from sorted list to hashmap  
        HashMap<String, Integer> temp_map = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> v : list) {
            temp_map.put(v.getKey(), v.getValue());
        }
        return temp_map;
    }

    //tokenize an email to words
    public String[] getTokens(String document) {
        String[] tokens = document.trim().split("\\s+");
        List<String> cleanTokens = new ArrayList<>();

        for (String token : tokens) {
            String cleanToken = token.trim().toLowerCase().replaceAll("[^A-Za-z\']+", "");
            if (cleanToken.length() > 3) {
                cleanTokens.add(cleanToken);
            }
        }
        return cleanTokens.toArray(new String[0]);
    }

    //returns the top entries of hashmap
    public HashMap<String, Integer> getHashMapByEntries(HashMap<String, Integer> hashmap, int entries) {
        HashMap<String, Integer> temp_hashmap = new HashMap<String, Integer>();

        int counter = 0;
        for (Map.Entry<String, Integer> pair : hashmap.entrySet()) {
            temp_hashmap.put(pair.getKey(), pair.getValue());
            counter++;
            if (counter == entries) {
                break;
            }
        }
        return temp_hashmap;
    }

    //print hashmap
    public void printHashMap(HashMap<String, Integer> hashmap, int entries) {
        int count = 0;
        //print hashmap
        for (Map.Entry<String, Integer> pair : hashmap.entrySet()) {
            System.out.format("word: %s, frequency: %d%n", pair.getKey(), pair.getValue());

            count++;
            if (count == entries) {
                break;
            }
        }
    }

    //create confusion matrix and stats
    public void printConfusionMatrix(int[] label, ArrayList<Integer> testEmailsPredict) {
        /* 1. True_Positive Spam Emails classified as Spam
		 * 2. True_Negative Spam emails classified as Ham
		 * 3. False_Positive Ham emails  classified as Spam
		 * 4. True_Negative Ham emails  classified as Ham
		 * */
        int spamEmailsSize = 0;
        int hamEmailsSize = 0;

        int True_Positive = 0;
        int True_Negative = 0;
        int False_Positive = 0;
        int False_Negative = 0;
        int spam = 1;
        int ham = 0;
        float Precision;
        float Recall;
        float F1_score;
        float Accuracy;

        for (int i = 0; i < label.length; i++) {
            if (label[i] == spam) {
                spamEmailsSize++;
            } else {
                hamEmailsSize++;
            }

            if (label[i] == spam && testEmailsPredict.get(i) == spam) {
                True_Positive++;
            }
            if (label[i] == spam && testEmailsPredict.get(i) == ham) {
                False_Positive++;
            }
            if (label[i] == ham && testEmailsPredict.get(i) == ham) {
                True_Negative++;
            }
            if (label[i] == ham && testEmailsPredict.get(i) == spam) {
                False_Negative++;
            }

        }
        //What proportion of positive identifications was actually correct
        Precision = True_Positive / (float) (True_Positive + False_Positive);
        Recall = True_Positive / (float) (True_Positive + False_Negative);
        F1_score = 2 * (Precision * Recall) / (Precision + Recall);
        Accuracy = (True_Positive + True_Negative) / (float) (True_Positive + True_Negative + False_Positive + False_Negative);

        System.out.println("-----Confusion Matrix-----");
        System.out.println("\tSpam   Ham");
        System.out.println("\t_____ _____");
        System.out.println("Spam   | " + True_Positive + " |  " + False_Negative + "  |");
        System.out.println("Ham    |  " + False_Positive + "  | " + True_Negative + " |");

        System.out.println("\n---Stats---");

        System.out.printf("Accuracy: %,.3f%%\n", Accuracy * 100);
        System.out.printf("Recall: %,.3f%n", Recall);
        System.out.printf("Precision: %,.3f%n", Precision);
        System.out.printf("F1_score: %,.3f%n", F1_score);
    }

}//end Class

