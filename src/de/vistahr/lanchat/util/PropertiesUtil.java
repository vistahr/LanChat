/**
 * 
 * 	Copyright 2012 Vince. All rights reserved.
 * 	
 * 	Redistribution and use in source and binary forms, with or without modification, are
 * 	permitted provided that the following conditions are met:
 * 	
 * 	   1. Redistributions of source code must retain the above copyright notice, this list of
 * 	      conditions and the following disclaimer.
 * 	
 * 	   2. Redistributions in binary form must reproduce the above copyright notice, this list
 * 	      of conditions and the following disclaimer in the documentation and/or other materials
 * 	      provided with the distribution.
 * 	
 * 	THIS SOFTWARE IS PROVIDED BY Vince ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * 	WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * 	FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Vince OR
 * 	CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * 	CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * 	SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * 	ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * 	NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * 	ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 	
 * 	The views and conclusions contained in the software and documentation are those of the
 * 	authors and should not be interpreted as representing official policies, either expressed
 * 	or implied, of Vince.
 */
package de.vistahr.lanchat.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import de.vistahr.util.JLoggerUtil;

public class PropertiesUtil {

	
	private static final String lanchatProperty = "/lanchat.properties";
	private static final String smileyProperty  = "/smileys/smileys.properties";

	
	private static String getKey(String key, String property) {
		Properties properties = new Properties();
		String resultProperty = "";
		
		try {
			properties.load(PropertiesUtil.class.getResourceAsStream(property));
			resultProperty = properties.getProperty(key);
			
		} catch (FileNotFoundException e) {
			JLoggerUtil.getLogger().warn("FileNotFoundException " + property);
			
		} catch (IOException e) {
			JLoggerUtil.getLogger().warn("IOException " + property);

		} catch (NullPointerException e) {
			JLoggerUtil.getLogger().warn("NullPointerException " + property);
		}
		
		return resultProperty;		
	}

	
	private static Set<String> getAllKeys(String property) {
		Properties properties = new Properties();
		Set<String> resultProperty = new HashSet<String>();
		
		try {
			properties.load(PropertiesUtil.class.getResourceAsStream(property));
			resultProperty = properties.stringPropertyNames();
			
		} catch (FileNotFoundException e) {
			JLoggerUtil.getLogger().warn("FileNotFoundException " + property);
			
		} catch (IOException e) {
			JLoggerUtil.getLogger().warn("IOException " + property);

		} catch (NullPointerException e) {
			JLoggerUtil.getLogger().warn("NullPointerException " + property);
		}
		
		return resultProperty;		
	}	
	
	
	public static String getLanchatPropertyString(String key) {
		return getKey(key, lanchatProperty);
	}
	
	
	public static Image getLanchatPropertyImage(String key) {
		return Toolkit.getDefaultToolkit().getImage(PropertiesUtil.class.getResource(PropertiesUtil.getLanchatPropertyString(key)));
	}
	
	
	public static Set<String> getAllSmileysFromPropertyFile() {
		return getAllKeys(smileyProperty);
	}
	
	
	public static String getSmileyPathByKey(String key) {
		return getKey(key, smileyProperty);
	}
	
}
