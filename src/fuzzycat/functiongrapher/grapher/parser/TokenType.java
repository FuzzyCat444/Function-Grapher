package fuzzycat.functiongrapher.grapher.parser;

public enum TokenType {
	OPEN_PARENTHESES("("),
	CLOSE_PARENTHESES(")"),
	PLUS("+"),
	MINUS("-"),
	TIMES("*"),
	DIVIDED_BY("/"),
	RAISED_TO("^"),
	SINE("sin"),
	COSINE("cos"),
	TANGENT("tan"),
	COTANGENT("cot"),
	SECANT("sec"),
	COSECANT("csc"),
	CEILING("ceil"),
	FLOOR("floor"),
	LOG("log"),
	MODULO("%"),
	NTHROOT("nthroot"),
	SQUARE_ROOT("sqrt"),
	ABSOLUTE_VALUE("abs"),
	COMMA(","),
	X("x"),
	Y("x"),
	Z("z"),
	NUMBER("");
	
	public static final TokenType[] FUNCTIONS = {
			SINE, COSINE, TANGENT, COTANGENT, SECANT, COSECANT, SQUARE_ROOT,
			CEILING, FLOOR, LOG, MODULO, ABSOLUTE_VALUE, NTHROOT
			};
	
	public final String name;
	
	private TokenType(String name) {
		this.name = name;
	}
}
