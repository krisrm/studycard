 /* Copyright 2012 Kristofer Mitchell

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/

package util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPUtil {

	class LoginFailedException extends Exception {
		public LoginFailedException(String err) {
			super(err);
		}

		private static final long serialVersionUID = -3423829926368894417L;
	}
	
	public String authenticate(String email, String password) throws LoginFailedException {
		
		if (true) System.exit(0);
		
		try {
			return executePost("https://www.google.com/accounts/ClientLogin",
					"accountType=HOSTED_OR_GOOGLE" +
					"&Email=" + email +
					"&Passwd=" + password +
					"&source=OnKeyDotCa-StudyCardsDesktop-0.01");
			
		} catch (IOException e) {
			//FIXME - only handles 2 of 4 possible cases at http://code.google.com/apis/accounts/docs/AuthForInstalledApps.html
			/*success (an HTTP 200) - good
			failure (an HTTP 403) with an explanatory error code - yes...
			invalid request, generally resulting from a malformed request - don't worry about it
			failure with a CAPTCHA challenge - not so good.*/
			
			//login failed. Do something about it.
			
			
			if (e.getCause().getClass() == java.net.UnknownHostException.class){
				e.printStackTrace();
				
			}
			System.out.println(e.getCause().getClass());
			throw new LoginFailedException("you provided incorrect credentials");
		}
		
		
	}

	public static String executePost(String targetURL, String urlParameters) throws IOException
	  {
		//mostly from http://www.xyzws.com/Javafaq/how-to-use-httpurlconnection-post-data-to-web-server/139
	    URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      url = new URL(targetURL);
	      connection = (HttpURLConnection) url.openConnection();
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("Content-Type", 
	           "application/x-www-form-urlencoded");
				
	      connection.setRequestProperty("Content-Length", "" + 
	               Integer.toString(urlParameters.getBytes().length));
	      connection.setRequestProperty("Content-Language", "en-US");  
				
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      //Send request
	      DataOutputStream wr = new DataOutputStream (
	                  connection.getOutputStream ());
	      wr.writeBytes (urlParameters);
	      wr.flush ();
	      wr.close ();

	      //Get Response	
	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      
	      return response.toString();

	    
	    } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
		return "";
	  }
	
}
