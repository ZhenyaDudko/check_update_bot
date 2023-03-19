class StackOverflowParsingHandler extends ParsingHandler {

    StackOverflowParsingHandler(ParsingHandler successor) {
        super(successor);
    }

    StackOverflowParsingHandler() {
        super();
    }

    @Override
    boolean checkResponsibility(String link) {
        return link.startsWith("https://stackoverflow.com/questions/");
    }

    @Override
    ParsingResult parse(String link) {
        if (!checkResponsibility(link)) {
            return null;
        }
        String[] splitResult = link.split("/", 6);
        if (splitResult.length < 5 || splitResult[4].isEmpty()) {
            return null;
        }
        return new StackOverflowParsingResult(splitResult[4]);
    }
}
