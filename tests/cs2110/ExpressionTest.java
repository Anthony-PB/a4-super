package cs2110;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstantExpressionTest {

    @Test
    @DisplayName("A Constant node should evaluate to its value (regardless of var table)")
    void testEval() throws UnboundVariableException {
        Expression expr = new Constant(1.5);
        assertEquals(1.5, expr.eval(MapVarTable.empty()));
    }


    @Test
    @DisplayName("A Constant node should report that 0 operations are required to evaluate it")
    void testOpCount() {
        Expression expr = new Constant(1.5);
        assertEquals(0, expr.opCount());
    }


    @Test
    @DisplayName("A Constant node should produce an infix representation with just its value (as " +
            "formatted by String.valueOf(double))")
    void testInfix() {
        Expression expr = new Constant(1.5);
        assertEquals("1.5", expr.infixString());

        expr = new Constant(Math.PI);
        assertEquals("3.141592653589793", expr.infixString());
    }

    @Test
    @DisplayName("A Constant node should produce an postfix representation with just its value " +
            "(as formatted by String.valueOf(double))")
    void testPostfix() {
        Expression expr = new Constant(1.5);
        assertEquals("1.5", expr.postfixString());

        expr = new Constant(Math.PI);
        assertEquals("3.141592653589793", expr.postfixString());
    }


    @Test
    @DisplayName("A Constant node should equal itself")
    void testEqualsSelf() {
        Expression expr = new Constant(1.5);
        // Normally `assertEquals()` is preferred, but since we are specifically testing the
        // `equals()` method, we use the more awkward `assertTrue()` to make that call explicit.
        assertTrue(expr.equals(expr));
    }

    @Test
    @DisplayName("A Constant node should equal another Constant node with the same value")
    void testEqualsTrue() {
        Expression expr1 = new Constant(1.5);
        Expression expr2 = new Constant(1.5);
        assertTrue(expr1.equals(expr2));
    }

    @Test
    @DisplayName("A Constant node should not equal another Constant node with a different value")
    void testEqualsFalse() {
        Expression expr1 = new Constant(1.5);
        Expression expr2 = new Constant(2.0);
        assertFalse(expr1.equals(expr2));
    }


    @Test
    @DisplayName("A Constant node does not depend on any variables")
    void testDependencies() {
        Expression expr = new Constant(1.5);
        Set<String> deps = expr.dependencies();
        assertTrue(deps.isEmpty());
    }


    @Test
    @DisplayName("A Constant node should optimize to itself (regardless of var table)")
    void testOptimize() {
        Expression expr = new Constant(1.5);
        Expression opt = expr.optimize(MapVarTable.empty());
        assertEquals(expr, opt);
    }
}

class VariableExpressionTest {

    @Test
    @DisplayName("A Variable node should evaluate to its variable's value when that variable is " +
            "in the var map")
    void testEvalBound() throws UnboundVariableException {
        Expression expr = new Variable("x");
        assertEquals(1.5,expr.eval(MapVarTable.of("x", 1.5)));
        assertEquals(-1.5,expr.eval(MapVarTable.of("x", -1.5)));
        assertEquals(0,expr.eval(MapVarTable.of("x", 0)));
        assertEquals(10,expr.eval(MapVarTable.of("x", 10)));
    }

    @Test
    @DisplayName("A Variable node should throw an UnboundVariableException when evaluated if its " +
            "variable is not in the var map")
    void testEvalUnbound() {
        // They assume that your `Variable` constructor takes its name as an argument.
        Expression expr = new Variable("x");
        assertThrows(UnboundVariableException.class, () -> expr.eval(MapVarTable.empty()));
        Expression expr1 = new Variable("z");
        assertThrows(UnboundVariableException.class, () -> expr1.eval(MapVarTable.empty()));
        assertThrows(UnboundVariableException.class, () -> expr.eval(MapVarTable.empty()));
    }


    @Test
    @DisplayName("A Variable node should report that 0 operations are required to evaluate it")
    void testOpCount() {
        Expression expr = new Variable("x");
        assertEquals(0, expr.opCount());
    }


