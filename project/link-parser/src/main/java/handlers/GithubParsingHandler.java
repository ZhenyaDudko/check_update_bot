package handlers;

import results.GithubParsingResult;
import results.ParsingResult;

public class GithubParsingHandler extends AbstractParsingHandler {

    public GithubParsingHandler(final ParsingHandler successor) {
        super(successor);
    }

    public GithubParsingHandler() {
    }

    private static final String LINK_PATTERN = "^https://github.com/[a-zA-Z0-9-]{1,39}/[a-zA-Z0-9-_.]+/?$";
    private static final int USER_LINK_INDEX = 3;
    private static final int REPO_LINK_INDEX = 4;

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
        return new GithubParsingResult(splitResult[USER_LINK_INDEX], splitResult[REPO_LINK_INDEX]);
    }
}
