package cs2110;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.NoSuchElementException;

public class RpnParser {

    /**
     * Parse the RPN expression in `exprString` and return the corresponding expression tree. Tokens
     * must be separated by whitespace.  Valid tokens include decimal numbers (scientific notation
     * allowed), arithmetic operators (+, -, *, /, ^), function names (with the suffix "()"), and
     * variable names (anything else).  When a function name is encountered, the corresponding
     * function will be retrieved from `funcDefs` using the name (without "()" suffix) as the key.
     *
     * @throws IncompleteRpnException     if the expression has too few or too many operands
     *                                    relative to operators and functions.
     * @throws UndefinedFunctionException if a function name applied in `exprString` is not present
     *                                    in `funcDefs`.
     */
    public static Expression parse(String exprString, Map<String, UnaryFunction> funcDefs)
            throws IncompleteRpnException, UndefinedFunctionException {
        assert exprString != null;
        assert funcDefs != null;

        // Each token will result in a subexpression being pushed onto this stack.  If the
        // subexpression requires arguments, they are first popped off of this stack.
        Deque<Expression> stack = new ArrayDeque<>();

        // Loop over each token in the expression string from left to right
        for (Token token : Token.tokenizer(exprString)) {
            // TODO: Based on the dynamic type of the token, create the appropriate Expression node
            // and push it onto the stack, popping arguments as needed.
            // The "number" token is done for you as an example.

            if (token instanceof Token.Operator && stack.peek()!= null){
                Token.Operator opToken = (Token.Operator) token;
                Operator op = opToken.opValue();
                Expression operand2 = stack.pop();
                Expression operand1 = stack.pop();
                Operation interNode = new Operation(op, operand1, operand2);
                stack.push(interNode);
            }

            if (token instanceof Token.Variable){
                Token.Variable numVariable = (Token.Variable) token;
                String variableName = numVariable.value();
                stack.push(new Variable(variableName));
            }

            if (token instanceof Token.Function && stack.peek() != null){
               Expression newToken = (Expression) token;
               Expression arg = stack.pop();
               Expression func = stack.pop();
//               Application newFunc = (Application) newToken;
               Application newFunc = new Application(func, arg);
               stack.push(newFunc);
            }

            if (token instanceof Token.Number) {
                Token.Number numToken = (Token.Number) token;
                stack.push(new Constant(numToken.doubleValue()));
            }
        }

        //ANTHONYYYYYYYY: I did not do this part: 3. Assuming the expression is valid, there will be one node left on the stack. This is the root of the expression tree.//

        // TODO: Return the overall expression node.  (This might also be a good place to check that
        // the string really did correspond to a single expression.)
        throw new UnsupportedOperationException();
    }
}
