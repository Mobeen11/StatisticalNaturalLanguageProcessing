import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * A simple spell correction approach based on tri-grams and the Levenshtein
 * distance.
 */
public class TrigramBasedSpellCorrector {
    // YOUR CODE HERE
    public static final String SENTENCE_START = "<s>";
    public static final String SENTENCE_END = "</s>";

	Map<String, Integer> trigram = new HashMap<String, Integer>();
    
    List<String> values = new ArrayList();
    List<String> keys = new ArrayList();
    
    /**
     * Constructor.
     */
    public TrigramBasedSpellCorrector(String text) {
        create(text);
    }
    
    public static boolean isPunctuation(char c) {
        return c == '.'|| c == '!'|| c == '?';
    }
    
    public static List<String> preprocess(String text){
        List<String> tokens = new ArrayList<String>();
        
//    		TO LOWER CASE 
        text = text.toLowerCase();
        text = text.replaceAll("[^a-zA-Z0-9!.?]", " ");

    // to CHAR 
//     		System.out.print("nontrim: "+text.length());
        text = text.trim();

    //   System.out.print("trim: "+text.length());
        char[] char_array = text.toCharArray();
        String punctutations = "\"#$%&'()*+,-/:;<=>@[]^_`{|}~";
        String ending_punc = "!.?";
        String string = "";
        boolean start_char = true;
        for (int i = 0; i < char_array.length; i++) {
            if(start_char) {
                tokens.add(SENTENCE_START);
                start_char = false;
            }
//     			System.out.print("\n C: "+" ("+i+") " + char_array[i]);
        if(Character.isLetterOrDigit(char_array[i])) {
            string = string + char_array[i];
        }
        else if(Character.isWhitespace(char_array[i])) {
            if (!string.isEmpty()) {
                tokens.add(string);
                string = "";
                continue;
            }
        }
        else if(isPunctuation(char_array[i])) {
            if(!(string.isEmpty() || string.contains(" "))) {
                tokens.add(string);
                string = "";
                tokens.add(SENTENCE_END);
                if(i != char_array.length-1) {
                    start_char = true;
                }
            }
        }
    }
//     		System.out.print("\n token: "+ tokens);
        return tokens;
    }
    
    public int calcLevenshteinDistance(String string1, String string2) {
        int distance = 0;
        // YOUR CODE HERE    
        
        int [] costs = new int [string2.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= string1.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= string2.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), string1.charAt(i - 1) == string2.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
//         System.out.print("\n1- :"+ costs[string2.length()]);
        distance = costs[string2.length()];
        return distance;
    }
    
    
    /**
     * Internal methods for determining the necessary statistics about the tri-grams
     * of the given text.
     */
    protected void create(String text) {
        // YOUR CODE HERE
        List<String> tokens = preprocess(text);
        
        for (int i = 0; i < tokens.size() - 2 ; i++) {
            String string = tokens.get(i) + " " + tokens.get(i+1) + " " + tokens.get(i+2);
            values.add(string);
        }
        
        for (int i = 0; i < values.size(); i++) {
            if (!trigram.containsKey(values.get(i))) {  
                trigram.put(values.get(i), 1);
            }
            else {
                int count = trigram.get(values.get(i));
                trigram.put(values.get(i), count + 1);
            }
		}
        
        
    }

    /**
     * Returns the correction of the third word based on the internal tri-grams that
     * start with word1 and word2 as well as the Levenshtein distance of candidates
     * from these tri-grams to the given word3.
     * 
     * @return a word for which a tri-gram with word1 and word2 at the beginning
     *         exists and which has the smallest Levenshtein distance to the given
     *         word3. Or null, if such a word does not exist.
     */
    public String getCorrection(String word1, String word2, String word3) {
        String correctWord = null;
        word1 = word1.replaceAll("[^A-Za-z0-9]", "");
        word2 = word2.replaceAll("[^A-Za-z0-9]", "");
        word3 = word3.replaceAll("[^A-Za-z0-9]", "");

         String temp_str = (word1+ " " +word2).toLowerCase();

         int temp = 1000;
         List<String> keys = new ArrayList<>(trigram.keySet());
         String lastWord = "";

         for(int i=0; i < keys.size(); i++){

             if(word1 == "" || word2 == "" || word3 == "" || temp_str == "" || keys.get(i) == "")
                 correctWord = null;

             else if(keys.get(i).startsWith(temp_str)){


                 lastWord = keys.get(i).substring (keys.get(i).lastIndexOf (" ")+1);
                 int distance = calcLevenshteinDistance(lastWord,word3);
                 if(distance < temp){
                     temp = distance;
                     correctWord = lastWord;
                 }

             }
         }
         return correctWord;
        // YOUR CODE HERE
    }
}
// This line should make sure that compile errors are directly identified when executing this cell
// (the line itself does not produce any meaningful result)
Arrays.sort(new Object[]{new TrigramBasedSpellCorrector("")});