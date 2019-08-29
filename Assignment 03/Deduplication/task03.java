import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * A simple class representing a pair of documents
 */
public class Duplicate {
    public int id1, id2;
    public Duplicate(int id1, int id2) {
        if (id1 < id2) {
            this.id1 = id1;
            this.id2 = id2;
        } else {
            this.id1 = id2;
            this.id2 = id1;
        }
    }
    public void setId1(int id1) { this.id1 = id1; }
    public void setId2(int id2) { this.id2 = id2; }
    @Override
    public int hashCode() { return 31 * (31 + id1) + id2; }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Duplicate))
            return false;
        Duplicate other = (Duplicate) obj;
        if (id1 != other.id1)
            return false;
        if (id2 != other.id2)
            return false;
        return true;
    }
    @Override
    public String toString() {
        return (new StringBuilder()).append('(').append(id1).append(',').append(id2).append(')').toString();
    }
}
/**
 * A simple class implementing the Jaccard similarity and counting the number of times it is called.
 */
public class JaccardSimilarity {
    private AtomicInteger calls = new AtomicInteger(0);
    public int getCalls() { return calls.get(); }
    
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
    
    public double jaccardSim(Set<Integer> set1, Set<Integer> set2) {
        calls.incrementAndGet();
        double similarity = 0;
        // You may want to copy your solution from task 1
        
        // YOUR CODE HERE
        
//         Set<Integer> u = new HashSet<>(set1);
//         Set<Integer> i = new HashSet<>(set1);
        
//         u.addAll(set2); // Union
//         i.retainAll(set2); // Intersection
        
//         if (u.size() == 0 || i.size() == 0) {
//             similarity = 0;
//         } else {
//             similarity = (float) i.size() / (float) u.size();
//         }
        
//         similarity = (float) i.size() / (float) u.size();
        
        float neumerator = Intersection(set1, set2).size();
        float denominator = Union(set1, set2).size();
        
        similarity = neumerator / denominator;  
        
        return similarity;
        
    }
}

// YOUR CODE HERE

/**
 * Class for finding duplicates in a given corpus
 */
public class Deduplicater implements Consumer<String> {
    
    public final JaccardSimilarity jaccard = new JaccardSimilarity();
    // YOUR CODE HERE

    double threshold;
    int shingleLength;
    int numberOfHashes;
    long seed;
    int b;
    int r;
    int numberOfShingles = -1;
    int docTracker = 0;

    // All shingles with their id storage
    HashMap allShingleStore = new HashMap();

    // All documents and their shingles storage
    HashMap<Integer, Set<Integer>> docsWithShingleStore = new HashMap<Integer, Set<Integer>>();

    // Bucket
    HashMap<Integer, ArrayList<Integer>> bucket = new HashMap<Integer, ArrayList<Integer>>();

    int[][] permutations;

    /**
     * Constructor.
     * 
     * @param threshold
     *            the similarity threshold theta.
     * @param shingleLength
     *            the length of the shingles
     * @param numberOfHashes
     *            the number of hash functions (i.e., permutations) that should be
     *            used
     * @param seed
     *            the seed that can be used for pseudo random processes
     * @param b
     *            the number of bands for the LSH
     * @param r
     *            the number of rows of a single band for LSH
     */
    public Deduplicater(double threshold, int shingleLength, int numberOfHashes, long seed, int b, int r) {
        // YOUR CODE HERE
        this.threshold = threshold;
        this.shingleLength = shingleLength;
        this.numberOfHashes = numberOfHashes;
        this.seed = seed;
        this.b = b;
        this.r = r;
    }

