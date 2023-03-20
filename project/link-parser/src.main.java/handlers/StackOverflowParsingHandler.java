package handlers;

import results.ParsingResult;
import results.StackOverflowParsingResult;

public class StackOverflowParsingHandler extends AbstractParsingHandler {

    public StackOverflowParsingHandler(final ParsingHandler successor) {
        super(successor);
    }

    public StackOverflowParsingHandler() {
        super();
    }

    private static final String linkPattern = "^https://stackoverflow.com/questions/\\d+/[^/]+/?$";

    @Override
    protected String getLinkPattern() {
        return linkPattern;
    }

    @Override
    protected ParsingResult parse(final String link) {
        if (!checkResponsibility(link)) {
            return null;
        }
        String[] splitResult = link.split("/");
        return new StackOverflowParsingResult(splitResult[4]);
    }
}
