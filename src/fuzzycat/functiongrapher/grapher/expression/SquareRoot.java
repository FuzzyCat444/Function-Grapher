package fuzzycat.functiongrapher.grapher.expression;

public class SquareRoot extends Unary {
	
	public SquareRoot(Quantity q) {
		super(q);
	}

	@Override
	public double getValue() {
		double val = realValue(q);
		return Math.sqrt(val);
	}

}
