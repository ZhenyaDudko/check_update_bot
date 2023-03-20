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

    private static final String LINK_PATTERN = "^https://stackoverflow.com/questions/\\d+/[^/]+/?$";

    @Override
    protected String getLinkPattern() {
        return LINK_PATTERN;
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
