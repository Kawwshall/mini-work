package com.tl.reap_admin_api.service; 

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tl.reap_admin_api.exception.KPAddPlayListToChannelException;
import com.tl.reap_admin_api.exception.KPChannleNotFoundException;
import com.tl.reap_admin_api.exception.KPPlaylistCreationException;
import com.tl.reap_admin_api.exception.KPVideoUploadException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.json.JSONObject;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class KPointService implements ThirdPartyVideoService {
    @Value("${kpoint.baseUrl}")
	private String baseUrl;
	
	@Value("${kpoint.playlist.url}")
	private String playlistUrl;
	
	@Value("${kpoint.channel.url}")
	private String channelUrl;
	
	@Value("${kpoint.video.url}")
	private String videoUrl;

    private final RestTemplate restTemplate;
     private final AuthTokenManager authTokenManager;

    public KPointService( RestTemplate restTemplate, AuthTokenManager authTokenManager) {
        this.restTemplate = restTemplate;   
        this.authTokenManager = authTokenManager;
    }

    @Override
    public JsonNode getChannel(String channelDisplayName)  {
        
        // Implement the logic to check if the channel exists
        try {
            String apiUrl = channelUrl+"?scope=all&first=0&max=10&shallow_search=true&qtext=\""+channelDisplayName+"\"&xt="+authTokenManager.getAuthToken();
          
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                JsonNode listNode = rootNode.get("list");
               
                return listNode.get(0) ;
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            // Log the exception
            e.printStackTrace();
            // You might want to throw a custom exception or handle it differently
            return null;
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();	         
        }

        return null;
    }

    @Override
    public JsonNode getPlaylist(String playlistDisplayName) {
        // Implement the logic to check if the playlist exists
        try {
            String apiUrl = playlistUrl+"?scope=all&first=0&max=10&shallow_search=true&qtext=\""+playlistDisplayName+"\"&xt="+authTokenManager.getAuthToken();
            //System.out.println("\n\ngetPlaylist url - " + apiUrl);
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                JsonNode listNode = rootNode.get("list");
                if(listNode.size() > 0)
                {
                    return listNode.get(0);
                }
            }

            //System.out.println("\n\ngetPlaylist response - " + response.getStatusCode());
        }  catch (InvalidKeyException | NoSuchAlgorithmException e) {
            // Log the exception
            e.printStackTrace();
            // You might want to throw a custom exception or handle it differently
            return null;
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();	         
        }
        return null;
    }

    @Override
    public boolean checkPlaylistExistsInChannel(String playlistName, String channelName) {
        // Implement the logic to check if the playlist exists in the channel
       
	    try {
	    	String token = authTokenManager.getAuthToken();
	    	String url = String.format("%s/%s/content?qtext=%s&type=%s&first=0&max=1&xt=%s",
	    			channelUrl, channelName, playlistName, "playlists", token);

            //System.out.println("\n\ncheckPlaylistExistsInChannel -- " + url);
	        HttpHeaders headers = new HttpHeaders();
	        HttpEntity<String> entity = new HttpEntity<>(headers);
	        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	        
	        if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                JsonNode node = objectMapper.readTree(response.getBody());
                //System.out.println("\n\ncheckPlaylistExistsInChannel NODE -- " + node.toString() );
                
                JsonNode listNode = node.get("list");

                //System.out.println("\n\ncheckPlaylistExistsInChannel listNode size-- " + listNode.size() );
                if(listNode.size() > 0)
                {
                    JsonNode plJsonNode = listNode.get(0);
                    //System.out.println("\n\ncheckPlaylistExistsInChannel JsonNode -- " + plJsonNode );
                    return plJsonNode != null;
                }
	        }

            //System.out.println("\n\ncheckPlaylistExistsInChannel Response -- " + response.getStatusCode() );
	    }  catch (InvalidKeyException | NoSuchAlgorithmException e) {
            // Log the exception
            e.printStackTrace();
            // You might want to throw a custom exception or handle it differently
           
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();	         
        }
        return false;
    }

    @Override
    public JsonNode createPlaylist(String playlistDisplayName) {
        JsonNode plnode = null;
        // Implement the logic to create a playlist
        try {
	    	String token = authTokenManager.getAuthToken();
	    	String url = String.format("%s?displayname=%s&visibility=%s&xt=%s",
		            playlistUrl, playlistDisplayName, "PUBLIC", token);
		   
		    
	        HttpHeaders headers = new HttpHeaders();
	        HttpEntity<String> entity = new HttpEntity<>(headers);
	        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
	        
	        if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                plnode = objectMapper.readTree(response.getBody());	  
                return plnode;
	        }
	    }  catch (InvalidKeyException | NoSuchAlgorithmException e) {
            // Log the exception
            e.printStackTrace();
            // You might want to throw a custom exception or handle it differently
            
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();	         
        }
	    
        return null;
    }

    @Override
    public JsonNode addPlayListToChannel(int playlistid, String channelName) {
         // Implement the logic to add a playlist to a channel
        JsonNode respNode = null;
        try {
            String token = authTokenManager.getAuthToken();
            String url = String.format("%s/%s/content/%d?type=%s&xt=%s",
                    channelUrl, channelName, playlistid, "PLAYLIST", token);

            System.out.println("\n\n addPlayListToChannel - " + url);
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                respNode = objectMapper.readTree(response.getBody());	    
                return respNode;   
            }

            System.out.println("\n\n addPlayListToChannel responsecode - " + response.getStatusCode());
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("\n\n addPlayListToChannel error : "+e.getMessage());
        }
       
        return null;
    }

    public boolean checkVideoExistsInPlaylist(String playlistName, String videoName, int first, int max) {
        // Implement the logic to check if the video exists in the playlist
        JsonNode plvNode = null;
         try {
        	String token = authTokenManager.getAuthToken();
        	String url = String.format("%s/%s/videos?xt=%s&first=%d&max=%d", playlistUrl, playlistName, token, first, max);

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers); 
            //System.out.println("checkVideoExistsInPlaylist:url: "+url +" playlistName: "+playlistName+" videoName: "+videoName);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                JsonNode respNode = objectMapper.readTree(response.getBody());
                int total = (respNode.get("totalcount") == null) ? 0 : respNode.get("totalcount").asInt();
    
                JsonNode listNode = objectMapper.readTree(response.getBody()).get("list");
                for (JsonNode node : listNode) {
                     if (node.has("name") && node.get("name").asText().equals(videoName)) {
                         plvNode = node;
                        break;
                    }
               }
                if(plvNode != null)
                {
                    return true;
                }
                else if((first+max) < total)
                {
                    first = first+max;
                    return checkVideoExistsInPlaylist(playlistName, videoName, first, max);
                }
                else
                {
                    return false;
                }
               
            } 
        } catch (Exception e) {
            //System.out.println("\nError in checkVideoExistsInPlaylist: "+playlistName+" videoName: "+videoName);
           // e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean checkVideoExistsInPlaylist(String playlistName, String videoName) {
        // Implement the logic to check if the video exists in the playlist
        return checkVideoExistsInPlaylist(playlistName, videoName, 0, 25);    
    }   
    
    @Override   
    public JsonNode checkAndUploadVideo(String ytUrl, String channelDisplayName, String language_code) {
        // Implement the logic to check and upload a video
        try {
            JsonNode channelNode = this.getChannel(channelDisplayName);

            //Todo return appropriate error code
            if(channelNode == null)
            {
                throw new KPChannleNotFoundException(channelDisplayName + " not found");
            }
            
            String channelname = channelNode.get("name").asText();

            String playlistDisplayName = "PL_" + channelDisplayName+"_"+language_code;
            
            JsonNode playlistNode = this.getPlaylist(playlistDisplayName);
           
            String playlistname = "";
            JsonNode videoNode = null;
            if(playlistNode == null)
            {
                playlistNode = this.createPlaylist(playlistDisplayName);
                playlistname = playlistNode.get("name").asText();

                videoNode = uploadVideo(ytUrl, playlistname);
                
                playlistNode = this.addPlayListToChannel(playlistNode.get("id").asInt(), channelname);
                    
                //Todo return appropriate error code
                if(playlistNode == null) 
                {
                    throw new KPPlaylistCreationException(playlistDisplayName + " not created");
                } 
            }
            else
            {
               
                playlistname = playlistNode.get("name").asText();
                videoNode = uploadVideo(ytUrl, playlistname);
                if(!this.checkPlaylistExistsInChannel(playlistname, channelname))
                {
                    playlistNode = this.addPlayListToChannel(playlistNode.get("id").asInt(), channelname);
                    
                    //Todo return appropriate error code
                    if(playlistNode == null) 
                    {
                        throw new KPAddPlayListToChannelException(playlistname + " not added to channel " + channelname);
                    } 
                }                
            } 
          
            return videoNode;
        } 
        catch(KPAddPlayListToChannelException | KPVideoUploadException |  KPPlaylistCreationException | KPChannleNotFoundException e)
        {
           throw e;
        } catch (Exception e) {
           // e.printStackTrace();
           System.out.println("Error in checkAndUploadVideo: "+ytUrl);
            return null;
        }
    }   
    
    private JsonNode uploadVideo(String ytUrl, String playlistname) {
            JsonNode videoNode = this.getVideo(ytUrl);
            String videoname = "";

            if(videoNode == null)
            {
                videoNode = this.uploadVideo(ytUrl);
                if(videoNode == null)
                {
                    throw new KPVideoUploadException("Video uploaded failed for url: "+ytUrl);
                }
                
                videoname = videoNode.get("data").asText();               
            }
            else {
                videoname = videoNode.get("id").asText();
            }
            
            if(!this.checkVideoExistsInPlaylist(playlistname, videoname))
            {
                videoNode = this.addVideoToPlaylist(playlistname, videoname);
            }
            return videoNode;
    }
    @Override
    public JsonNode getVideo(String ytUrl) {       
		try {
			String token = authTokenManager.getAuthToken();
            String ytid = getytIdFromUrl(ytUrl);
            String url = String.format("%s?scope=recent&facet.properties.youtube_id=%s&xt=%s",
            videoUrl, ytid, token);

            HttpHeaders headers = new HttpHeaders();
	        HttpEntity<String> entity = new HttpEntity<>(headers);
	        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	        
	        if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                JsonNode respNode = objectMapper.readTree(response.getBody());	  
                if(respNode != null && respNode.has("list") && respNode.get("list").size() > 0)
                {
                    return respNode.get("list").get(0);
                }
	        }

        } catch (Exception e) {
            e.printStackTrace();
        }
       
        return null;
    }

    @Override
    public JsonNode uploadVideo(String ytUrl) {
        // Implement the logic to upload a video
        JsonNode vNode = null;
		try {
			String token = authTokenManager.getAuthToken();
			String url = String.format("%s%s?xt=%s", videoUrl, "/import", token);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			//Create request body
			JSONObject requestBody = new JSONObject();
	        requestBody.put("video_link", ytUrl);
            System.out.println("\n\n upload video kpoint url -" +url);
	        
			HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);		
            System.out.println("\n\n Entity" + entity.getBody());	
           
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				vNode = objectMapper.readTree(response.getBody());	     
				return vNode;
			} 

             System.out.println("\n\nresponse.getStatusCode()"+ response.getStatusCode());

		} catch (Exception e) {
           // e.printStackTrace();
           System.out.println("uploadVideo error: " + e.getMessage());
		}
		
        return null;
    }

    @Override
    public JsonNode addVideoToPlaylist(String playlistName, String videoName) {
        // Implement the logic to add a video to a playlist

        try {
	    	String token = authTokenManager.getAuthToken();
	    	String url = String.format("%s/%s/videos/%s?xt=%s", playlistUrl, playlistName, videoName, token);
	        HttpHeaders headers = new HttpHeaders();	        
	        HttpEntity<String> entity = new HttpEntity<>(null, headers); 
            System.out.println(" \n\n addVideoToPlaylist url: "+url+"  -- " +entity.getBody());
	        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

	        if (response.getStatusCode() == HttpStatus.OK) {
	            ObjectMapper objectMapper = new ObjectMapper();
	            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	            return objectMapper.readTree(response.getBody());
	        }
             System.out.println(" \n\n response.getStatusCode() addVideoToPlaylist " + response.getStatusCode());

	    } catch (Exception e) {
	       // e.printStackTrace();
           System.out.println("Error in addVideoToPlaylist: "+playlistName+" videoName: "+videoName +" " + e.getMessage());
	    }
        return null;
    }
    
    //helper function to get ytId from url
    private String getytIdFromUrl(String url) {
        String youtube_id="";
        URI uri;
       
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        String path = uri.getPath();
        String[] pathSegments = path.split("/");
        
        if(pathSegments[pathSegments.length -1].equals("watch")){
            String query = uri.getQuery();
            String[] pairs = query.split("&");
            String[] keyValue = pairs[0].split("=");
            youtube_id = keyValue[1];
        }
        else
        {
            youtube_id = pathSegments[pathSegments.length -1];
        }
        return youtube_id;
    }

    @Service
    private static class AuthTokenManager {       
        
        @Value("${kpoint.client.id}")
        private String clientId;

        @Value("${kpoint.client.secret}")
        private String clientSecret;

        @Value("${kpoint.client.displayname}")
        private String userName;
        
        @Value("${kpoint.client.email}")
        private String userEmail;

        public String getAuthToken() throws InvalidKeyException, NoSuchAlgorithmException {
            long challenge = System.currentTimeMillis() / 1000;
            String data = clientId + ":" + userEmail + ":" + userName + ":" + challenge;

            String xauthToken = stringToBase64HMACMD5(data);
            String xtEncode = "client_id=" + clientId + "&user_email=" + userEmail + "&user_name=" + userName + "&challenge="
                    + challenge + "&xauth_token=" + xauthToken;

            String xt = Base64.encodeBase64URLSafeString(xtEncode.getBytes());
            return xt;
        }

        private String stringToBase64HMACMD5(String message) throws NoSuchAlgorithmException, InvalidKeyException {
            SecretKeySpec keySpec = new SecretKeySpec(clientSecret.getBytes(), "HmacMD5");
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(keySpec);
            byte[] rawHmac = mac.doFinal(message.getBytes());
            return Base64.encodeBase64URLSafeString(rawHmac);
        }
    }

}
