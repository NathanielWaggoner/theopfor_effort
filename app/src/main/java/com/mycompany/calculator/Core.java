package com.mycompany.calculator;
import java.util.Scanner;
import java.util.Stack;
import java.util.Arrays;

public class Core{
	
	public static final String[] BINARY = {"+", "-", "*", "/", "^"};
	public static final String[] UNARY = {"√", "!", "log", "cos", "tan", "sin"};
	public static final String[] FUNCTIONS = {"log", "cos", "tan", "sin"};
			
	public static String spaceString(String s, String mode){
		StringBuilder newString = new StringBuilder();
		Character lastChar = null;
		Character lastOp = null;
		//Double lastNum = null;
		
		for (int i=0; i < s.length(); i++){
			
			char c = s.charAt(i);
			
			if (lastChar != null && (lastChar == '%' || lastChar == ')') && 
					(Arrays.asList(UNARY).contains(c) || Arrays.asList(FUNCTIONS).contains(c) || Arrays.asList(BINARY).contains(c)))
				newString.append(" * ");
			
			if(c == '+' || c == '*' || c=='/' || c == '^' || c == ')'){
				newString.append(" " + c + " ");
				lastChar = c;
				lastOp = c;
				continue;
			}
			// Multiplication with parenthesis
			else if (c == '(' || c == '√'){
				if (lastChar != null && Character.isDigit(lastChar)){
					newString.append(" * " + c + " ");
					lastChar = c;
					lastOp = c;
					continue;
				}
				else{
					newString.append(" " + c + " ");
					lastChar = c;
					lastOp = c;
					continue;
				}
			}
			// ensure negative numbers stay negative
			else if (c == '-'){
				if (lastChar != null && Character.isDigit(lastChar)){
					newString.append(" - ");
					lastChar = c;
					lastOp = c;
					continue;
				}
			}
			// Because percentages suck
			else if (c == '%'){
				Scanner read = new Scanner(newString.toString());
				Double pNum = null;
				int offset = -1;
				
				// Loop through the string to calculate blank spaces that need removing
				while (read.hasNext()){
					if (read.hasNextDouble()){
						pNum = read.nextDouble();
						// Reset offset
						offset = -1;
					}
					else{
						// It is not a number, so count
						read.next();
						offset++;
					}
				}
				
				// Remove the excess spaces
				if ((pNum.toString().length() + offset) > newString.length())
					newString.setLength(0);
				else
					newString.setLength(newString.length() - (pNum.toString().length() + offset));
				
				read.close();
				
				if (lastOp != null && (lastOp == '+' || lastOp == '-')){
					Double oNum = null;
					offset = -1;
					Scanner read2 = new Scanner(newString.toString());
					
					// Loop through and calculate offset once again...
					while (read2.hasNext()){
						if (read2.hasNextDouble()){
							oNum = read2.nextDouble();
							offset = -1;
						}
						else{
							read2.next();
							offset++;
						}
					}
					// Trim it
					newString.setLength(newString.length() - (oNum.toString().length() + offset));
					
					// Original number is now manipulated by the percent, and inserted into the string
					if (lastOp == '+')
						newString.append(oNum + (oNum * (pNum / 100)));
					else
						newString.append(oNum - (oNum * (pNum / 100)));
					
					lastChar = '%';
					lastOp = '%';
					read2.close();
					continue;
				}
				else{
					// For anything else, just convert the whole number to decimal
					pNum /= 100;
					newString.append(pNum.toString());
					lastChar = '%';
					lastOp = '%';
					continue;
				}
			}
			else if (mode.equals("graphing") && Character.toString(c).matches("[a-z]")){
				if (lastChar != null && Character.isDigit(lastChar))
					newString.append(" * " + c + " ");
				else
					newString.append(" " + c + " ");
				
				lastChar = c;
				lastOp = c;
				continue;
			}

			newString.append(c);
			if (c != ' ')
				lastChar = c;
		}
		
		return newString.toString();
	}
	
