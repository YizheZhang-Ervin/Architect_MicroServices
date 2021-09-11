package pers.ervin.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "pattern")
public class PatternProperties {
  private String dateformat;
  private String envSharedValue;
  private String name;

  public String getDateformat() {
    return dateformat;
  }

  public void setDateformat(String dateformat) {
    this.dateformat = dateformat;
  }
}
