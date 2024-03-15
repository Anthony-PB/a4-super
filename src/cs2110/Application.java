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