    @Test
    @DisplayName("A Variable node should produce an infix representation with just its name")
    void testInfix() {
        Expression expr = new Variable("x");
        Variable var = (Variable) expr;
        assertEquals(var.getName(),expr.infixString());
    }

    @Test
    @DisplayName("A Variable node should produce an postfix representation with just its name")
    void testPostfix() {
        Expression expr = new Variable("x");
        Variable var = (Variable) expr;
        assertEquals(var.getName(),expr.postfixString());
    }


    @Test
    @DisplayName("A Variable node should equal itself")
    void testEqualsSelf() {
        Expression expr = new Variable("x");
        assertTrue(expr.equals(expr));
        Expression expr1 = new Variable("z");
        assertTrue(expr1.equals(expr1));
    }

    @Test
    @DisplayName("A Variable node should equal another Variable node with the same name")
    void testEqualsTrue() {
        // Force construction of new String objects to detect inadvertent use of `==`
        Expression expr1 = new Variable(new String("x"));
        Expression expr2 = new Variable(new String("x"));
        assertTrue(expr1.equals(expr2));
    }

    @Test
    @DisplayName("A Variable node should not equal another Variable node with a different name")
    void testEqualsFalse() {
        Expression expr = new Variable("x");
        Expression expr1 = new Variable("z");
        assertFalse(expr.equals(expr1));
    }


    @Test
    @DisplayName("A Variable node only depends on its name")
    void testDependencies() {
        Expression expr = new Variable("x");
        Set<String> deps = expr.dependencies();
        assertTrue(deps.contains("x"));
        assertEquals(1, deps.size());
        Expression expr1 = new Variable("z");
        Set<String> deps1 = expr1.dependencies();
        assertTrue(deps1.contains("z"));
        assertEquals(1, deps1.size());
    }


    @Test
    @DisplayName("A Variable node should optimize to a Constant if its variable is in the var map")
    void testOptimizeBound() {
        Expression expr = new Variable("x");
        Expression opt = expr.optimize(MapVarTable.of("x", 1.5));
        assertEquals(new Constant(1.5), opt);
        Expression expr1 = new Variable("z");
        Expression opt1 = expr1.optimize(MapVarTable.of("z", 5.5));
        assertEquals(new Constant(5.5), opt1);
    }

    @Test
    @DisplayName("A Variable node should optimize to itself if its variable is not in the var map")
    void testOptimizeUnbound() {
        Expression expr = new Variable("x");
        Expression opt = expr.optimize(MapVarTable.of("z", 1.5));
        assertEquals(expr, opt);
        Expression expr1 = new Variable("z");
        Expression opt1 = expr1.optimize(MapVarTable.of("x", 1.5));
        assertEquals(expr1, opt1);
    }
}

class OperationExpressionTest {

    @Test
    @DisplayName("An Operation node for ADD with two Constant operands should evaluate to their " +
            "sum")
    void testEvalAdd() throws UnboundVariableException {
        Expression expr = new Operation(Operator.ADD, new Constant(1.5), new Constant(2));
        assertEquals(3.5, expr.eval(MapVarTable.empty()));
    }

    @Test
    @DisplayName("An Operation node for ADD with a Variable for an operand should evaluate " +
            "to its operands' sum when the variable is in the var map")
    void testEvalAddBound() throws UnboundVariableException {
        Expression var1 = new Variable("x");
        Expression var2 = new Variable("y");

        Expression expr1 = new Operation(Operator.ADD, var1, new Constant(2));
        MapVarTable map = MapVarTable.of("x", 1.5,"y", 2.5);
        Expression expr2 = new Operation(Operator.ADD, var1, var2);

        assertEquals(3.5, expr1.eval(map));
        assertEquals(4.0, expr2.eval(map));
    }

    @Test
    @DisplayName("An Operation node for ADD with a Variable for an operand should throw an " +
            "UnboundVariableException when evaluated if the variable is not in the var map")
    void testEvalAddUnbound() {
        Expression var1 = new Variable("z");
        MapVarTable map = MapVarTable.of("x", 1.5,"y", 2.5);
        Expression expr = new Operation(Operator.ADD, var1, new Constant(2));
        assertThrows(UnboundVariableException.class, () -> expr.eval(map));
    }


