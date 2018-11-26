
public class MinHeap {
	public MinHeap() {

	}

	public void heapify(HuffmanTree.BinaryTree[] a, int i, int n) {
		HuffmanTree.BinaryTree ai = a[i];
		while(i < n / 2) {
			int j = 2 * i + 1;
			if(j + 1 < n && a[j + 1].getNodeInt(0) < a[j].getNodeInt(0))
				++j;
			if(a[j].getNodeInt(0) >= ai.getNodeInt(0)) break;
			a[i] = a[j];
			i = j;
		}
		a[i] = ai;
	}

	public void buildHeap(HuffmanTree.BinaryTree[] a, int i, int n) {
		for(int j = n / 2 - 1; j >= 0; j--)
			heapify(a, i, n);
	}
}
