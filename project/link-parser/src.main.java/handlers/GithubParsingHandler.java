package handlers;

import results.GithubParsingResult;
import results.ParsingResult;

public class GithubParsingHandler extends AbstractParsingHandler {

    public GithubParsingHandler(final ParsingHandler successor) {
        super(successor);
    }

    public GithubParsingHandler() {
        super();
    }

    private static final String linkPattern = "^https://github.com/[a-zA-Z0-9-]{1,39}/[a-zA-Z0-9-_.]+/?$";

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
        return new GithubParsingResult(splitResult[3], splitResult[4]);
    }
}
