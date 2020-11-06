import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to represent a sentence generator, like a factory design but not quite
 * Takes in a Grammar object, and builds a string through recursive string replacement
 * Properties:
 * - sentence
 * - grammar
 * - placeholder pattern
 * Methods:
 * + find placeholder from regex
 * + build grammar queue from string
 * + generate sentence from grammar queue
 * + get final sentence
 */
public class SentenceGenerator {

    private String sentence;
    private Grammar grammar;
    private static final String PLACEHOLDER_PATTERN = "(?<=\\<)(.*?)(?=\\>)";

    /**
     * Default constructor for SentenceGenerator class, requires Grammar object to be passed
     * @param grammar Grammar object created from JSONFileParser
     */
    public SentenceGenerator(Grammar grammar) {
        this.grammar = grammar;
        this.sentence = "";
    }

    /**
     * Method to fetch a random String from Grammar ArrayList based on HashMap key lookup
     * @param key String value for HashMap key
     * @return random String element from ArrayList
     */
    private String getRandomGrammarElement(String key) throws NoSuchGrammarTypeException{
        ArrayList<String> grammarList = this.grammar.getInfoValue(key);
        if (grammarList == null) {
            throw new NoSuchGrammarTypeException("The provided JSON file references a grammar type that is invalid or not defined.");
        }
        Random rand = new Random();
        return grammarList.get(rand.nextInt(grammarList.size()));
    }

    /**
     * Method to find a placeholder within a String input using regex
     * @param input String input token from randomly selected ArrayList element
     * @return String placeholder found or null if no match
     */
    private static boolean isTokenPlaceholder(String input) {
        Pattern pattern = Pattern.compile(PLACEHOLDER_PATTERN);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method to build a Stack of String tokens from grammar element
     * Stack is in reverse order so we can work with tokens from front to end (in sentence order)
     * @param input String grammar element randomly chosen
     * @return Stack of strings in reverse order
     */
    private static Stack<String> buildGrammarStack(String input) {
        List<String> stringList = Arrays.asList(input.split(" "));
        int n = stringList.size();
        Stack<String> sentenceStack = new Stack<String>();
        for (int i=n-1; i>=0; i--) {
            sentenceStack.push(stringList.get(i));
        }
        return sentenceStack;
    }

    /**
     * Helper method to build a sentence by getting random starting place,
     * Then building the grammar stack, and passing the Stack into recursion
     * @return String sentence built from recursive Stack string construction
     */
    public String buildSentence() throws NoSuchGrammarTypeException{
        String sentenceStart = getRandomGrammarElement("start");
        Stack<String> grammarStack = buildGrammarStack(sentenceStart);
        this.sentence = recursiveStringReplace(grammarStack);
        return getFinishedSentence();
    }

    /**
     * Method to recursively step into the Stack of grammar String
     * Pops elements from Stack to check if placeholder match
     * If placeholder match, swap placeholder with random choice, push back to Stack and recurse down
     * Else append first element of Stack to complete sentence, and recurse down with remaining Stack
     * @param grammarStack Stack of Strings in sentence order
     * @return String constructed on recursion stack
     */
    private String recursiveStringReplace(Stack<String> grammarStack) throws NoSuchGrammarTypeException{
        if (!grammarStack.empty()) {
            String topOfStack = grammarStack.pop();
            boolean matchResult = isTokenPlaceholder(topOfStack);
            if (matchResult) {
                String hashKey = topOfStack.replace("<", "").replace(">","");
//                System.out.println("Top of stack: " + hashKey);
                String placeholderReplacement = getRandomGrammarElement(hashKey);
                //System.out.println("Replacing " + topOfStack + " by searching for " + hashKey + " with '" + placeholderReplacement + "'");
                String[] replacementList = placeholderReplacement.split(" ");
                for (int i=replacementList.length-1; i>=0; i--) {
                    grammarStack.push(replacementList[i]);
                }
                return recursiveStringReplace(grammarStack);
            } else {
                return topOfStack + " " + recursiveStringReplace(grammarStack);
            }
        }
        return "";
    }

    /**
     * Method to fetch the finished sentence String property
     * @return String sentence property
     */
    public String getFinishedSentence () { return this.sentence; }

    /**
     * Override method for default toString()
     * @return String
     */
    @Override
    public String toString() {
        return "SentenceGenerator{" +
                "sentence='" + sentence + '\'' +
                ", grammar=" + grammar +
                '}';
    }

    /**
     * Override method for default equals()
     * @param o object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SentenceGenerator that = (SentenceGenerator) o;
        return Objects.equals(sentence, that.sentence) &&
                Objects.equals(grammar, that.grammar);
    }

    /**
     * Override method for default hashCode()
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(sentence, grammar);
    }
    
   
    public static void main(String[] args) throws IOException, ParseException, NoSuchJSONObjectException, NoSuchGrammarTypeException {
        JSONFileParser poemTest = new JSONFileParser("term_paper_grammar.json");
        Grammar poemGrammar = new Grammar(poemTest);
        System.out.println(poemGrammar);

        SentenceGenerator sentenceGen = new SentenceGenerator(poemGrammar);
        String sentence = sentenceGen.buildSentence();
        System.out.println(sentence);
    }
}
