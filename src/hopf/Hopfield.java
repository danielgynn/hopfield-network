package hopf;

import java.util.*;

/**
 * Class Hopfield
 * @author Daniel Gynn (DJG44)
 *
 */
public class Hopfield {
    static int patternLength;
    float[][] weights;
    float[] tempStorage;
    Vector<float[]> trainingPattern = new Vector<float[]>();
    
    /**
     * Hopfield class constructor
     * @param patternLength
     */
    public Hopfield(int patternLength) {
        Hopfield.patternLength = patternLength;
        weights = new float[patternLength][patternLength];
        tempStorage = new float[patternLength];
    }
    
//	public static float[] getTrainingData(String[] sp) {
//		// For each line in the training 
//		for (int i = 0; i < sp.length;) {
//		    // Get the stored patterns into int array format, by lines.
//		    float[] storedPattern = Hopfield.getPattern(sp[i]);
//		    System.out.println("GETDATA: " + storedPattern);
//		    return storedPattern;
//		}
//		return null;
//	}
    
    static void generateOutput(Hopfield test, float[] pattern) {
        float[] data = new float[patternLength];
        
        for (int i = 0; i < patternLength; i++) {
          data[i] = pattern[i];
        }
        
        for (int i = 0; i < 1; i++) {
          int index = (int) ((patternLength - 1) * (float) Math.random());
          data[index] = -data[index];
          
          if (data[index] < 0.0f) {
              data[index] = 1.0f;
          } else {
              data[index] = -1.0f;
          }
        }
 
        float[] node = test.makeNetwork(data, patternLength);
        int output = 0;

        for (int i = 0; i < patternLength; i++) {
        	if (node[i] > 0.1f) {
        		output = 1;
        	} else {
        		output = -1;
        	}
        	
        	System.out.print(output + " ");
        }
        System.out.println();
    }
    
    public static float[] getPattern(String patternString) {
		String[] patternNodes = patternString.split(" ");
		float[] pattern = new float[patternNodes.length];

		for(int i = 0; i < patternNodes.length; i++) {
		    try {
		        pattern[i] = Integer.parseInt(patternNodes[i]);
		    } catch (NumberFormatException nfe) {
		        //Not an integer
		    }
		}

		return pattern;
	}

    public void learn(float[] pattern) {
    	trainingPattern.addElement(pattern);
        
        for (int i = 1; i < patternLength; i++) {
            for (int j = 0; j < i; j++) {
                for (int n = 0; n < trainingPattern.size(); n++) {
                    float[] data = (float[]) trainingPattern.elementAt(n);
                    float temp = data[i] * data[j] + weights[j][i];
                    
                    weights[i][j] = weights[j][i] = temp;
                }
            }
        }
        
        for (int i = 0; i < patternLength; i++) {
            tempStorage[i] = 0.0f;
            
            for (int j = 0; j < i; j++) {
                tempStorage[i] += weights[i][j];
            }
        }
    }

    public float[] makeNetwork(float[] pattern, int numIterations) {
    	float[] nodes = new float[patternLength];
    	
        for (int i = 0; i < patternLength; i++) {
        	nodes[i] = pattern[i];
        }
   
        for (int i = 0; i < numIterations; i++) {
            for (int j = 0; j < patternLength; j++) {
                if (energy(j, nodes) > 0.0f) {
                	nodes[j] = 1.0f;
                } else {
                	nodes[j] = -1.0f;
                }
            }
        }
        
        return nodes;
    }

    private float energy(int index, float[] inputNodes) {
        float temp = 0.0f;
        
        for (int i = 0; i < patternLength; i++) {
            temp += weights[index][i] * inputNodes[i];
        }
        
        return 2.0f * temp - tempStorage[index];
    }
}