
/*

Implement a Naive Bayes classifier which is able to assign multiple classes to a single document by finalizing the two given classes. 
The MultiLabelLearner class acts like a builder for the MultiLabelClassifier instances. 
That means that the learner gets the number of classes during its creation and the learnExample method of the learner is called once for each 
document of the training set. Internally, the learner should gather all statistics that are necessary for the classifier 
when processing the training examples. After the learner saw all training documents, the createClassifier method is 
called which creates an instance of the MultiLabelClassifier class and initializes it with the statistics gathered before. 
The classification itself is carried out by the classify method which takes an unknown document and assigns it a set of classes learned before.

*/


// YOUR CODE HERE
    // stopwords
    public String[] stopwords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};
                    
    public static Set<String> stopWords_set ;
    // hashmaps for vocabulary
    HashSet<String> vocabulary = new HashSet<String>();                                 // all unique words so far
    
    HashMap<String, HashMap<String, Integer>> tokens_count_in_class = new HashMap<String,HashMap<String, Integer>>();                    // contains the tokens count in class
    HashMap<String, Integer> total_vocabulary_in_class = new HashMap<String, Integer>();         // total words in a class (not unique)
    // hashmaps for classes
    HashMap<String, Integer> classes_hashmap = new HashMap<String, Integer>();          // number of times class occur in our data set
    HashMap<String, Float> class_probability = new HashMap<String, Float>();            // probability of class over total classes

    // list of calculated probabilities of words
    HashMap<String, Float> final_probabilities = new HashMap<String, Float>();          // to store the calculated probabilities of word

/**
 * Classifier implementing naive Bayes classification for a multilabel
 * classification.
 */
public class MultiLabelClassifier {
    // YOUR CODE HERE
    public String[] stopWords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};
    ArrayList<String> stopWordsList =  new ArrayList<String>(Arrays.asList(stopWords));
    /**
     * Classifies the given document and returns the class names.
     */
    public Set<String> classify(String text) {
        Set<String> results = null;
        // YOUR CODE HERE
        results = testModel(text);
        return results;
    }
    public Boolean isStopWord (String word) {
        return stopWordsList.contains(word);   
    }
    
    public Set<String> testModel(String text) {
        text = text.toLowerCase();
          // to replace 
        text = text.replaceAll("[^\\p{L}\\s]", "");
         // split with space
        String[] word = text.split("\\s+");
        float best_probability = 0;
        String best_probability_class = null;
        
        Iterator iterator = tokens_count_in_class.keySet().iterator();
        // iterate over all classes and calculate probability of word in each class
        for (String entry : tokens_count_in_class.keySet()){
              
            
            float words_probability_of_class = 0;
            float sum_of_probabilities = 1;
            
            String key = iterator.next().toString();
            HashMap<String, Integer> value =  tokens_count_in_class.get(key);
            for (String string : word) {
                if(isStopWord(string)){
                    continue;
                }
                float word_prob = 0;
                
                float document_count_class =  value.get(string) == null ? 0 : value.get(string);       // count of word (w[i]) occur in class

                float total_class_count = total_vocabulary_in_class.get(key);                     // count of all words in class 
                
                float total_unique_count = vocabulary.size();   
                
                word_prob = (document_count_class + 1) / (total_class_count + total_unique_count);
                
                sum_of_probabilities = sum_of_probabilities +(float) Math.log(word_prob);
            
            }
            // P(c|d5) = 3/4×(3/7)3 ×1/14×1/14 = .0003 
            words_probability_of_class = (float) (Math.log(class_probability.get(key)) +  sum_of_probabilities);
            final_probabilities.put(key,words_probability_of_class);
            
             if( best_probability == 0){
                best_probability = words_probability_of_class;
                best_probability_class = key;
            }else if( best_probability < words_probability_of_class){
                best_probability = words_probability_of_class;
                best_probability_class = key;
                
            }
            
        }
//         System.out.println("best_probability_class: "+ best_probability_class);
        
        if (best_probability_class.contains(",")) {
            // Split it.
//             System.out.println("splitedif: "+new HashSet<String>(Arrays.asList(best_probability_class.split(","))));
            return new HashSet<String>(Arrays.asList(best_probability_class.split(","))); 
        } 
        else{
//             System.out.println("splited_else: "+ new HashSet<String>(Arrays.asList(best_probability_class)));
            return new HashSet<String>(Arrays.asList(best_probability_class)); 
        }
            
    }
    
}

/**
 * Learner (or Builder) class for a naive Bayes multilabel classifier.
 */
