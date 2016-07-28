package Sparsevector.solved;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;

import org.junit.Rule;

public class TestSparseVector {
	
	@Rule
	public FailReporter<Integer> frpter = new FailReporter<>();

	@org.junit.Test
	public void test() {
		final double density = 1/5.0;
		final int growthFactor = 50;
		final int numTests = 20;
		for (int i = 0; i <= numTests; ++i) {
			int size = i*growthFactor + 20;
			int nnz = (int) (size*density);
			runTest(size, nnz);
		} 
	}
	
	private void runTest(long size, int nnz) {
		LinkedList<Tuple<Integer> > nonZeros = genList(size, nnz);
		System.out.println("Testing with size=" + size + " and numNonZeros=" + nnz);
		SparseVector<Integer> sv = new SparseVector<>(size, nnz, false);
		frpter.setVec(sv);
		
		// Inserts all the nonzeros in random order into the sparsevector
		// a hashset is used to ensure not duplicate insertions
		HashSet<Integer> inserted = new HashSet<>();
		HashMap<Integer, Tuple<Integer> > rowToTuple = new HashMap<>();
		while (inserted.size() != nnz) {
			int index = (int) (Math.random()*(nnz));
			if (!inserted.contains(index)) {
				inserted.add(index);
				Tuple<Integer> item = nonZeros.get(index);
				sv.insert(item.row, item.thing);
				rowToTuple.put(item.row, item);
				// Each time something is inserted, the student's array is
				// checked for correctness
				checkArray(sv, inserted.size(),
						rowToTuple);
				assertTrue("Returned incorrect value", item.thing.equals(sv.get(item.row)));
			}
		}
		
		checkEntireVector(sv, nonZeros);
		checkRandom(sv, rowToTuple);
		
	}
	
	private void checkRandom(SparseVector<Integer> sv, 
			HashMap<Integer, Tuple<Integer> > rowToTuple) {
		long numIt = sv.size()*10;
		for (long i = 0; i < numIt; ++i) {
			int row = (int) (Math.random()*sv.size());
			if (rowToTuple.containsKey(row)) {
				Tuple<Integer> item = rowToTuple.get(row);
				assertEquals("Element not corret!", item.thing, 
						sv.get(row));
			} else {
				assertNull("", sv.get(row));
			}
		}
	}
	
	private void checkEntireVector(SparseVector<Integer> sv,
			LinkedList<Tuple<Integer> > nonZeros) {
		// Iterate over every row index
		ListIterator<Tuple<Integer> > it = nonZeros.listIterator(0);
		Tuple<Integer> l = new Tuple<Integer>(-1, null);
		Tuple<Integer> c;
		while (it.hasNext()) {
			c = it.next();
			for (int i = l.row + 1; i < c.row; ++i) {
				assertNull("", sv.get(i));
			}
			assertEquals("Element not correct!", c.thing, sv.get(c.row));
			l = c;
		}
		for (int i = l.row + 1; i < sv.size(); ++i) {
			assertNull("", sv.get(i));
		}	
	}
	
	private void checkArray(SparseVector<Integer> sv, int nnz,
			HashMap<Integer, Tuple<Integer> > rowToTuple) {
		for (int i = 0; i < nnz; ++i) {
			Tuple<Integer> s = sv.peek(i);
			assertNotNull("", s);
			Tuple<Integer> t = rowToTuple.get(s.row);
			assertNotNull("Could not find " + s + " in list of entries", t);
			assertTrue("Element not correct!", t.compare(s) == 0);
		}
		for (int i = nnz; i < sv.nnz(); ++i) {
			assertNull("", sv.peek(i));
		}
		for (int i = 0; i < nnz - 1; ++i) {
			assertTrue("Elements out of order!", sv.peek(i).row < sv.peek(i + 1).row);
		}
	}
	
	/*
	 * This works by selecting a chunk size that represents the average distance
	 * between nonzeros in the vector.  The chunksize is then made 25% smaller so that 
	 * 25% more nonzeros than needed will be gerneated.  Next size is iterated over by
	 * offsets of chunksize.  Each time, a random value is added at the index of
	 * i + a random value in the range [0, chunkSize).  Then, values are randomly removed
	 * from the list until only nnz nonzeros remain. The results is a list of random values,
	 * at random indices, that is sorted by index due to the nature of its creation.
	 */
	public static LinkedList<Tuple<Integer> > genList(long size, int nnz) {
		int chunkSize = (int) (size/nnz/1.5);
		LinkedList<Tuple<Integer> > list = new LinkedList<>();

		for (int i = 0; i < size - chunkSize; i += chunkSize) {
			int offset = (int) (Math.random()*chunkSize);
			assertTrue("", i + offset < size);
			int val = (int) (Math.random()*1e4);
			list.add(new Tuple<Integer>(i + offset, val));
		}
		
		assertTrue("List of tuples not the correct size!", list.size() >= nnz);
		
		while (list.size() != nnz) {
			int index = (int) (Math.random()*(list.size()));
			list.remove(index);
		}
		
		return list;
	}
}
