import java.util.*;
import java.util.regex.*;

/**
 * Resume-to-Job Match Score
 * A simple NLP tool that scores how well a resume matches a job description
 * using TF-IDF vectorization and cosine similarity.
 *
 * Building AI course project demo
 */
public class ResumeJobMatcher {

    // Tokenize text: lowercase and extract words only
    public static List<String> tokenize(String text) {
        List<String> words = new ArrayList<>();
        Matcher matcher = Pattern.compile("[a-zA-Z]+").matcher(text.toLowerCase());
        while (matcher.find()) {
            words.add(matcher.group());
        }
        return words;
    }

    // Term frequency: count of each word divided by total word count
    public static Map<String, Double> computeTf(List<String> words) {
        Map<String, Double> tf = new HashMap<>();
        int total = words.size();

        for (String word : words) {
            tf.put(word, tf.getOrDefault(word, 0.0) + 1.0);
        }
        for (String word : tf.keySet()) {
            tf.put(word, tf.get(word) / total);
        }
        return tf;
    }

    // Inverse document frequency across both documents (resume + JD)
    public static Map<String, Double> computeIdf(List<List<String>> documents, Set<String> vocabulary) {
        Map<String, Double> idf = new HashMap<>();
        int n = documents.size();

        for (String word : vocabulary) {
            long docCount = documents.stream().filter(doc -> doc.contains(word)).count();
            double value = Math.log((double) (n + 1) / (docCount + 1)) + 1; // smoothed idf
            idf.put(word, value);
        }
        return idf;
    }

    // Build tf-idf vector for one document, ordered by vocabulary list
    public static double[] computeTfidfVector(Map<String, Double> tf, Map<String, Double> idf, List<String> vocabulary) {
        double[] vector = new double[vocabulary.size()];
        for (int i = 0; i < vocabulary.size(); i++) {
            String word = vocabulary.get(i);
            double tfValue = tf.getOrDefault(word, 0.0);
            vector[i] = tfValue * idf.get(word);
        }
        return vector;
    }

    // Cosine similarity between two vectors
    public static double cosineSimilarity(double[] vec1, double[] vec2) {
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < vec1.length; i++) {
            dotProduct += vec1[i] * vec2[i];
            norm1 += vec1[i] * vec1[i];
            norm2 += vec2[i] * vec2[i];
        }

        if (norm1 == 0 || norm2 == 0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    // Calculates a match score (0-100%) between a resume and a job description
    public static double matchScore(String resumeText, String jobDescriptionText) {
        List<String> resumeWords = tokenize(resumeText);
        List<String> jdWords = tokenize(jobDescriptionText);

        List<List<String>> documents = Arrays.asList(resumeWords, jdWords);

        Map<String, Double> tfResume = computeTf(resumeWords);
        Map<String, Double> tfJd = computeTf(jdWords);

        Set<String> vocabularySet = new HashSet<>();
        vocabularySet.addAll(resumeWords);
        vocabularySet.addAll(jdWords);
        List<String> vocabulary = new ArrayList<>(vocabularySet);

        Map<String, Double> idf = computeIdf(documents, vocabularySet);

        double[] vecResume = computeTfidfVector(tfResume, idf, vocabulary);
        double[] vecJd = computeTfidfVector(tfJd, idf, vocabulary);

        double similarity = cosineSimilarity(vecResume, vecJd);
        return Math.round(similarity * 10000) / 100.0; // percentage rounded to 2 decimals
    }

    public static void main(String[] args) {
        String resumeText = "Senior Software Engineer with 7+ years of experience in Java Spring Boot "
                + "microservices and REST API development. Led API development at Westpac "
                + "Banking Group. Experience with Kafka, SQL, Docker, Jenkins CICD and "
                + "Agile Scrum methodology. AWS Cloud Practitioner certified.";

        String jobDescriptionText = "We are seeking a Senior Software Developer with experience in Java "
                + "Spring Boot and microservices. Hands-on experience with REST API design, "
                + "SQL databases, Docker containerisation and CICD pipelines using Jenkins. "
                + "Experience in Agile Scrum methodology required. Banking domain experience "
                + "is a strong advantage.";

        double score = matchScore(resumeText, jobDescriptionText);
        System.out.println("Resume-Job Match Score: " + score + "%");
    }
}
