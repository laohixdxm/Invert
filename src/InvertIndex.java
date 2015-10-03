import java.io.*;
import java.util.ArrayList;

public class InvertIndex {
	public int sizePerDoc=0;

	public InvertIndex(int size) {
		BufferedReader[] br = new BufferedReader[size];
		String[] strs = new String[size];
		
		MinHeap mp = new MinHeap(size);
		Tuples prev = Tuples.MIN_VALUE;
		int sum = 0;
		try {
			File output = new File(".\\finalOutput");
			output.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(output));
			
			File outputIndex = new File(".\\IndexFinalOutput");
			outputIndex.createNewFile();
			BufferedWriter outIndex = new BufferedWriter(new FileWriter(outputIndex));

			for (int i = 0; i < size; i++) {
				br[i] = new BufferedReader(
						new InputStreamReader(new FileInputStream(new File(".\\result\\File" + i))));

				strs[i] = br[i].readLine();
				System.out.println(strs[i]);
				mp.insert(new Tuples(strs[i]));
			}
			int flag = 0;

			Tuples temp = mp.remove();
			InvertIndexPerDoc iipd = new InvertIndexPerDoc(temp.getDocid(), temp.getPos());
			outIndex.write(temp.getWord() + " ");
			int lengthpd = 0;
			while (true) {
				prev = temp;
				temp = mp.remove();
				boolean tage = true;
				sizePerDoc++;
				for (int i = 0; i < size && tage == true; i++) {
					if (temp.toString().equals(strs[i])) {
						tage = false;
					
						strs[i] = br[i].readLine();
						if (strs[i] == null) {
							System.out
									.println("---------------------" + flag++ + "-----" + i + "------" + mp.getSize());
							br[i].close();
						} else
							mp.insert(new Tuples(strs[i]));
					}
				}

				if (temp.getWord().equals(prev.getWord())) {
					if (temp.getDocid() == prev.getDocid()) {
						iipd.add(temp);

					} else {// !=docId
						// arrayInvertIndex.add(iipd);
						String iipdtoString = iipd.toString() + " ";
						out.write(iipdtoString);
						lengthpd += iipdtoString.length();
						// System.out.println("iipd-length-------------------"+iipdtoString.length()+"/"+lengthpd);
						iipd = new InvertIndexPerDoc(temp.getDocid(), temp.getPos());

					}
				} else {// !=word
					int tempSum = sum;
					String iipdtoString = iipd.toString() + " ";
					out.write(iipdtoString);
					lengthpd += iipdtoString.length();
					sum += lengthpd;
					String tttttemp = tempSum + " " + lengthpd + '\n' + temp.getWord() + " ";
					outIndex.write(tttttemp);
					// System.out.println("~~~~--------" +tttttemp );
					iipd = new InvertIndexPerDoc(temp.getDocid(), temp.getPos());
					lengthpd = 0;
				}
				if (mp.getSize() == 1) {
					int tempSum = sum;
					String iipdtoString = iipd.toString() + " ";
					out.write(iipdtoString);
					lengthpd += iipdtoString.length();
					outIndex.write(tempSum + " " + lengthpd + '\n');
					out.write(iipd.toString());
					out.flush();
					out.close();
					outIndex.flush();
					outIndex.close();
					return;
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String iiWrite(ArrayList<InvertIndexPerDoc> dataIIPD) {
		String ans = "";
		for (InvertIndexPerDoc iipd : dataIIPD) {
			if (iipd == null)
				System.out.println("nimei");
			ans += iipd.toString() + " ";
		}
		return ans;

	}
}
