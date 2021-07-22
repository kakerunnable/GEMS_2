package ui;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import Util.StringDiffUtils;

public class ChangeResult {

  private String before;
  private String after;

  public ChangeResult(String before, String after) {
	  this.before = before;
	  this.after = after;
  }

  public String getBefore() {
    return before;
  }

  public void setBefore(String before) {
    this.before = before;
  }

  public String getAfter() {
    return after;
  }

  public void setAfter(String after) {
    this.after = after;
  }

  public String diffMethodRegex() {

	  String diff = StringDiffUtils.diff(before, after).split("\n")[0].trim();

	  String argsRegex = ".+\\((.+)\\)";
	  Matcher argsMatcher = Pattern.compile(argsRegex).matcher(diff);
	  if (argsMatcher.find()) {
		  String argTypes = argsMatcher.group(1);
		  String args = Arrays.stream(argTypes.split(",")).map(String::trim).map(s -> s + " .+").collect(Collectors.joining(", "));

		  diff = diff.replace("(" + argTypes + ")", "\\(" + args + "\\)");
	  }

	  return "^.*" + diff + ".*$";
  }
}
