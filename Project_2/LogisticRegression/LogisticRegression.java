
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LogisticRegression {

    public static void main(String[] args) throws IOException {

        LogisticRegressionFunctions lg = new LogisticRegressionFunctions();

        /*-------------TRAIN---------------*/
        //the size of features vector								
        int featureVectorSize = 150;
        //max iterations of gradient ascent
        int maxIters = 500;
        //learning rate
        double learningRate = 0.01;
        //get directory of train data
        String dir = "..//train_data";
        //create file object
        File file = new File(dir);
        //arrayList of strings, each string is an email
        ArrayList<String> emails = lg.getEmails(file);
        //get features vocabulary 
        HashMap<String, Integer> featuresVocabulary = lg.getFeaturesVocabulary(emails, featureVectorSize);
        //get train email matrix every email is an String array		
        ArrayList<String[]> emailMatrix = lg.getEmailsMatrix(emails);
        //get feature matrix
        double[][] featureMatrix = lg.featuresMatrix(featuresVocabulary, emailMatrix);
        //get label array
        double[] labelVector = lg.labelArray(file);
        //get predicted weights	 				
        double[] weights = lg.stochasticGradientAscent(labelVector, featureMatrix, learningRate, maxIters);

        /*-------------TEST---------------*/
        //get directory of test data
        String dir_test = "..//test_data";
        //create file object
        File test_file = new File(dir_test);
        //arrayList of strings, each string is an email
        ArrayList<String> test_emails = lg.getEmails(test_file);
        //get test email matrix every email is an String array 
        ArrayList<String[]> testEmailMatrix = lg.getEmailsMatrix(test_emails);
        //get feuture matrix
        double[][] testFeutureMatrix = lg.featuresMatrix(featuresVocabulary, testEmailMatrix);

        /*-------------PREDICT---------------*/
        //get predictions 
        double[] testEmailsPredict = lg.getPredictions(weights, testFeutureMatrix);
        // get test email Labels       
        double[] testLabelVector = lg.labelArray(test_file);

        /*-------------PRINT RESULTS---------------*/
        //print stats
        lg.printConfusionMatrix(testLabelVector, testEmailsPredict);

        System.out.println("\nTrain Emails :" + emails.size());
        System.out.println("Test Emails :" + test_emails.size());
        System.out.printf("Train Emails percentage: %,.1f%%\n", (emails.size() / (double) (test_emails.size() + emails.size())) * 100);
        System.out.printf("Test Emails percentage: %,.1f%%\n", (test_emails.size() / (double) (test_emails.size() + emails.size())) * 100);
        System.out.println("Vocabulary Size :" + featureVectorSize);
        System.out.println("Learning Rate :" + learningRate + "\n");

    }
}
