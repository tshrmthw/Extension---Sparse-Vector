package Sparsevector.solved;

public class SparseVector<T> {
	final VMSimulatedArray<Tuple<T> > array;
	final long size;			//technically the size of the sparse vector
	final int nnz;			//number of nnz that the sparse vector can take in
	int currentnnz;   	//current non zero entry location
	
	public SparseVector(long size, int nnz, boolean sim) {
		this.size = size;
		this.array = new VMSimulatedArray<>(nnz, sim);
		this.nnz = nnz;
	}
	
	public void insert(int row, T thing) {
		if (row < 0 || row >= size) {
            throw new RuntimeException("\nError : Out of Bounds\n");
		} else if(currentnnz==nnz) {
			throw new RuntimeException("\nError : Too many nnz\n");
		} else {
			int pos=0;
			if(currentnnz>=2)		//find where to insert
			{
				pos = binarysearch(array,currentnnz,row);
				if(pos==currentnnz-1 && row>array.at(pos).row)
					pos=currentnnz;
				else
					if (row > array.at(pos).row) {
						++pos;
					}
					//shift elements down by one position
					for(int ini = currentnnz - 1; ini >= pos; ini--)  {
						Tuple<T> tmp = array.at(ini);
						array.put(ini + 1, tmp);
					}
			} else if(currentnnz==1) { //for condition with only one array element
				if(row > array.at(0).row)  
					pos=1;
				else {
					Tuple<T> tmp = array.at(0);
					array.put(1, tmp);
				}
			}
			currentnnz++;
			array.put(pos, new Tuple<T> (row, thing));
		}
	}
	
	public T get(int row) {
		//Tuple<T> key = new Tuple<T>(row, null);
		//int index =  Arrays.binarySearch(array, key, new CompareTuple<T>());
		int index = binarysearch(array,currentnnz,row);
		
		if (index < 0 || array.at(index).row != row )
			return null;
		else
			return array.at(index).thing;
	}
	
	
	
	public long size() {
		return size;
	}
	
	public int nnz() {
		return this.nnz;
	}
	
	public Tuple<T> peek(int pos) {
		return array.at(pos);
	}
	
	public boolean isEmpty() {
		return currentnnz==0;
	}

	private int binarysearch(VMSimulatedArray<Tuple<T> > array,int currnnz ,int row) {
		int min = 0;
		int max = currnnz-1;
		int mid = 0;
		while(max >= min) {
			mid = (max+min)/2;
			if(row < array.at(mid).row)
				max = mid - 1;
			else if(row > array.at(mid).row)
				min = mid + 1;
			else
				return mid;
			
		}
		return mid;
	}
}
