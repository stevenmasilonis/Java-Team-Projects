import components.queue.Queue;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.statement.Statement;
import components.statement.Statement1;
import components.utilities.Reporter;
import components.utilities.Tokenizer;

/**
 * Layered implementation of secondary methods {@code parse} and
 * {@code parseBlock} for {@code Statement}.
 *
 * @author Steven Masilonis and Aaron Lucas
 *
 */
public final class Statement1Parse1 extends Statement1 {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Converts {@code c} into the corresponding {@code Condition}.
     *
     * @param c
     *            the condition to convert
     * @return the {@code Condition} corresponding to {@code c}
     * @requires [c is a condition string]
     * @ensures parseCondition = [Condition corresponding to c]
     */
    private static Condition parseCondition(String c) {
        assert c != null : "Violation of: c is not null";
        assert Tokenizer
                .isCondition(c) : "Violation of: c is a condition string";
        return Condition.valueOf(c.replace('-', '_').toUpperCase());
    }

    /**
     * Parses an IF or IF_ELSE statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires <pre>
     * [<"IF"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [an if string is a proper prefix of #tokens] then
     *  s = [IF or IF_ELSE Statement corresponding to if string at start of #tokens]  and
     *  #tokens = [if string at start of #tokens] * tokens
     * else
     *  [reports an appropriate error message to the console and terminates client]
     * </pre>
     */
    private static void parseIf(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals("IF") : ""
                + "Violation of: <\"IF\"> is proper prefix of tokens";

        String ifStatement = tokens.dequeue(); //first token must be an if
        Reporter.assertElseFatalError(Tokenizer.isKeyword(ifStatement),
                "No 'if' found.");

        String condition = tokens.dequeue(); //check for condition of if
        Reporter.assertElseFatalError(Tokenizer.isCondition(condition),
                "No valid 'if' condition found");
        Condition cond = parseCondition(condition); //parse our condition

        String then = tokens.dequeue(); //check for then/execute action
        Reporter.assertElseFatalError(then.equals("THEN"), "No 'then' found.");

        Statement firstBlock = s.newInstance(); //proceed to block
        firstBlock.parseBlock(tokens);

        String endOrElse = tokens.dequeue(); //either ends or If Else
        Reporter.assertElseFatalError(
                endOrElse.equals("ELSE") || endOrElse.equals("END"),
                "No 'else' or 'end' keyword found.");

        if (endOrElse.equals("ELSE")) {
            Statement secondBlock = s.newInstance(); //need another block
            secondBlock.parseBlock(tokens);
            s.assembleIfElse(cond, firstBlock, secondBlock);

            /*
             * Process 'END IF' at the end of if statement.
             */
            String end = tokens.dequeue();
            Reporter.assertElseFatalError(end.equals("END"),
                    "No 'end' keyword found.");
            String ifEnd = tokens.dequeue();
            Reporter.assertElseFatalError(Tokenizer.isKeyword(ifEnd),
                    "No 'if' found.");
        } else {
            // means there was no else
            s.assembleIf(cond, firstBlock);
            String ifEnd = tokens.dequeue();
            Reporter.assertElseFatalError(Tokenizer.isKeyword(ifEnd),
                    "No 'if' found.");
        }

    }

    /**
     * Parses a WHILE statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires <pre>
     * [<"WHILE"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [a while string is a proper prefix of #tokens] then
     *  s = [WHILE Statement corresponding to while string at start of #tokens]  and
     *  #tokens = [while string at start of #tokens] * tokens
     * else
     *  [reports an appropriate error message to the console and terminates client]
     * </pre>
     */
    private static void parseWhile(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals("WHILE") : ""
                + "Violation of: <\"WHILE\"> is proper prefix of tokens";

        String whileState = tokens.dequeue(); //first token must be while
        Reporter.assertElseFatalError(Tokenizer.isKeyword(whileState),
                "No 'WHILE' found.");

        String condition = tokens.dequeue(); //check condition
        Reporter.assertElseFatalError(Tokenizer.isCondition(condition),
                "No while condition found.");
        Condition cond = parseCondition(condition); //grab our condition

        String doState = tokens.dequeue(); //check for do (while DO)
        Reporter.assertElseFatalError(doState.equals("DO"), "No 'DO' found.");

        Statement block = s.newInstance(); //proceed for block
        block.parseBlock(tokens);

        String end = tokens.dequeue(); //look for end
        Reporter.assertElseFatalError(end.equals("END"),
                "No 'END' keyword found.");

        //check for final WHILE
        String endWhile = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isKeyword(endWhile),
                "No end 'WHILE' found");

        s.assembleWhile(cond, block);

    }

    /**
     * Parses a CALL statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires [identifier string is a proper prefix of tokens]
     * @ensures <pre>
     * s =
     *   [CALL Statement corresponding to identifier string at start of #tokens]  and
     *  #tokens = [identifier string at start of #tokens] * tokens
     * </pre>
     */
    private static void parseCall(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0
                && Tokenizer.isIdentifier(tokens.front()) : ""
                        + "Violation of: identifier string is proper prefix of tokens";

        String callState = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isIdentifier(callState),
                "This is not an identifier");
        s.assembleCall(callState);

    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Statement1Parse1() {
        super();
    }

    /*
     * Public methods ---------------------------------------------------------
     */

    @Override
    public void parse(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        String kind = tokens.front(); //checks kind or if identifier
        Reporter.assertElseFatalError(
                Tokenizer.isIdentifier(kind) || Tokenizer.isKeyword(kind),
                "Not a keyword or identifier");

        if (kind.equals("IF")) { //can only be these three from here
            parseIf(tokens, this); //in bugsworld language
        } else if (kind.equals("WHILE")) {
            parseWhile(tokens, this);
        } else {
            parseCall(tokens, this);
        }

    }

    @Override
    public void parseBlock(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        Statement block = this.newInstance();
        //we need to account for end statements, else's or end of input
        //to stop the while loop for block
        while (!tokens.front().equals("ELSE") && !tokens.front().equals("END")
                && !tokens.front().equals(Tokenizer.END_OF_INPUT)) {

            this.parse(tokens); //go get statements of block
            block.addToBlock(block.lengthOfBlock(), this); //add them to block
        }
        this.transferFrom(block); //result is in block so it needs transferred

    }

    /*
     * Main test method -------------------------------------------------------
     */

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        /*
         * Get input file name
         */
        out.print("Enter valid BL statement(s) file name: ");
        String fileName = in.nextLine();
        /*
         * Parse input file
         */
        out.println("*** Parsing input file ***");
        Statement s = new Statement1Parse1();
        SimpleReader file = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(file);
        file.close();
        s.parse(tokens); // replace with parseBlock to test other method
        /*
         * Pretty print the statement(s)
         */
        out.println("*** Pretty print of parsed statement(s) ***");
        s.prettyPrint(out, 0);

        in.close();
        out.close();
    }

}
