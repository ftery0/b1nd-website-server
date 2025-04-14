package b1nd.b1nd_website_server.global.libs.webclient.template;

import b1nd.b1nd_website_server.domain.auth.presentation.dto.api.DAuthToken;
import b1nd.b1nd_website_server.domain.auth.presentation.dto.api.OpenApiDto;
import b1nd.b1nd_website_server.domain.auth.presentation.dto.api.TokenRes;
import b1nd.b1nd_website_server.global.libs.webclient.WebClientUtil;
import b1nd.b1nd_website_server.global.properties.OAuthProperties;
import b1nd.b1nd_website_server.global.libs.webclient.parser.HeaderParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static b1nd.b1nd_website_server.global.enums.baseurl.DodamBaseUrl.DODAM_AUTH;
import static b1nd.b1nd_website_server.global.enums.baseurl.DodamBaseUrl.DODAM_OPENAPI;


@Component
@RequiredArgsConstructor
public class DodamWebClientTemplate {
    private final WebClientUtil webClientUtil;
    private final OAuthProperties authProperties;
    private static final Logger log = LoggerFactory.getLogger(DodamWebClientTemplate.class);


    public String auth(String code, String path) {
        log.info("[Dodam Auth 요청] code: {}, path: {}", code, path);

        TokenRes tokenRes = webClientUtil.post(
                DODAM_AUTH.getEndpoint() + path,
                DAuthToken.builder()
                        .code(code)
                        .client_id(authProperties.getClientId())
                        .client_secret(authProperties.getClientSecret()).build(),
                TokenRes.class
        ).getBody();

        String accessToken = tokenRes.getAccessToken();
        log.info("[Dodam Auth 응답] accessToken: {}", accessToken);

        return accessToken;
    }

    public OpenApiDto openApi(String accessToken, String path) {
        return webClientUtil.get(
                DODAM_OPENAPI.getEndpoint() + path,
                HeaderParser.builder()
                        .type("Authorization")
                        .value("Bearer " + accessToken).build(),
                OpenApiDto.class
        ).getBody();
    }
}
