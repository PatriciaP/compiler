package compiler.virtual_machine;

import compiler.lexical_analysis.LexicalAnalysis;
import compiler.syntactic_analysis.Parser;
import compiler.syntactic_analysis.ResultReturn;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class VirtualMachine {


    public static int lenghtVariables;

    public static List<Variables> variablesList;

    private static int lenVariablesMachine;

    private static List<Variables> listVariablesMachine;


    private ResultReturn codeResult;

    // key = label, value = code index
    private HashMap<String, Integer> labels = new HashMap<>();

    private HashMap<String, Object> vars = new HashMap<>();

    private Set<String> operators = new HashSet<>();


    public VirtualMachine(ResultReturn codeResult, HashMap<String, Integer> labels, HashMap<String, Object> vars) {
        this.codeResult = codeResult;
        this.labels = labels;
        this.vars = vars;
    }


    private VirtualMachine() {
    }

    public static void main(String[] args) throws Throwable {
        String fileName = "src\\main\\java\\compiler\\input\\test";

        VirtualMachine virtualMachine = new VirtualMachine();
        virtualMachine.start(fileName);


    }

    private void createLabels() {
        //storage the index of code labels
        for (int i = 0; i < this.codeResult.getListQuadruple().size(); i++) {

            if (this.codeResult.getListQuadruple().get(i).getOp().equals("label")) {
                this.labels.put(this.codeResult.getListQuadruple().get(i).getArg1(), i);
            }

        }
    }

    private void populateOperators() {
        operators.add("-");
        operators.add("+");
        operators.add("/");
        operators.add("//");
        operators.add("%");
        operators.add("*");
        operators.add("=");
        operators.add(">=");
        operators.add("<=");
        operators.add("<");
        operators.add(">");
        operators.add("==");
        operators.add("!=");
        operators.add("&&");
        operators.add("||");
        operators.add("!");
    }


    private void start(String fileName) throws Throwable {

        LexicalAnalysis.file = new File("/home/renan/IdeaProjects/compiler/src/main/java/compiler/input/test").getCanonicalFile();
        LexicalAnalysis.contentFile = LexicalAnalysis.loadArq(LexicalAnalysis.file.toPath());
        System.out.println(LexicalAnalysis.contentFile);
        this.populateOperators();
        this.codeResult = Parser.parser();
        this.createLabels();
        this.run();
        System.exit(0);
    }


    private void run(Quadruple lista) {

        Quadruple quadruple = lista;

        while (quadruple != null) {

            //int 1 -> float, int 0 -> int
            int type;
            String op = quadruple.op;
            String opquadruple;
            float value;
            String str;

            switch (op) {

                case "=":
                    type = Integer.parseInt(quadruple.arg2);
                    //float
                    if(type == 1){
                        value = Float.parseFloat(quadruple.getArg2());
                        listVariablesMachine.add(new Variables(quadruple.getArg1(), value, 1));
                    }else {
                        value = Integer.parseInt(quadruple.getArg2());
                        listVariablesMachine.add(new Variables(quadruple.getArg1(), value, 0));
                    }

                    break;
                case "==":
                    type = Integer.parseInt(quadruple.arg2);
                    if(quadruple.getArg2().equals(quadruple.getResult())){
                        type = 1;
                    } else {
                        type = 0;
                    }
                    //float
                    if(type == 1){
                        value = quadruple.getArg2().equals(quadruple.getResult());
                        listVariablesMachine.add(new Variables(quadruple.getArg1(), value, 1));
                    }else {
                        value = Integer.parseInt(quadruple.getArg2());
                        listVariablesMachine.add(new Variables(quadruple.getArg1(), value, 0));
                    }
                    break;
                case ">":
                    result = division(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                    break;
                case ">=":
                    result = mod(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                    break;
                case "<":
                    result = mult(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                    break;
                case "<=":
                    result = attribution(op1, op2);
                    break;
                case "!=":
                    result = biggerEqual(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                    break;
                case "!":
                    result = smallerEqual(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                    break;
                case "&&":
                    result = smaller(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                    break;
                case "||":
                    result = bigger(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                    break;
                case "if":
                    result = equals(op1, op2);
                    break;
                case "jump":
                    result = difference(op1, op2);
                    break;
                case "call":
                    result = and(op1, op2);
                    break;
                case "print":
                    result = or(op1, op2);
                    break;
                case "scan":
                    result = not(op1, op2);
                    break;
                case "+":
                    result = not(op1, op2);
                    break;
                case "-":
                    result = not(op1, op2);
                    break;
                case "*":
                    result = not(op1, op2);
                    break;
                case "/":
                    result = not(op1, op2);
                    break;
                case "%":
                    result = not(op1, op2);
                    break;

            }


        }

        // calls: scan, print
        // jumps: if, jump

        //process count
        int pc = 0;

        while (!this.codeResult.getListQuadruple().get(pc).getOp().equals("stop")) {

            //jump labels
            if (this.codeResult.getListQuadruple().get(pc).getOp().equals("label")) {
                pc++;

                //execute jumps
            } else if (this.codeResult.getListQuadruple().get(pc).getOp().matches("if||jump")) {

                String function = this.codeResult.getListQuadruple().get(pc).getOp();

                Object expression = this.codeResult.getListQuadruple().get(pc).getArg1();

                if (vars.containsKey(expression)) {
                    expression = vars.get(expression);
                }

                int newPC = -1;

                if (function.equals("if")) {
                    //function if
                    newPC = ifFunction(expression,
                            this.codeResult.getListQuadruple().get(pc).getArg2(),
                            this.codeResult.getListQuadruple().get(pc).getResult());

                } else {
                    // function jump
                    newPC = jump(expression,
                            this.codeResult.getListQuadruple().get(pc).getArg2(),
                            this.codeResult.getListQuadruple().get(pc).getResult());

                }

                if (newPC != -1) {
                    pc = newPC;
                }

            }

            //execute operators
            else if (operators.contains(this.codeResult.getListQuadruple().get(pc).getOp())) {
                Object op1 = this.codeResult.getListQuadruple().get(pc).getArg2();
                Object op2 = this.codeResult.getListQuadruple().get(pc).getResult();

                String function = this.codeResult.getListQuadruple().get(pc).getOp();

                //verifies if the value of the operators is in some variable, determine the real value of each one
                if (vars.containsKey(op1)) {
                    op1 = vars.get(op1);
                }

                if (vars.containsKey(op2)) {
                    op2 = vars.get(op2);
                }

                Object result = null;
                //operation execute and treatment of results types
                switch (function) {

                    case "-":
                        result = subtraction(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                        break;
                    case "+":
                        result = sum(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                        break;
                    case "/":
                        result = division(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                        break;
                    case "%":
                        result = mod(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                        break;
                    case "*":
                        result = mult(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                        break;
                    case "=":
                        result = attribution(op1, op2);
                        break;
                    case ">=":
                        result = biggerEqual(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                        break;
                    case "<=":
                        result = smallerEqual(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                        break;
                    case "<":
                        result = smaller(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                        break;
                    case ">":
                        result = bigger(Float.parseFloat((String) op1), Float.parseFloat((String) op2));
                        break;
                    case "==":
                        result = equals(op1, op2);
                        break;
                    case "!=":
                        result = difference(op1, op2);
                        break;
                    case "&&":
                        result = and(op1, op2);
                        break;
                    case "||":
                        result = or(op1, op2);
                        break;
                    case "!":
                        result = not(op1, op2);
                        break;

                }

                if (this.vars.containsKey(this.codeResult.getListQuadruple().get(pc).getArg1())) {
                    Object type = this.vars.get(this.codeResult.getListQuadruple().get(pc).getArg1());
                    if (type.equals("0")) {
                        result = Integer.parseInt(String.valueOf(result));
                    }
                }

                this.vars.put(this.codeResult.getListQuadruple().get(pc).getArg1(), result);


            } else if (this.codeResult.getListQuadruple().get(pc).getOp().matches("scan||print")) {

                String function = this.codeResult.getListQuadruple().get(pc).getOp();

                String op1 = this.codeResult.getListQuadruple().get(pc).getArg2();

                if (vars.containsKey(op1)) {
                    op1 = String.valueOf(vars.get(op1));
                }

                String op2 = this.codeResult.getListQuadruple().get(pc).getResult();

                if (vars.containsKey(op2)) {
                    op2 = String.valueOf(vars.get(op2));
                }

                if (function.equals("scan")) {

                    if (this.codeResult.getListQuadruple().get(pc).getArg1() != null) {
                        String type = (String) this.vars.get(this.codeResult.getListQuadruple().get(pc).getArg1());
                        if (type.equals("0")) {
                            this.vars.put(this.codeResult.getListQuadruple().get(pc).getArg1(), Integer.parseInt((String) scan(op1, op2)));
                        } else {
                            this.vars.put(this.codeResult.getListQuadruple().get(pc).getArg1(), Float.parseFloat((String) scan(op1, op2)));

                        }
                    }

                } else {
                    print(op1, op2);

                }


                pc++;

            }

        }


    }


    private List<Quadruple> getLabel(List<Quadruple> aux, String lexeme){
        for (int i = 0; i < aux.size() ; i++) {
            if(aux.get(i).getArg1().equals("label")){
                if(aux.get(i).getArg2().equals(lexeme)){
                    return aux;
                }
            }
        }
        return null;
    }




    private int ifFunction(Object expression, String label1, String label2) {

        if (expression.equals("0")) {
            return labels.get(label2);
        }
        return -1;

    }


    private Integer jump(Object index, String label1, String label2) {
        return labels.get(index);
    }

    private Float sum(Float x, Float y) {
        return x + y;
    }

    private Float subtraction(Float x, Float y) {
        return x - y;
    }

    private Float division(Float x, Float y) {
        return x / y;
    }

    private Float mult(Float x, Float y) {
        return x * y;
    }

    private Float mod(Float x, Float y) {
        return x % y;

    }

    private Object attribution(Object x, Object y) {
        return x;
    }

    private Boolean biggerEqual(Float x, Float y) {
        return x >= y;
    }

    private Boolean smallerEqual(Float x, Float y) {
        return x <= y;
    }

    private Boolean difference(Object x, Object y) {
        return !x.equals(y);
    }

    private Boolean bigger(Float x, Float y) {
        return x > y;
    }

    private Boolean smaller(Float x, Float y) {
        return x < y;
    }

    private Boolean and(Object x, Object y) {
        Boolean bX = true, bY = true;
        if (x.equals("false")) {
            bX = false;
        }
        if (y.equals("false")) {
            bY = false;
        }
        return bX && bY;
    }

    private Boolean or(Object x, Object y) {
        Boolean bX = true, bY = true;
        if (x.equals("false")) {
            bX = false;
        }
        if (y.equals("false")) {
            bY = false;
        }
        return bX || bY;
    }

    private Boolean not(Object x, Object y) {
        Boolean bX = true, bY = true;
        if (x.equals("false")) {
            bX = false;
        }
        if (y.equals("false")) {
            bY = false;
        }
        return !bY;
    }

    private Boolean equals(Object x, Object y) {
        return x.equals(y);
    }


    private Object scan(Object x, Object y) {
        String str = "";
        if (x != null) {
            return x;
        } else {
            return str;
        }

    }

    private String print(Object x, Object y) {
        if (x != null) {
            System.out.print(x);
        }
        if (y != null) {
            System.out.print(y);
        }
        return null;
    }


}
