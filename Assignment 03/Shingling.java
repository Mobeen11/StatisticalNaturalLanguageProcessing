/*

Its apply method implements the shingling from the lecture slides based on set semantics. It returns the ids of the shingles that have been found inside of the document.
Its constructor takes the length of ths shingles.
The jaccardSim method should return the jaccard similarity of the two given shingle sets.
Example
The document

google is good
has the following shingles with length 3

"goo", "oog", "ogl", "gle", "le ", "e i", " is", "is ", "s g", " go", "ood"
Since set semantics is used, the second occurence of "goo" is not added a second time to the list of shingles. If the shingles are simply assigned ids in the order in which they have been seen, the document would be represented by the following shingle ids (starting with 0):

    0,     1,     2,     3,     4,     5,     6,     7,     8,     9,   10
A second document

gooses google
would lead to the shingles

"goo", "oos", "ose", "ses", "es ", "s g", " go", "oog", "ogl", "gle"
and the ids

    0,    11,    12,    13,    14,     8,     9,     1,     2,     3
Their intersections is $\{0,1,2,3,7,8\}$ while their union is  $\{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14\}$. Therefore, their Jaccard similarity is $6/15 = 0.4$
*/



import java.util.Set;
import java.util.List;
import java.util.*;

/**
 * Class implementing the shingling of documents
 */
public class ShinglingProcessor {
    // YOUR CODE HERE
    int k;
    static List<String> checkingString = new ArrayList<String>();
    
    public ShinglingProcessor(int shingleLength) {
        // YOUR CODE HERE
        k = shingleLength;
    }
    
    static List<String> removeDuplicates(List<String> list) {

        // Store unique items in result.
        List<String> result = new ArrayList<String>();

        // Record encountered Strings in HashSet.
        HashSet<String> set = new HashSet<>();

        // Loop over argument list.
        for (String item : list) {

            // If String is not in set, add it to the list and the set.
            if (!set.contains(item)) {
                result.add(item);
                set.add(item);
            }
        }
        return result;
    }

    // return -1 if string is not in splitedString
    public Integer checkShingleInList(String string) {
        return checkingString.indexOf(string);
    }
    
    public Set<Integer> applyShingling(String text) {
        Set<Integer> shingles = new HashSet<Integer>();
        // YOUR CODE HERE
        List<String> splitedString = new ArrayList<String>();
        char[] textArray = text.toCharArray();
        String temp_string = "";
        
        for(int i=0; i < text.length() - k + 1; i++) {
            String shingle = text.substring(i, i + k);
            splitedString.add(shingle);	
        }
        
        // remove duplicates from string
        splitedString = removeDuplicates(splitedString);
        
        // adding to Set
        for (int i=0; i < splitedString.size(); i++) {
            if(checkingString.isEmpty()) {
                shingles.add(i);
            }else {
                int j = checkShingleInList(splitedString.get(i));
                if(j != -1) {
                    // add the shingles
                    shingles.add(j);
                }
                else {
                    if(i > 0){
                        shingles.add(checkingString.size() + i-1);
                    }else{
                        shingles.add(j);
                    }
                    
                }
            }
        }
        checkingString = splitedString;
        return shingles;
    }
    
    
    public static Set<Integer> Union(Set<Integer> set1, Set<Integer> set2){
        Set<Integer> union = new HashSet<Integer>(set1); 
        union.addAll(set2);
        return union;
    }
    

    public static Set<Integer> Intersection(Set<Integer> set1, Set<Integer> set2){
        Set<Integer> intersection = new HashSet<Integer>(set1); 
        intersection.retainAll(set2); 
        return intersection;
    }
    
    public static double jaccardSim(Set<Integer> set1, Set<Integer> set2) {
        double similarity = 0;
        // YOUR CODE HERE
        checkingString = new ArrayList<String>();
        float neumerator = Intersection(set1, set2).size();
        float denominator = Union(set1, set2).size();
        similarity = neumerator / denominator;
        return similarity;
    }
}

// This line should make sure that compile errors are directly identified when executing this cell
// (the line itself does not produce any meaningful result)
new ShinglingProcessor(0);
System.out.println("compiled");