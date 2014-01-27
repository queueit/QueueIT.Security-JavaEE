package queueit.security;

public enum RedirectType { 
    Unknown("unknown"), 
    Queue("queue"), 
    Safetynet("safetynet"), 
    AfterEvent("afterevent"), 
    Disabled("disabled"), 
    DirectLink("directlink");
    
    private String text;
    
    RedirectType(String text) {
        this.text = text;
    }

  public String getText() {
    return this.text;
  }

  public static RedirectType fromString(String text) {
    if (text != null) {
      for (RedirectType b : RedirectType.values()) {
        if (text.equalsIgnoreCase(b.text)) {
          return b;
        }
      }
    }
    return RedirectType.Unknown;
  }
}
