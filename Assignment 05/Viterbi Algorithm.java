/**
 * Simple structure for storing a sequence of states and its probability.
 */
public static class StateSequence {
    /**
     * The sequence of states.
     */
    public final String[] states;
    /**
     * The logarithm of the probability of this sequence.
     */
    public final double logProbability;

    public StateSequence(String[] states, double logProbability) {
        this.states = states;
        this.logProbability = logProbability;
    }
}

/**
 * A class implementing the Viterbi algorithm based on an Hidden Markov Model
 * with the given states, observations, transition matrix and emission matrix.
 */
public static class ViterbiAlgorithm {
    // YOUR CODE HERE

    
    String[] states;
    String[] observationVocab;
    double[][] transitionMatrix;
    double[][] emissionMatrix;
    double[][] viterbi ;
    Integer[][] backpointer ;
    int N, T, F;
    
    
    /**
    * Constructor.
    */
    public ViterbiAlgorithm(String[] states, String[] observationVocab, double[][] transitionMatrix,
            double[][] emissionMatrix) {
        // YOUR CODE HERE
        this.states = states;
        this.observationVocab = observationVocab;
        this.emissionMatrix = emissionMatrix;
        this.transitionMatrix = transitionMatrix;
        N = states.length;
    }

    /**
     * Returns the sequence of states which has the highest probability to create
     * the given sequence of observations.
     * 
     * @param observations a sequence of observations
     * @return the sequence of states
     */
    public StateSequence getStateSequence(String[] observations) {
        StateSequence sequence = null;
        // YOUR CODE HERE
        T = observations.length;
        F = N + 1;
        
        viterbi = new double[N+2][T];
        backpointer = new Integer[N+2][T];
        
        int observation_index = 0;
        for(int i = 0; i < observationVocab.length; i++) {
            if(observations[0] == observationVocab[i]) {
                observation_index = i;
            }
        }

        for (int s = 1; s <= N; s++) {
            viterbi[s][0] = Math.log(transitionMatrix[0][s]) + Math.log(emissionMatrix[s-1][observation_index]);
            backpointer[s][0] = 0;
        }

        for (int t = 1; t < T; t++) {

            int index = 1;
            for(int k =0; k<observationVocab.length; k++)
                if(observations[t] == observationVocab[k]) index = k;

            for (int s = 1; s <= N; s++) {
                HashMap<Integer, Double> prob_array = new HashMap<Integer, Double>();
                double max_prob = -Double.MAX_VALUE;
                int max_prob_index = 0;
                for (int i = 1; i <=N; i++) {
                    double current_prob = viterbi[i][t - 1] + Math.log(transitionMatrix[i][s]);
                    if (current_prob >= max_prob) {
                        max_prob = current_prob;
                        max_prob_index = i; 
                    }
                }

                viterbi[s][t] = Math.log(emissionMatrix[s-1][index]) + max_prob;
                backpointer[s][t] = max_prob_index;
            }
        }
        
        double max_prob = -Double.MAX_VALUE;
        int max_prob_index = 0;
        for (int s = 1; s <= N; s++) {    
            double calcProb = viterbi[s][T-1] + Math.log(transitionMatrix[s][N + 1]);
            if (calcProb >= max_prob) {
                max_prob = calcProb;
                max_prob_index = s;
            }

        }
        viterbi[N+1][T-1] = max_prob;
        backpointer[N + 1][T-1] = max_prob_index;


         // return the backtrace path by following states from bp[F,T] backwards
        ArrayList<String> final_path = new ArrayList<String>();


        for (int i = 1; i < T; i++) {
            double max = viterbi[1][i];
            int arg_max=1;
            for(int s=1; s<=N; s++)
            {
                if(viterbi[s][i] >= max)
                {
                    max = viterbi[s][i];
                    arg_max = backpointer[s][i];
                }

            }

            final_path.add(states[arg_max-1]);

        }
        final_path.add(states[backpointer[N+1][T-1] - 1]);
//         System.out.println("path: "+ final_path + " prob: "+ viterbi[N + 1][T - 1]);

        String[] stringArray = final_path.toArray(new String[0]);
        sequence = new StateSequence(stringArray, viterbi[N + 1][T - 1]);

        return sequence;
    }
}

// This line should make sure that compile errors are directly identified when executing this cell
// (the line itself does not produce any meaningful result)
new ViterbiAlgorithm(new String[0],new String[0],new double[2][2],new double[0][0]);
System.out.println("compiled");