package fuzzycat.functiongrapher.grapher.expression;

public class Function {
	private Quantity root;
	private Variable x;
	private Variable y;
	private Variable z;
	
	public Function(Quantity root, Variable x, Variable y, Variable z) {
		this.root = root;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double evaluateAt(double x, double y, double z) {
		this.x.set(x);
		this.y.set(y);
		this.z.set(z);
		return root.getValue();
	}
}
