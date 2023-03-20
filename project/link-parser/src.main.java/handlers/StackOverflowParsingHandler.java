package handlers;

import results.ParsingResult;
import results.StackOverflowParsingResult;

public class StackOverflowParsingHandler extends AbstractParsingHandler {

    public StackOverflowParsingHandler(AbstractParsingHandler successor) {
        super(successor);
    }

    public StackOverflowParsingHandler() {
        super();
    }

    @Override
    protected boolean checkResponsibility(String link) {
        return link.matches("^https://stackoverflow.com/questions/\\d+/[^/]+/?$");
    }

    @Override
    protected ParsingResult parse(String link) {
        if (!checkResponsibility(link)) {
            return null;
        }
        String[] splitResult = link.split("/");
        return new StackOverflowParsingResult(splitResult[4]);
    }
}
