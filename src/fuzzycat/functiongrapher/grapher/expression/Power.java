package fuzzycat.functiongrapher.grapher.expression;

public class Power extends Binary {
	
	public Power(Quantity x, Quantity exp) {
		super(x, exp);
	}

	@Override
	public double getValue() {
		double val1 = realValue(q1);
		double val2 = realValue(q2);
		return Math.pow(val1, val2);
	}
}