    @Test
    @DisplayName("An Operation node with leaf operands should report that 1 operation is " +
            "required to evaluate it")
    void testOpCountLeaves() {
        Expression expr = new Operation(Operator.ADD, new Constant(5), new Constant(2));
        assertEquals(1, expr.opCount());
    }


    @Test
    @DisplayName("An Operation node with an Operation for either or both operands should report " +
            "the correct number of operations to evaluate it")
    void testOpCountRecursive() {
        Expression expr = new Operation(Operator.ADD,
                new Operation(Operator.MULTIPLY, new Constant(1.5), new Variable("x")),
                new Constant(2.0));
        assertEquals(2, expr.opCount());

        expr = new Operation(Operator.SUBTRACT,
                new Operation(Operator.MULTIPLY, new Constant(1.5), new Variable("x")),
                new Operation(Operator.DIVIDE, new Constant(1.5), new Variable("x")));
        assertEquals(3, expr.opCount());
    }


    @Test
    @DisplayName("An Operation node with leaf operands should produce an infix representation " +
            "consisting of its first operand, its operator symbol surrounded by spaces, and " +
            "its second operand, all enclosed in parentheses")
    void testInfixLeaves() {
        Expression expr = new Operation(Operator.DIVIDE, new Variable("x"), new Constant(10));
        assertEquals("(x / 10.0)", expr.infixString());

        expr = new Operation(Operator.MULTIPLY, new Variable("z"), new Constant(10));
        assertEquals("(z * 10.0)", expr.infixString());

        expr = new Operation(Operator.ADD, new Variable("y"), new Constant(10));
        assertEquals("(y + 10.0)", expr.infixString());

        expr = new Operation(Operator.SUBTRACT, new Variable("x"), new Constant(10));
        assertEquals("(x - 10.0)", expr.infixString());
    }

    @Test
    @DisplayName("An Operation node with an Operation for either operand should produce the " +
            "expected infix representation with parentheses around each operation")
    void testInfixRecursive() {
        Expression expr = new Operation(Operator.ADD,
                new Operation(Operator.MULTIPLY, new Constant(1.5), new Variable("x")),
                new Constant(2.0));
        assertEquals("((1.5 * x) + 2.0)", expr.infixString());

        expr = new Operation(Operator.SUBTRACT,
                new Constant(2.0),
                new Operation(Operator.DIVIDE, new Constant(1.5), new Variable("x")));
        assertEquals("(2.0 - (1.5 / x))", expr.infixString());
    }


    @Test
    @DisplayName("An Operation node with leaf operands should produce a postfix representation " +
            "consisting of its first operand, its second operand, and its operator symbol " +
            "separated by spaces")
    void testPostfixLeaves() {
        Expression expr = new Operation(Operator.DIVIDE, new Variable("x"), new Constant(10));
        assertEquals("((x 10.0) /)", expr.postfixString());

        expr = new Operation(Operator.MULTIPLY, new Variable("z"), new Constant(10));
        assertEquals("((z 10.0) *)", expr.postfixString());

        expr = new Operation(Operator.ADD, new Variable("y"), new Constant(10));
        assertEquals("((y 10.0) +)", expr.postfixString());

        expr = new Operation(Operator.SUBTRACT, new Variable("x"), new Constant(10));
        assertEquals("((x 10.0) -)", expr.postfixString());
    }

    @Test
    @DisplayName("An Operation node with an Operation for either operand should produce the " +
            "expected postfix representation")
    void testPostfixRecursive() {
        Expression expr = new Operation(Operator.ADD,
                new Operation(Operator.MULTIPLY, new Constant(1.5), new Variable("x")),
                new Constant(2.0));
        assertEquals("((((1.5 x) *) 2.0) +)", expr.postfixString());

        expr = new Operation(Operator.SUBTRACT,
                new Constant(2.0),
                new Operation(Operator.DIVIDE, new Constant(1.5), new Variable("x")));
        //((2.0 (1.5 x /) -)
        assertEquals("((2.0 ((1.5 x) /)) -)", expr.postfixString());
    }


