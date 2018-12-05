package fuzzycat.functiongrapher.grapher.expression;

public class Secant extends Unary {
	
	public Secant(Quantity q) {
		super(q);
	}

	@Override
	public double getValue() {
		double val = realValue(q);
		return 1.0 / Math.cos(val);
	}
}
