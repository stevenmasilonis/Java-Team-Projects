import components.map.Map;
import components.program.Program;
import components.program.Program1;
import components.queue.Queue;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.statement.Statement;
import components.utilities.Reporter;
import components.utilities.Tokenizer;

/**
 * Layered implementation of secondary method {@code parse} for {@code Program}.
 *
 * @author Steven Masilonis and Aaron Lucas
 *
 */
public final class Program1Parse1 extends Program1 {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Parses a single BL instruction from {@code tokens} returning the
     * instruction name as the value of the function and the body of the
     * instruction in {@code body}.
     *
     * @param tokens
     *            the input tokens
     * @param body
     *            the instruction body
     * @return the instruction name
     * @replaces body
     * @updates tokens
     * @requires <pre>
     * [<"INSTRUCTION"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [an instruction string is a proper prefix of #tokens]  and
     *    [the beginning name of this instruction equals its ending name]  and
     *    [the name of this instruction does not equal the name of a primitive
     *     instruction in the BL language] then
     *  parseInstruction = [name of instruction at start of #tokens]  and
     *  body = [Statement corresponding to the block string that is the body of
     *          the instruction string at start of #tokens]  and
     *  #tokens = [instruction string at start of #tokens] * tokens
     * else
     *  [report an appropriate error message to the console and terminate client]
     * </pre>
     */
    private static String parseInstruction(Queue<String> tokens,
            Statement body) {
        assert tokens != null : "Violation of: tokens is not null";
        assert body != null : "Violation of: body is not null";
        assert tokens.length() > 0 && tokens.front().equals("INSTRUCTION") : ""
                + "Violation of: <\"INSTRUCTION\"> is proper prefix of tokens";

        tokens.dequeue(); //first token is instruction

        String identifier = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isIdentifier(identifier),
                "The instruction name must be identifier.");

        String is = tokens.dequeue(); //taking out is
        Reporter.assertElseFatalError(is.equals("IS"),
                "'IS' must come after the program name.");

        body.parseBlock(tokens); //rest of code is ready to parse

        String end = tokens.dequeue(); //after code parsed reach end
        Reporter.assertElseFatalError(end.equals("END"),
                "END keyword was not found");

        String endName = tokens.dequeue(); //instruction name at end
        Reporter.assertElseFatalError(endName.equals(identifier),
                "The identifier at the end of the program must be the same "
                        + "as the identifier at the beginning of the program.");
        return identifier;
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Program1Parse1() {
        super();
    }

    /*
     * Public methods ---------------------------------------------------------
     */

    @Override
    public void parse(SimpleReader in) {
        assert in != null : "Violation of: in is not null";
        assert in.isOpen() : "Violation of: in.is_open";
        Queue<String> tokens = Tokenizer.tokens(in);
        this.parse(tokens);
    }

    @Override
    public void parse(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        Map<String, Statement> map = this.newContext(); //used to hold instr's

        String prog = tokens.dequeue(); //grabs program
        Reporter.assertElseFatalError(prog.equals("PROGRAM"),
                "'PROGRAM' must be at the beginning");

        String name = tokens.dequeue(); //grabs name of program
        Reporter.assertElseFatalError(Tokenizer.isIdentifier(name),
                "The program name must be identifier.");
        this.setName(name);

        String iss = tokens.dequeue(); //grabs is
        Reporter.assertElseFatalError(iss.equals("IS"),
                "'IS' must come after the program name.");

        //run through instructions
        while (tokens.front().equals("INSTRUCTION")) {
            Statement instrBody = this.newBody();

            String result = parseInstruction(tokens, instrBody);
            Reporter.assertElseFatalError(!map.hasKey(result),
                    "Instruction has same name as previous instruction");

            map.add(result, instrBody); //keep adding instructions to map
        }

        this.swapContext(map); //want to swap instructions

        String beginning = tokens.dequeue(); //beginning of program
        Reporter.assertElseFatalError(beginning.equals("BEGIN"),
                "'BEGIN' must come after instructions.");

        Statement progBody = this.newBody();
        progBody.parseBlock(tokens); //parse main statement in program
        this.swapBody(progBody);

        String endD = tokens.dequeue();
        Reporter.assertElseFatalError(endD.equals("END"),
                "END keyword was not found after program");

        String endName = tokens.dequeue(); //instruction name at end
        Reporter.assertElseFatalError(name.equals(endName),
                "The identifier at the end of the program must be the same "
                        + "as the identifier at the beginning of the program.");

        tokens.dequeue(); //last token (end)
        Reporter.assertElseFatalError(tokens.length() == 0,
                "No more content after end of program");

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
        out.print("Enter valid BL program file name: ");
        String fileName = in.nextLine();
        /*
         * Parse input file
         */
        out.println("*** Parsing input file ***");
        Program p = new Program1Parse1();
        SimpleReader file = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(file);
        file.close();
        p.parse(tokens);
        /*
         * Pretty print the program
         */
        out.println("*** Pretty print of parsed program ***");
        p.prettyPrint(out);

        in.close();
        out.close();
    }

}
