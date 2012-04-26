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
package de.vistahr.lanchat.util.settings;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import de.vistahr.util.logger.JLoggerUtil;

public class PropertiesUtil {

	
	private static String basename = "/lanchat.properties";
	
	
	public static String getLanchatPropertyString(String key) {
		Properties properties = new Properties();
		String resultProperty = "";
		
		try {
			properties.load(PropertiesUtil.class.getResourceAsStream(basename));
			resultProperty = properties.getProperty(key);
			
		} catch (FileNotFoundException e) {
			JLoggerUtil.getLogger().warn("FileNotFoundException " + basename);
			
		} catch (IOException e) {
			JLoggerUtil.getLogger().warn("IOException " + basename);

		} catch (NullPointerException e) {
			JLoggerUtil.getLogger().warn("NullPointerException " + basename);
		}
		
		
		return resultProperty;
	}
	
	public static Image getLanchatPropertyImage(String key) {
		return Toolkit.getDefaultToolkit().getImage(PropertiesUtil.class.getResource(PropertiesUtil.getLanchatPropertyString(key)));
	}
	
}
