package ds.lab;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Pair;

import java.io.*;
import java.util.*;

public class WordAnalyzer {

    public static Map<String, Pair<Integer, String>> analyze(Map<String, Integer> map) {

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation annotation;

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(
                    new BufferedWriter(
                            new FileWriter("./output.xml", false)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Pair<Integer, String>> analyzedMap = new HashMap<>();

        String text = String.join(" ", map.keySet());

        annotation = new Annotation(text);

        pipeline.annotate(annotation);

        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        List<String> lemmas = new ArrayList<>();
        Map<String, String> tags = new HashMap<>();
        for (CoreMap sentence : sentences) {
            List<CoreLabel> labels = sentence.get(CoreAnnotations.TokensAnnotation.class);
            for (CoreLabel label : labels) {
                analyzedMap.put(label.value(), new Pair<>(map.get(label.value()), label.lemma()));
                lemmas.add(label.lemma());
            }
        }

        annotation = new Annotation(String.join(" ", lemmas));
        pipeline.annotate(annotation);
        sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            List<CoreLabel> labels = sentence.get(CoreAnnotations.TokensAnnotation.class);
            for (CoreLabel label : labels) {
                analyzedMap.put(label.value(), new Pair<>(map.get(label.value()), label.lemma()));
                tags.put(label.value(), label.tag());
            }
        }

        Utils.addTags(tags);

        pw.close();
        return analyzedMap;
    }
}