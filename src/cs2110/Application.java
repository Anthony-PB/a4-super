package cs2110;

import java.util.Set;

public class Application implements Expression {
    private UnaryFunction func;
    private Expression argument;

    public Application(UnaryFunction func, Expression  arg){
        this.func = func;
        argument = arg;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        double child = argument.eval(vars);
        return func.apply(child);
    }

    @Override
    public int opCount() {
        return 1 + argument.opCount();
    }

    @Override
    public String infixString() {
        return func.name() + "("+argument.infixString()+")";
    }

    @Override
    public String postfixString() {
        return argument.postfixString() + func.name();
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
