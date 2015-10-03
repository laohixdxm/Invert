import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class BufferIO {
	private int size;
	public static int MAX_DOC_ID = 52140;
	/*
	 * private infoPerDoc[] ipd = new infoPerDoc[MAX_DOC_ID];
	 * 
	 * public float docLengthAvg() { int sum = 0; for (infoPerDoc temp : ipd)
	 * sum += temp.getLength(); System.out.println("sum-----" + sum); return
	 * (float) sum / MAX_DOC_ID; }
	 * 
	 * public void writeToK() { int docid = 0; float dAvg = docLengthAvg(); for
	 * (infoPerDoc temp : ipd) { ipd[docid++].setK(1.2 * (0.25 + 0.75 * ((float)
	 * temp.getLength() / dAvg))); if (docid == 100) System.out.println(1.2 *
	 * (0.25 + 0.75 * ((float) temp.getLength() / dAvg)) + " " + dAvg); } }
	 */

	public BufferIO() {
		int DocID = 0, i = 0, count = 0;
		try {
			ArrayList<Tuples> arrayTuples = new ArrayList<Tuples>();
			for (int indexid = 0; indexid < 83; indexid++) {
				System.out.println(indexid);
				i++;
				BufferedInputStream inputData = new BufferedInputStream(
						new GZIPInputStream(new FileInputStream(".\\nz2_merged\\" + indexid + "_data")));

				BufferedReader inputIndax = new BufferedReader(new InputStreamReader(
						new GZIPInputStream(new FileInputStream(".\\nz2_merged\\" + indexid + "_index"))));
				String indexLine = inputIndax.readLine();

				while (indexLine != null) {

					String[] tempString = indexLine.split(" ");
					int length = Integer.parseInt(tempString[3]);
					byte[] b = new byte[length];
					String url = tempString[0];
					inputData.read(b, 0, length);
					/// int tempsize = arrayTuples.size();
					String aaaa = new String(b);
					parse.parsePage(url, aaaa, arrayTuples, DocID);
					// tempsize -= arrayTuples.size();
					// ipd[DocID] = new infoPerDoc(url, -1 * tempsize);

					indexLine = inputIndax.readLine();
					DocID++;
				}

				inputIndax.close();
				inputData.close();

				// every 9 data sets, write into file
				if (i == 7 || indexid == 82) {
					System.out.println("-------------" + indexid);
					Collections.sort(arrayTuples);

					i = 0;
					File writename = new File(".\\result\\output_" + count++); // 相对路径，如果没有则要建立一个新的output。txt文件
					size = count;
					writename.createNewFile();
					BufferedWriter out = new BufferedWriter(new FileWriter(writename));
					for (int i1 = 0; i1 < arrayTuples.size(); i1++) {
						out.write(arrayTuples.get(i1).toString() + '\n');
					}
					out.flush();
					out.close();
					arrayTuples = new ArrayList<Tuples>();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int getSize() {
		return size;
	}

	public static void main(String[] args) throws IOException {
		long a = System.currentTimeMillis();

		// BufferIO bio = new BufferIO();
		long b = System.currentTimeMillis();
		System.out.println("time of bio is: " + (b - a) + " ms");
		// int size = 10;
		// bio.writeToK();
		// int size = bio.getSize();
		// 
		// int sum=0;
		// for(int i:ii.sizePerDoc){sum+=i;System.out.println(i+"-----");}
	//	countNumber cn = new countNumber(12);
		long c = System.currentTimeMillis();
		System.out.println("time of invertIndex is: " + (c - b) + " ms");
		search sc = new search();
		String []keyset={"iphone"};
		sc.getKeyData(keyset);
		long d = System.currentTimeMillis();
		System.out.println("time of search is: " + (d - c) + " ms");

	}
}