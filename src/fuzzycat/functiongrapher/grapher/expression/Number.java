package fuzzycat.functiongrapher.grapher.expression;

public class Number extends Quantity {
	
	protected double num;
	
	public Number(double num) {
		this.num = num;
	}

	@Override
	public double getValue() {
		return num;
	}
}
