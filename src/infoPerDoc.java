import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class infoPerDoc {
	int maxsiz = BufferIO.MAX_DOC_ID;
	int[] length = new int[BufferIO.MAX_DOC_ID];
	double[] K = new double[BufferIO.MAX_DOC_ID];

	public void add(Tuples tempT) {
		int docid = tempT.getDocid();
		length[docid]++;
	}

	public void calcK() {
		int tempsum = 31513840;
		// for (int i : length)
		// tempsum += i;

		System.out.println("calck------------------------------------");
		File output = new File(".\\Kvalu");
		try {
			output.createNewFile();
			int indexid = 0;
			BufferedReader inputIndax = new BufferedReader(new InputStreamReader(
					new GZIPInputStream(new FileInputStream(".\\nz2_merged\\" + indexid + "_index"))));

			BufferedWriter out = new BufferedWriter(new FileWriter(output));

			double davg = (double) tempsum / maxsiz;
			System.out.println(tempsum + "/" + maxsiz);
			for (int i = 0; i < maxsiz; i++) {
				String indexLine = inputIndax.readLine();
				if (indexLine == null) {
					inputIndax = new BufferedReader(new InputStreamReader(
							new GZIPInputStream(new FileInputStream(".\\nz2_merged\\" + indexid++ + "_index"))));
				indexLine=inputIndax.readLine();
				}
				String temp = indexLine.split(" ")[0];

				K[i] = 1.2 * (0.25 + 0.75 * (length[i] / davg));
				System.out.println((K[i] + " " + temp + '\n'));
				out.write((K[i] + " " + temp + '\n'));
			}
			out.flush();
			out.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
