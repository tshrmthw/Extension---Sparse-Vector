package Sparsevector.solved;

/*
 * This class creates a final array and forces all accesses to the to go through
 * a virtual memory simulator, if sim is set to true.  This is very useful for examining
 * the trade off between memory usage and speed of two different algorithms or data structures.
 * This simulator uses the LRU policy to replace pages
 * 
 * By:  Erik Wijmans
 */

import java.util.HashMap;
import java.util.Set;

public class VMSimulatedArray<T> {
	final long memSize; // Number of elements of type T that can fit in the physical memory, if your array is larger than this, your accesses will be very slow
	final long pageSize; // Number of elements of type T per page of physical memory
	final long serviceTime; // Time to service a page fault in nanoseconds.  For reference, the average time of a 
	final long numPages;	// disk on a real system is 8 ms (8e6 ns)
	final boolean sim; // You can use this just like a normal array if you set sim to false
	final T [] array;
	long clock, misses;
	// A hashmap is used to map a VMPage in physical memory to how recently it was used
	// This means that a memory access which is a hit take Theta(1) time
	// while a replacement takes Theta(numPages) time.  Which is very similar to how this
	// is done on a real OS
	HashMap<Long, Long> memory;

	@SuppressWarnings("unchecked")
	public VMSimulatedArray(int size, long memSize, long pageSize, long serviceTime, boolean sim) {
		this.memSize = memSize;
		this.pageSize = pageSize;
		this.serviceTime = serviceTime;
		this.sim = sim;
		numPages = memSize/pageSize;
		memory = new HashMap<>();
		clock = 0;
		misses = 0;
		array = (T []) new Object [size];
	}
	
	public VMSimulatedArray(int size, boolean sim) {
		this(size, 1 << 17, 1 << 7, (long) 1.5e6, sim);
	}
	
	public VMSimulatedArray(int size) {
		this(size, 1 << 17, 1 << 7, (long) 1.5e6, true);
	}
	
	public T at(int index) {
		if (sim)
			simMemAccess(index);
		return array[index];
	}
	
	public void put(int index, T thing) {
		if (sim)
			simMemAccess(index);
		array[index] = thing;
	}
	
	private void simMemAccess(long address) {
		// Calculated which vmpage this address corresponds to
		long vmPage = address/pageSize;
		// If the memory contains the page, or the memory isn't full,
		// update how recently the page was used or add it
		if (memory.containsKey(vmPage) || memory.size() != numPages) {
			memory.put(vmPage, clock);
		} else {
			Set<Long> vmpages = memory.keySet();
			long toRemove = 0;
			long LRU = Long.MAX_VALUE;
			// Iterator over all the vmpages in memory to figure out
			// which one should be removed
			for (Long page : vmpages) {
				long RU = memory.get(page);
				if (RU < LRU) {
					toRemove = page;
					LRU = RU;
				}
			}
			memory.remove(toRemove);
			memory.put(vmPage, clock);
			++misses;
			try {
				// Thread.sleep wants a sleep time broken up into milliseconds
				// and nanoseconds
				int ms = (int) Math.floorDiv(serviceTime, (long)1e6);
				int ns = (int) (serviceTime % (long)(1e6));
				Thread.sleep(ms, ns); // Used to change how slow a dense vector is,
											// for reference, a realistic value is 8ms
			} catch (InterruptedException e) {
				// FIXME Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Increase the use clock
		++clock;
	}
	
	
	public long misses() {
		return misses;
	}
}
