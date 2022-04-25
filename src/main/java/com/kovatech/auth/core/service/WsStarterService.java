package com.kovatech.auth.core.service;

import java.util.HashSet;
import java.util.List;

public interface WsStarterService {
  String formatAndCompareMsisdn(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2);
  
  String formatAndHashMsisdn(String paramString, boolean paramBoolean);
  
  String hashText(String paramString);
  
  HashSet<String> getHashSet(String paramString1, String paramString2);
  
  List<String> getList(String paramString1, String paramString2);
  
  boolean contentValid(String paramString1, String paramString2);
  
  String serialize(Object paramObject);
  
  String getRandomText(Integer paramInteger, String paramString);
  
  String generateRandomText(String[] paramArrayOfString, int paramInt, String paramString);
  
  boolean parametersMatch(String paramString1, String paramString2);
}