package io.dnsdb.getdns4j;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.dnsdb.sdk.APIUser;
import java.util.Date;

/**
 * <code>APIUserWrapper</code>类包装了<code>io.dnsdb.sdk.APIUser</code>，用于JSON序列化。
 *
 * @author Remonsan
 * @version 1.0
 */
@JsonPropertyOrder({"api_id", "user", "remaining_requests", "creation_time", "expiration_time"})
public class APIUserWrapper extends APIUser {

  public APIUserWrapper(String apiId, String user, int remainingRequests, Date creationTime,
      Date expirationTime) {
    super(apiId, user, remainingRequests, creationTime, expirationTime);
  }

  public APIUserWrapper(APIUser apiUser) {
    this(apiUser.getApiId(), apiUser.getUser(), apiUser.getRemainingRequests(),
        apiUser.getCreationTime(), apiUser.getExpirationTime());
  }


  @JsonProperty("api_id")
  @Override
  public String getApiId() {
    return super.getApiId();
  }

  @Override
  public String getUser() {
    return super.getUser();
  }

  @JsonProperty("remaining_requests")
  @Override
  public int getRemainingRequests() {
    return super.getRemainingRequests();
  }

  @JsonProperty("creation_time")
  @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  @Override
  public Date getCreationTime() {
    return super.getCreationTime();
  }

  @JsonProperty("expiration_time")
  @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  @Override
  public Date getExpirationTime() {
    return super.getExpirationTime();
  }
}
