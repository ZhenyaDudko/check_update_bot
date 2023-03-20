package handlers;

import results.ParsingResult;

public interface ParsingHandler {

    ParsingResult handle(final String link);

}
