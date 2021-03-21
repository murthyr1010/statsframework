package teamcity;


	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
	import com.fasterxml.jackson.annotation.JsonAnyGetter;
	import com.fasterxml.jackson.annotation.JsonAnySetter;
	import com.fasterxml.jackson.annotation.JsonIgnore;
	import com.fasterxml.jackson.annotation.JsonInclude;
	import com.fasterxml.jackson.annotation.JsonProperty;
	import com.fasterxml.jackson.annotation.JsonPropertyOrder;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({
	"count",
	"href",
	"testOccurrence"
	})
	public class ResultPojo {

	@JsonProperty("count")
	private Integer count;
	@JsonProperty("href")
	private String href;
	@JsonProperty("testOccurrence")
	private List<TestOccurrence> testOccurrence = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("count")
	public Integer getCount() {
	return count;
	}

	@JsonProperty("count")
	public void setCount(Integer count) {
	this.count = count;
	}

	@JsonProperty("href")
	public String getHref() {
	return href;
	}

	@JsonProperty("href")
	public void setHref(String href) {
	this.href = href;
	}

	@JsonProperty("testOccurrence")
	public List<TestOccurrence> getTestOccurrence() {
	return testOccurrence;
	}

	@JsonProperty("testOccurrence")
	public void setTestOccurrence(List<TestOccurrence> testOccurrence) {
	this.testOccurrence = testOccurrence;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
	return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
	this.additionalProperties.put(name, value);
	}

	}

