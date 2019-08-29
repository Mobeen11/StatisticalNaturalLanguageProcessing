import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Simple implementation of a bi-gram matrix which can normalize itself and
 * apply Laplace smoothing to its inner counts.
 */
public class BigramMatrix {
    /**
     * Token used for the start of a sentence.
     */
    public static final String SENTENCE_START = "<s>";
    /**
     * Token used for the end of a sentence.
     */
    public static final String SENTENCE_END = "</s>";
    
    // YOUR CODE HERE
    int row, column;
    // Non Duplicated TOkens
    List<String> NDTokens = new ArrayList();
    // Non Duplicated TOkens size
    int NDTokens_size;
    // UNI-Grams
    int[] uni_grams;
    // BI-Grams
    double[][] bi_grams;
    // BI-Grams Normalized 
    double[][] bi_grams_normalized;
    // Ngram is smooth
    boolean smooth = false;
    
    /**
     * Constructor.
     */
    public BigramMatrix(List<String> tokens) {
        create(tokens);
    }

    /**
     * Internal method for creating the bi-gram matrix from a given list of tokens.
     * 
     * @param tokens
     *            the tokens of an input text for which the matrix should be
     *            initialized.
     */
    static List<String> removeDuplicates(List<String> list) {
        // Store unique items in result.
        List<String> result = new ArrayList<String>();
        // Loop over argument list.
        for (String token : list) {
          if (result.indexOf(token) == -1) { 
              result.add(token);
          }
        }
        return result;
    }
    
    protected void create(List<String> tokens) {
        // YOUR CODE HERE
        NDTokens = removeDuplicates(tokens);
        NDTokens_size = NDTokens.size();
        
        column = row = NDTokens_size;
        uni_grams = new int[NDTokens_size];
        
        bi_grams = new double[row][column];
        bi_grams_normalized = new double[row][column];
        
        for (int i = 0; i < tokens.size()-1; i ++) {
            bi_grams[NDTokens.indexOf(tokens.get(i))][NDTokens.indexOf(tokens.get(i+1))] += 1;
            uni_grams[NDTokens.indexOf(tokens.get(i))] += 1;
          }
        uni_grams[NDTokens.indexOf(tokens.get(tokens.size() - 1))] += 1;
    }

    /**
     * Transforms the internal count matrix into a normalized counts matrix.
     */
    public void normalize() {
        // YOUR CODE HERE
        String w1, w2;
        for (int i = 0; i < row; i++) {
            w1 = NDTokens.get(i);
            for (int j = 0; j < column; j++) {
                w2 = NDTokens.get(j);
                if(smooth) {
                    bi_grams_normalized[NDTokens.indexOf(w1)][NDTokens.indexOf(w2)] = (getCount(w1, w2))/(uni_grams[NDTokens.indexOf(w1)] + NDTokens_size);
                }else {
                    bi_grams_normalized[NDTokens.indexOf(w1)][NDTokens.indexOf(w2)] = getCount(w1, w2)/(uni_grams[NDTokens.indexOf(w1)]);
                }
            }
        }
    }
    

    /**
     * Performs the Laplace smoothing on the bi-gram matrix.
     */
    public void performLaplaceSmoothing() {
        // YOUR CODE HERE
        String w1, w2;
        for (int i = 0; i < NDTokens.size(); i++) {
            w1 = NDTokens.get(i);
            for (int j = 0; j < NDTokens.size(); j++) {
                w2 = NDTokens.get(j);
                // Adding one with everything
                bi_grams[NDTokens.indexOf(w1)][NDTokens.indexOf(w2)]++;
             }
           }
           smooth = true;
           normalize();
    }
    
    /**
     * Returns the count of the bi-gram matrix for the bi-gram (word1, word2).
     */
    public double getCount(String word1, String word2) {
        double count = 0;
        // YOUR CODE HERE
        if (KnownWord(word1) && KnownWord(word2)) {
            count = bi_grams[NDTokens.indexOf(word1)][NDTokens.indexOf(word2)];
        } else {
            count = smooth ? 1 : 0;
        }
        return count;
    }
    
    public double getCount_Normalized(String word1, String word2) {
        double count = 0;
        // YOUR CODE HERE
        if (KnownWord(word1) && KnownWord(word2)) {
            count = bi_grams_normalized[NDTokens.indexOf(word1)][NDTokens.indexOf(word2)];
        } else {
        if(smooth) {
            double temp = (!KnownWord(word1)) ? 0 : uni_grams[NDTokens.indexOf(word1)];
            double sum = temp+NDTokens_size;
            count = 1/sum;
        }
            count = smooth ? 1 : 0;
        }
        return count;
   }
    
    public boolean KnownWord(String string) {
        return NDTokens.indexOf(string) != -1;
    }

    /**
     * Returns the normalized count of the bi-gram matrix for the bi-gram (word1, word2) (i.e., P(word2 | word1)).
     */
    public double getNormalizedCount(String word1, String word2) {
//         double normalizedCount = 0;
//         // YOUR CODE HERE
//         return normalizedCount;
        
        double normalizedCount = 0;
        // YOUR CODE HERE
        if (KnownWord(word1) && KnownWord(word2)) {
            normalizedCount = bi_grams_normalized[NDTokens.indexOf(word1)][NDTokens.indexOf(word2)];
        } else {
        if(smooth) {
            double temp = (!KnownWord(word1)) ? 0 : uni_grams[NDTokens.indexOf(word1)];
            double sum = temp+NDTokens_size;
            normalizedCount = 1/sum;
        }
        }
        return normalizedCount;
        
    }
}
// This line should make sure that compile errors are directly identified when executing this cell
// (the line itself does not produce any meaningful result)
(new BigramMatrix(Arrays.asList("a", "b"))).normalize();