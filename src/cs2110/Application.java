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

    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        double toEval= argument.eval(vars);
        return func.apply(toEval);

//        double child = argument.eval(vars);
//        return func.apply(child);
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
        Expression optArg = argument.optimize(vars);
        if(optArg instanceof Constant ){
            Constant consArg = (Constant) optArg;
            return new Constant(func.apply(consArg.eval(vars)));
        }
        return new Application(func, optArg);
    }

    @Override
    public Set<String> dependencies() {
        return new HashSet<String>(argument.dependencies());
    }
}
