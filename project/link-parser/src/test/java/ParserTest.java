import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import parser.Parser;
import results.GithubParsingResult;
import results.ParsingResult;
import results.StackOverflowParsingResult;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ParserTest {

    Parser parser = new Parser();

    @ParameterizedTest()
    @ValueSource(strings = {"github", " ", "", "123", "htp://hello", "https:/com.com"})
    void parser_shouldReturnNull(String link) {
        //when
        ParsingResult result = parser.parse(link);

        //then
        assertThat(result).isNull();
    }

    @Test
    void parser_shouldParseGithub() {
        //given
        String link = "https://github.com/user/project";

        //when
        ParsingResult result = parser.parse(link);

        //then
        assertAll("Assert response fields",
                () -> assertThat(result).isInstanceOf(GithubParsingResult.class),
                () -> assertThat(((GithubParsingResult) result).user()).isEqualTo("user"),
                () -> assertThat(((GithubParsingResult) result).repository()).isEqualTo("project")
        );
    }

    @Test
    void parser_shouldParseStackoverflow() {
        //given
        String link = "https://stackoverflow.com/questions/123/how-to";

        //when
        ParsingResult result = parser.parse(link);

        //then
        assertAll("Assert response fields",
                () -> assertThat(result).isInstanceOf(StackOverflowParsingResult.class),
                () -> assertThat(((StackOverflowParsingResult) result).id()).isEqualTo("123")
        );
    }

}
