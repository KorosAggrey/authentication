package com.kovatech.auth.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailProperties {
    private String name;
    private String Subject;
    private String plainTextContent;
    private String htmlContent;
    private String emailTO;
}
