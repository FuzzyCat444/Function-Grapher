package fuzzycat.functiongrapher.grapher.expression;

public class Log extends Binary {
	public Log(Quantity x, Quantity base) {
		super(x, base);
	}

	@Override
	public double getValue() {
		double val1 = realValue(q1);
		double val2 = realValue(q2);
		return Math.log(val1) / Math.log(val2);
	}
}
