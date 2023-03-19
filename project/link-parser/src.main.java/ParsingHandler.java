abstract class ParsingHandler {
    private ParsingHandler successor;

    ParsingHandler(ParsingHandler successor) {
        this.successor = successor;
    }

    ParsingHandler() {}

    ParsingHandler getSuccessor() {
        return successor;
    }

    void setSuccessor(ParsingHandler successor) {
        this.successor = successor;
    }

    ParsingResult handle(final String link) {
        if (checkResponsibility(link)) {
            return parse(link);
        }
        if (successor != null) {
            return successor.handle(link);
        }
        return null;
    }

    abstract boolean checkResponsibility(final String link);

    abstract ParsingResult parse(final String link);
}
