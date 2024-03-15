package cs2110;

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
        return op.operate(leftOperand.eval(vars), rightOperand.eval(vars));
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
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> dependencies() {
        throw new UnsupportedOperationException();
    }
}
