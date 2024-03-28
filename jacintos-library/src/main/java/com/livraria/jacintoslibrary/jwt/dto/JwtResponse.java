package com.livraria.jacintoslibrary.jwt.dto;


import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private Long id;

    private String email;

    private String senha;

    public static JwtResponse getUsers(Claims jwtClaims){

        try {
            return JwtResponse
                    .builder()
                    .id((Long) jwtClaims.get("id"))
                    .email((String) jwtClaims.get("id"))
                    .senha((String) jwtClaims.get("id"))
                    .build();

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
