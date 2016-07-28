package Sparsevector.solved;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import timing.Experiment;
import timing.GensRepeatRunnable;
import timing.Output;
import timing.RepeatRunnable;
import timing.SizeAndTiming;

public class SparseVectorTimer implements GensRepeatRunnable{
	public static final int SV = 1;
	public static final int DENSE = 2;
	final int type;
	public SparseVectorTimer(int type) {
		this.type = type;
	}
	
	final double density = 1/45.0;
	public RepeatRunnable genRunnable(final long n) {
		if (type == SV) {
			return new RepeatRunnable() {
				final int nnz = (int) (n*density);
				SparseVector<Integer> sv = new SparseVector<>(n, nnz, true);
				LinkedList<Tuple<Integer> > nonZeros = TestSparseVector.genList(n, nnz);
				@Override
				public void run() {
					for (Tuple<Integer> item : nonZeros) {
						sv.insert(item.row, item.thing);
					}
					for (Tuple<Integer> item : nonZeros) {
						sv.get(item.row);
					}
				}

				@Override
				public void reset() {
					sv = new SparseVector<>(n, nnz, true);
					nonZeros = TestSparseVector.genList(n, nnz);
				}
				public String toString() {
					return "Sparse Vector of " + nnz + " nonzeros with size: " + n;
				}
			};
		} else {
			return new RepeatRunnable() {
				final int nnz = (int) (n*density);
				VMSimulatedArray<Integer> array = new VMSimulatedArray<>((int)n);
				LinkedList<Tuple<Integer> > nonZeros = TestSparseVector.genList(n, nnz);
				@Override
				public void run() {
					for (Tuple<Integer> item : nonZeros) {
						array.put(item.row, item.thing);
					}
					for (Tuple<Integer> item : nonZeros) {
						array.at(item.row);
					}
				}

				@Override
				public void reset() {
					array = new VMSimulatedArray<>((int)n);
					nonZeros = TestSparseVector.genList(n, nnz);
				}
				public String toString() {
					return "Dense Vector of " + nnz + " nonzeros, size: " + n;
				}
			};
		}
		
	}
	
	public static void main(String[] args) {
		runExperiment(7000);   // FIXME change to 25000 or some large number so times show up
	}
	
	private static void runExperiment(int factor) {
		int start = 1;
		int end = 25;
		double KB = 1 << 10;
		ArrayList<Integer> s = new ArrayList<>();
		for (int i=start; i < end; i++) {
			s.add(i*factor);
			System.out.println(i*factor + " is: " + i*factor/KB + " K elements");
		}
		Integer[] sizes = new Integer [s.size()];
		sizes = s.toArray(sizes);
				
		Experiment e = new Experiment(new SparseVectorTimer(SV), Arrays.asList(sizes), 3);
		
		System.out.println("Running Sparse");
		e.run();
		for (SizeAndTiming st : e.getSizeAndTiming()) {
			System.out.println(st.size + " " + st.timing.toMillis());
		}
		
		Output.writeSizeTiming("outputs/sparseVector.csv", "Sparse Vector", e.getSizeAndTiming()); 
		
		e = new Experiment(new SparseVectorTimer(DENSE), Arrays.asList(sizes), 3);
		
		System.out.println("Running Dense");
		e.run();
		for (SizeAndTiming st : e.getSizeAndTiming()) {
			System.out.println(st.size + " " + st.timing.toMillis());
		}
		
		Output.writeSizeTiming("outputs/sparseVector.csv", "Dense Vector", e.getSizeAndTiming());
	}

	@Override
	public RepeatRunnable gen(long size) {
		return genRunnable(size);
	}

}
