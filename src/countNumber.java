import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class countNumber {
	InvertIndexPerDoc iipd;
	infoPerDoc infopd = new infoPerDoc();
	int sum = 0, length = 0;
	int countofjudg = 0;
	public int countTuple = 0;
	BufferedReader[] br;
	String[] strs;
	MinHeap mh;
	int size;
	Tuples minTuple, prev;
	BufferedWriter outData, outIndex;

	public countNumber(int size) {
		this.size = size;
		br = new BufferedReader[size];
		mh = new MinHeap(size);
		strs = new String[size];
		File output = new File(".\\finalOutput");
		try {
			output.createNewFile();

			outData = new BufferedWriter(new FileWriter(output));

			File outputIndex = new File(".\\IndexFinalOutput");
			outputIndex.createNewFile();
			outIndex = new BufferedWriter(new FileWriter(outputIndex));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (int i = 0; i < size; i++) {
			try {
				br[i] = new BufferedReader(
						new InputStreamReader(new FileInputStream(new File(".\\result\\Tuples" + (i + 1)))));

				strs[i] = br[i].readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("nimeinimeinimei");
				e.printStackTrace();
			}
			mh.insert(new Tuples(strs[i]));
		}
		try {
			while (getNext()) {

				if (prev == null) {// 1st time
					writeToIndex(minTuple.getWord());
					iipd = new InvertIndexPerDoc(minTuple);
					continue;
				}

				if (minTuple.getWord().equals(prev.getWord())) {
					if (minTuple.getDocid() == prev.getDocid()) {

						iipd.add(minTuple);

					} else {// docid changed

						length += writeToData(iipd);
						iipd = new InvertIndexPerDoc(minTuple);

					}
				} else {// word changed

					length += writeToData(iipd);
					String writeIndex = " " + sum + " " + length + '\n' + minTuple.getWord();
					writeToIndex(writeIndex);

					iipd = new InvertIndexPerDoc(minTuple);
					sum += length;
					length = 0;

				}

			}
			infopd.calcK();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int writeToData(InvertIndexPerDoc iipd) throws IOException {
		String tempiipdtostring = iipd.toString();
		outData.write(tempiipdtostring);
		return tempiipdtostring.length();
	}

	private int writeToIndex(String writeIndex) throws IOException {
		outIndex.write(writeIndex);
		return writeIndex.length();
	}

	private boolean getNext() {
		prev = minTuple;
		minTuple = mh.remove();
		infopd.add(minTuple);
		for (int i = 0; i < size; i++)
			if (minTuple.toString().equals(strs[i])) {
				countTuple++;
				try {
					strs[i] = br[i].readLine();
					// System.out.println(strs[i]);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("error on getnext readline");
					e.printStackTrace();
				}
				if (strs[i] != null) {
					mh.insert(new Tuples(strs[i]));
					return true;
				} else if (mh.getSize() == 1)
					return false;
			}

		return false;
	}
}
