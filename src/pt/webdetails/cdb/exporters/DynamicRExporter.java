/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.exporters;

import com.github.mustachejava.Mustache;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pdpi
 */
public class DynamicRExporter extends AbstractExporter {

  public DynamicRExporter() {
    this.fileExportExtension = "r";
    this.templateFile = "DynamicR.mustache";
  }

  

  @Override
  public String export(String group, String id, String url) {
    
      return ""
          + "#install.packages(\"rjson\");\n"
          + "require(\"rjson\")\n"
          + "\n"
          + "cdb.fromJSONUrl <- function(server,group,query,user,password, toNumeric = TRUE, toDate = TRUE) {\n"
          + "  \n"
          + "  # Utility functions\n"
          + "  toNumericFunc <- function(ds){\n"
          + "    return(as.numeric(levels(ds)[ds]));  \n"
          + "  }\n"
          + "  \n"
          + "  toDateFunc <- function(d){\n"
          + "    return(as.POSIXct(d, tz = \"GMT\"));\n"
          + "  }\n"
          + "  \n"
          + "  # assemble the complete URL\n"
          + "  completeUrl <- paste(c(server,\"/content/cdb/doQuery?group=\",\n"
          + "                         group,\"&outputType=json&id=\",\n"
          + "                         query,\"&userid=\",user,\"&password=\",password), collapse = \"\");\n"
          + "  \n"
          + "  # Read it\n"
          + "  json <- fromJSON(,URLencode(completeUrl));\n"
          + "  \n"
          + "  # Get types\n"
          + "  ct <- sapply(json$metadata,function(d){d$colType})\n"
          + "  cn <- sapply(json$metadata,function(d){d$colName});\n"
          + "  df <- as.data.frame(t(sapply(json$resultset, function(d){unlist(rbind(d))})),stringsAsFactors=TRUE)\n"
          + "  \n"
          + "  # Convert numerics\n"
          + "  for(i in c(1:length(ct))){\n"
          + "    \n"
          + "    if(ct[i]==\"Numeric\"){\n"
          + "      if(toNumeric == TRUE)\n"
          + "        df[[i]] <- toNumericFunc(df[[i]])\n"
          + "    }\n"
          + "    else{\n"
          + "      # Introspection for date\n"
          + "      if(toDate == TRUE && length(grep(\"date\",cn[[i]],ignore.case=TRUE))>0){\n"
          + "        df[[i]] <- toDateFunc(df[[i]])\n"
          + "      }      \n"
          + "    }    \n"
          + "  }  \n"
          + "  names(df) <- make.names(cn);\n"
          + "  return(df);\n"
          + "}\n"
          + "\n"
          + "user<- \"palves@mozilla.com\";\n"
          + "password <-\"\";\n"
          + "\n"
          + "result <- cdb.fromJSONUrl(\""+url+"\",\""+ group +"\", \""+id+"\",user,password,toNumeric=TRUE, toDate=TRUE);\n"
          + "\n";

  }

}
