package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.ObjectStack;

/**
 * Class that represents demo stack which is run from terminal
 * @author leokiparje
 *
 */
public class StackDemo {
	
	/**
	 * method that checks if the given string is an actual Integer
	 * @param s String that is given 
	 * @return returns boolean true or false
	 */
	public static boolean isInteger(String s) {
	      boolean isValidInteger = false;
	      try{
	         Integer.parseInt(s);
	         isValidInteger = true;
	      }catch (NumberFormatException ex){}
	      return isValidInteger;
	   }
	
	public static void main(String[] args) {
		if (args.length!=1) throw new IllegalArgumentException();
		
		String[] array = args[0].split("\\s+");
		
		ObjectStack stack = new ObjectStack();
		
		for (String element : array) {
			if (isInteger(element)) {
				stack.push(Integer.parseInt(element));
				continue;
			}
			
			int n2 = (int) stack.pop();
			int n1 = (int) stack.pop();
			
			switch(element) {
			
			case "+":
				stack.push(n1+n2);
				break;
			case "-":
				stack.push(n1-n2);
				break;
			case "/":
				if (n2==0) {
					System.out.println("Djeljenje s nulom nije dozvoljeno!");
					System.exit(1);
				}
				stack.push(n1/n2);
				break;
			case "*":
				stack.push(n1*n2);
				break;
			case "%":
				stack.push(n1%n2);
				break;
			default:
				System.out.println("Nedozvoljeni znak!");
				System.exit(1);
			}
			
		}
		if (stack.size()!=1) {
			System.out.println("Greška, mogući razlog je previše inicijalnih argumenata!");
			System.exit(1);
		}
		System.out.println(stack.pop());
	}
}
