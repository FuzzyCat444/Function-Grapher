package fuzzycat.functiongrapher.grapher.parser;

import java.util.ArrayList;
import java.util.List;

import fuzzycat.functiongrapher.grapher.expression.AbsoluteValue;
import fuzzycat.functiongrapher.grapher.expression.Ceiling;
import fuzzycat.functiongrapher.grapher.expression.Cosecant;
import fuzzycat.functiongrapher.grapher.expression.Cosine;
import fuzzycat.functiongrapher.grapher.expression.Cotangent;
import fuzzycat.functiongrapher.grapher.expression.Difference;
import fuzzycat.functiongrapher.grapher.expression.Floor;
import fuzzycat.functiongrapher.grapher.expression.Function;
import fuzzycat.functiongrapher.grapher.expression.Log;
import fuzzycat.functiongrapher.grapher.expression.Modulo;
import fuzzycat.functiongrapher.grapher.expression.NthRoot;
import fuzzycat.functiongrapher.grapher.expression.Number;
import fuzzycat.functiongrapher.grapher.expression.Power;
import fuzzycat.functiongrapher.grapher.expression.Product;
import fuzzycat.functiongrapher.grapher.expression.Quantity;
import fuzzycat.functiongrapher.grapher.expression.Quotient;
import fuzzycat.functiongrapher.grapher.expression.Secant;
import fuzzycat.functiongrapher.grapher.expression.Sine;
import fuzzycat.functiongrapher.grapher.expression.SquareRoot;
import fuzzycat.functiongrapher.grapher.expression.Sum;
import fuzzycat.functiongrapher.grapher.expression.Tangent;
import fuzzycat.functiongrapher.grapher.expression.Variable;

public class ExpressionParser {
	
	private Error error;
	private Variable x;
	private Variable y;
	private Variable z;
	
	public ExpressionParser() {
		error = new Error();
		x = new Variable();
		y = new Variable();
		z = new Variable();
	}
	
	public Function parse(String expr) {
		TokenString tokens = tokenize(expr);
		if (tokens != null) {
			checkParentheses(tokens);
			substituteUnaryMinus(tokens);
			Quantity root = doOrderOfOperations(tokens);
			if (root == null) {
				error.makeError("Parsing of the function \"" + expr + "\" failed.");
				return null;
			}
			return new Function(root, x, y, z);
		}
		
		error.makeError("Parsing of the function \"" + expr + "\" failed.");
		return null;
	}
	
	private TokenString tokenize(String expr) {
		TokenString tkString = new TokenString();
		
		String name = "";
		String number = "";
		int numDecimals = 0;
		for (int i = 0; i < expr.length(); i++) {
			char currentChar = expr.charAt(i);
			boolean special = false;
			
			if (Character.isAlphabetic(currentChar)) {
				if (currentChar == 'x') {
					tkString.addToken(new Token(TokenType.X));
				} else if (currentChar == 'y') {
					tkString.addToken(new Token(TokenType.Y));
				} else if (currentChar == 'z') {
					tkString.addToken(new Token(TokenType.Z));
				} else {
					name += currentChar;
				}
				special = true;
			} else if (name.length() > 0) {
				TokenType type = getTokenTypeByName(name);
				if (type == null) {
					error.makeError("The function name " + name + " is not valid!");
					return null;
				}
				tkString.addToken(new Token(type));
				name = "";
			}
			
			if (Character.isDigit(currentChar) || currentChar == '.') {
				if (currentChar == '.') {
					if (numDecimals == 0) number += currentChar;
					numDecimals++;
				} else {
					number += currentChar;
				}
				special = true;
			} else if (number.length() > 0) {
				tkString.addToken(new Token(TokenType.NUMBER, number));
				number = "";
				numDecimals = 0;
			}
			
			if (!(special || currentChar == ' ')) {
				if (currentChar == '(') tkString.addToken(new Token(TokenType.OPEN_PARENTHESES));
				else if (currentChar == ')') tkString.addToken(new Token(TokenType.CLOSE_PARENTHESES));
				else if (currentChar == ',') tkString.addToken(new Token(TokenType.COMMA));
				else if (currentChar == '+') tkString.addToken(new Token(TokenType.PLUS));
				else if (currentChar == '-') tkString.addToken(new Token(TokenType.MINUS));
				else if (currentChar == '*') tkString.addToken(new Token(TokenType.TIMES));
				else if (currentChar == '/') tkString.addToken(new Token(TokenType.DIVIDED_BY));
				else if (currentChar == '^') tkString.addToken(new Token(TokenType.RAISED_TO));
				else if (currentChar == '%') tkString.addToken(new Token(TokenType.MODULO));
				else {
					error.makeError("The character '" + currentChar + "' is not allowed!");
					return null;
				}
			}
		}
		if (name.length() > 0) {
			TokenType type = getTokenTypeByName(name);
			if (type == null) {
				error.makeError("The function name '" + name + "' is not valid!");
				return null;
			}
			tkString.addToken(new Token(type));
			name = "";
		}
		if (number.length() > 0) {
			tkString.addToken(new Token(TokenType.NUMBER, number));
			number = "";
			numDecimals = 0;
		}
		
		return tkString;
	}
	
