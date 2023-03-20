package parser;

import handlers.GithubParsingHandler;
import handlers.ParsingHandler;
import handlers.StackOverflowParsingHandler;
import results.ParsingResult;

public class Parser {

    private static final ParsingHandler PARSING_CHAIN =
            new GithubParsingHandler(new StackOverflowParsingHandler());

    public ParsingResult parse(String link) {
        return PARSING_CHAIN.handle(link);
    }

}
