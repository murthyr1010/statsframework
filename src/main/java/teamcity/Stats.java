package teamcity;
import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.authentication.BasicAuthScheme;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Stats {

	public static void main(String[] args) throws IOException {
		String buildId=args[0];
		RestAssured.baseURI = "http://172.16.108.158/app/rest";
		BasicAuthScheme auth = new BasicAuthScheme();

		auth.setUserName("rmurthy");
		auth.setPassword("rmurthy");

		RestAssured.authentication = auth;
		Response resp = RestAssured.given().header("Accept", "Application/json").
				when().get("/testOccurrences?locator=build:"+buildId+",count:100000");
		//System.out.println(resp.asString());
		JsonPath jsonPath1 = new JsonPath(resp.asString());
		long executed_count = jsonPath1.getInt("count");
		//System.out.println(executed_count);
		ResultPojo result =RestAssured.given().relaxedHTTPSValidation().header("Accept", "Application/json").
		when().get("/testOccurrences?locator=build:"+buildId+",count:100000").as(ResultPojo.class);
		//System.out.println("------"+result.getTestOccurrence().size());
		
		long passed=0;
		long failed =0;
		for(int i=0;i<result.getTestOccurrence().size();i++)
		{
			if(result.getTestOccurrence().get(i).getStatus().equals("SUCCESS"))
			{
				passed++;
			}
			else if(result.getTestOccurrence().get(i).getStatus().equals("FAILURE"))
			{
				failed++;
			}
		}
		
		

		Response resp1 = RestAssured.given().relaxedHTTPSValidation().header("Accept", "Application/json").
				when().get("testOccurrences?locator=build:"+buildId+",count:10000,muted:true");
		//System.out.println(resp1.asString());
		JsonPath jsonPath2 = new JsonPath(resp1.asString());
		long muted_count = jsonPath2.getInt("count");
		
		System.out.println("Passed "+ passed);
		System.out.println("Failed "+ failed);
		System.out.println("Muted " + muted_count);
		long total = passed+failed+muted_count;
		System.out.println("Total "+ (passed+failed+muted_count));
		double pass_percent = ((float)passed/(float)total)*100;
		System.out.println("Pass %"+pass_percent);
		
		JSONObject obj=new JSONObject();    
		  obj.put("passed",passed);    
		  obj.put("failed",failed);    
		  obj.put("muted",muted_count);
		  obj.put("total",total);
		  obj.put("passpercent",pass_percent);
		  
		   System.out.print(obj); 
		   File f = new File(System.getProperty("user.dir") + File.separatorChar + "src" + File.separator + "main"
					+ File.separator + "java" + File.separator + "resources" + File.separator + "stats.json");
		   FileWriter fileWriter = new FileWriter(f);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		    printWriter.print(obj);
		    printWriter.close();
		
	}

}
