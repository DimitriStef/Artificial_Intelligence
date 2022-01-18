
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

public class LogisticRegressionFunctions {

    //sigmoid function
    public double sigmoid(double num) {
        return 1.0 / (1.0 + Math.exp(-num));
    }

    //get random value array
    public double[] getRandomWeightVector(int emailsSize) {
        double[] vec = new double[emailsSize];
        for (int i = 0; i < emailsSize; i++) {
            vec[i] = getRandomDouble(-0.2, 0.2);
        }
        return vec;
    }
    //get random numbers

    public static double getRandomDouble(double min, double max) {
        double x = (Math.random() * ((max - min) + 1)) + min;
        return x;
    }

    //read all txt files/emails
    public ArrayList<String> getEmails(File file) throws IOException {
        ArrayList<String> emails = new ArrayList<String>();
        BufferedReader breader = null;
        for (File f : file.listFiles()) {
            breader = new BufferedReader(new FileReader(f));
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

    //convert email file to arralist of array string, each array object contains an word array of email
    public ArrayList<String[]> getEmailsMatrix(ArrayList<String> emails) {
        ArrayList<String[]> matrix = new ArrayList<String[]>();
        for (int i = 0; i < emails.size(); i++) {
            matrix.add(getTokens(emails.get(i)));
        }
        return matrix;
    }

    //create a 2d matrix 
    public double[][] featuresMatrix(HashMap<String, Integer> voc, ArrayList<String[]> emails) {
        int rowSize = voc.size() + 1;
        int columnSize = emails.size();
        double[][] matrix = new double[columnSize][rowSize];

        for (int i = 0; i < emails.size(); i++) {
            matrix[i][0] = 1;
            for (int j = 0; j < emails.get(i).length; j++) {
                String word = emails.get(i)[j];
                int z = 1;
                for (Map.Entry<String, Integer> map : voc.entrySet()) {
                    if (map.getKey().equals(word)) {
                        matrix[i][z] = 1;
                    }
                    z++;
                }
            }
        }
        return matrix;
    }

    //get label array
    public double[] labelArray(File file) {
        double[] array = new double[file.listFiles().length];
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

    //multiply matrix with a vector
    public double[] multiplyMatrix(double[][] matrix, double[] vector) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        double[] vec = new double[rows];

        for (int row = 0; row < rows; row++) {
            double sum = 0;
            for (int column = 0; column < columns; column++) {
                sum += matrix[row][column] * vector[column];
            }
            vec[row] = sum;
        }
        return vec;
    }

    // apply sigmoid function to weight vector
    public double[] calculateSigmoidVector(double[] vec) {
        double[] arr = new double[vec.length];
        for (int i = 0; i < vec.length; i++) {
            arr[i] = sigmoid(vec[i]);
        }
        return arr;
    }

    //get model predictions
    public double[] getPredictions(double[] weights, double[][] testFeutureMatrix) {
        double[] testEmailsPredict = calculateSigmoidVector(multiplyMatrix(testFeutureMatrix, weights));
        for (int i = 0; i < testEmailsPredict.length; i++) {

            if (testEmailsPredict[i] >= 0.5) {
                testEmailsPredict[i] = 1;
            } else {
                testEmailsPredict[i] = 0;
            }
        }
        return testEmailsPredict;
    }
    //get the vocabulary

    public HashMap<String, Integer> getFeaturesVocabulary(ArrayList<String> emails, int getEntries) {
        //put words in hashMap, with key the word/value the frequency
        HashMap<String, Integer> vocabulary = countWords(getWordList(emails));
        //sort the hashmap by value
        HashMap<String, Integer> sorted_voc = sortHashMap(vocabulary);
        //returns the top entries of hashmap
        HashMap<String, Integer> final_voc = getHashMapByEntries(sorted_voc, getEntries);
        return final_voc;
    }

    //train model
    public double[] stochasticGradientAscent(double[] label, double[][] featuresMatrix, double lr, int max_iters) {
        double[] weights = getRandomWeightVector(featuresMatrix[0].length);
        double precision = 0.00001;
        int iters = 0;
        double yHat = 0;
        double error = 0;

        while (error > precision || max_iters > iters) {

            for (int i = 0; i < featuresMatrix.length; i++) {
                double y = label[i];
                for (int j = 0; j < weights.length; j++) {
                    yHat += weights[j] * featuresMatrix[i][j];
                }
                yHat = sigmoid(yHat);
                error = objectiveFunction(yHat, y);
                for (int j = 0; j < weights.length; j++) {
                    weights[j] = weights[j] + lr * (y - yHat) * featuresMatrix[i][j];
                }
            }
            iters++;
        }
        return weights;
    }

    //error function
    private double objectiveFunction(double yHat, double y) {
        return (Math.log(yHat) * y) + ((1 - y) * Math.log(1 - yHat));
    }

    //confusion matrix 
    public static void printConfusionMatrix(double[] testLabelVector, double[] testEmailsPredict) {
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

        for (int i = 0; i < testLabelVector.length; i++) {
            if (testLabelVector[i] == spam) {
                spamEmailsSize++;
            } else {
                hamEmailsSize++;
            }
            if (testLabelVector[i] == spam && testEmailsPredict[i] == spam) {
                True_Positive++;
            }
            if (testLabelVector[i] == spam && testEmailsPredict[i] == ham) {
                False_Positive++;
            }
            if (testLabelVector[i] == ham && testEmailsPredict[i] == ham) {
                True_Negative++;
            }
            if (testLabelVector[i] == ham && testEmailsPredict[i] == spam) {
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

}
