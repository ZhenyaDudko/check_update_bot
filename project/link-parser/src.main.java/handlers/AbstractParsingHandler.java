package handlers;

import results.ParsingResult;

public abstract class AbstractParsingHandler implements ParsingHandler {

    private ParsingHandler successor;

    public AbstractParsingHandler(final ParsingHandler successor) {
        this.successor = successor;
    }

    public AbstractParsingHandler() {}

    public ParsingHandler getSuccessor() {
        return successor;
    }

    public void setSuccessor(final ParsingHandler successor) {
        this.successor = successor;
    }

    @Override
    public ParsingResult handle(final String link) {
        if (checkResponsibility(link)) {
            return parse(link);
        }
        if (successor != null) {
            return successor.handle(link);
        }
        return null;
    }

    protected boolean checkResponsibility(final String link) {
        return link.matches(getLinkPattern());
    }

    protected abstract String getLinkPattern();

    protected abstract ParsingResult parse(final String link);
}
