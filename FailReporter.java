package Sparsevector.solved;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;


public class FailReporter<T> extends TestWatcher{
	private SparseVector<T> sv;
	@Override
	public void failed(Throwable t, Description d) {
		System.out.println("===== Failure report for " + d + " ========");
		System.out.println("Failure message: " + t);
		System.out.print("Array: [");
		System.out.print(sv.peek(0));
		for (int i = 1; i < sv.nnz(); ++i) {
			if (sv.peek(i) != null)
			System.out.print(", " + sv.peek(i));
		}
		System.out.println("]");
		System.out.println("===== End Failure report for " + d + " ========");
		System.out.println("\n");
	}
	
	public void setVec(SparseVector<T> sv) {
		this.sv = sv;
	}
}

