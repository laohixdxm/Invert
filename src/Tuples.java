
public class Tuples implements Comparable<Tuples> {
	public static Tuples MIN_VALUE = new Tuples(null, 0, 0);
	public static Tuples MAX_VALUE = new Tuples(null, 1, 0);
	private String word;
	private int docid, pos;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getDocid() {
		return docid;
	}

	public void setDocid(int docid) {
		this.docid = docid;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public Tuples(String word, int docid, int pos) {
		// TODO Auto-generated constructor stub
		this.word = word;
		this.docid = docid;
		this.pos = pos;
	}

	public Tuples(String str) {
		// TODO Auto-generated constructor stub
		// System.out.println(str);
		// System.out.println(str);
		this.word = str.split(" ")[0];
		// System.out.println(str.split(" ")[3]);
		this.docid = Integer.valueOf(str.split(" ")[1]);

		this.pos = Integer.valueOf(str.split(" ")[2]);
	}

	@Override
	public String toString() {
		return word + " " + docid + " " + pos;
	}

	@Override
	public int compareTo(Tuples o) {
		// TODO Auto-generated method stub
		if (!this.getWord().equals(o.getWord())) {
			// System.out.println(this.getWord()+"-----"+o.getWord());
			if (o.getWord() == null)
				return 1;
			else
				return this.getWord().compareTo((String) o.getWord());
		}
		if (this.getDocid() == o.getDocid())
			return this.getPos() - o.getPos();
		return this.getDocid() - o.getDocid();

	}

}
