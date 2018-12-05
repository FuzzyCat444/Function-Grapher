package fuzzycat.functiongrapher.grapher.expression;

public class Cotangent extends Unary {
	
	public Cotangent(Quantity q) {
		super(q);
	}

	@Override
	public double getValue() {
		double val = realValue(q);
		return 1.0 / Math.tan(val);
	}
}
