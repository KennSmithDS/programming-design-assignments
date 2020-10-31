import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
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

    public void buildSentence() {

    }

    public String recursiveStringReplace(LinkedList<String> grammarQueue) {
//        String firstToken = grammarQueue.peek();
        String matchResult = findPlaceholder(grammarQueue.peek());
        if (matchResult != null) {
            return "";
        } else {
            String firstToken = grammarQueue.remove();
            return firstToken + recursiveStringReplace(grammarQueue);
        }
    }

    public String getSentence () { return this.sentence; }

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
