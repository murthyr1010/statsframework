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
		  
		   File f1 = new File(System.getProperty("user.dir") + File.separatorChar + "src" + File.separator + "main"
			+ File.separator + "stats.json");
		   
		   	FileWriter fileWriter1 = new FileWriter(f1);
		    PrintWriter printWriter1 = new PrintWriter(fileWriter1);
		    printWriter1.print(obj);
		    printWriter1.close();
		    
		    File f2 = new File(System.getProperty("user.dir") + File.separatorChar + "src" + File.separator + "main"
					+ File.separator + "stats.html");
		    FileWriter fileWriter2 = new FileWriter(f2);
		    PrintWriter printWriter2 = new PrintWriter(fileWriter2);
		    String logo ="<img src=\"data:image/png;base64,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\" alt=\"Progress.com\" style=\"width:200px;height:150px;\">";
		    
		    String htmlCont = "<!DOCTYPE html>\r\n" + 
		    		"<html>\r\n" + 
		    		"<body style=\"background-color:Ivory;\">\r\n" + 
		    		"\r\n" + logo+
		    		"<h2>Tdriver Results</h2>\r\n" + 
		    		"\r\n" + 
		    		"<p id=\"demo\"></p>\r\n" + 
		    		"\r\n" + 
		    		"<script>\r\n" + 
		    		"var txt ="+"\'"+ obj+"\'"+"\r\n" + 
		    		"var obj = JSON.parse(txt);\r\n" + 
		    		"document.getElementById(\"demo\").innerHTML = 'Total Tests: ' +  obj.total+'<br>Passed Tests: '+obj.passed+'<br>Failed Tests: '+obj.failed+'<br>Muted Tests :'+obj.muted+'<br>Pass Percentage: '+obj.passpercent+'%'"+
		    		"</script>\r\n" + 
		    		"\r\n" + 
		    		"</body>\r\n" + 
		    		"</html>\r\n" ;
		    
		    printWriter2.print(htmlCont);
		    printWriter2.close();
		    
		    
		
	}

}
