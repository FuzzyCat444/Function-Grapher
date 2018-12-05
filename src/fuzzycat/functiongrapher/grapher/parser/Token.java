package fuzzycat.functiongrapher.grapher.parser;

public class Token {
	public final TokenType type;
	public final String data;
	
	public Token(TokenType type, String data) {
		this.type = type;
		this.data = data;
	}
	
	public Token(TokenType type) {
		this(type, "");
	}
	
	@Override
	public String toString() {
		return type.toString();
	}
}
