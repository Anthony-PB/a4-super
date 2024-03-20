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

    /**
     * Compares this variable node to another object to determine equality. Two variable nodes
     * are considered equal if they represent the same variable name. This method overrides
     * the `equals` method to specifically compare variable names, adhering to the principle
     * that variable nodes, being leaf nodes, are equal if they have identical names.
     *
     * @param obj The object to compare with this variable node.
     * @return true if `obj` is a variable node representing the same variable name as this node;
     *         false otherwise. This includes cases where `obj` is not a variable node.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != Variable.class){
            return false;
        }
        Variable var = (Variable) obj;
        return var.getName().equals(name);
    }

    /**
     * Evaluates the expression represented by this variable node. It substitutes the variable
     * with its corresponding value from the provided `VarTable` instance.
     *
     * @param vars The VarTable containing variable-value mappings necessary for evaluation.
     * @return The value of the variable from `vars` if the variable is present.
     * @throws UnboundVariableException If the variable is not present in `vars`, indicating
     *         that the variable's value is unbound and cannot be evaluated.
     */
    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        if(vars.contains(name)){
            return vars.get(name);
        }
        throw new UnboundVariableException("Variable NOT In VarTable.");
    }
    /**
     * Calculates the number of operations in the expression represented by this node.
     * For variable nodes, which are represented by their names and do not involve any
     * mathematical operations, this method always returns 0 as they are considered leaf nodes
     * without any operations.
     *
     * @return An integer value of 0, indicating no operations are present in a variable node.
     */
    @Override
    public int opCount() {
        return 0;
    }

    /**
     * Generates a string representation of this variable node in infix notation. Since variable
     * nodes are represented by their names and do not involve any operations, this method returns
     * the variable's name directly as variables are considered leaf nodes and are simply
     * identified by their names.
     *
     * @return The name of the variable as a string,representing the variable node in infix notation
     */
    @Override
    public String infixString() {
        return name;
    }

    /**
     * Generates a string representation of this variable node in postfix notation.
     * Since variable nodes are represented by their names and do not involve any operations,
     * this method returns the variable's name directly as variables are considered leaf nodes
     * and are simply identified by their names.
     * @return The name of the variable as a string,representing the variable node in infix notation
     */
    @Override
    public String postfixString() {
        return name;
    }

    /**
     * optimizes this variable expression based on its value in the provided `VarTable`.
     * If the variable has an assigned value in `vars`, the method optimizes the variable to a
     * `Constant` expression with the same value. If the variable's value is not specified in
     * `vars`, the variable cannot be optimized and thus the method returns the variable
     * expression itself.
     *
     * @param vars The VarTable containing variable-value mappings that may be used for optimization
     * @return A `Constant` expression containing the value of the variable if it is defined in
     * `vars`; otherwise, returns this variable expression unmodified.
     */
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
    /**
     * Determines the set of dependencies for this variable node. Since a variable node depends
     * only on itself, this method returns a set containing just the variable's name. This
     * implementation aligns with the requirement that interior nodes calculate their dependencies
     * based on the union of their children's dependencies, whereas variable nodes, being leaf
     * nodes, depend solely on themselves.
     *
     * @return A Set containing a single element: the name of this variable, indicating its
     *         dependency on itself.
     */
    @Override
    public Set<String> dependencies() {
        Set<String> dep;
        dep = new HashSet<String>();
        dep.add(name);
        return dep;
    }
}
