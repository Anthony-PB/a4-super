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

    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        double child1 = leftOperand.eval(vars);
        double child2 = rightOperand.eval(vars);
        return op.operate(child1, child2);

//        return op.operate(leftOperand.eval(vars), rightOperand.eval(vars));
    }

    @Override
    public int opCount() {
        return 1 + leftOperand.opCount() + rightOperand.opCount();
    }

    @Override
    public String infixString() {
        return "("+leftOperand.infixString() + " " +op.symbol()+ " " + rightOperand.infixString()+")";
    }

    @Override
    public String postfixString() {
        return "((" + leftOperand.postfixString() + " " + rightOperand.postfixString() + ") "
                + op.symbol() + ")";
    }

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

    @Override
    public Set<String> dependencies() {
        Set<String> dep = new HashSet<String>();
        dep.addAll(leftOperand.dependencies());
        dep.addAll(rightOperand.dependencies());
        return dep;
    }
}
