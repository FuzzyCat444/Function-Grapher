package fuzzycat.functiongrapher.grapher.expression;

public abstract class Binary extends Quantity {

	Quantity q1;
	Quantity q2;
	
	public Binary(Quantity q1, Quantity q2) {
		this.q1 = q1;
		this.q2 = q2;
	}
	
	@Override
	public abstract double getValue();
}
