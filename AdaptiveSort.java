/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */ 
 
import java.util.Arrays;

/**
 * <p>An experimental recursive sorting algorithm that adapts to the data set and attempts to
 * optimize its performance. This algorithm has high space requirements but may outperform
 * other algorithms in some cases. Comparative tests are done on various data sets using the
 * JVM's Dual-Pivot Quicksort implementation as a benchmark.  
 *    
 * @author Robert Laniewski <depthn@gmail.com>
 * @version 1.0 
 */

public class AdaptiveSort {

    private int maxRecursionLevel;

    private final int maxDebugRecursionLevel = 0;

	long[] elements1;
	long[] sortedElements1;
	        
	AdaptiveSort() {
    	maxRecursionLevel = 0;
    }
    
    public String getRecursionLevelPrefix(int recursionLevel) {
    	return "[" + (recursionLevel) + "] ";
    }
    
    public int getMaxRecursionLevel() {
    	return maxRecursionLevel;	
    }
       
	private void createRandomElements(int initialSize, long maxInt) {
		elements1 = new long[initialSize];		

		for (int i = 0; i < initialSize; i++) {
			//elements1[i] = (int)((Math.random() - 1) * maxInt); // negative values
			//elements1[i] = (long)((Math.random() - 0.5) * 2 * maxInt); // negative and positive values
			elements1[i] = (long)((Math.random()) * 1 * maxInt); // positive values			
		}	
	}
	
	public void setElements(long[] elements) {
		this.elements1 = elements;
	}
	

	public long[] getElements() {
		return elements1;
	}		
	
	public long[] getSortedElements() {
		return sortedElements1;
	}
	
	private void printElements() {
		for (int i = 0; i < elements1.length; i++) {
			System.out.println("element["+i+"] = " + elements1[i]);
		}		
	}

	private void printSortedElements() {
		for (int i = 0; i < sortedElements1.length; i++) {
			System.out.println("sortedElement["+i+"] = " + sortedElements1[i]);
		}		
	}
			
	public boolean verifySortedElements() {
		boolean sorted = true;
		if (sortedElements1 == null) {
			return false;
		} else {
			for (int i = 0; i < sortedElements1.length-1; i++) {
				if (sortedElements1[i] > sortedElements1[i+1]) sorted = false;
			}			
			return sorted;
		}
	}
	
	private boolean sortElements() {
		int size = 0;
		
		if (elements1 != null) {
			size = elements1.length;
		} else {
			return false; 			
		}
		
		if (size <= 0) {
			// List is empty.
			return false;
		}

		sortedElements1 = new long[size];				

		if (size == 1) {
			// Don't sort the list if size is 1.
			sortedElements1[0] = elements1[0];
			return true;
		}						
		
		long maxValue = Long.MIN_VALUE;
		long minValue = Long.MAX_VALUE;
		
		int numberOfNegativeElements = 0;
		int numberOfPositiveElements = 0;		
		
		// Pass 1: Get min and max values.
		for (int i = 0; i < size; i++) {
			if (elements1[i] > maxValue) maxValue = elements1[i];
			if (elements1[i] < minValue) minValue = elements1[i];
			if (elements1[i] < 0) {
				numberOfNegativeElements++;
			} else {
				numberOfPositiveElements++;			
			}
		}			

		long[] positiveElements = null;		
		long[] negativeElements = null;
		
		// Pass 2: Split positive and negative values.
		if (minValue < 0) {
			negativeElements = new long[numberOfNegativeElements];
			positiveElements = new long[numberOfPositiveElements];
			
			int negativeIndex = 0;
			int positiveIndex = 0;
			
			for (int i = 0; i < size; i++) {
				if (elements1[i] < 0) {
					negativeElements[negativeIndex] = -elements1[i];
					negativeIndex++;
				} else {
					positiveElements[positiveIndex] = elements1[i];	
					positiveIndex++;
				}
			}						
			
			int sortedIndex = 0;
			
			long[] sortedNegativeElements = sortElements(negativeElements, -minValue, 0);			
			negativeElements = sortedNegativeElements;
							
			if ((numberOfNegativeElements > 0) && (negativeElements != null)) {
				for (int i = negativeElements.length - 1; i >= 0; i--) {
					sortedElements1[sortedIndex] = -negativeElements[i];
					sortedIndex++;
				}
			}	
			
			if (maxValue >= 0) {
				long[] sortedPositiveElements = sortElements(positiveElements, maxValue, 0);			
				positiveElements = sortedPositiveElements;					
				
				if ((numberOfPositiveElements > 0) && (positiveElements != null)) {
					for (int i = 0; i < positiveElements.length; i++) {
						sortedElements1[sortedIndex] = positiveElements[i];
						sortedIndex++;
					}
				}
			}					
		} else if (maxValue >= 0) {
			// Sort positive values. 
			sortedElements1 = sortElements(elements1, maxValue, 0);
		}
									
		return true;		
	}

