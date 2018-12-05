package fuzzycat.functiongrapher.grapher.expression;

public class Sum extends Binary {
	
	public Sum(Quantity q1, Quantity q2) {
		super(q1, q2);
	}

	@Override
	public double getValue() {
		double val1 = realValue(q1);
		double val2 = realValue(q2);
		return val1 + val2;
	}
}
