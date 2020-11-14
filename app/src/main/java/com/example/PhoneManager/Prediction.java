package com.example.PhoneManager;

import android.util.Log;

import java.util.Arrays;
import java.util.List;

import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;

public class Prediction {
    public static void main(String[] categories, String[] features) {
        // Create a new bayes classifier with string categories and string features.
        Classifier<String, String> bayes = new BayesClassifier<String, String>();

        categories[0] = "y";
        categories[1] = "y";
        categories[2] = "n";
        features[0] = "0";
        features[1] = "1";
        features[2] = "2";

// Learn by classifying examples.
// New categories can be added on the fly, when they are first used.
// A classification consists of a category and a list of features
// that resulted in the classification in that category.
        for (int i = 0; i < categories.length; i++)
            bayes.learn(categories[i], Arrays.asList(features[i]));

// Here are two unknown sentences to classify.

        String[] testStrings = {"0", "1", "2"};
        for (int i = 0; i < testStrings.length; i++) {
            String[] unknownText1 = testStrings[i].split("\\s");

            String string = bayes.classify(Arrays.asList(unknownText1)).getCategory();
            // will output "positive"
            Log.e("liu", string);
        }
        String[] unknownText1 = "hello world".split("\\s");
// Get more detailed classification result.
        ((BayesClassifier<String, String>) bayes).classifyDetailed(
                Arrays.asList(unknownText1));

// Change the memory capacity. New learned classifications (using
// the learn method) are stored in a queue with the size given
// here and used to classify unknown sentences.
        bayes.setMemoryCapacity(500);// Create a new bayes classifier with string categories and string features.
    }
}
