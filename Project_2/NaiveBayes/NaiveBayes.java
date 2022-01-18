
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class NaiveBayes {

    private static NaiveBayesFunctions nb = new NaiveBayesFunctions();
    private static ArrayList<String> spam_emails = nb.getSpam_emails();
    private static ArrayList<String> ham_emails = nb.getHam_emails();

    public static void main(String args[]) throws IOException {

        /*-------------TRAIN---------------*/
        //get directory
        String dir = "..\\train_data";
        //get file object
        File file = new File(dir);
        //read file and spit spam/ham
        nb.getTrainEmails(file);
        //
        ArrayList<String> spam_wordList = nb.getWordList(spam_emails);
        ArrayList<String> ham_wordList = nb.getWordList(ham_emails);

        //put words in hashMap, with key the word/value the frequency
        HashMap<String, Integer> spam_vocabulary = nb.countWords(spam_wordList);
        HashMap<String, Integer> ham_vocabulary = nb.countWords(ham_wordList);

        //sort the hashmap by value
        HashMap<String, Integer> sorted_spam_voc = nb.sortHashMap(spam_vocabulary);
        HashMap<String, Integer> sorted_ham_voc = nb.sortHashMap(ham_vocabulary);

        int vocabularySize = 80;
        //returns the top entries of hashmap
        HashMap<String, Integer> final_spam_voc = nb.getHashMapByEntries(sorted_spam_voc, vocabularySize);
        HashMap<String, Integer> final_ham_voc = nb.getHashMapByEntries(sorted_ham_voc, vocabularySize);

        /*-------------TEST---------------*/
        //get directory
        String dir_test = "..\\test_data";
        //get file object
        File test_file = new File(dir_test);
        //arrayList of strings, each string is an email
        ArrayList<String> test_emails = nb.getTestEmails(test_file);
        //arryList of all words spam/ham
        ArrayList<ArrayList<String>> test_wordList = nb.getTestWordList(test_emails);
        //put words in hashMap, with key the word/value the frequency
        ArrayList<HashMap<String, Integer>> test_vocabulary = nb.countTestWords(test_wordList);
        //size of spam emails
        double spams_size = spam_emails.size();
        //size of ham emails
        double ham_size = ham_emails.size();
        //size of total emails
        double total_emails = spams_size + ham_size;
        //get label array  
        int[] label = nb.labelArray(test_emails.size(), test_file);
        //arrayList of predicted values
        ArrayList<Integer> testEmailsPredict = new ArrayList<Integer>();

        /*-------------	PREDICT---------------*/
        // get prediction 
        int isSpam;
        for (HashMap<String, Integer> email : test_vocabulary) {
            isSpam = nb.getPrediction(email, final_spam_voc, final_ham_voc, spams_size, ham_size, total_emails);
            testEmailsPredict.add(isSpam);
        }

        /*-------------PRINT RESULTS---------------*/
        //print confusion matrix
        nb.printConfusionMatrix(label, testEmailsPredict);

        System.out.println("\nTrain Emails :" + total_emails);
        System.out.println("Test Emails :" + test_emails.size());
        System.out.printf("Train Emails percentage: %,.1f%%\n", (total_emails / (double) (test_emails.size() + total_emails)) * 100);
        System.out.printf("Test Emails percentage: %,.1f%%\n", (test_emails.size() / (double) (test_emails.size() + total_emails)) * 100);
        System.out.println("Vocabulary Size :" + vocabularySize);

    }//end main()

}
