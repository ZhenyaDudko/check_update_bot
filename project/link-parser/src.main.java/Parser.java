import handlers.GithubParsingHandler;
import handlers.ParsingHandler;
import handlers.StackOverflowParsingHandler;
import results.ParsingResult;

public class Parser {

    private static final ParsingHandler parsingChain =
            new GithubParsingHandler(new StackOverflowParsingHandler());

    public ParsingResult parse(String link) {
        return parsingChain.handle(link);
    }

}
