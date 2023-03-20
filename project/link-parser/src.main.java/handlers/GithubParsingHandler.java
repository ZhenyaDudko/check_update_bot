package handlers;

import results.GithubParsingResult;
import results.ParsingResult;

public class GithubParsingHandler extends AbstractParsingHandler {

    public GithubParsingHandler(AbstractParsingHandler successor) {
        super(successor);
    }

    public GithubParsingHandler() {
        super();
    }

    @Override
    protected boolean checkResponsibility(String link) {
        return link.matches("^https://github.com/[a-zA-Z0-9-]{1,39}/[a-zA-Z0-9-_.]+/?$");
    }

    @Override
    protected ParsingResult parse(String link) {
        if (!checkResponsibility(link)) {
            return null;
        }
        String[] splitResult = link.split("/");
        return new GithubParsingResult(splitResult[3], splitResult[4]);
    }
}
