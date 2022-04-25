package com.kovatech.auth.core.service;

import com.kovatech.auth.core.model.WsResponse;
import com.kovatech.auth.core.model.WsResponseDetails;

import java.time.LocalDateTime;
import java.util.Map;

public interface WsMappingService {
  WsResponseDetails getErrorMapper(String paramString1, String paramString2, String paramString3);
  
  WsResponse setErrResponse(WsResponseDetails paramWsResponseDetails, Object paramObject, String paramString, boolean paramBoolean, Map<String, String> paramMap);
  
  void logApiRequest(WsResponseDetails paramWsResponseDetails, String paramString1, LocalDateTime paramLocalDateTime, String paramString2, String paramString3, String paramString4, boolean paramBoolean, Map<String, String> paramMap);
}