public class MultiLabelLearner {
    // YOUR CODE HERE
    public String[] stopWords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};
    ArrayList<String> stopWordsList =  new ArrayList<String>(Arrays.asList(stopWords));
    /**
     * Constructor taking the number of classes the classifier should be able to
     * distinguish.
     */
    public MultiLabelLearner(Set<String> classes) {
        // YOUR CODE HERE
    }
    
    public HashMap<String, Integer> add_vocab_count(String clazz, String word) {
        // to get the count of vocaulary
        HashMap<String, Integer> vocab_count = new HashMap<String, Integer>();
        int word_count = 0;
        vocab_count =  tokens_count_in_class.get(clazz);
        // get the vocab
        if(vocab_count.containsKey(word)) {
            word_count = vocab_count.get(word);
        }
        word_count ++;
        vocab_count.put(word, word_count);
        return vocab_count;
    }
    
    public String preprocessing(String text) {
        // preprocessing 
        // to lower case
        text = text.toLowerCase();
        // to replace 
        text = text.replaceAll("[^\\p{L}\\s]", "");
        // split with space
        return text;
    }
        
    public int get_total_counts(HashMap<String, Integer> hash_count) {
        // to get the total words in a class
        int class_counts = 0;
        for (int f : hash_count.values()) {
            class_counts += f;
        }
        return class_counts;
    }
    
    public void calculate_class_probabilities() {
        int total_class_count = get_total_counts(classes_hashmap);
        
        HashMap<String, Integer> map_count = classes_hashmap;
        Iterator iterator_prob = map_count.keySet().iterator();
        
        for (String sub_map : map_count.keySet()) {
            String key = iterator_prob.next().toString();
            float value = map_count.get(key);
            float prob = (float) value / total_class_count;
            
            class_probability.put(key, prob);
        }
    }
    
    public void calculate_vocabulary_in_class() {
        int total_class_count;
        HashMap<String, HashMap<String, Integer>> map_count = tokens_count_in_class;
        Iterator iterator_prob = map_count.keySet().iterator();
        
        for (String sub_map : map_count.keySet()) {
            String key = iterator_prob.next().toString();
            HashMap<String, Integer> value = map_count.get(key);
            total_class_count = get_total_counts(value);
            total_vocabulary_in_class.put(key, total_class_count);
        }
        
    }
        public Boolean isStopWord (String word) {
        return stopWordsList.contains(word);   
    }

    /**
     * The method used to learn the training examples. It takes the names of the
     * classes as well as the text of the training document.
     */
    public void learnExample(Set<String> classes, String text) {
        // YOUR CODE HERE
        String joined_classes = String.join(",", classes);
//         System.out.println("joined_classes: "+ joined_classes + " text: "+ text);
        String new_text = preprocessing(text);
        
        String[] splited_text = new_text.split("\\s+");
        
        //and adding class to tokens count in hashmap
        int class_counter = 0;
        float prob = 0.000f;
        // to get the class count from HashMap
        if(classes_hashmap.containsKey(joined_classes)) {
            class_counter = classes_hashmap.get(joined_classes);
        }
        class_counter ++;
        classes_hashmap.put(joined_classes, class_counter);
    
        HashMap<String, Integer> vocab_count = new HashMap<String, Integer>();
        for (int i = 0; i < splited_text.length; i++) {
            // check if string is not empty and is not stop_word
            if (! isStopWord(splited_text[i])) {
                vocabulary.add(splited_text[i]);
                // get the class
                if(tokens_count_in_class.containsKey(joined_classes)) {
                    vocab_count = add_vocab_count(joined_classes, splited_text[i]);
                }else {
                    tokens_count_in_class.put(joined_classes, vocab_count);
                    vocab_count = add_vocab_count(joined_classes, splited_text[i]);
                }
                tokens_count_in_class.put(joined_classes, vocab_count);
            }
        }
        
    }

    /**
     * Creates a MultiLabelClassifier instance based on the statistics gathered from
     * the training example.
     */
    public MultiLabelClassifier createClassifier() {
        MultiLabelClassifier classifier = new MultiLabelClassifier();
        // YOUR CODE HERE
        
        calculate_class_probabilities(); 
        
        calculate_vocabulary_in_class();
        
        return classifier;
    }
}






// This line should make sure that compile errors are directly identified when executing this cell
// (the line itself does not produce any meaningful result)
new MultiLabelLearner(new HashSet<>(Arrays.asList("good","bad")));
System.out.println("compiled");