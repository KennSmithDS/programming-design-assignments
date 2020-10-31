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

    public SentenceGenerator(Grammar grammar) {
        this.grammar = grammar;
    }

    public String getRandomGrammarElement(String key) {
        ArrayList<String> grammarList = this.grammar.getInfo().get(key);
        Random rand = new Random();
        return grammarList.get(rand.nextInt(grammarList.size()));
    }

    public static String findPlaceholder(String input) {
        Pattern pattern = Pattern.compile(PLACEHOLDER_PATTERN);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    public Queue<String> buildGrammarQueue(String input) {
        String[] stringList = input.split(" ");
        Queue<String> sentenceQueue = new LinkedList<String>();
        for (String s : stringList){
            sentenceQueue.offer(s);
        }
        if (sentenceQueue.size()!=0) {
            return sentenceQueue;
        }
        return null;
    }

    public static Stack<String> buildGrammarStack(String input) {
        List<String> stringList = Arrays.asList(input.split(" "));
        int n = stringList.size();
        Stack<String> sentenceStack = new Stack<String>();
        for (int i=n-1; i>=0; i--) {
            sentenceStack.push(stringList.get(i));
        }
        return sentenceStack;
    }

    public String buildSentence() {
        String sentenceStart = getRandomGrammarElement("start");
        Stack<String> grammarStack = buildGrammarStack(sentenceStart);
        this.sentence = recursiveStringReplace(grammarStack);
        return getFinishedSentence();
    }

    public String recursiveStringReplace(Stack<String> grammarStack) {
        if (!grammarStack.empty()) {
            String topOfStack = grammarStack.pop();
            String matchResult = findPlaceholder(topOfStack);
            if (matchResult != null) {
                String placeholderReturn = getRandomGrammarElement(topOfStack);
                grammarStack.push(placeholderReturn);
                return recursiveStringReplace(grammarStack);
            } else {
                String firstToken = grammarStack.pop();
                return firstToken + recursiveStringReplace(grammarStack);
            }
        }
        return null;
    }

    public String getFinishedSentence () { return this.sentence; }

    @Override
    public String toString() {
        return "SentenceGenerator{" +
                "sentence='" + sentence + '\'' +
                ", grammar=" + grammar +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SentenceGenerator that = (SentenceGenerator) o;
        return Objects.equals(sentence, that.sentence) &&
                Objects.equals(grammar, that.grammar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sentence, grammar);
    }
}
