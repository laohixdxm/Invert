import java.io.*;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class search {
	private double[] Kvalue = new double[BufferIO.MAX_DOC_ID];
	private String[] Kurl = new String[BufferIO.MAX_DOC_ID];

	/* calc BM25 */
	private double calc(ArrayList<InvertIndexPerDoc> arrayiipd, int docId) {
		int[] ftfdt = getValueForBM25(arrayiipd, docId);
		int N = BufferIO.MAX_DOC_ID;
		int ft = ftfdt[0];
		int fdt = ftfdt[1];
		System.out.println("k");
		double numlog = (0.5 + N - ft) / (0.5 + ft);
		return Math.log(numlog) / Math.log(2) * 2.2 * fdt / (Kvalue[docId] + fdt);
	}

	/*
	 * ft fdt only used at calc()
	 */
	private int[] getValueForBM25(ArrayList<InvertIndexPerDoc> arrayiipd, int docId) {
		int[] valuForBM = new int[2];
		valuForBM[0] = arrayiipd.size();// ft
		for (InvertIndexPerDoc tempiipd : arrayiipd) {
			if (tempiipd.getDocId() == docId) {
				valuForBM[1] = tempiipd.getF();// fdt
				break;
			}
		}
		return valuForBM;

	}

	// -----Get K Valu&Url-----------
	public search() {

		String pathname = ".\\Kvalu";
		File filename = new File(pathname);
		InputStreamReader reader;
		try {
			reader = new InputStreamReader(new FileInputStream(filename));
			BufferedReader br = new BufferedReader(reader);

			for (int i = 0; i < Kvalue.length; i++) {

				String line = br.readLine();
				if (i == 0)
					System.out.println(i + "-line----------" + line);

				Kvalue[i] = Double.valueOf(line.split(" ")[0]);
				Kurl[i] = line.split(" ")[1];
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<InvertIndexPerDoc> getKeyData(String[] keys) {

		ArrayList<InvertIndexPerDoc> arrayIipd = new ArrayList<InvertIndexPerDoc>();
		ArrayList<ArrayList<InvertIndexPerDoc>> arrayListarrayiipl = new ArrayList<ArrayList<InvertIndexPerDoc>>();
		ArrayList<ArrayList<Integer>> arrayDocId = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < keys.length; i++) {// for every keyword

			ArrayList<Integer> tempArrayDocid = new ArrayList<Integer>();
			try {
				String dataOfWord = searchWord(keys[i]);// dataOfWord=docId F
														// pos pos pos
				String[] splitData = dataOfWord.split(" ");
				InvertIndexPerDoc iipd = null;
				for (int ii = 0; ii < splitData.length; ii += 3) {
					// per Doc
					tempArrayDocid.add(Integer.valueOf(splitData[ii]));// docid
					iipd = new InvertIndexPerDoc(new Tuples(keys[i] + " " + splitData[ii] + " " + splitData[ii + 2]));// keyword
																														// docid
																														// pos
					int f = Integer.valueOf(splitData[ii + 1]) - 1;
					int begin = ii;
					for (int j = begin; j < begin + f; j++) {// f>1
						iipd.add(Integer.valueOf(splitData[j + 3]));// add pos
						ii++;
					}
					arrayIipd.add(iipd);
				}

				arrayListarrayiipl.add(arrayIipd);
				arrayIipd = new ArrayList<InvertIndexPerDoc>();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!tempArrayDocid.isEmpty())
				arrayDocId.add(tempArrayDocid);// add data per keyword
			else
				System.err.println("empty!!!temparraydocid");
		}
		ArrayList<ArrayList<Integer>> tempDocid = findSameDocId(arrayDocId);
		ArrayList<Integer> docid = new ArrayList<Integer>();
		if (tempDocid.size() == 1)
			docid = tempDocid.get(0);
		else
			System.err.println("someting wrong at findSameDocId");
		System.out.println("docid---" + docid);
		PriorityQueue<BM25Pdoc> PQBM25 = new PriorityQueue<BM25Pdoc>();
		for (int i = 0; i < docid.size(); i++) {
			int j = 0;
			double tempcalc = 0.0;

			for (String a : keys)
				tempcalc += calc(arrayListarrayiipl.get(j++), docid.get(i));
			BM25Pdoc tempbm25pdoc = new BM25Pdoc(tempcalc, docid.get(i));
			PQBM25.add(tempbm25pdoc);
			if (PQBM25.size() > 11)
				PQBM25.remove();
		}
		int sizepqbm25 = PQBM25.size();

		for (int i = 0; i < sizepqbm25; i++) {
			BM25Pdoc tempbm25=PQBM25.remove();
			System.out.println(tempbm25.getBM25() + ", url=" + Kurl[tempbm25.getDocid()] + "  " + i);
		}
		return arrayIipd;
	}

	private ArrayList<ArrayList<Integer>> findSameDocId(ArrayList<ArrayList<Integer>> comp) {
		ArrayList<Integer> temp;
		// System.out.println("--------findsamedocid-----" + comp.get(0).size()
		// + "/" + comp.get(1).size());
		if (comp.size() > 2) {
			System.out.println("size>2");
			temp = comp.remove(comp.size() - 1);
			ArrayList<ArrayList<Integer>> ans = findSameDocId(comp);
			ans.add(temp);
			return findSameDocId(ans);
		} else {
			System.out.println("comp.size()<=2");

			if (comp.size() == 1)
				return comp;
			else {
				System.out.println("size==2!");
				temp = new ArrayList<Integer>();
				int size = comp.get(0).size() < comp.get(1).size() ? comp.get(0).size() : comp.get(1).size();
				temp = comp.get(0);
				temp.retainAll(comp.get(1));
				// for (int i = 0, j = 0; i <= size && j <= size;) {
				// if (comp.get(0).get(i) == comp.get(1).get(j)) {
				// System.out.println("============"+i+"/"+j);
				// System.out.println(comp.get(0).get(i));
				// System.out.println(comp.get(1).get(j));
				// temp.add(comp.get(0).get(i));
				// i++;
				// j++;
				// continue;
				//
				// }
				// else if (comp.get(0).get(i) < comp.get(1).get(j)) {
				// System.out.println("<<<<<<<<<<<<<<<<"+i+"/"+j);
				// System.out.println(comp.get(0).get(i));
				// System.out.println(comp.get(1).get(j));
				// temp.add(comp.get(0).get(i));
				// i++;
				// continue;
				// }
				// else if (comp.get(0).get(i) > comp.get(1).get(j)) {
				// System.out.println(">>>>>>>>>>>>>"+i+"/"+j);
				// System.out.println(comp.get(0).get(i));
				// System.out.println(comp.get(1).get(j));
				// temp.add(comp.get(0).get(i));
				// j++;
				// continue;
				// }else {
				// System.err.println("sssss");
				// }
				//
				// }

				ArrayList<ArrayList<Integer>> aaaans = new ArrayList<ArrayList<Integer>>();
				aaaans.add(temp);
				return aaaans;
			}
		}

	}

	public String searchWord(String key) throws IOException {
		String pathname = ".\\IndexFinalOutput"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
		File filename = new File(pathname); // 要读取以上路径的input。txt文件
		InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader
		BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
		String line = br.readLine();
		while (line != null) {

			String LineToString = line.split(" ")[0];
			if (LineToString.equals(key)) {
				System.out.println("line--" + line);
				int length = Integer.valueOf(line.split(" ")[2]);
				int start = Integer.valueOf(line.split(" ")[1]);
				BufferedInputStream scan = new BufferedInputStream(new FileInputStream(new File(".\\finalOutput")));
				byte[] b = new byte[length];
				scan.skip(start);
				if (scan.read(b, 0, length) != -1) {
					String s = new String(b);
					return s;
				} else
					System.out.println("mimei");
				return null;
			}
			line = br.readLine(); // 一次读入一行数据
		}
		return null;

	}

}
