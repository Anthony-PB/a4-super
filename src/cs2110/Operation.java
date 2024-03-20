package cs2110;

import java.util.HashSet;
import java.util.Set;

public class Operation implements Expression {
    private Operator op;
    private Expression leftOperand;
    private Expression rightOperand;

    public Operation(Operator op, Expression left, Expression right){
        this.op = op;
        leftOperand = left;
        rightOperand = right;
    }

    /**
     * Checks if this Operation node is equal to another object. Equality is defined based on
     * the equality of both the operator and operands. An Operation node is considered equal to
     * another object if and only if the object is also an Operation node, and they share the same
     * operator as well as equivalent left and right operands. This method ensures a deep equality
     * check, considering the structural and functional aspects of the Operation nodes.
     *
     * @param obj The object to compare with this Operation node for equality.
     * @return true if `obj` is an Operation node with the same operator and equivalent operands as
     * this node; false otherwise, including when `obj` is not an Operation node or when there is a
     * discrepancy in the operator or either operand.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != Operation.class){
            return false;
        }
        Operation ops = (Operation) obj;
        boolean pool1 = ops.leftOperand.equals(leftOperand) &&
                ops.rightOperand.equals(rightOperand);
        boolean pool2 = ops.op.equals(op);
        return pool1 && pool2;
    }

    /**
     * Evaluates this Operation node by first evaluating its left and right operand children within
     * the context of the provided variable-value mappings (`VarTable`). After obtaining the values
     * of both operands, it applies the operation defined by this node's operator to these values
     * and returns the result.
     *
     * @param vars The VarTable containing variable-value mappings necessary for the evaluation of
     *             the operands.
     * @return The result of applying this node's operator to the evaluated values of its left and
     * right operands.
     * @throws UnboundVariableException If during the evaluation of either operand, a variable is
     * encountered that is not bound within the provided VarTable, indicating an attempt to use an
     * unbound variable.
     */
    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        double child1 = leftOperand.eval(vars);
        double child2 = rightOperand.eval(vars);
        return op.operate(child1, child2);

//        return op.operate(leftOperand.eval(vars), rightOperand.eval(vars));
    }

    /**
     * Calculates the total number of operations within this Operation node, including itself and
     * any operations present in its left and right operands. This method acknowledges each distinct
     * operation, including the operation executed by this node, as contributing to the overall
     * operation count. This approach quantifies the complexity of the expression tree rooted at
     * this node by tallying the individual operations required to compute the expression's value.
     *
     * @return The total count of operations involved in this Operation node and recursively
     * within its operands, with the operation represented by this node itself counted as one.
     */
    @Override
    public int opCount() {
        return 1 + leftOperand.opCount() + rightOperand.opCount();
    }

    /**
     * Generates the string representation of this Operation node in infix notation. In infix notation,
     * the operation is represented by enclosing the entire expression in parentheses, with operands
     * separated by the operator symbol and spaces. This method constructs the infix representation
     * by recursively obtaining the infix strings of the left and right operands, placing the
     * operator symbol between them with spaces, and enclosing the result in parentheses.
     *
     * Example: The mathematical expression "((2 * y) + 1) ^ 3" is an example of how this method
     * represents an operation in infix notation.
     *
     * @return A string that represents the infix notation of the expression encapsulated by this
     * Operation node.
     */
    @Override
    public String infixString() {
        return "("+leftOperand.infixString() + " "
                +op.symbol()+ " " + rightOperand.infixString()+")";
    }

    /**
     * Produces the string representation of this Operation node in postfix notation. Postfix
     * notation, or reverse Polish notation, places operators after their operands and does not
     * require parentheses to denote order of operations. This method constructs the postfix
     * representation by performing a postorder traversal of the expression tree, appending the
     * operator symbol after the operands. The entire expression is enclosed in parentheses for
     * clarity, though in practice, postfix notation naturally defines the order of operations
     * without them.
     *
     * Example: For the infix expression "((2 * y) + 1) ^ 3", its postfix representation would be
     * "2 y * 1 + 3 ^".
     *
     * @return A string that encapsulates the postfix notation of the expression represented by this
     * Operation node.
     */
    @Override
    public String postfixString() {
        return "((" + leftOperand.postfixString() + " " + rightOperand.postfixString() + ") "
                + op.symbol() + ")";
    }

    /**
     * Optimizes the expression represented by this Operation node. If both operands of the
     * operation can be evaluated to constants, the operation is fully optimized by performing the
     * operation and returning a new Constant node with the result. If one or both operands depend
     * on variables not present in `vars`, each operand is optimized as far as possible, and the
     * operation is reconstructed with these optimized operands, potentially simplifying the
     * expression but not reducing it to a single constant.
     *
     * @param vars The VarTable containing variable-value mappings for optimization.
     * @return A Constant node if both operands can be fully evaluated; otherwise, a new Operation
     * node with optimized operands.
     */
    @Override
    public Expression optimize(VarTable vars) {
        Expression optLeft = leftOperand.optimize(vars);
        Expression optRight = rightOperand.optimize(vars);

        if (optLeft instanceof Constant && optRight instanceof Constant) {
            Constant constLeft = (Constant) optLeft;
            Constant constRight = (Constant) optRight;
            //Was thinking about casting on this one line but that would have been hard to read
            //Therefore, ignore the yellow squiggly lines
            double result = op.operate(constLeft.eval(vars), constRight.eval(vars));
            return new Constant(result);
        }

        // If at least one operand is not a constant, return a new Operation node
        //optLeft and optRight are as optimized as possible
        return new Operation(op, optLeft, optRight);
    }

    /**
     * Determines the set of dependencies for this Operation node, which consists of the union of
     * dependencies from both its left and right operands. This reflects that the operation depends
     * on all unique variables required by either operand. This method collects and combines the
     * dependencies from each operand to identify all variables upon which the Operation's
     * evaluation could depend.
     * @return A Set containing the names of all variables that are dependencies for this Operation
     * node, derived from both operands.
     */
    @Override
    public Set<String> dependencies() {
        Set<String> dep = new HashSet<String>();
        dep.addAll(leftOperand.dependencies());
        dep.addAll(rightOperand.dependencies());
        return dep;
    }
}
