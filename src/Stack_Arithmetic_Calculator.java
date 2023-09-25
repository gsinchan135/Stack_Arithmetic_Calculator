/*
Gregory Sin-Chan
40225969
COMP352
Assignment 2
 */


import java.io.*;
import java.util.Scanner;

public class Stack_Arithmetic_Calculator {

    static ArrayStack<Integer> valStk = new ArrayStack<>();
    static ArrayStack<String> opStk = new ArrayStack<>();

    public static void main(String[] args) {
        try{
            String exp;
            File myFile = new File("expressions.txt");
            Scanner scan = new Scanner(myFile);

            FileWriter myWriter = new FileWriter("solutions.txt");
            BufferedWriter myBuffer = new BufferedWriter(myWriter);
            PrintWriter myPrinter = new PrintWriter(myBuffer);

            //loops through while there are still expressions to be read
            while(scan.hasNextLine()){
                exp = scan.nextLine().replaceAll("\\s","");//replaceAll removes potential white space
                ReadTokens(exp);
                myPrinter.append(exp+"\n");
                myPrinter.append(valStk.toString()+"\n");
                System.out.println(valStk.toString());
                //reinstantiates stacks to clear them
                valStk = new ArrayStack<>();
                opStk = new ArrayStack<>();
            }
            myPrinter.close();
        }catch (Exception e){
            System.err.println(e);
        }
    }

    //Using an object allows the returned value to be true/false or an int
    private static Object ReadTokens(String exp){
        //if true, it means the previous token was also a number and that the current token and previous token are 1 number
        boolean succeedsNumber = false;
        boolean equalities = false;
        String token;
        for(int i =0;i<exp.length();i++){
            token = String.valueOf(exp.charAt(i));
            if(isNum(token)){
                //for numbers with more than 1 digit
                if(succeedsNumber){
                    //pops the top and concatenate it as a string to the current token
                    //cast it to an integer to be pushed back to the value stack
                    valStk.push(Integer.valueOf(valStk.pop().toString()+token));
                }
                else{
                    valStk.push(Integer.valueOf(token));
                }
                succeedsNumber=true;
            }
            else{
                if(equalities){
                    token = opStk.pop()+token;
                    equalities = false;
                }
                if(token.equals("=")||token.equals("!")){
                    equalities = true;
                    opStk.push(token);
                    continue;
                }
                else{
                    equalities = false;
                }
                //next loop through program, can tell that the previous token was not a number, therefore not a multi-digit number
                succeedsNumber = false;
                repeatOps(token);
                //a ')' signifies the end of a sequence of inner operations, it is not an operation itself and should not be pushed
                if(exp.charAt(i)!=')')
                    opStk.push(token);
            }
        }
        repeatOps("$");
        return valStk.top();
    }

    private static void repeatOps(String token){
        //if a close bracket is found do operations until the top of the op stack is an open bracket, to perform the operations within brackets.
        if(token.equals(")")){
            while(!opStk.top().equals("(")){
                doOp();
            }
            opStk.pop();
        }//if the token is a '(', there is no operation to be done, return immediately
        //or if there is a top of the stack and the top is '(', there is no operation to perform
        else if(token.equals("(")||(opStk.size()>0&&opStk.top().equals("("))){
            return;
        }
        else{
            //if there are at least 2 values and the top operation has higher precedence that isn't a bracket, then do the operation
            while(valStk.size() > 1&&precedence(token)<=precedence(opStk.top())&&!opStk.top().equals("(")){
                doOp();
            }
        }

    }

    private static void doOp(){
        Integer ans = 0;
        int x = valStk.pop();
        int y = valStk.pop();
        String op = opStk.pop();

        switch (op){
            case "(":
            case ")":
                return;
            case "^"://Assignment says operators only need to work on integers. Any decimals will be lost from truncation
                ans = (int) Math.pow(y,x); //All inputs should be integers anyways, which will only return integers
                break;
            case "*":
                ans = x*y;
                break;
            case "/": //Assignment says operators only need to work on integers. Any decimals will be lost from truncation
                ans = y/x;
                break;
            case "+":
                ans = x+y;
                break;
            case "-":
                ans = y-x;
                break;
                //Because the following 4 operations occur last, Integer.MIN_VALUE will be used to denote a false comparison
                //Integer.MAX_VALUE denotes a true comparison
            case ">":
                if(y>x)
                    ans = Integer.MAX_VALUE;
                else
                    ans = Integer.MIN_VALUE;
                break;
            case "≥":
                if(y>x||y==x)
                    ans = Integer.MAX_VALUE;
                else
                    ans = Integer.MIN_VALUE;
                break;
            case "≤":
                if(y<x||y==x)
                    ans = Integer.MAX_VALUE;
                else
                    ans = Integer.MIN_VALUE;
                break;
            case "<":
                if(y<x)
                    ans = Integer.MAX_VALUE;
                else
                    ans = Integer.MIN_VALUE;
                break;
            case "==":
                if(y==x)
                    ans = Integer.MAX_VALUE;
                else
                    ans = Integer.MIN_VALUE;
                break;
            case "!=":
                if(y!=x)
                    ans = Integer.MAX_VALUE;
                else
                    ans = Integer.MIN_VALUE;
                break;
            default:
                break;
        }
        valStk.push(ans);
    }

    private static int precedence(String token){
        switch (token){
            case "(":
            case ")":
                return 6;
            case "^":
                return 5;
            case "*":
            case "/":
                return 4;
            case "+":
            case "-":
                return 3;
            case ">":
            case "≥":
            case "≤":
            case "<":
                return 2;
            case "==":
            case "!=":
                return 1;
            default:
                return 0;
        }
    }

    private static boolean isNum(String num){
        try{
            int number = Integer.parseInt(num);
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }

}