	// Sorts an array of positive longs with a given maximum value. 
	private long[] sortElements(long[] elements, long maxValue, int recursionLevel) {	
		boolean debug = false;
		
		if (recursionLevel > maxRecursionLevel) {
			maxRecursionLevel = recursionLevel;
			System.out.println(getRecursionLevelPrefix(recursionLevel) + " elements.length = " + elements.length);
			debug = true;
		}		
						
		int size = elements.length;
		long[] sortedElements = new long[size];

		if ((size == 0) || (maxValue <= 0)) {
			return sortedElements;
		}
		
		if (size == 1) {
			sortedElements[0] = elements[0];			
			return sortedElements;
		}
		
		if (size == 2) {
			// If the list size is 2.		
			long minValue;
			
			if (elements[1] == maxValue) {
				minValue = elements[0];				
			} else {
				minValue = elements[1];								
			}
			
			sortedElements[0] = minValue;
			sortedElements[1] = maxValue;						
			
			if ((recursionLevel <= maxDebugRecursionLevel) || debug) {
				System.out.println(getRecursionLevelPrefix(recursionLevel) + "List size is 2 ("+minValue+", "+maxValue+").");
			}
			
			return sortedElements;
		}		

        int numberOfBuckets;            
        long scaleFactor;            
                        	
        if (maxValue < 35) { 
	        numberOfBuckets = (int)(maxValue + 1);
	        scaleFactor = 1; 
        } else if ((maxValue < 3000) && (size > maxValue * 0.5)) {
	        numberOfBuckets = (int)(maxValue + 1);
	        scaleFactor = 1;         	        
        } else if ((maxValue < 300000) && (size > maxValue * 0.85)) {
	        numberOfBuckets = (int)(maxValue + 1);
	        scaleFactor = 1;         	        
        } else {
        	if (size < 25) {	
        		numberOfBuckets = 25;
				scaleFactor = ((maxValue + 1) / (numberOfBuckets)) + 1;    			       	     	
	        } else {            
	        	numberOfBuckets = (int)((Math.pow(size, 1.0 / 4.0)) * Math.log(maxValue) * 8) + 100;  	        	
	        	//numberOfBuckets = (int)((Math.pow(size, 1.0 / 4.0)) * (Math.pow(maxValue, 1.0 / 5.0))) + 10;	        	
				scaleFactor = ((maxValue + 1) / (numberOfBuckets)) + 1;    							
	        }  
        	
			if (scaleFactor <= 1) {
				scaleFactor = 1;
				numberOfBuckets = (int)(maxValue + 1);
			}   
        }
			
        long productSize = numberOfBuckets * (int)scaleFactor;
        
		if ((recursionLevel <= maxDebugRecursionLevel) || debug) {		
			System.out.println("---");
			System.out.println(getRecursionLevelPrefix(recursionLevel) + "listSize = " + size);
			System.out.println(getRecursionLevelPrefix(recursionLevel) + "maxValue = " + maxValue);			
			System.out.println(getRecursionLevelPrefix(recursionLevel) + "buckets = " + numberOfBuckets);
			System.out.println(getRecursionLevelPrefix(recursionLevel) + "scaleFactor = " + scaleFactor);
			System.out.println(getRecursionLevelPrefix(recursionLevel) + "productSize = " + productSize);			
			System.out.println("---");	
		}
		
		int[] buckets = new int[numberOfBuckets];		

		if (scaleFactor == 1) {
			for (int i = 0; i < size; i++) {
				int value = (int)elements[i];								
				buckets[value]++; 		
			}		
			
			int sortedIndex = 0;
			
			for (int i = 0; i < numberOfBuckets; i++) {
				int bucketSize = buckets[i];
				
				Arrays.fill(sortedElements, sortedIndex, sortedIndex + bucketSize, i);
				sortedIndex += bucketSize;
			}
		} else if (((scaleFactor < 50) & (size > 15)) || ((size > 1) & ((scaleFactor < 15000) && (productSize < 250000) && ((size > productSize * 0.22))))) {
          int[][] remainderTable = new int[numberOfBuckets][(int)scaleFactor];
                               
            for (int i = 0; i < size; i++) {
                   long element = elements[i];                  
                   int value = (int)(element / scaleFactor);                        
                   int remainder = (int)(element % scaleFactor);                                         
                   remainderTable[value][remainder]++;
                   buckets[value]++;         
            }                         

            int sortedIndex = 0;
           
            for (int i = 0; i < numberOfBuckets; i++) {
                   int bucketSize = buckets[i];

                   if (bucketSize > 0) {              
                         for (int j = 0; j < scaleFactor; j++) {
                                int remainder = j;
                                int freq = remainderTable[i][j];
                                
                                if (freq > 0) {
                                	long value = (i * scaleFactor) + remainder;
                                	Arrays.fill(sortedElements, sortedIndex, sortedIndex + freq, value);
                    				sortedIndex += freq;
                                }                                  
                         }
                   }
            }                                                                      
        } else if (size < 0) {        	  	
			int[] bucketIndices = new int[size];					
			long[][] elementMatrix = new long[numberOfBuckets][size];			
			long[] maxRemainders = new long[numberOfBuckets];		
			long[][] remainders = new long[numberOfBuckets][size];		
			
			for (int i = 0; i < size; i++) {
				long element = elements[i];				
				int value = (int)(element / scaleFactor);				
				long remainder = element % scaleFactor;
				
				if (remainder > maxRemainders[value]) maxRemainders[value] = remainder;												
				remainders[value][buckets[value]] = remainder;					
				elementMatrix[value][buckets[value]] = element;				
				bucketIndices[i] = value;				
				buckets[value]++; 		
			}				

			int sortedIndex = 0;
			
			for (int i = 0; i < numberOfBuckets; i++) {
				int bucketSize = buckets[i];
	
				if (bucketSize > 0) {
					if ((bucketSize > 1) && (maxRemainders[i] > 0) && (remainders[i].length > 1)) {						
						long[] trimmedRemainders = new long[bucketSize];
						for (int j = 0; j < bucketSize; j++) {
							trimmedRemainders[j] = remainders[i][j];
						}

						Arrays.sort(trimmedRemainders);
						remainders[i] = trimmedRemainders;
					}					
					
					if (bucketSize == 1) {
						sortedElements[sortedIndex] = elementMatrix[i][0]; //(i * scaleFactor) + remainders[i][1];
						sortedIndex++;
					} else {
						for (int j = 0; j < bucketSize; j++) {
							sortedElements[sortedIndex] = (i * scaleFactor) + remainders[i][j];							
							sortedIndex++;
						}
					}
				}
			}				
		} else {
			int[] bucketIndices = new int[size];					
			int[] buckets1 = new int[numberOfBuckets];	
			
			long[] maxRemainders = new long[numberOfBuckets];		
			long[][] remainders = new long[numberOfBuckets][];		
			
			long[] largeValues = new long[numberOfBuckets];
								
			for (int i = 0; i < size; i++) {
				long element = elements[i];				
				int value = (int)(element / scaleFactor);				
				bucketIndices[i] = value;
				largeValues[value] = element;
				buckets[value]++; 		
			}				
	
			for (int i = 0; i < numberOfBuckets; i++) {
				int bucketSize = buckets[i];
				if (bucketSize > 0) {
					remainders[i] = new long[bucketSize];
				}
			}
	
			if (recursionLevel <= maxDebugRecursionLevel) {
				System.out.println(getRecursionLevelPrefix(recursionLevel) + "Populate buckets with remainder after division (modulus) values.");
			}				
			
			// Pass 4: Populate buckets with remainder after division (modulus) values.				
			for (int i = 0; i < size; i++) {
				int value = bucketIndices[i];
				long element = elements[i];
				long remainder = element % scaleFactor;	

				if (remainder > maxRemainders[value]) maxRemainders[value] = remainder;												
				remainders[value][buckets1[value]] = remainder;												
				buckets1[value]++;	
			}

			int sortedIndex = 0;
			
			for (int i = 0; i < numberOfBuckets; i++) {
				int bucketSize = buckets[i];

				if (bucketSize > 0) {
					if ((bucketSize > 1) && (maxRemainders[i] > 0)) {
						remainders[i] = sortElements(remainders[i], maxRemainders[i], recursionLevel+1);
						
						for (int j = 0; j < bucketSize; j++) {
							sortedElements[sortedIndex] = (i * scaleFactor) + remainders[i][j];							
							sortedIndex++;
						}
					} else {
						long value = largeValues[i];

						Arrays.fill(sortedElements, sortedIndex, sortedIndex + bucketSize, value);
						sortedIndex += bucketSize;
					}					
				}
			}				
		}
		
		return sortedElements; 
	}
	
		
	public static void main(String[] args) {
		AdaptiveSort sort = new AdaptiveSort();
		
		int size = 8000000;
		
		// Uncomment the test to run 
		//sort.createRandomElements(size, 100); 						
		sort.createRandomElements(size, 5000200); 
		//sort.createRandomElements(size, Integer.MAX_VALUE); 			
		//sort.createRandomElements(size, Long.MAX_VALUE / 2000000);
		//sort.createRandomElements(size, Long.MAX_VALUE);
		
		long[] elements = sort.getElements();
		long[] copyOfElements = new long[elements.length]; 
		for (int i = 0; i < elements.length; i++) {
			copyOfElements[i] = elements[i];
		}		
	
		System.out.println("Starting QuickSort on " + size + " elements.");
		long startTime = System.nanoTime();    
		Arrays.sort(copyOfElements);
		long estimatedTimeQuickSort = System.nanoTime() - startTime;
		System.out.println("Completed QuickSort in " + estimatedTimeQuickSort + " nanoseconds.");
		System.out.println();
		
		System.out.println("Starting AdaptiveSort on " + size + " elements.");	
		//sort.printElements();
		long startTime1 = System.nanoTime(); 		
		sort.sortElements();
		long estimatedTimeAdaptiveSort = System.nanoTime() - startTime1;		
		//sort.printSortedElements();
				
		System.out.println("Completed AdaptiveSort in " + estimatedTimeAdaptiveSort + " nanoseconds.");				
		
		// Relative performance > 1 is good. 
		System.out.println("Relative performance: " + (float)estimatedTimeQuickSort / estimatedTimeAdaptiveSort);
		System.out.println("---");		
		System.out.println("Done.");
		
		System.out.println("maxRecursionLevel = " + sort.getMaxRecursionLevel());
		System.out.println("verifySortedElements = " + sort.verifySortedElements());	
	}
	
	
}
