package school.sptech.crudrisecanvas.utils.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

public class CEPValidator implements ConstraintValidator<CEP, String> {

    @Override
    public void initialize(CEP constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String valorCep, ConstraintValidatorContext ctx) {
        return valorCep != null && valorCep.matches("^[0-9]{5}-?[0-9]{3}$") &&
                validarViaCep(valorCep);
    }

    private boolean validarViaCep(String valorCep) {
        RestClient client = RestClient.builder()
                .baseUrl("https://viacep.com.br/ws/")
                .messageConverters(httpMessageConverters -> httpMessageConverters.add(new MappingJackson2HttpMessageConverter()))
                .build();

        valorCep = valorCep.replace("-", "");

        String raw = client.get()
                .uri(valorCep + "/json")
                .retrieve()
                .body(String.class);

        return raw != null && !raw.contains("\"erro\": true");
    }
}
