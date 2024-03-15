package cs2110;

import java.util.Set;

public class Variable implements Expression {
    private String name;

    public Variable(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != Variable.class){
            return false;
        }
        Variable var = (Variable) obj;
        return var.getName().equals(name);
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
        return name;
    }

    @Override
    public String postfixString() {
        return name;
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
