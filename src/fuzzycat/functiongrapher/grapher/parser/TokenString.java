package fuzzycat.functiongrapher.grapher.parser;

import java.util.ArrayList;
import java.util.List;

public class TokenString {
	private List<Token> tokens;
	
	public TokenString() {
		tokens = new ArrayList<>();
	}
	
	private TokenString(List<Token> tokens) {
		this.tokens = tokens;
	}
	
	public void addToken(Token token) {
		tokens.add(token);
	}
	
	public Token tokenAt(int i) {
		return tokens.get(i);
	}
	
	public int getLength() {
		return tokens.size();
	}
	
	public TokenString split(int start, int stop) {
		start = Math.max(0, start);
		stop = Math.min(tokens.size(), stop);
		
		List<Token> subList = new ArrayList<>();
		for (int i = start; i < stop; i++) {
			subList.add(tokens.get(i));
		}
		return new TokenString(subList);
	}
	
	public void insert(int i, Token token) {
		tokens.add(i, token);
	}
	
	public Token remove(int i) {
		return tokens.remove(i);
	}
	
	@Override
	public String toString() {
		String line = "";
		for (Token t : tokens) {
			line += t.toString();
			if (t.data.length() > 0) line += "<" + t.data + ">";
			line += " ";
		}
		return line;
	}
}
