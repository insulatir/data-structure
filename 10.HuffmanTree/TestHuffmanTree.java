import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TestHuffmanTree {

	public static void main(String[] args) {
		try {
			HuffmanTree huffmanTree = new HuffmanTree("C://text.txt");
			HashMap<Character, Integer> map = new HashMap<Character, Integer>();
			HuffmanTree.PriorityQueue priorityQueue = huffmanTree.new PriorityQueue();
			HuffmanTree.BinaryTree binaryTree = huffmanTree.new BinaryTree(0);
			BufferedReader reader = new BufferedReader(new FileReader(huffmanTree.txt));
			// 정보를 파일로부터 읽음
			String line = reader.readLine();
			// 파일로부터 한 줄을 읽어 String 형태로 반환

			map = huffmanTree.frequency();
			// 입력 문자에 대한 빈도수가 저장된 맵
			priorityQueue = huffmanTree.priorityQueue(map);
			// (문자 - 빈도) 쌍이 저장된 우선순위 큐
			binaryTree = huffmanTree.huffmanTree(priorityQueue);
			// 허프만 트리
			huffmanTree.huffmanCode(binaryTree);
			// 허프만 코드 프린트
			huffmanTree.codeStream(line);
			//주어진 텍스트의 처음 한 문장에 대해 인코딩

			reader.close();
		}
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		catch (IOException ex) {
			// 예외 발생시 처리 코드
			ex.printStackTrace();
		}
	}

}