	public static boolean errorCheck(String s){
		int parenCount = 0;
		
		// check if every '(' has a matching ')'
		for (int i = 0; i < s.length(); i++){
			if (s.charAt(i) == '(')
				parenCount++;
			else if (s.charAt(i) == ')')
				parenCount--;
		}
		
		if (parenCount == 0)
			return true;
		else
			return false;
	}
	
	public static boolean checkPrecedence(String op1, String op2){
		// true if op1 can go on top of op2
		
		if (Arrays.asList(FUNCTIONS).contains(op1)){
			if (Arrays.asList(FUNCTIONS).contains(op2))
				return false;
			return true;
		}
		else if (op1.equals("^") || op1.equals("√")){
			if (op2.equals("^") || op2.equals("√"))
				return false;
			return true;
		}
		else if ((op1.equals("*") || op1.equals("/")) && (op2.equals("+") || op2.equals("-"))){
			return true;
		}
		else if ((op1.equals("*") || op1.equals("/")) && (op2.equals("*") || op2.equals("/"))){
			return false;
		}
		else if (op2.equals("(")){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static String postfixConversion(String infix){
		StringBuilder postfix = new StringBuilder();
		Scanner read = new Scanner(infix);
		Stack<String> s = new Stack<>();
		
		// read through the string
		while (read.hasNext()){
			
			// if the next character is a number
			if (read.hasNextDouble()){
				// print it out
				postfix.append(read.nextDouble());
				postfix.append(" ");
			}
			else{
				// get the character, but ignore spaces
				String c = read.next().toString();
				if (!c.equals(" ")){
					
					// if stack is empty, just put it on
					if (s.empty()){
						s.push(c);
					}
					// push on ( always
					else if (c.equals("(")){
						s.push(c);
					}
					// if ) then empty stack up to (
					else if (c.equals(")")){
						while (!s.peek().equals("(")){
							postfix.append(s.pop());
							postfix.append(" ");
						}
						// remove )
						s.pop();
					}
					// if new thing can be on top of other
					else if (checkPrecedence(c, s.peek())){
						s.push(c);
					}
					// if not, append
					else{
						while (!checkPrecedence(c, s.peek())){
							postfix.append(s.pop());
							postfix.append(" ");
							
							if (s.empty())
								break;
						}
						s.push(c);
					}
				}
			}
		}
		
		// get rid of the remaining operators
		while (!s.empty()){
			postfix.append(s.pop());
			postfix.append(" ");
		}
		
		// exit
		read.close();
		return postfix.toString();
	}
	
	public static Double solve(String equation){
		Double answer = 0.0;
		Scanner read = new Scanner(equation);
		Stack<Double> s = new Stack<Double>();
		
		while (read.hasNext()){
			if (read.hasNextDouble()){
				// numbers are immediately pushed to stack
				s.push(read.nextDouble());
			}
			else{
				
				String c = read.next().toString();
				
				if (Arrays.asList(UNARY).contains(c)){
					Double num1 = s.pop();
					
					switch(c){
						case "√": s.push(Math.sqrt(num1)); break;
						case "log": s.push(Math.log10(num1)); break;
						case "sin": s.push(Math.sin(num1)); break;
						case "cos": s.push(Math.cos(num1)); break;
						case "tan": s.push(Math.tan(num1)); break;
					}
				}
				else{
					Double num2 = s.pop();
					Double num1 = s.pop();
					
					// apply the operations
					switch(c){
						case "+": s.push(num1 + num2); break;
						case "-": s.push(num1 - num2); break;
						case "*": s.push(num1 * num2); break;
						case "/": s.push(num1 / num2); break;
						case "^": s.push(Math.pow(num1, num2)); break;
					}
				}
			}
		}
		
		// set the answer equal to the last remaining stack item
		if (!s.empty()){
			answer = s.pop();
		}
		
		read.close();
		return answer;
	}
}