package me.redstoner2019.bingobackend.util;

import org.json.JSONObject;

import java.util.HashMap;

public class TokenChecker {
    public static HashMap<String, JSONObject> buffer = new HashMap<>();
    public static HashMap<String, Long> timeoutBuffer = new HashMap<>();

    public static String getUsername(String token){
        if(!isValidToken(token)){
            return null;
        }
        JSONObject tokenInfo = getTokenInfo(token);
        return tokenInfo.getString("username");
    }

    public static boolean isValidToken(String token){
        JSONObject tokenInfo = getTokenInfo(token);
        return tokenInfo != null;
    }

    public static JSONObject getTokenInfo(String token){
        if(buffer.containsKey(token)){
            if(timeoutBuffer.get(token) - 60000 < System.currentTimeMillis()){
                return buffer.get(token);
            } else {
                timeoutBuffer.remove(token);
                buffer.remove(token);
            }
        }

        JSONObject request = new JSONObject();
        request.put("token", token);

        JSONObject tokenInfo = HttpRequests.httpPost("http://158.220.105.209:8080/verifyToken", request.toString());

        if(tokenInfo.getInt("status") != 0){
            tokenInfo = null;
        } else {
            tokenInfo = HttpRequests.httpPost("http://158.220.105.209:8080/tokenInfo", request.toString());
        }

        timeoutBuffer.put(token, System.currentTimeMillis());
        buffer.put(token,tokenInfo);

        return tokenInfo;
    }
}
