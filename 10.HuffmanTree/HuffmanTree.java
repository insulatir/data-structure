import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

// Huffman 코드
public class HuffmanTree {
	String txt;
	private Map<Character, Integer> map;
	// (문자 - 빈도) 쌍이 저장된 맵
	private Map<Character, String> sMap = new HashMap<Character, String>();
	// (문자 - 문자열) 쌍이 저장된 맵
	private String code = "";
	private int count = 0;
	private PriorityQueue priorityQueue = new PriorityQueue();

	public HuffmanTree(String txt) {
		this.txt = txt;
	}

	// 텍스트를 읽어 각 캐릭터(영어 알파벳, 공백, 마침표 등)의 빈도수를 조사
	public HashMap<Character, Integer> frequency() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(txt));
			// 정보를 파일로부터 읽음
			String line = reader.readLine();
			// 파일로부터 한 줄을 읽어 String 형태로 반환

			map = new HashMap<Character, Integer>();

			while(true) {
				if(line != null) {
					// 파일로부터 읽어온 데이터가 있는가?
					String lineU = line.toUpperCase();
					// 대소문자 구별 x

					// 파일에 주어진 캐릭터들을 해시 테이블에 삽입
					for(int idx = 0; idx < lineU.length(); idx++) {
						char c = lineU.charAt(idx);
						// idx에 해당하는 문자를 반환

						if(!map.containsKey(c))
							map.put(c, 1);
							// 캐릭터가 키(key), 값(value)은 1로 설정
						else
							map.replace(c, (int)map.get(c) + 1);
							// 캐릭터의 발생 횟수 업데이트
					}
				}
				else {
					break;
				}

				line = reader.readLine();
				// 파일로부터 한 줄을 읽어들임
			}

			reader.close();

			return (HashMap<Character, Integer>) map;
		}
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		catch (IOException ex) {
			// 예외 발생시 처리 코드
			ex.printStackTrace();
		}

		return null;
	}

	// 각 캐릭터와 그 빈도수를 노드로 가지는 단독트리를 구성하고 이것을 원소로 하는 우선 순위 큐 구현
	public PriorityQueue priorityQueue(HashMap<Character, Integer> map) {
		PriorityQueue priorityQueue;
		BinaryTree[] trees;
		// 각 캐릭터와 그것의 빈도수를 노드로 가지는 단독 트리들
		Object[] cs;

		cs = map.keySet().toArray();
		trees = new BinaryTree[cs.length];
		priorityQueue = new PriorityQueue(trees.length);

		for(int i = 0; i < trees.length; i++) {
		 	trees[i] = new BinaryTree(1);
		 	trees[i].setBinaryTree(0, map.get(cs[i]), (char)cs[i]);
		}

		for(int i = 0; i < trees.length; i++)
			priorityQueue.add(trees[i]);
			// 우선 순위 큐에 삽입

		return priorityQueue;
	}

	// 허프만 트리 구성
	public BinaryTree huffmanTree(PriorityQueue priorityQueue) {
		BinaryTree x, y, z = new BinaryTree(1);

		while(priorityQueue.size() > 1) {
			x = (BinaryTree)priorityQueue.remove();
			// 트리 삭제
			y = (BinaryTree)priorityQueue.remove();
			// 트리 삭제
			z = new BinaryTree(1);
			z.setBinaryTree(0, x.getNodeInt(0) + y.getNodeInt(0), 'x');
			// 하위 트리들의 빈도수의 합
			z.left = x;
			z.right = y;
			z.size = x.size + y.size + 1;
			// 허프만 트리 z 생성
			priorityQueue.add(z);
			// 큐에 z를 추가
		}

		BinaryTree huff = (BinaryTree)priorityQueue.best();

		BinaryTree best = (BinaryTree)priorityQueue.best();
		Object[] a = map.values().toArray();
		int sum = 0;

		for(int i = 0; i < a.length; i++)
			sum += (int)a[i];

		if(best.getNodeInt(0) < sum)
			System.out.println("잘못된 허프만 트리");

		return huff;
	}

	// 각 알파벳에 대한 허프만 코드를 프린트
	public void huffmanCode(BinaryTree tree) {
		// recursive
		if(tree.left == null && tree.right == null) {
			// 리프 트리
			char c = tree.getNodeChar(0);
			sMap.put(c, code);
			priorityQueue.add(tree);

			if(++count == map.size()) {
				BinaryTree[] a = new BinaryTree[count];
				int j = 0;
				int size = priorityQueue.size();
				for(int i = 0; i < size; i++) {
					BinaryTree tempTree = (BinaryTree)priorityQueue.remove();
					if(tempTree.getNodeInt(0) != 0)
						a[count-1-j++] = tempTree;
				}

				for(int i = 0; i < count; i++) {
					System.out.print(a[i].getNodeChar(0) + "(발생 빈도 : ");
					if(a[i].getNodeInt(0) < 10)
						System.out.print(" ");
					if(a[i].getNodeInt(0) < 100)
						System.out.print(" ");
					System.out.println(a[i].getNodeInt(0) + ") " + sMap.get(a[i].getNodeChar(0)));
				}
			}
			return;
		}
		code = code + "0";
		// 왼쪽 트리 : "0"
		huffmanCode(tree.left);
		code = code.substring(0, code.length()-1);
		// backTrace
		code = code + "1";
		// 오른쪽 트리 : "1"
		huffmanCode(tree.right);
		code = code.substring(0, code.length()-1);
		// backTrace
	}

	// 문장이 입력으로 들어오면 Huffman 코드를 사용하여 해당 문장을 인코딩한 후, 비트 스트림을 출력
	public void codeStream(String string) {
		String buf = "";
		char[] codeStream = string.toUpperCase().toCharArray();

		System.out.println("Encoding this String : " + string);

		for(int i = 0; i < codeStream.length; i++) {
			if(sMap.get(codeStream[i]) == null)
				System.out.println(codeStream[i]);

			buf = buf + sMap.get(codeStream[i]);
		}
		System.out.println("Huffman Code Stream : " + buf);
	}

	// MinHeap을 이용한 우선순위 큐
	public class PriorityQueue {
		private static final int CAPACITY = 100;
		private BinaryTree[] a;
		private MinHeap minHeap;
		// MinHeap
		private int size;

		public PriorityQueue() {
			this(CAPACITY);
		}

		public PriorityQueue(int capacity) {
			a = new BinaryTree[capacity];
			minHeap = new MinHeap();
			for(int i = 0; i < a.length; i++)
				a[i] = new BinaryTree(1);
		}

		public void add(Object object) {
			if(!(object instanceof BinaryTree))
				throw new IllegalArgumentException();
			BinaryTree x = (BinaryTree)object;
			if (size == a.length) resize();
			int i = size++;
			while (i > 0) {
				int j = i;
				i = (i-1)/2;
				if (a[i].tree[0].n <= x.tree[0].n) {
					a[j] = x;
					return;
				}
				a[j] = a[i];
			}
			a[i] = x;
		}

		public Object best() {
			if (size == 0)
				throw new java.util.NoSuchElementException();
			return a[0];
		}

		public Object remove() {
			Object best = best();
			a[0] = a[--size];
			minHeap.heapify(a, 0, size);
			return best;
		}

		public int size() {
			return size;
		}

		private void resize() {
			BinaryTree[] aa = new BinaryTree[2*a.length];
			System.arraycopy(a, 0, aa, 0, a.length);
			a = aa;
		}
	}

	// 이진 트리
	public class BinaryTree {
		// Field
		Node[] tree;
		// 배열을 이용하여 트리를 저장
		int size;
		// 트리의 크기 저장
		BinaryTree left;
		BinaryTree right;

		// Constructor
		public BinaryTree(int size) {
			this.size = size;
			this.tree = new Node[this.size];
			for (int i = 0; i < this.tree.length; i++)
				this.tree[i] = new Node();
		}

		public void setBinaryTree(int index, int n, char c) {
			this.tree[index].n = n;
			this.tree[index].c = c;
		}

		public int getNodeInt(int index) {
			return this.tree[index].n;
		}

		public char getNodeChar(int index) {
			return this.tree[index].c;
		}

		private class Node {
			int n;
			char c;

			public Node() {

			}
		}
	}

}
