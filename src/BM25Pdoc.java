
public class BM25Pdoc implements Comparable<BM25Pdoc>{
	double BM25;
	int docid;
	public BM25Pdoc(double BM25,int docid) {
		// TODO Auto-generated constructor stub
		this.BM25=BM25;
		this.docid=docid;
	}
	@Override
	public int compareTo(BM25Pdoc o) {
		// TODO Auto-generated method stub
		return Double.compare(this.BM25, o.BM25);
	}
	public double getBM25() {
		return BM25;
	}
	public void setBM25(double bM25) {
		BM25 = bM25;
	}
	public int getDocid() {
		return docid;
	}
	public void setDocid(int docid) {
		this.docid = docid;
	}
	@Override
	public String toString() {
		return  "BM25="+BM25 + ", docid=" + docid ;
	}

}
