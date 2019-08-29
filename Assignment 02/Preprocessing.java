/**
 * The token used to indicate the beginning of a new sentence.
 */
public static final String SENTENCE_START = "<s>";
/**
 * The token used to indicate the end of a sentence.
 */
public static final String SENTENCE_END = "</s>";

public static boolean isPunctuation(char c) {
        return c == '.'|| c == '!'|| c == '?';
}

public static List<String> preprocess(String text){
    List<String> tokens = new ArrayList<String>();
    
//		TO LOWER CASE 
    text = text.toLowerCase();
    text = text.replaceAll("[^a-zA-Z0-9!.?]", " ");

// to CHAR 
// 		System.out.print("nontrim: "+text.length());
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
// 			System.out.print("\n C: "+" ("+i+") " + char_array[i]);
    if(Character.isLetterOrDigit(char_array[i])) {
        string = string + char_array[i];
    }
    else if(Character.isWhitespace(char_array[i])) {
        if (!string.isEmpty()) {
//             System.out.print("\n white space: "+char_array[i]);
//             System.out.print("\n string: "+string.length());
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
// 		System.out.print("\n token: "+ tokens);
    return tokens;
}

// This line should make sure that compile errors are directly identified when executing this cell
// (the line itself does not produce any meaningful result)
preprocess("").clear();