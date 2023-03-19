public class Parser {

    private static final ParsingHandler parsingChain = new GithubParsingHandler(new StackOverflowParsingHandler());

    public ParsingResult parse(String link) {
        return parsingChain.handle(link);
    }

}
