package cs2110;

import java.util.Set;

public class Variable implements Expression {
    private String name;

    public Variable(String name){
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        if(vars.contains(name)){
            return vars.get(name);
        }
        throw new UnboundVariableException("Variable NOT In VarTable.");
    }

    @Override
    public int opCount() {
        return 0;
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
