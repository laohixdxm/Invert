import java.util.ArrayList;

public class InvertIndexPerDoc {
	private int docId, f;
	private ArrayList<Integer> pos;
	private String word;

	public InvertIndexPerDoc(int docId, int pos) {
		this.docId = docId;
		this.f = 1;
		this.pos = new ArrayList<Integer>();
		this.pos.add(pos);
	}

	public InvertIndexPerDoc(Tuples tempTuples) {
		docId = tempTuples.getDocid();
		f = 1;
		pos = new ArrayList<Integer>();
		pos.add(tempTuples.getPos());
		word = tempTuples.getWord();
	}

	public void add(Tuples t) {
		f++;
		if (!word.equals(t.getWord()))
			System.err.println("-------------word changed!!!!!!!!!!!!!!");
		pos.add(t.getPos());
	}
	public void add(int pos) {
		f++;
		this.pos.add(pos);
	}

	public int getDocId() {
		return docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	public int getF() {
		return f;
	}

	public void setF(int f) {
		this.f = f;
	}

	public ArrayList<Integer> getPos() {
		return pos;
	}

	public void setPos(ArrayList<Integer> pos) {
		this.pos = pos;
	}

	public String toString() {
		String ans = docId + " " + f;
		for (int i : pos)
			ans += " " + i;
		return ans + " ";
	}
}