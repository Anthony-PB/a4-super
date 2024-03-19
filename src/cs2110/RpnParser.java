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

            //LGTM!
            if (token instanceof Token.Operator){
                Token.Operator opToken = (Token.Operator) token;
                Operator op = opToken.opValue();
                if (stack.size() < 2) {
                    throw new IncompleteRpnException(opToken.value(), stack.size());
                }
                Expression operand2 = stack.pop();
                Expression operand1 = stack.pop();
                Operation interNode = new Operation(op, operand1, operand2);
                stack.push(interNode);
            }

            //LGTM!
            if (token instanceof Token.Variable){
                Token.Variable numVariable = (Token.Variable) token;
                String variableName = numVariable.value();
                stack.push(new Variable(variableName));
            }

            //
            if (token instanceof Token.Function){
                Token.Function newToken = (Token.Function) token;
                String funcName = newToken.name();
                if (!funcDefs.containsKey(funcName)) {
                    throw new UndefinedFunctionException(funcName);
                }
                UnaryFunction func = funcDefs.get(funcName);
                if (stack.isEmpty()) {
                    throw new IncompleteRpnException(funcName, 0);
                }
                Expression arg = stack.pop();
                Application newFunc = new Application(func, arg);
                stack.push(newFunc);
            }

            if (token instanceof Token.Number) {
                Token.Number numToken = (Token.Number) token;
                stack.push(new Constant(numToken.doubleValue()));
            }
        }

        if (stack.size() != 1) {
            throw new IncompleteRpnException(exprString, stack.size());
        }

        return stack.pop();
    }
}
