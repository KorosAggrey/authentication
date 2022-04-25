package com.kovatech.auth.core.service;

import com.kovatech.auth.core.model.WsResponse;
import com.kovatech.auth.core.model.WsResponseDetails;
import java.util.Map;
import reactor.core.publisher.Mono;

public interface WsResponseMapper {
  Mono<WsResponse> setApiResponse(String paramString1, Object paramObject, String paramString2, String paramString3, String paramString4, String paramString5, boolean paramBoolean, Map<String, String> paramMap);
  
  Mono<WsResponse> setApiResponse(WsResponseDetails paramWsResponseDetails, Object paramObject, String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean, Map<String, String> paramMap);
  
  WsResponse setApiResponse(String paramString1, String paramString2, Object paramObject, String paramString3, String paramString4, String paramString5, boolean paramBoolean, Map<String, String> paramMap);
  
  WsResponse setApiResponse(String paramString1, WsResponseDetails paramWsResponseDetails, Object paramObject, String paramString2, String paramString3, String paramString4, boolean paramBoolean, Map<String, String> paramMap);
  
  Mono<Object> setTmfResponse(String paramString1, Object paramObject, String paramString2, String paramString3, String paramString4, String paramString5, boolean paramBoolean, Map<String, String> paramMap);
  
  Mono<Object> setTmfResponse(WsResponseDetails paramWsResponseDetails, Object paramObject, String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean, Map<String, String> paramMap);
  
  Object setTmfResponse(String paramString1, String paramString2, Object paramObject, String paramString3, String paramString4, String paramString5, boolean paramBoolean, Map<String, String> paramMap);
  
  Object setTmfResponse(String paramString1, WsResponseDetails paramWsResponseDetails, Object paramObject, String paramString2, String paramString3, String paramString4, boolean paramBoolean, Map<String, String> paramMap);
}