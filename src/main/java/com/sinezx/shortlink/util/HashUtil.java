package com.sinezx.shortlink.util;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

@Component
public class HashUtil {

    private HashFunction hashFunction = Hashing.murmur3_32_fixed();

    private final char[] BASECHARS = new char[]{
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
            '0','1','2','3','4','5','6','7','8','9','+','/'};

    private final static BigInteger BASE62 = new BigInteger("62");

    public String hash(String str){
        HashCode hashCode = hashFunction.hashBytes(str.getBytes(StandardCharsets.UTF_8));
        return hashCode.toString();
    }

    public String hashBase62(String str){
        StringBuilder sb = new StringBuilder();
        BigInteger bigInteger = new BigInteger(str);
        while(!bigInteger.equals(new BigInteger("0"))){
            BigInteger[] divideAndRemainder = bigInteger.divideAndRemainder(BASE62);
            sb.append(BASECHARS[divideAndRemainder[1].intValue()]);
            bigInteger = divideAndRemainder[0];
        }
        return sb.reverse().toString();
    }

}
