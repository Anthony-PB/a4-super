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
        return super.equals(obj);
    }

    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        return op.operate(leftOperand.eval(vars), rightOperand.eval(vars));
    }

    @Override
    public int opCount() {
        return 1;
    }

    @Override
    public String infixString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String postfixString() {
        throw new UnsupportedOperationException();
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
