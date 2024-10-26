package school.sptech.crudrisecanvas.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ResponseLambda {

    private Integer statusCode;
    private String body;
}
