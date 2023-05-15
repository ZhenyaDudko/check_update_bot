package handlers;

import results.ParsingResult;
import results.StackOverflowParsingResult;

public class StackOverflowParsingHandler extends AbstractParsingHandler {

    public StackOverflowParsingHandler(final ParsingHandler successor) {
        super(successor);
    }

    public StackOverflowParsingHandler() {
    }

    private static final String LINK_PATTERN = "^https://stackoverflow.com/questions/\\d+/[^/]+/?$";

    private static final int QUESTION_ID_LINK_INDEX = 4;

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
        return new StackOverflowParsingResult(splitResult[QUESTION_ID_LINK_INDEX]);
    }
}
