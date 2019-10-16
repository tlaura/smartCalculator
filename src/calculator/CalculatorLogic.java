package calculator;

import java.util.*;

public class CalculatorLogic {
    private Map<String, Integer> variables = new HashMap<>();

    private static boolean isOperator(String c) {
        return c.equals("+") || c.equals("-") || c.equals("*") || c.equals("/") || c.equals("^") || c.equals("(") || c.equals(")");
    }
    private static int getPriority(String c){
        switch (c) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
        }
        return -1;
    }

    private static boolean isOperand(String s){
        return s.matches("^\\w+$") || s.matches("^\\d+$");
    }

    public String postfix(String line){
        Deque<String> stack = new ArrayDeque<>();
        StringBuilder postfix = new StringBuilder();

//        Add operands (numbers and variables) to the result as they arrive
        String[] equation = line.split(" ");
        for(String e: equation){
            if(isOperand(e)){
                postfix.append(e).append(" ");
            } else if(e.equals("(")) {
                stack.push(e);
            } else if(e.equals(")")) {
//                If the incoming element is a right parenthesis,
//                pop the stack and add operators to the result until you see a left parenthesis
                while(!stack.isEmpty() && !stack.peek().equals("(")) {
                    postfix.append(stack.pop()).append(" ");
                }
//                pop '('
                if(!stack.isEmpty()){
                    stack.pop();
                }
            } else if(isOperator(e)) {
                while(!stack.isEmpty() && getPriority(e) <= getPriority(stack.peek())){
                    postfix.append(stack.pop()).append(" ");
                }
                stack.push(e);
            }
        }
        while(!stack.isEmpty()){
            postfix.append(stack.pop()).append(" ");
        }
        return postfix.toString();
    }


    public static String cleanEquation(String line){
//        add whitespaces before each operator
        String plusRegEx = "(--|\\+)+";
        line = line.replaceAll(plusRegEx, " + ");
        String minusRegex = "(-|---+|-\\+|\\+-)+";
        line = line.replaceAll(minusRegex, " - ");
        line = line.replaceAll("(\\()|(\\))|(\\*)|(\\/)|(\\^)", " $1$2$3$4$5 ");
//        \\p{Zs} - for non-breaking space
        line = line.trim().replaceAll("(\\p{Zs})+", " ");
        return line;
    }

    private static boolean parenthesesCheck(String line){
        int leftParentheses = 0;
        int rightParentheses = 0;
        for (int i = 0; i < line.length(); i++) {
            if(line.charAt(i) == '('){
                leftParentheses++;
            } else if(line.charAt(i) == ')'){
                rightParentheses++;
            }
        }
        return leftParentheses == rightParentheses;
    }

    public void calculations(String line){
        try {
            if ((!line.contains("-") && !line.contains("+")
                        && !line.contains("*") && !line.contains("/") && line.contains(" "))
                        || line.matches(".*[\\/|\\*]{2,}.*")
                        || !parenthesesCheck(line)) {
                    System.out.println("Invalid expression");
            } else {
                line = cleanEquation(line);
                line = postfix(line);
                System.out.println(line);
                Deque<Integer> stack = new ArrayDeque<>();
                String[] equation = line.split(" ");
                for (String s : equation) {
                    if (s.matches("^\\d+$")) {
                        stack.push(Integer.parseInt(s));
                    } else if (s.matches("^[a-zA-Z]+$")) {
                        stack.push(variables.get(s));
                    } else {
                        int num1 = stack.pop();
                        int num2 = stack.pop();

                        switch (s){
                            case "+":
                                stack.push(num2+num1);
                                break;
                            case "-":
                                stack.push(num2-num1);
                                break;
                            case "*":
                                stack.push(num2*num1);
                                break;
                            case "/":
                                stack.push(num2/num1);
                                break;
                            case "^":
                                stack.push((int)Math.pow(num2, num1));
                                break;
                        }
                    }
                }
                System.out.println(stack.peek());
            }
        }
        catch (Exception e) {
            System.out.println("Unknown variable");
        }
    }

    public void singleVariable(String line){
        if(variables.containsKey(line)){
            System.out.println(variables.get(line));
        } else {
            System.out.println("Unknown variable");
        }
    }

    public void variables(String line) {
        try {
            line = line.replaceAll("\\s+", "");
            String[] var = line.split("=");
            if(var[0].matches(".*\\d.*")){
//            check if variable consists of Latin letters only
                System.out.println("Invalid identifier");
            } else if(var.length > 2){
//                variable can only be assigned one value
                System.out.println("Invalid assignment");
            } else {
                if(var[1].matches("^\\d+$")){
//                if value is digits only
                    variables.put(var[0], Integer.parseInt(var[1]));
                } else if(var[1].matches("^\\w$")) {
//                    if variable is assigned a value of another variable
                    if(variables.containsKey(var[1])){
                        variables.put(var[0], variables.get(var[1]));
                    } else {
                        System.out.println("Unknown variable");
                    }
                } else {
                    System.out.println("Invalid assignment");
                }
            }
        } catch (Exception e){
            System.out.println("Invalid assignment");
        }
    }
}
