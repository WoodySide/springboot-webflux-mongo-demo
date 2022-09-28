package com.woodyside.reactive.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestApiResponse {

    private Integer status;

    private String date;

    private String message;

    private String description;

    private boolean success;

    private String path;
}
