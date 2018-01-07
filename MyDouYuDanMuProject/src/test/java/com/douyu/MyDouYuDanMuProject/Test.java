package com.douyu.MyDouYuDanMuProject;

import java.io.IOException;
import java.util.regex.Matcher;

import com.douyu.until.ParseUntil;


public class Test {

/*	@org.junit.Test
	public void test() throws IOException {
		String a = "rid@=321358";
		Matcher matcher = ParseUntil.getMatcher(a, "rid"+"(.*)=");
		while(matcher.find()){
			
			System.out.println(matcher.group());
			a = a.replaceAll("rid"+"(.*)=", "");
			System.out.println(a);
		}
	}*/
	
	@org.junit.Test
	public void test1(){
		String b = "type@=chatmsg/rid@=2227593/ct@=2/uid@=142414930/nn@=丿雁南川外灬/txt@=蛤蟆/cid@=ab0648ad39e343c3894a450000000000/ic@=avanew@Sface@S201705@S30@S14@S287e111d8bac568e18c29242c1ba8327/level@=2/sahf@=0/bnn@=/bl@=0/brid@=0/hc@=/el@=/";
		Matcher matcher = ParseUntil.getMatcher(b, "uid@=(.*)/nn@=(.*)/txt@=(.*)/cid@=(.*)/");
		while(matcher.find()){
			
			System.out.println(matcher.group(3));
			b = b.replaceAll("nn@=(.*)/", "");
			System.out.println(b);
		}
	}	

}
