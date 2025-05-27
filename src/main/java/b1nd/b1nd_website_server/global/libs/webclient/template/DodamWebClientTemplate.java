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
        try {
            log.info("[Dodam Auth 요청] path: {}, code: {}, client_id: {}", path, code, authProperties.getClientId());

            return  webClientUtil.post(
                    DODAM_AUTH.getEndpoint() + path,
                    DAuthToken.builder()
                            .code(code)
                            .client_id(authProperties.getClientId())
                            .client_secret(authProperties.getClientSecret())
                            .build(),
                    TokenRes.class
            ).getBody().getAccessToken();



        } catch (Exception e) {
            // 예외 로그 개선
            log.error("Dodam Auth 실패 ❌\n- code: {}\n- path: {}\n- client_id: {}\n- message: {}\n- cause: {}",
                    code,
                    path,
                    authProperties.getClientId(),
                    e.getMessage(),
                    e.getCause() != null ? e.getCause().toString() : "N/A",
                    e
            );
            throw e;
        }
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
