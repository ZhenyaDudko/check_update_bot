class GithubParsingHandler extends ParsingHandler {

    GithubParsingHandler(ParsingHandler successor) {
        super(successor);
    }

    @Override
    boolean checkResponsibility(String link) {
        return link.startsWith("https://github.com/");
    }

    @Override
    ParsingResult parse(String link) {
        if (!checkResponsibility(link)) {
            return null;
        }
        String[] splitResult = link.split("/", 6);
        if (splitResult.length < 5 || splitResult[3].isEmpty() || splitResult[4].isEmpty()) {
            return null;
        }
        return new GithubParsingResult(splitResult[3], splitResult[4]);
    }
}
