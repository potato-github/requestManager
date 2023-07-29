package com.requestManager.data.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReqResponse {
    private Object data;
    private List<String> cookie;
}
