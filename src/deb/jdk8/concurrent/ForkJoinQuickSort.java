/**
 * 
 */
package deb.jdk8.concurrent;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
//import java.util.concurrent.RecursiveTask;
//import java.util.concurrent.locks.AbstractQueuedSynchronizer;
/**
 * @author GHOSH
 *	Beautiful implementation of Quick Sort using the Fork/Join Framework.
 *  ForkJoinPool (Executor Service implementation for fork/join framework) used
 *  to first invoke either a recursiveTask(if the compuation returns value) or
 *  RecursiveAction (if the computation doesn't return a value). Since this is a
 *  in-place quickSort implementation the computation updates the sorting by reference
 *  and hence no return value. Hence RecursiveAction implemntation used for the QuickSortTask.
 *  The computation is divided into two parts . First if the array is either 0 or no length
 *  then returned. If not then some processing is done to compute the position of pivot
 *  and two different parallel tasks are invoked for the elements to the left of the array
 *  and right of the array .
 */
public class ForkJoinQuickSort extends RecursiveAction {

	public static void main(String [] args){
		int arr [] = {5,6,9,2,5,4,7};
		ForkJoinQuickSort fjQs = new ForkJoinQuickSort(arr,0,arr.length);
		ForkJoinPool fjpool = new ForkJoinPool();
		fjpool.invoke(fjQs);
		for(int i:arr){
			System.out.println(i);
		}
	}
	private int [] m_ElementsTobeSorted;
	private int m_StartIndex;
	private int m_EndIndex;
	public ForkJoinQuickSort(int [] elements , int startIndex , int endIndex){
		m_ElementsTobeSorted=elements;
		m_StartIndex=startIndex;
		m_EndIndex = endIndex;
	}
	public void compute(){
		sort(m_ElementsTobeSorted,m_StartIndex,m_EndIndex);
	}
	public int[] getSortedElements(){
		return m_ElementsTobeSorted;
	}
	private void sort(int arr[],int startlength,int endLength){
		if( (endLength-startlength==1)||(endLength-startlength==0))
			return ;
		else{
			int pivot = arr[endLength-1];
			int pivotIndex = endLength-1;
			for(int i=startlength;i<pivotIndex;i++){
				if(arr[i]<pivot){
					int temp = arr[i];
					shiftLeft(arr,i,pivotIndex);
					arr[pivotIndex]=temp;
					i--;
					pivotIndex--;
				}
			}
			
			invokeAll(new ForkJoinQuickSort(arr, startlength, pivotIndex),
					new ForkJoinQuickSort(arr,pivotIndex+1,endLength));
		}
		
	}
	
	private void shiftLeft(int [] args,int startIndex , int endIndex){
	
		for(int i=startIndex;i<endIndex;i++){
			args[i]=args[i+1];
		}
	}
	
}