    @Test
    @DisplayName("An Operation node should equal itself")
    void testEqualsSelf() {
        Expression expr = new Operation(Operator.ADD, new Constant(1.5), new Variable("x"));
        assertTrue(expr.equals(expr));
        Expression expr1 = new Operation(Operator.ADD, new Constant(5.5), new Variable("z"));
        assertTrue(expr1.equals(expr1));
    }

    @Test
    @DisplayName("An Operation node should equal another Operation node with the same " +
            "operator and operands")
    void testEqualsTrue() {
        Expression expr1 = new Operation(Operator.ADD, new Constant(1.5), new Variable("x"));
        Expression expr2 = new Operation(Operator.ADD, new Constant(1.5), new Variable("x"));
        assertTrue(expr1.equals(expr2));
    }

    @Test
    @DisplayName("An Operation node should not equal another Operation node with a different " +
            "operator")
    void testEqualsFalse() {
        Expression expr1 = new Operation(Operator.ADD, new Constant(2.5), new Variable("z"));
        Expression expr2 = new Operation(Operator.ADD, new Constant(5.5), new Variable("z"));
        assertFalse(expr1.equals(expr2));
    }


    @Test
    @DisplayName("An Operation node depends on the dependencies of both of its operands")
    void testDependencies() {
        Expression expr = new Operation(Operator.ADD, new Variable("x"), new Variable("y"));
        Set<String> deps = expr.dependencies();
        assertTrue(deps.contains("x"));
        assertTrue(deps.contains("y"));
        assertEquals(2, deps.size());
    }


    @Test
    @DisplayName("An Operation node for ADD with two Constant operands should optimize to a " +
            "Constant containing their sum")
    void testOptimizeAdd() {
        Expression expr1 = new Operation(Operator.ADD, new Variable("x"), new Variable("y"));
        assertEquals(expr1, expr1.optimize(MapVarTable.empty()));

        Expression var1 = new Variable("z");
        Expression var2 = new Variable("y");
        MapVarTable map = MapVarTable.of("z", 1.5, "y", 10.0);
        Expression expr2 = new Operation(Operator.ADD, var1, var2);
        Expression const1 = new Constant(11.5);
        assertEquals(const1, expr2.optimize(map));

    }
}

class ApplicationExpressionTest {

    @Test
    @DisplayName("An Application node for SQRT with a Constant argument should evaluate to its " +
            "square root")
    void testEvalSqrt() throws UnboundVariableException {
        Expression expr = new Application(UnaryFunction.SQRT, new Constant(4));
        assertEquals(2.0, expr.eval(MapVarTable.empty()));
    }

    @Test
    @DisplayName("An Application node with a Variable for its argument should throw an " +
            "UnboundVariableException when evaluated if the variable is not in the var map")
    void testEvalAbsUnbound() {
        Expression expr = new Application(UnaryFunction.SQRT, new Variable("x"));
        assertThrows(UnboundVariableException.class, () -> expr.eval(MapVarTable.empty()));
        Expression expr1 = new Application(UnaryFunction.COS, new Variable("z"));
        assertThrows(UnboundVariableException.class, () -> expr1.eval(MapVarTable.empty()));
    }


    @Test
    @DisplayName("An Application node with a leaf argument should report that 1 operation is " +
            "required to evaluate it")
    void testOpCountLeaf() {
        Expression expr = new Application(UnaryFunction.SQRT, new Variable("x"));
        assertEquals(1, expr.opCount());
        expr = new Application(UnaryFunction.EXP, new Constant(2));
        assertEquals(1, expr.opCount());
    }


    @Test
    @DisplayName("An Application node with non-leaf expressions for its argument should report " +
            "the correct number of operations to evaluate it")
    void testOpCountRecursive() {
        Expression expr = new Application(UnaryFunction.SQRT,
                new Operation(Operator.MULTIPLY, new Constant(1.5), new Variable("x")));
        assertEquals(2, expr.opCount());
    }


    @Test
    @DisplayName(
            "An Application node with a leaf argument should produce an infix representation " +
                    "consisting of its name, followed by the argument enclosed in parentheses.")
    void testInfixLeaves() {
        Expression expr = new Application(UnaryFunction.SQRT, new Constant(4));
        assertEquals("sqrt(4.0)", expr.infixString());
        Expression expr1 = new Application(UnaryFunction.SQRT, new Constant(16));
        assertEquals("sqrt(16.0)", expr1.infixString());
    }

