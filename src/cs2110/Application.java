package cs2110;

import java.util.HashSet;
import java.util.Set;

public class Application implements Expression {
    private UnaryFunction func;
    private Expression argument;

    public Application(UnaryFunction func, Expression  arg){
        this.func = func;
        argument = arg;
    }

    /**
     * Determines whether this Application node is equal to another object. Two Application nodes
     * are considered equal if and only if both their function and argument nodes are equal.
     * This method overrides the `equals` method to perform a deep comparison of the function
     * and argument properties of the Application nodes.
     *
     * @param obj The object to be compared with this Application node.
     * @return true if `obj` is an Application node with a function and argument that are equal to
     *         those of this node; false otherwise. This includes cases where `obj` is not an
     *         Application node or if either the function or argument does not match.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != Application.class){
            return false;
        }
        Application app = (Application) obj;
        boolean pool1 = app.func.equals(func);
        boolean pool2 = app.argument.equals(argument);
        return pool1 && pool2;
    }

    /**
     * Evaluates this Application node within the context of a given set of variable values.
     * The evaluation process involves first evaluating the argument child of this node to
     * obtain a value, and then applying the function associated with this node to that value.
     * The result of this application is returned as the outcome of the evaluation.
     *
     * @param vars The VarTable containing variable-value mappings necessary for the evaluation of
     *             the argument.
     * @return The result of applying the function to the evaluated value of the argument.
     * @throws UnboundVariableException If during the evaluation of the argument, a variable
     * is encountered that is not bound within the provided VarTable, indicating an attempt to
     * use an unbound variable.
     */
    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        double toEval= argument.eval(vars);
        return func.apply(toEval);

//        double child = argument.eval(vars);
//        return func.apply(child);
    }

    /**
     * Computes the number of operations involved in this Application node. In this context,
     * an "operation" is defined as the act of applying its function, which counts as one operation,
     * in addition to the operations counted within the argument child of this node. Therefore, the
     * total operation count for an Application node includes the operation of applying its function
     * plus the operation count of its argument.
     *
     * @return The total number of operations involved in this Application node, which is the sum of
     *         one (for the function application itself) and the operation count of its argument.
     */
    @Override
    public int opCount() {
        return 1 + argument.opCount();
    }

    /**
     * Generates the string representation of this Application node in infix notation. In this
     * notation the Application is represented by concatenating the function's name, followed by the
     * infix representation of its argument enclosed in parentheses.Example: For a function 'sin'
     * applied to an argument represented by "y / 2", this method would return "sin((y / 2))",
     * where the double parentheses indicate the function application on the operation argument.
     *
     * @return A string that represents this Application node in infix notation, following the
     * pattern "[functionName]([argumentInInfix])".
     */
    @Override
    public String infixString() {
        return func.name() + "("+argument.infixString()+")";
    }

    /**
     * Produces the string representation of this Application node in postfix notation. In postfix
     * notation, an Application is represented by first providing the postfix string of its
     * argument, followed by the function's name. This notation places the operator (function) after
     * its operands (arguments). To clearly distinguish function names from variable names,
     * a "()" suffix is appended to the function name.
     *
     * Example: For an application of the 'sin' function to an argument "y / 2", the postfix
     * representation would be "y 2 / sin()", indicating the order of operations to be performed
     * without the need for parentheses.
     *
     * @return A string that represents this Application node in postfix notation, following the
     * pattern "[argumentInPostfix][functionName]()", clearly distinguishing functions
     * from variables.
     */
    @Override
    public String postfixString() {
        return argument.postfixString() + func.name();
    }

    /**
     * Optimizes this Application node based on the current variable-value mappings provided in
     * `vars`. The optimization process first optimizes the argument of this Application.
     * If the optimized argument is a Constant (indicating a fully reduced form), the function is
     * immediately applied to this constant value, resulting in a new Constant node that
     * encapsulates the result. If the argument cannot be reduced to a Constant, the Application is
     * reconstructed with the optimized argument, preserving the structure but with potentially
     * reduced complexity.
     *
     * @param vars The VarTable containing variable-value mappings for optimization purposes.
     * @return An optimized Expression, which may be a Constant if the argument and application
     * can be fully resolved, or a new Application node with an optimized argument otherwise.
     */
    @Override
    public Expression optimize(VarTable vars) {
        Expression optArg = argument.optimize(vars);
        if(optArg instanceof Constant ){
            Constant consArg = (Constant) optArg;
            return new Constant(func.apply(consArg.eval(vars)));
        }
        return new Application(func, optArg);
    }

    /**
     * Determines the dependencies of this Application node, which are derived entirely from its
     * argument. Since Application nodes apply a function to their argument, the dependencies of an
     * Application are equivalent to the dependencies of its argument. This method collects and
     * returns the set of variables upon which the argument depends.
     *
     * @return A Set of String objects representing the variable names on which this Application
     * node's argument depends. This effectively lists all variables involved in the argument
     * expression.
     */
    @Override
    public Set<String> dependencies() {
        return new HashSet<String>(argument.dependencies());
    }
}
