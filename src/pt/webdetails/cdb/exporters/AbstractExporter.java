/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cdb.exporters;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import pt.webdetails.cpf.repository.AliasedGroup;

/**
 *
 * @author pdpi
 */
public abstract class AbstractExporter implements Exporter {

  protected String fileExportExtension, templateFile;
  private AliasedGroup aliasGroup;

  AbstractExporter() {
    aliasGroup = new AliasedGroup();
    aliasGroup.addSolutionDir("cdb/exporters/templates");
    aliasGroup.addClass(this.getClass());
  }

  @Override
  public String export(String group, String id, String url, Map<String, String> params) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public String export(String group, String id, String url) {
    return getSource(group, id, url);
  }

  protected String getSource(String group, String id, String url) {
    Map<String, String> values = new HashMap<String, String>();
    values.put("group", group);
    values.put("id", id);
    values.put("url", url);
    Mustache templ;
    try {
      templ = loadTempate(templateFile);
    } catch (Exception e) {
      return null;
    }
    StringWriter writer = new StringWriter();
    templ.execute(writer, values);
    return writer.getBuffer().toString();
  }

  @Override
  public void binaryExport(String group, String id, String url, Map<String, String> params, OutputStream out) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void binaryExport(String group, String id, String url, OutputStream out) throws IOException {
    out.write(export(group, id, url).getBytes("utf-8"));
  }

  @Override
  public String getFilename(String group, String id, String url) {
    return group + "-" + id + "." + fileExportExtension;
  }

  public Mustache loadTempate(String name) throws FileNotFoundException {
    InputStream templateStream = aliasGroup.getResourceStream(name);
    Reader templateReader = new InputStreamReader(templateStream);
    MustacheFactory mf = new DefaultMustacheFactory();
    return mf.compile(templateReader, name);
  }

}
