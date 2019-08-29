
public static class MinHashingProcessor {
    // YOUR CODE HERE
    int[][] vectorMatrix;
    
    /**
     * Constructor for creating the class with an already given set of permutations.
     */
    public MinHashingProcessor(int[][] permutations) {
        // YOUR CODE HERE
        vectorMatrix = permutations;
    }

    /**
     * Constructor for creating the class with a generated set of permutations.
     * 
     * @param numberOfHashes
     *            number of hash functions (i.e., different permutations) that
     *            should be generated
     * @param numberOfShingles
     *            number of different shingles the given documents can have
     * @param seed
     *            a seed if the generation is based on a random process
     */
    public MinHashingProcessor(int numberOfHashes, int numberOfShingles, long seed) {
        // YOUR CODE HERE
        int rows = numberOfHashes;
        int columns = numberOfShingles;
        Random generator = new Random(seed);
        int min = 0, max = numberOfShingles;
        vectorMatrix = new int[numberOfHashes][numberOfShingles];
         for (int i = 0; i < rows; i++) {
             for (int j = 0; j < columns; j++) {
                 vectorMatrix[i][j] = generator.nextInt(((max+1) - min) + min);
             }
         }
         
//         for (int i = 0; i < vectorMatrix.length; i++) {
//             for (int j = 0; j < vectorMatrix[i].length; j++) {
//                System.out.print(vectorMatrix[i][j] + "\t");
//             }
//             System.out.println();
//         }
        
    }

    public int[] minHash(Set<Integer> s) {
        int hash[] = null;
        // YOUR CODE HERE
        hash = new int[vectorMatrix.length];
        
        for (int i = 0; i < vectorMatrix.length; i++) {
            for (int j = 0; j < vectorMatrix[i].length; j++) {
               if(s.contains(vectorMatrix[i][j])) {
                   System.out.print(vectorMatrix[i][j] + "\t");
                   hash[i] = j;
                   
                   break;
                }
            }
        }   
//         for (int j = 0; j < hash.length; j++) {
//                System.out.print(hash[j] + "\t");
//         }
        return hash;
    }
}

// This line should make sure that compile errors are directly identified when executing this cell
// (the line itself does not produce any meaningful result)
new MinHashingProcessor(new int[][]{{0}});
System.out.println("compiled");