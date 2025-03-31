package b1nd.b1nd_website_server.global.error;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomError extends RuntimeException {
    private final ErrorCode errorCode;

    public static  CustomError of(ErrorCode errorCode) {
        return  new CustomError(errorCode);
    }

}
