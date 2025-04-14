package b1nd.b1nd_website_server.global.libs.webclient.parser;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HeaderParser {
    private String type;
    private String value;
}
