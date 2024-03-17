package cs2110;

import java.util.HashSet;
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
        if (vars.contains(name)) {
            try {
                double tabVal = vars.get(name);
                return new Constant(tabVal); //"optimizes to a Constant"
            } catch (UnboundVariableException e) {
                return this;
            }
        } else {
            return this;
        }
    }

    @Override
    public Set<String> dependencies() {
        Set<String> dep;
        dep = new HashSet<String>();
        dep.add(name);
        return dep;
    }
}
