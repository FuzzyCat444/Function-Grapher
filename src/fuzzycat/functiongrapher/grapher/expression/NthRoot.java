package fuzzycat.functiongrapher.grapher.expression;

public class NthRoot extends Binary {

	public NthRoot(Quantity q, Quantity root) {
		super(q, root);
	}
	
	@Override
	public double getValue() {
		double val1 = realValue(q1);
		double val2 = realValue(q2);
		return Math.pow(val1, 1.0 / val2);
	}

}
