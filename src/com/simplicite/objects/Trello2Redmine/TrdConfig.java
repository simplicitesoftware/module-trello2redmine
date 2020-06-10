package com.simplicite.objects.Trello2Redmine;

import java.util.*;
import com.simplicite.util.*;
import com.simplicite.util.tools.*;
import org.json.*;

/**
 * Business object TrdConfig
 */
public class TrdConfig extends ObjectDB {
	private static final long serialVersionUID = 1L;
	
	private class TrelloConfig {
		public String boardId;
		public String token;
		public String key;
		
		public TrelloConfig(String trlBoard, String trlToken, String trlKey){
			boardId = trlBoard;
			token = trlToken;
			key = trlKey;
		}
	}
	
	private class RedmineConfig {
		public static final int RED_TRACKER_FEATURE = 2;
		public static final int RED_TRACKER_BUG = 1;
		public static final int RED_PRIORITY = 2;
		public static final int RED_STATUS_CLOSED = 5;
		public String key;
		public int user;
		public int project;
		
		public RedmineConfig(String rdKey, int rdUser, int proj){
			key = rdKey;
			user = rdUser;
			project = proj;
		}
	}
	
	private TrelloConfig getTrelloConfig(){
		return new TrelloConfig(
			getFieldValue("trdCgfTrelloBoard"),
			getFieldValue("trdCgfTrelloToken"),
			getFieldValue("trdCgfTrelloKey")
		);
	}
	
	private RedmineConfig getRedmineConfig(){
		return new RedmineConfig(
			getFieldValue("trdCgfRedmineKey"),
			getField("trdCgfRedmineUser").getInt(0),
			getField("trdCgfRedmineProject").getInt(0)
		);
	}
	
	public void importToRedmine() throws Exception{
		TrelloConfig trello = getTrelloConfig();
		RedmineConfig redmine = getRedmineConfig();
		
		JSONArray board = getTrelloBoardLists(trello);
		
		for(int i = 0; i<board.length(); i++){
			JSONObject list = board.getJSONObject(i);
			String listName = list.getString("name");
			
			if(listName.startsWith("v")){
				AppLog.info(getClass(), "main", listName, null);
				
				JSONArray cards = list.getJSONArray("cards");
				for(int j = 0; j<cards.length(); j++){
					JSONObject card = cards.getJSONObject(j);
					
					JSONObject redmineItem = new JSONObject();
					redmineItem.put("project_id", redmine.project);
					redmineItem.put("subject", card.getString("name"));
					redmineItem.put("priority_id", redmine.RED_PRIORITY);
					redmineItem.put("status_id", redmine.RED_STATUS_CLOSED);
					redmineItem.put("tracker_id", card.getString("name").contains("ANO") ? redmine.RED_TRACKER_BUG : redmine.RED_TRACKER_FEATURE);
					redmineItem.put("assigned_to_id", redmine.user);
					redmineItem.put("fixed_version_id", getVersionId(listName, redmine));
					redmineItem.put("description", card.getString("shortUrl")+"\n\n"+card.getString("desc"));
					
					JSONObject issue = new JSONObject();
					issue.put("issue", redmineItem);

					createRedmineIssue(issue, redmine);
				}
			}
		}
	}
	
	private static JSONArray getTrelloBoardLists(TrelloConfig cfg) throws Exception{
		String request = "https://api.trello.com/1/boards/"+cfg.boardId+"/lists?cards=all&token="+cfg.token+"&key="+cfg.key;
		String rep = Tool.readUrl(request);
		return new JSONArray(rep);
	}
	
	private void createRedmineIssue(JSONObject issue, RedmineConfig redmine) throws Exception{
		AppLog.info(getClass(), "createRedmineIssue", issue.toString(), null);
		Tool.readUrl("https://projects.simplicite.io/issues.json?key="+redmine.key, issue);
	}
	
	private int getVersionId(String listName, RedmineConfig redmine) throws Exception{
		JSONObject rep = new JSONObject(Tool.readUrl("https://projects.simplicite.io/projects/"+redmine.project+"/versions.json?key="+redmine.key));
		
		JSONArray versions = rep.getJSONArray("versions");
		for(int i = 0; i < versions.length(); i++){
			JSONObject version = versions.getJSONObject(i);
			if(listName.equals(version.getString("name"))){
				return version.getInt("id");
			}
		}
		return 0;
	}
}
