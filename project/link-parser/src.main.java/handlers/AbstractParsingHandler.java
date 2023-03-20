package handlers;

import results.ParsingResult;

public abstract class AbstractParsingHandler implements ParsingHandler {

    private AbstractParsingHandler successor;

    public AbstractParsingHandler(AbstractParsingHandler successor) {
        this.successor = successor;
    }

    public AbstractParsingHandler() {}

    public AbstractParsingHandler getSuccessor() {
        return successor;
    }

    public void setSuccessor(AbstractParsingHandler successor) {
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

    protected abstract boolean checkResponsibility(final String link);

    protected abstract ParsingResult parse(final String link);
}
