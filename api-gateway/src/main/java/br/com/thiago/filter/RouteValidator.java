package br.com.thiago.filter;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private static final List<String> openAiEndPoints = List.of(
            "/auth"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openAiEndPoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