    /**
     * This method is called with a single document that should be added to the internal, 
     * shingled representation of documents.
     *
     * @param line
     *            a single document that should be processed by the Deduplicator.
     */
    public void accept(String line) {
        // YOUR CODE HERE    
        Set<Integer> docShingles = new HashSet<Integer>();
        
        line = line.toLowerCase();
        line = line.replaceAll("[^\\p{L}\\d\\s]", "");
        String shingle;
        for (int i = 0 ; i < line.toCharArray().length-(this.shingleLength - 1) ; i ++) {
            shingle = line.substring(i, i + this.shingleLength);
            
            if (! allShingleStore.containsKey(shingle)) {
                allShingleStore.put(shingle, ++numberOfShingles);
                docShingles.add(numberOfShingles);
            } else {
                // shingle is in store
                
                // Check if doc contains this shingle or not
                if (! docShingles.contains(allShingleStore.get(shingle))) {
                    // If doc does not contain it then have it from allShingleStore
                    docShingles.add((int) allShingleStore.get(shingle));
                }
            }
        }
        
        docsWithShingleStore.put(docTracker++ , docShingles);
    }
    
    public void Permutation () {
        int rows = numberOfHashes;
        int columns = numberOfShingles;
        Random generator = new Random(seed);
        int min = 0, max = numberOfShingles;
        permutations = new int[numberOfHashes][numberOfShingles];
         for (int i = 0; i < rows; i++) {
             for (int j = 0; j < columns; j++) {
                 permutations[i][j] = generator.nextInt(((max+1) - min) + min);
             }
         }
    }
    
    public Set<Duplicate> determineDuplicates() {
        Set<Duplicate> duplicates = new HashSet<>();
        
        int[] docsWithMinHashList = new int[this.r];
        
        // creating permutation
        Permutation();
        
        // Calculating min hash 
        for (int i = 0; i < docsWithShingleStore.size(); i++) {
            int[] minList = minHash(docsWithShingleStore.get(i));
            int j = 0, p = -1;
            // Band creation r=5
            for (int t = 0; t < minList.length; t++) {
                if (t != 0 && (t % r) == 0) {
                    // calc hash and add this band into bucket 
                    AddInBucket(docsWithMinHashList, i);
                    j = j+1;
                    p = -1;
                }
                p++;
                docsWithMinHashList[p] = minList[t];
            }
        }
        for (int hash : bucket.keySet()) {
            if (bucket.get(hash).size() > 1) {
                ArrayList<Integer> cps = bucket.get(hash); // Candidate pairs
                
                for (int i = 0; i < cps.size(); i++) {
                    for (int j = 0; j < cps.size() ; j++) {
                        if (j != i) {
                            Set<Integer>  doc1Shingles = docsWithShingleStore.get(cps.get(i));
                            Set<Integer>  doc2Shingles = docsWithShingleStore.get(cps.get(j));
                            
                            double jaccard_calculated = jaccard.jaccardSim(doc1Shingles, doc2Shingles);
                            if (jaccard_calculated >= this.threshold) {
                                duplicates.add(new Duplicate(cps.get(i), cps.get(j)));
                            }
                        }
                    }
                }
            }
        }
        
        return duplicates;
    }
        
    public int[] minHash(Set<Integer> s) {
        int hash[] = new int[permutations.length];
        // YOUR CODE HERE
        for (int i = 0; i < permutations.length; i++) {
            for (int j = 0; j < permutations[i].length; j++) {
                if (s.contains(permutations[i][j])) {
                    // TODO what if this condition never met??
                    hash[i] = j;
                    break;
                }
            }
        }
        return hash;
    }
    
    public void AddInBucket(int[] arr, int docId) {
        
        int hash = stringHasher(Arrays.toString(arr).replaceAll("\\[|\\]| |\\,", ""));
        
        if (!bucket.containsKey(hash)) {
            ArrayList<Integer> docs = new ArrayList<Integer>();
            docs.add(docId);
            bucket.put(hash, docs);
        } else {
            ArrayList<Integer> docs = bucket.get(hash);
            
            if (! docs.contains(docId)) {
                docs.add(docId);
                bucket.put(hash, docs);
            }
        }
    }
    
    public int stringHasher(String str) {
         int hash = 7;
        for (int i = 0; i < str.length(); i++) {
            hash = hash*31 + str.charAt(i);
        }
        return hash;
     }
}
// This line should make sure that compile errors are directly identified when executing this cell
// (the line itself does not produce any meaningful result)
new Deduplicater(0.9, 10, 50, 123, 10, 5);
System.out.println("compiled");