	private Quantity doOrderOfOperations(TokenString tokens) {
		/*
		 * Order of operations in reverse:
		 * Addition, Subtraction, Division, Multiplication, Modulo, Exponentiation, Function, Parentheses, Variables, Numbers
		 * All from right to left 
		 */
		int location = 0;	// Location of some operator
		Quantity ret = new Number(0.0);
		
		location = scanFromRight(tokens, TokenType.PLUS);
		if (location != -1) {
			TokenString left = tokens.split(0, location);
			TokenString right = tokens.split(location + 1, tokens.getLength());
			ret = new Sum(doOrderOfOperations(left), doOrderOfOperations(right));
		} else {
			location = scanFromRight(tokens, TokenType.MINUS);
			if (location != -1) {
				TokenString left = tokens.split(0, location);
				TokenString right = tokens.split(location + 1, tokens.getLength());
				ret = new Difference(doOrderOfOperations(left), doOrderOfOperations(right));
			} else {
				location = scanFromRight(tokens, TokenType.DIVIDED_BY);
				if (location != -1) {
					TokenString left = tokens.split(0, location);
					TokenString right = tokens.split(location + 1, tokens.getLength());
					ret = new Quotient(doOrderOfOperations(left), doOrderOfOperations(right));
				} else {
					location = scanFromRight(tokens, TokenType.TIMES);
					if (location != -1) {
						TokenString left = tokens.split(0, location);
						TokenString right = tokens.split(location + 1, tokens.getLength());
						ret = new Product(doOrderOfOperations(left), doOrderOfOperations(right));
					} else {
						location = scanFromRight(tokens, TokenType.MODULO);
						if (location != -1) {
							TokenString left = tokens.split(0, location);
							TokenString right = tokens.split(location + 1, tokens.getLength());
							ret = new Modulo(doOrderOfOperations(left), doOrderOfOperations(right));
						} else {
							location = scanFromRight(tokens, TokenType.RAISED_TO);
							if (location != -1) {
								TokenString left = tokens.split(0, location);
								TokenString right = tokens.split(location + 1, tokens.getLength());
								ret = new Power(doOrderOfOperations(left), doOrderOfOperations(right));
							} else {
								location = scanFromRight(tokens, TokenType.FUNCTIONS);
								if (location != -1) {
									int endParams = getFunctionParamsEnd(tokens, location + 2);
									if (endParams != -1) {
										TokenString paramString = tokens.split(location + 2, endParams);
										ret = parseFunctionParams(paramString, tokens.tokenAt(location).type);
									}
								} else if (tokens.getLength() >= 2 && tokens.tokenAt(tokens.getLength() - 1).type == TokenType.CLOSE_PARENTHESES
										&& tokens.tokenAt(0).type == TokenType.OPEN_PARENTHESES) {
									TokenString inParentheses = tokens.split(1, tokens.getLength() - 1);
									ret = doOrderOfOperations(inParentheses);
								} else {
									location = scanFromRight(tokens, TokenType.X);
									if (location != -1) {
										ret = x;
									} else {
										location = scanFromRight(tokens, TokenType.Y);
										if (location != -1) {
											ret = y;
										} else {
											location = scanFromRight(tokens, TokenType.Z);
											if (location != -1) {
												ret = z;
											} else {
												location = scanFromRight(tokens, TokenType.NUMBER);
												if (location != -1) {
													ret = new Number(Double.parseDouble(tokens.tokenAt(location).data));
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return ret;
	}

	private Quantity parseFunctionParams(TokenString paramString, TokenType type) {
		List<TokenString> params = new ArrayList<>();
		int start = 0;
		for (int i = 0; i < paramString.getLength(); i++) {
			Token t = paramString.tokenAt(i);
			if (t.type == TokenType.COMMA) {
				params.add(paramString.split(start, i));
				start = i + 1;
			}
		}
		params.add(paramString.split(start, paramString.getLength()));
		
		if (params.size() == 0) return null;
		
		if (params.size() == 1) {
			Quantity param1 = doOrderOfOperations(params.get(0));
			switch (type) {
			case ABSOLUTE_VALUE:
				return new AbsoluteValue(param1);
			case CEILING:
				return new Ceiling(param1);
			case FLOOR:
				return new Floor(param1);
			case SINE:
				return new Sine(param1);
			case COSINE:
				return new Cosine(param1);
			case TANGENT:
				return new Tangent(param1);
			case COTANGENT:
				return new Cotangent(param1);
			case SECANT:
				return new Secant(param1);
			case COSECANT:
				return new Cosecant(param1);
			case SQUARE_ROOT:
				return new SquareRoot(param1);
			default:
				return null;
			}
		} else if (params.size() == 2) {
			Quantity param1 = doOrderOfOperations(params.get(0));
			Quantity param2 = doOrderOfOperations(params.get(1));
			switch (type) {
			case NTHROOT:
				return new NthRoot(param1, param2);
			case LOG:
				return new Log(param1, param2);
			default:
				return null;
			}
		}
		return null;
	}

	private int getFunctionParamsEnd(TokenString tokens, int location) {
		int openParentheses = 0;
		for (int i = location; i < tokens.getLength(); i++) {
			Token t = tokens.tokenAt(i);
			if (t.type == TokenType.OPEN_PARENTHESES) {
				openParentheses++;
			} else if (t.type == TokenType.CLOSE_PARENTHESES) {
				if (openParentheses == 0) {
					return i;
				}
				openParentheses--;
			}
		}
		return -1;
	}

	private int scanFromRight(TokenString tokens, TokenType type) {
		int openParentheses = 0;
		for (int i = tokens.getLength() - 1; i >= 0; i--) {
			Token t = tokens.tokenAt(i);
			if (t.type == TokenType.CLOSE_PARENTHESES) {
				openParentheses++;
			} else if (t.type == TokenType.OPEN_PARENTHESES) {
				openParentheses--;
			} else if (t.type == type && openParentheses == 0) {
				return i;
			}
		}
		return -1;
	}
	
	private int scanFromRight(TokenString tokens, TokenType[] types) {
		int openParentheses = 0;
		for (int i = tokens.getLength() - 1; i >= 0; i--) {
			Token t = tokens.tokenAt(i);
			if (t.type == TokenType.CLOSE_PARENTHESES) {
				openParentheses++;
			} else if (t.type == TokenType.OPEN_PARENTHESES) {
				openParentheses--;
			} else {
				if (openParentheses == 0) {
					for (int j = 0; j < types.length; j++) {
						if (t.type == types[j]) {
							return i;
						}
					}
				}
			}
		}
		return -1;
	}
	
	private void substituteUnaryMinus(TokenString tokens) {
		Token prev = null;
		for (int i = 0; i < tokens.getLength(); i++) {
			Token t = tokens.tokenAt(i);
			if (t.type == TokenType.MINUS) {
				if (prev == null || !(prev.type == TokenType.NUMBER || prev.type == TokenType.X || prev.type == TokenType.CLOSE_PARENTHESES)) {
					// Ex: -x becomes (0-1)*x
					tokens.remove(i);
					tokens.insert(i, new Token(TokenType.TIMES));
					tokens.insert(i, new Token(TokenType.CLOSE_PARENTHESES));
					tokens.insert(i, new Token(TokenType.NUMBER, "1"));
					tokens.insert(i, new Token(TokenType.MINUS));
					tokens.insert(i, new Token(TokenType.NUMBER, "0"));
					tokens.insert(i, new Token(TokenType.OPEN_PARENTHESES));
					i += 6;
				}
			}
			prev = t;
		}
	}

	private void checkParentheses(TokenString tokens) {
		// Test for correct number of parentheses
		int openParentheses = 0;
		for (int i = 0; i < tokens.getLength(); i++) {
			Token t = tokens.tokenAt(i);
			if (t.type == TokenType.OPEN_PARENTHESES) {
				openParentheses++;
			} else if (t.type == TokenType.CLOSE_PARENTHESES) {
				openParentheses--;
			}
			if (openParentheses < 0) {
				error.makeError("You closed too many parentheses!");
			}
		}
		if (openParentheses > 0) {
			error.makeError("You did not close enough parentheses!");
		}
	}


	private TokenType getTokenTypeByName(String name) {
		TokenType[] values = TokenType.FUNCTIONS;
		for (TokenType v : values) {
			if (v.name.equals(name)) return v;
		}
		return null;
	}
}