    @Test
    @DisplayName("An Application node with an Operation for its argument should produce the " +
            "expected infix representation with redundant parentheses around the argument")
    void testInfixRecursive() {
        Expression expr = new Application(UnaryFunction.ABS,
                new Operation(Operator.MULTIPLY, new Constant(1.5), new Variable("x")));
        assertEquals("abs((1.5 * x))", expr.infixString());
    }


    @Test
    @DisplayName("An Application node with a leaf argument should produce a postfix " +
            "representation consisting of its argument, followed by a space, followed by its " +
            "function's name appended with parentheses")
    void testPostfixLeaves() {
        Expression expr = new Application(UnaryFunction.SQRT, new Constant(4));
        assertEquals("4.0 sqrt", expr.postfixString());
        Expression expr1 = new Application(UnaryFunction.SQRT, new Constant(16));
        assertEquals("16.0 sqrt", expr1.postfixString());
    }

    @Test
    @DisplayName("An Application node with an Operation for its argument should produce the " +
            "expected postfix representation")
    void testPostfixRecursive() {
        Expression expr = new Application(UnaryFunction.SQRT, new Operation(Operator.MULTIPLY,
                new Constant(10), new Constant(25)));
        assertEquals("((10.0 25.0) *) sqrt", expr.postfixString());
    }

    @Test
    @DisplayName("An Application node should equal itself")
    void testEqualsSelf() {
        Expression expr = new Application(UnaryFunction.SQRT, new Constant(4.0));
        assertTrue(expr.equals(expr));
    }

    @Test
    @DisplayName("An Application node should equal another Application node with the same " +
            "function and argument")
    void testEqualsTrue() {
        Expression expr1 = new Application(UnaryFunction.SQRT, new Constant(4.0));
        Expression expr2 = new Application(UnaryFunction.SQRT, new Constant(4.0));
        assertTrue(expr1.equals(expr2));
    }

    @Test
    @DisplayName("An Application node should not equal another Application node with a different " +
            "argument")
    void testEqualsFalseArg() {
        Expression expr1 = new Application(UnaryFunction.SQRT, new Constant(4.0));
        Expression expr2 = new Application(UnaryFunction.SQRT, new Constant(5.0));
        assertFalse(expr1.equals(expr2));

        Expression expr3 = new Application(UnaryFunction.COS, new Constant(4.0));
        Expression expr4 = new Application(UnaryFunction.SQRT, new Constant(4.0));
        assertFalse(expr3.equals(expr4));
    }

    @Test
    @DisplayName("An Application node should not equal another Application node with a different " +
            "function")
    void testEqualsFalseFunc() {
        Expression expr1 = new Application(UnaryFunction.SQRT, new Constant(4.0));
        Expression expr2 = new Application(UnaryFunction.ABS, new Constant(4.0));
        assertFalse(expr1.equals(expr2));
    }


    @Test
    @DisplayName("An Application node has the same dependencies as its argument")
    void testDependencies() {
        Expression arg = new Variable("x");
        Expression expr = new Application(UnaryFunction.SQRT, arg);
        Set<String> argDeps = arg.dependencies();
        Set<String> exprDeps = expr.dependencies();
        assertEquals(argDeps, exprDeps);
    }


    @Test
    @DisplayName("An Application node for SQRT with a Constant argument should optimize to a " +
            "Constant containing its square root")
    void testOptimizeConstant() {
        Expression expr = new Application(UnaryFunction.SQRT, new Constant(4.0));
        assertEquals(new Constant(2.0), expr.optimize(MapVarTable.empty()));

        Expression expr1 = new Application(UnaryFunction.SQRT, new Constant(16.0));
        assertEquals(new Constant(4.0), expr1.optimize(MapVarTable.empty()));
    }


    @Test
    @DisplayName("An Application node with an argument depending on a variable should optimize " +
            "to an Application node if the variable is unbound")
    void testOptimizeUnbound() {
        Expression expr = new Application(UnaryFunction.SQRT, new Variable("x"));
        Expression opt = expr.optimize(MapVarTable.empty());
        assertInstanceOf(Application.class, opt);
    }

}
