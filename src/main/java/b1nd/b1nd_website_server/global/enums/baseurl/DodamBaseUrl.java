package b1nd.b1nd_website_server.global.enums.baseurl;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DodamBaseUrl {
    DODAM_AUTH("https://dauth.b1nd.com/api"),
    DODAM_OPENAPI("https://opendodam.b1nd.com/api");

    private String endpoint;
}