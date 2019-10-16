package calculator;

import java.util.Scanner;

public class UI {
    public static void input(){
        Scanner scanner = new Scanner(System.in);
        CalculatorLogic calculator = new CalculatorLogic();
        while(true){
            String line = scanner.nextLine();
            if(line.startsWith("/")){
                if(line.equals("/exit")){
                    System.out.print("Bye!");
                    break;
                } else if(line.equals("/help")){
                    System.out.println("The program calculates the sum of numbers.\n" +
                            "Operators\n" +
                            "   Addition:  + \n" +
                            "       (++ equals +, +++ equals +, -- equals +)\n" +
                            "   Subtraction:  - \n " +
                            "       (--- equals -)\n" +
                            "   Division:  / \n" +
                            "       (only single character accepted)\n" +
                            "   Multiplication:  * \n" +
                            "       (only single character accepted) \n" +
                            "   Parentheses: (...) \n" +
                            "       (highest priority)");
                } else {
                    System.out.println("Unknown command");
                }
            } else if(line.isEmpty()){
                continue;
            } else if(line.contains("=")){
                calculator.variables(line);
            } else if (line.matches("^-?\\d+$")){
                System.out.println(line);
            } else if(line.matches("^\\+\\d+$")){
                System.out.println(line.substring(1));
            } else if(!line.contains("=") && !line.matches("^\\w+$")){
                calculator.calculations(line);
            } else if (line.matches("^\\w+$")){
                calculator.singleVariable(line);
            }
        }
    }
}
